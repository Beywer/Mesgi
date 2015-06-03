package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.HashSet;
import java.util.Set;

import ru.smartsolutions.mesgi.planner.model.Device;
import ru.smartsolutions.mesgi.planner.model.Task;

public class DataProvider {

	private static Set<Device> devices = new HashSet<Device>();
	private static Set<Task> tasks = new HashSet<Task>();
	
	public static void addDevice(Device device){
		devices.add(device);
	}
	
	public static void addTask(Task task){
		tasks.add(task);
	}
	
	public static Set<Device> getDevices(){
		return devices;
	}

	public static Set<Task> getTasks() {
		return tasks;
	}
}
