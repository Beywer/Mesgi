package ru.smartsolutions.mesgi.planner.model;

import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class PlannedTask {
	
	private Interval plannedInterval;
	private String id;
	private Boolean successed;
	
	public PlannedTask(String id) {
		super();
		this.id = id;
		successed = false;
	}

	public Interval getPlannedInterval() {
		return plannedInterval;
	}
	
	public void setPlannedInterval(Interval plannedInterval) {
		this.plannedInterval = plannedInterval;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		DateTimeFormatter dtf = 
				DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		String start = dtf.print(plannedInterval.getStartMillis());
		String end = dtf.print(plannedInterval.getEndMillis());
		return id + " " + successed + " [" +start+" -- "+end+"]\n";
	}

	public Boolean getSuccessed() {
		return successed;
	}

	public void setSuccessed(Boolean successed) {
		this.successed = successed;
	}
}
