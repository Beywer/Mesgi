package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.HashMap;
import java.util.Map;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.Result;
import ru.smartsolutions.mesgi.model.Task;

public class DataProvider {

	private static Map<String, Device> devices = new HashMap<>();
	private static Map<String, Task> tasks = new HashMap<>();
	private static Map<String, Result> results = new HashMap<>();
	
	public static void addDevice(Device device){
		devices.put(device.getAddress(), device);
	}
	
	public static void addTask(Task task){
		tasks.put(task.getId(), task);
	}
	
	public static void addResult(Result result){
		results.put(result.getTaskId(), result);
	}
	
	public static Map<String, Device> getDevices(){
		return devices;
	}

	public static Map<String, Task> getTasks() {
		return tasks;
	}

	public static Map<String, Result> getResults() {
		return results;
	}
}
