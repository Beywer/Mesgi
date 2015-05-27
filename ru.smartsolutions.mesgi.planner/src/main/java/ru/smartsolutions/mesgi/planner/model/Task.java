package ru.smartsolutions.mesgi.planner.model;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
	
	private String name;
	private String id;
	private int duration;
	private Interval intervalAllowed;
	
	public Task(String name) {
		this.name = name;
		intervalAllowed = null;
		duration = 0;
		id = UUID.randomUUID().toString();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Interval getIntervalAllowed() {
		return intervalAllowed;
	}

	public void setIntervalAllowed(Interval intervalAllowed) {
		this.intervalAllowed = intervalAllowed;
	}

	@Override
	public int compareTo(Task task) {
		
		DateTime thisEndTime = intervalAllowed.getEnd();
		DateTime otherEndTime = task.getIntervalAllowed().getEnd();
		
		long thisMillis = thisEndTime.getMillis();
		long otherMillis = otherEndTime.getMillis();
		
		if(thisMillis < otherMillis) return -1;
		else if (thisMillis > otherMillis) return 1;
		else return 0;
	}
	
	@Override
	public String toString() {
		if(intervalAllowed != null){
			DateTimeFormatter dtf = 
					DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
			String start = dtf.print(intervalAllowed.getStartMillis());
			String end = dtf.print(intervalAllowed.getEndMillis());
			return id + " " + duration + " min "
					+ "[" +start+" -- "+end+"]\n";
		} else return id + "[:]";
	}
}
