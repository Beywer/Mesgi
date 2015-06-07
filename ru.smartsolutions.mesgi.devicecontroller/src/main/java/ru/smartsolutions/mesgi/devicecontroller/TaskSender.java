package ru.smartsolutions.mesgi.devicecontroller;

import java.util.Collection;
import java.util.TimerTask;

import ru.smartsolutions.mesgi.model.Task;

public class TaskSender extends TimerTask {

	private Planner planner;
	
	public TaskSender(Planner planner) {
		this.planner = planner;
	}
	
	@Override
	public void run() {
		
		 Collection<Task> unsendedTasks = planner.getUnplannedTasks().values();
		 
		 
		
	}

}
