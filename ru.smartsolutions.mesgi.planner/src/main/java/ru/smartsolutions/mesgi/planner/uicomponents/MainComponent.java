package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.planner.logic.NodeReciever;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class MainComponent extends HorizontalLayout{

	private static final long serialVersionUID = -7389175932554905939L;

//	Таймлайн
	private VTimeLine timeLine;
//	Список устройств
	private DevicePannel devicePannel;
//	Список задач-заявок и выполненных
	private TaskPannel taskPannel;
	
//	Базовые компоненты
	private UI ui;
	private BundleContext context; 
	private NodeReciever nodeReciver; //eventadmin ЕventHandler
	
	public MainComponent(UI ui) {
		
		this.ui = ui;
		this.setSizeFull();
		
		devicePannel = new DevicePannel();
		timeLine = new VTimeLine();
		taskPannel = new TaskPannel();
		

		this.addComponent(devicePannel);
		this.addComponent(timeLine);
        timeLine.setWidth(108, Unit.PERCENTAGE);
		this.addComponent(taskPannel);
		
		this.setExpandRatio(devicePannel, 2);
		this.setExpandRatio(timeLine, 5);
		this.setExpandRatio(taskPannel, 2);
		

//		if(context == null){
//			context = Activator.getContext();
//			nodeReciver = new NodeReciever(ui, deviceTree, deviceContainer);
//
//			context.registerService(EventHandler.class, 
//			nodeReciver, 
//			getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
//			System.out.println("Crated eventHandler in component");
//		}
		

//		Работа с тестовыми данными
//		setTestData();
//		Timer timer = new Timer();
//		timer.schedule(new TestUiTask(), 1000, 1000);
	}

	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
	
	
//	private void updateTaskPlan(String deviceIp){
//		List<Map<String,String>> timeLineData = new ArrayList<>();
//		
//		for(PlannedTask task : planner.getPlan()){
//			timeLineData.add(VTimeLine.createRow
//					(task.getPlannedInterval().getStartMillis(),
//					 task.getPlannedInterval().getEndMillis(), task.getId(), deviceIp));
//		}
//		
//		timeLine.setDatatable(timeLineData);
//		
////		ui.push();
//		
//	}
//	
//	private void setTestData(){
//		
////		DEVICES
//		for(int i = 0 ; i < 5 ; i++){
//			final Device device = new Device("Device " + (i + 1),
//					"Address " + (i +1), true);
//			device.setDescription("Device "+(i+1)+" decription");
//			
//			final Button deviceBut = new Button(device.getName());
//			
//			deviceBut.setSizeFull();
//			
//			if(device.getAvailability())
//				deviceBut.setStyleName("available-device");
//			else deviceBut.setStyleName("unavailable-device");
//			
//			deviceBut.addClickListener(new ClickListener() {
//				
//				@Override
//				public void buttonClick(ClickEvent event) {
//					deviceDescription.setReadOnly(false);
//					deviceDescription.setValue(device.getDescription());
//					deviceDescription.setReadOnly(true);
//					
//					lastSelectedDevice = deviceBut;
//				}
//			});
//			
//			deviceList.put(device.getAddress(), deviceBut);
//			devices.put(device.getAddress(), device);
//			devicePanelLayout.addComponent(deviceBut);
//		}
//		
////		PLANNER
////		long currentTime = System.currentTimeMillis();
////		Random r = new Random();
////		
////		for(int i = 0 ; i < 5; i++){
////			Task task = new Task("Task " + i);
////			task.setDuration(r.nextInt(30) + 20);
////			long endTime = currentTime + r.nextInt(80)*60*1000 + 10*60*1000;
////			Interval interval = new Interval(currentTime, endTime);
////			task.setIntervalAllowed(interval);
////			
////			planner.addTask(task);
////		}
////		
////		updateTaskPlan("sdfsd");
////
////		System.out.println(planner.getTasks()  );
////		System.out.println(planner.getPlan());
//		
//	}
//
//	private void addTask(Task task){
//
//		String ip = "";
//		for(String key : deviceList.keySet()){
//			System.out.println(key);
//			if(deviceList.get(key).equals(lastSelectedDevice)){
//				ip = key;
//				break;
//			}
//		}
//		
////		TODO change
////		task.setDevice(nodeReciver.getDevice(ip));
//		task.setDevice(devices.get(ip));
//
//		tasks.put(task.getId(), task);
//		final Button taskBut = new Button(task.getName());
//		taskBut.setSizeFull();
//		taskBut.setStyleName("black-task");
//		
//		taskList.put(task.getId(), taskBut);
//		
//		ui.access(new Runnable() {
//			
//			@Override
//			public void run() {
//				taskPanelVL.addComponent(taskBut);
//				ui.push();
//			}
//		});
//	}
//	
//	public void addDevice(final Device device){
//
//		final Button deviceBut = new Button(device.getName());
//		
//		deviceBut.setSizeFull();
//		
//		if(device.getAvailability())
//			deviceBut.setStyleName("available-device");
//		else deviceBut.setStyleName("unavailable-device");
//		
//		deviceBut.addClickListener(new ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				deviceDescription.setReadOnly(false);
//				deviceDescription.setValue(device.getDescription());
//				deviceDescription.setReadOnly(true);
//				
//				lastSelectedDevice = deviceBut;
//			}
//		});
//		
//		ui.access(new Runnable() {
//			
//			@Override
//			public void run() {
//				devicePanelLayout.addComponent(deviceBut);
//				ui.push();
//			}
//		});
//	}
//	
//	public void updateDeviceAvailability(final Device device){
//		String ip = device.getAddress();
//		final Button deviceBut = deviceList.get(ip);
//		
//		ui.access(new Runnable() {
//			
//			@Override
//			public void run() {
//				if(device.getAvailability())
//					deviceBut.setStyleName("available-device");
//				else deviceBut.setStyleName("unavailable-device");
//				ui.push();
//			}
//		});
//	}
//	
//	private class TestUiTask extends TimerTask{
//
//		private int count = 0;
//		
//		@Override
//		public void run() {
//			
////			DEVICE ADDING
////			Device device = new Device("Device " + count, "Address " + count, true);
////			addDevice(device);
//			
////			CHANGE AVAILAILITY
////			if(count > 4) count = 0;
////			String ip = "Address " + (count +1);
////			Device device = new Device("Device " + (count+1),ip,false);
////			device.setDescription("Device "+(count+1)+" decription");
////			updateDeviceAvailability(device);
//			
//			
//			count++;
//		}
//		
//	}
}





























