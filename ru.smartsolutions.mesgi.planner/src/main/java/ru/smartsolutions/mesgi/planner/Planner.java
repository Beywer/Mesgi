package ru.smartsolutions.mesgi.planner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.Interval;

import ru.smartsolutions.mesgi.planner.model.PlannedTask;
import ru.smartsolutions.mesgi.planner.model.Task;

public class Planner {
	
	private List<Task> tasks;
	private List<PlannedTask> plan;
	
	public Planner() {
		tasks = new ArrayList<Task>();
		plan = new ArrayList<PlannedTask>();
	}
	
	public void addTask(Task task){
		tasks.add(task);
		planTasks();
	}
	
	public void removeAllTasks(){
		tasks = new ArrayList<Task>();
		plan = new ArrayList<PlannedTask>();
	}
	
	public void removeTask(Task task){
		tasks.remove(task);
		planTasks();
	}
	
	public List<PlannedTask> getPlan() {
		return plan;
	}

	private void planTasks(){
		
		//Сортировка задач по времени завершения
		Collections.sort(tasks);
		
		long startMillis = 0;;
		for(Task task : tasks){
			
			//Планирование начинается с времени начала самой первой заявки
			if(startMillis == 0)
				startMillis= task.getIntervalAllowed().getStartMillis();
			
			PlannedTask plannedTask = new PlannedTask(task.getId());
			//вемя завершения задачи = конец + длительность(из минут в милисекунды)
			long endMillis = startMillis + task.getDuration()*1000*60;
			Interval interval = new Interval(startMillis, endMillis);
			plannedTask.setPlannedInterval(interval);
			//если время завершения задачи >= запланированного завершения
			//планирование считается успешным
			if(task.getIntervalAllowed().getEndMillis() >= endMillis)
				plannedTask.setSuccessed(true);
			else plannedTask.setSuccessed(false);
	
			plan.add(plannedTask);
			//Интервал 10сек между задачами
			startMillis = endMillis + 10000;
		}
	}
	
}
