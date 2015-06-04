package ru.smartsolutions.mesgi.devicecontroller;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Interval;

import ru.smartsolutions.mesgi.model.Task;

public class Planner {
	
	private List<Task> plan;
	
	public Planner() {
		plan = new ArrayList<Task>();
	}
	
	public void removeAllTasks(){
		plan = new ArrayList<Task>();
	}
	
	public void removeTask(Task task){
		plan.remove(task);
		replanTasks();
	}
	
	public List<Task> getPlan() {
		return plan;
	}
	
	public Task planTask(Task task){

		long startMillis = 0;
		long duration = task.getDuration()*60*1000;
		long endTime = startMillis + duration;
		
		if(endTime <= task.getIntervalAllowed().getEndMillis()){
			Interval plannedInterval = new Interval(startMillis, endTime);
			task.setPlannedInterval(plannedInterval);
			
			plan.add(task);
		}
		
		return task;
	}

	private void replanTasks(){
		
		//TODO получить вермя завершения текущей задачи
		long startTime = System.currentTimeMillis();
		
		for(Task task : plan){
		
			long duration = task.getDuration()*60*1000;
			long endTime = startTime + duration;
			
			if(endTime <= task.getIntervalAllowed().getEndMillis()){
				Interval plannedInterval = new Interval(startTime, endTime);
				task.setPlannedInterval(plannedInterval);
			}
		}
		
	}

}
