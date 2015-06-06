package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.Result;
import ru.smartsolutions.mesgi.model.Task;

public class DataProvider {

	private static Map<String, Device> devices = new HashMap<>();
	private static Map<String, Task> plannedTasks = new HashMap<>();
	private static Map<String, Task> unplannedTasks = new HashMap<>();
	private static Map<String, Result> results = new HashMap<>();
	
	private static List<MainComponent> components = new ArrayList<>();
	
	private static Map<String,List<Map<String, String>>> devicePlans = new HashMap<>();	
	
//	COMPONENTS
	public static void registerComponent(MainComponent mainComponent){
		components.add(mainComponent);
	}
	public static void removeComponent(MainComponent mainComponent){
		components.remove(mainComponent);
	}
	
//	PLANS
	public static void addPlannedTask(Device device, Task task){

//		если у  устройства не было списка задач, он создастся
		List<Map<String, String>> devicePaln = devicePlans.get(device.getAddress());
		if(devicePaln == null){
			devicePaln = new ArrayList<>();
			devicePlans.put(device.getAddress(), devicePaln);
		}
		
		//создается значение для Ганта
		Interval plannedInterval = task.getPlannedInterval();
		Map<String, String> value =
				VTimeLine.createRow(plannedInterval.getStartMillis(),
						plannedInterval.getEndMillis(),
						task.getName(), device.getName());
		//TODO можеть быть не понадобится
		value.put("id", task.getId());
		
		//записывается значение
		devicePaln.add(value);
		
//		объединение данных по всем устройствам
		List<Map<String, String>> timeLineData = new ArrayList<Map<String,String>>();
		for(String ip : devicePlans.keySet()){
			devicePaln = devicePlans.get(ip);
			timeLineData.addAll(devicePaln);
		}
//		обновление диаграмм Ганта
		for(MainComponent component : components){
			VTimeLine timeLine = component.getTimeLine();
			timeLine.setDatatable(timeLineData);
			timeLine.setCurrentInterval(System.currentTimeMillis() - 15*60*1000, 
										System.currentTimeMillis() + 2*60*60*1000);
			component.updateUI();
		}
	}
	
//	DEVICES
	public static void addDevice(Device device){

		devices.put(device.getAddress(), device);
		
		Task fictiveTask = new Task("ft");
		fictiveTask.setDuration(1);
		Interval plannedInterval = new Interval(15*60*1000,	15*60*1000);
		fictiveTask.setPlannedInterval(plannedInterval);
		addPlannedTask(device, fictiveTask);
		
		for(MainComponent component : components){
			
//			VTimeLine timeLine = component.getTimeLine();
//			timeLine.getDatatable().add(VTimeLine.createRow(
//					0 + 15*60*1000,
//					0 + 15*60*1000,
//					device.getName(), device.getAddress() ));
//			timeLine.setCurrentInterval(System.currentTimeMillis() - 15*60*1000,
//										System.currentTimeMillis() + 2*60*60*1000);
			
			component.getDevicePannel().addDevice(device);
			

		}
		
	}
	
	public static boolean checkDevice(String ip){
		return devices.containsKey(ip);
	}
	
	public static void updateDeviceAvailability(String ip, boolean availability){
		Device device = devices.get(ip);
		device.setAvailability(availability);
		
		for(MainComponent component: components){
			component.getDevicePannel().updateDevice(ip, device.isAvailability());
		}
	}
	
//	TASKS
	
	public static void addTask(Task task){
		if(task != null){
			
			unplannedTasks.put(task.getId(), task);
			
			for(MainComponent mainComponent : components){
				mainComponent.getTaskPannel().addTask(task);
			}
			
		}
	}
	
	public static void addResult(Result result){
		results.put(result.getTaskId(), result);
	}

	
//	GETTERS
	public static Map<String, Device> getDevices(){
		return devices;
	}

	public static Map<String, Task> getTasks() {
		return plannedTasks;
	}

	public static Map<String, Result> getResults() {
		return results;
	}
}
