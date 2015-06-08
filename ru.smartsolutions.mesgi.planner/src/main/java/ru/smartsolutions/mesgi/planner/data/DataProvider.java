package ru.smartsolutions.mesgi.planner.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.Result;
import ru.smartsolutions.mesgi.model.Task;
import ru.smartsolutions.mesgi.planner.uicomponents.DevicePanel;
import ru.smartsolutions.mesgi.planner.uicomponents.MainComponent;
import ru.smartsolutions.mesgi.planner.uicomponents.TaskPannel;

/**
 * @author beywer
 *
 */
public class DataProvider {

//	список всех устройств
	private static Map<String, Device> devices = new HashMap<>();
//	спиоск резлуьтатов
	private static Map<String, Result> results = new HashMap<>();

//	список вообще всех задач
	private static volatile Map<String, Task> tasks = new HashMap<>();
	private static volatile List<String> sendedTasks = new ArrayList<>();
	
//	список компонентов
	private static List<MainComponent> components = new ArrayList<>();
	
	
//	планы для устройств, по усутройтсвам
	private static Map<String,List<Map<String, String>>> devicePlans = new HashMap<>();	
	
//	COMPONENTS
	public static void registerComponent(MainComponent mainComponent){
		components.add(mainComponent);
		
//		отрисовки интерфейса нового компонента, если уже есть данные
		DevicePanel devicePanel = mainComponent.getDevicePannel();
		for(String ip : devices.keySet()){
			devicePanel.addDevice(devices.get(ip));
		}

		updatePlans();

		TaskPannel taskPannel = mainComponent.getTaskPannel();
		for(String id : tasks.keySet()){
			Task task = tasks.get(id);
			taskPannel.addTask(task);
		}
		
	}
	public static void removeComponent(MainComponent mainComponent){
		components.remove(mainComponent);
	}
	
//	PLANS
	public static void addTaskToDevicePlan(Device device, Task task){
		
		System.out.println("Planner\n");
		System.out.println("\tTask " + task.getName());
		System.out.println("\tadd to device's plan " + device.getAddress());

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
		
//		заменяется старая задача на новую (plannedInter не null)
//		если это не фиктивная задача
		if(task.getPlannedInterval().getEndMillis() > 45*60*1000)
			tasks.put(task.getId(), task);
		
		for(MainComponent mainComponent : components){
			mainComponent.getTaskPannel().updateTask(task.getId(), true);
		}

		updatePlans();
	}
	
//	DEVICES
	public static void addDevice(Device device){
//		сохранение в списке устройств
		devices.put(device.getAddress(), device);
		
//		добавление фиктивной задачи для доступных
//		устройств (для отображения в таймлайне)
		if(device.isAvailability()){
			Task fictiveTask = new Task("");
			fictiveTask.setDuration(1);
			Interval plannedInterval = new Interval(15*60*1000,	30*60*1000);
			fictiveTask.setPlannedInterval(plannedInterval);
			addTaskToDevicePlan(device, fictiveTask);
		}

//		обновление интерфейсов
		for(MainComponent component: components){
			component.getDevicePannel().addDevice(device);
		}
		
		ClientProvider.addClient(device.getAddress());
	}
	
	public static void changeDeviceAvailability(String ip, boolean availability){
		Device device = devices.get(ip);
		device.setAvailability(availability);
		
//		добавление фиктивной задачи для доступных
//		устройств (для отображения в таймлайне)
		if(device.isAvailability()){
			Task fictiveTask = new Task("");
			fictiveTask.setDuration(1);
			Interval plannedInterval = new Interval(15*60*1000,	15*60*1000);
			fictiveTask.setPlannedInterval(plannedInterval);
			System.out.println("Add fictive task for " + device.getName());
			addTaskToDevicePlan(device, fictiveTask);
		} else {
			devicePlans.remove(device.getAddress());
			updatePlans();
		}
		
		
		for(MainComponent component: components){
			component.getDevicePannel().updateDevice(ip, device.isAvailability());
		}
	}
	
	
//	TASKS
	
	public static void addTask(Task task){
		if(task != null){
			tasks.put(task.getId(), task);
			
			for(MainComponent mainComponent: components){
				TaskPannel pannel = mainComponent.getTaskPannel();
				pannel.addTask(task);
			}
		}
	}
	
	public static List<Task> getUnsendedTasks(){
		
		List<Task> unsendedTasks = new ArrayList<Task>();
		
		for(String id : tasks.keySet()){
			if(!sendedTasks.contains(id)) unsendedTasks.add(tasks.get(id));
		}
		
		return unsendedTasks;
	}
	
	public static void setTaskSended(Task task, Device device){
		sendedTasks.add(task.getId());
		tasks.get(task.getId()).setDevice(device);
	}
	
//	UPDATERS
	
	private static void updatePlans(){
		
//		объединение данных по всем устройствам
		List<Map<String, String>> devicePaln = null;
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

//	GETTERS
	public static Map<String, Device> getDevices(){
		return devices;
	}
	public static Device getDevice(String ip){
		return devices.get(ip);
	}
	public static Task getTask(String id){
		return tasks.get(id);
	}
	public static Map<String, Result> getResults() {
		return results;
	}
}
