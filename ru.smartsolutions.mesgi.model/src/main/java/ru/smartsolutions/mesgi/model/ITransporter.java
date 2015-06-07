package ru.smartsolutions.mesgi.model;

public interface ITransporter {
	
	public void sendTask(String ip, Task task);
	public boolean checkTask(String ip, Task task);
	
}
