package ru.smartsolutions.mesgi.devicecontroller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

import ru.smartsolutions.mesgi.model.Task;

public class Planner {
	
	private Map<String, Task> plan;
	private volatile Map<String, Task> unplannedTasks;
	
	private long lastTaskEnd;
	
	public Planner() {
		
		plan = new HashMap<String, Task>();
		unplannedTasks = Collections.synchronizedMap(new HashMap<String, Task>());
		lastTaskEnd= 0;
	}
	
	public void removeAllTasks(){
		plan = new HashMap<String, Task>();
	}
	
	public void removeUnplannedTask(String id){
		System.out.println("Old count to send " + unplannedTasks.size());
		unplannedTasks.remove(id);
		System.out.println("New count to send " + unplannedTasks.size());
	}
	
	public void removeTask(Task task){
		plan.remove(task);
		replanTasks();
	}
	
	public Map<String, Task> getPlan() {
		return plan;
	}

	public Map<String, Task> getUnplannedTasks() {
		return unplannedTasks;
	}
	
	public void removeUnsendedTask(String id){
		System.out.println(unplannedTasks.size()+"  task was unplanned");
		System.out.println("remove task " +id);
		for(Task task : unplannedTasks.values()){
			System.out.print(task.getId() + " ||| ");
		}
		unplannedTasks.remove(id);
		System.out.println("\n"+unplannedTasks.size()+"  now in unplanned tasks");
	}

	public Task planTask(Task task){

//		вермя для начала плана - текущее или с момента прошлой задачи + 10с
		long startMillis = System.currentTimeMillis();
		if(lastTaskEnd != 0) startMillis = lastTaskEnd + 10*1000;

//		вычисляется время окончания
		long duration = task.getDuration()*60*1000;
		long endTime = startMillis + duration;
		
//		если окончание до конца допустимого интервала задача
//		переносится в план
		if(endTime <= task.getIntervalAllowed().getEndMillis()){
//			создается интервал выполнения
			Interval plannedInterval = new Interval(startMillis, endTime);
			task.setPlannedInterval(plannedInterval);
			
//			задача переносится в план из незапланированных задач
			plan.put(task.getId(), task);
			unplannedTasks.remove(task.getId());
			
//			новое вермя заверешния послединей задачи
			lastTaskEnd = endTime;
		} else unplannedTasks.put(task.getId(), task);
		
		System.out.println("Unplanned tasks count  " + unplannedTasks.size());
		
		return task;
	}

	private void replanTasks(){
		
//		все задачи считаются незапланированными. план очищается
		for(String id : plan.keySet()){
			unplannedTasks.put(id, plan.get(id));
		}
		plan.clear();
		
//		сортировка задач по времени завершения
		List<Task> tasks = (List<Task>) unplannedTasks.values();
		Collections.sort(tasks);

//		время завершения последней задачи 0 - задач не было
		lastTaskEnd = 0;
//		поочередное планирование всех незапланированных задач
		for(Task task : tasks){
			planTask(task);
		}
		
	}
}
