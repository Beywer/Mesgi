package ru.smartsolutions.mesgi.planner;

import java.util.ArrayList;
import java.util.List;

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
	
	private void planTasks(){
		
	}
	
}
