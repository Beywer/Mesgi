package ru.smartsolutions.mesgi.model;

public interface ITransporter {
	
	public boolean sendTask(String ip, String path, Task task);
	public String checkTask(String ip, Task task);
	
}
