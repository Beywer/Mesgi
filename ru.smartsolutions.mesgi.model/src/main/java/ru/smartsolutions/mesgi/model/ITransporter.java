package ru.smartsolutions.mesgi.model;

public interface ITransporter {
	
	public String sendTask(String ip, String path, Task task);
	
	public Task getPlannedTask(String ip, String path, Task task);
	
	public String checkTask(String ip, Task task);
	
}
