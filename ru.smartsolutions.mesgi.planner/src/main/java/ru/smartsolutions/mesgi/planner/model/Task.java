package ru.smartsolutions.mesgi.planner.model;

import java.util.UUID;

import org.joda.time.Interval;

public class Task {
	
	private String name;
	private String id;
	private Interval interval;
	
	public Task(String name) {
		this.name = name;
		interval = null;
		id = UUID.randomUUID().toString();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Interval getInterval() {
		return interval;
	}
	
	public void setInterval(Interval interval) {
		this.interval = interval;
	}
	
	public String getId() {
		return id;
	}
}
