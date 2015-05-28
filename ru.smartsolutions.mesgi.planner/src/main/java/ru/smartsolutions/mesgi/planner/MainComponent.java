package ru.smartsolutions.mesgi.planner;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.Interval;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.planner.model.Device;
import ru.smartsolutions.mesgi.planner.model.PlannedTask;
import ru.smartsolutions.mesgi.planner.model.Task;

import com.google.gwt.visualization.client.DataTable;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MainComponent extends HorizontalLayout implements DevicesObserverListener {

	private static final long serialVersionUID = -7389175932554905939L;

//	Список устройств
	private VerticalLayout leftVerticalLayout;
	private Map<String, Device> devices;
	private VerticalLayout deviceLayout;
	//Описание устройств
	private TextArea deviceDescription;
	
//	Список задач
	private VerticalLayout rightVerticalLayout;
	private Map<String, Task> tasks;
	private Map<String, Button> taskList;
	//Описание задач
	private TextArea taskDescription;
	
//	Добавление удаление задач
	private Button addTask;
	private Button removeTask;
	
//	Таймлайн
	private VTimeLine timeLine;
	
//	Базовые компоненты
	private UI ui;
	private BundleContext context; 
	private NodeReciever nodeReciver; //eventadmin ЕventHandler
	private Planner planner;
	
	
	public MainComponent(UI ui) {
		
		this.ui = ui;
		this.setSizeFull();
		
		planner = new Planner();

		buildInterface();

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
		setTestData();
//		Timer timer = new Timer();
//		timer.schedule(new DeviceChangeTask(), 1000, 1000);
	}

	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
	
	private void buildInterface(){

        //Layout для списка устройств и описания
        leftVerticalLayout = new VerticalLayout();
        leftVerticalLayout.setSizeFull();

        //Список устройств
        deviceLayout = new VerticalLayout();
        deviceLayout.setSizeUndefined();
		deviceLayout.setWidth(100, Unit.PERCENTAGE);
		
        devices = new HashMap<String, Device>();

        Panel devicePanel = new Panel(deviceLayout);
        devicePanel.setCaption("Устройства");
        devicePanel.setSizeFull();
        
        //Описание устройтсва
        deviceDescription = new TextArea("Описание устройства");
        deviceDescription.setSizeFull();
        deviceDescription.setReadOnly(true);
        deviceDescription.setWordwrap(false);
        
        //Компоновка списка устройств и описания
        leftVerticalLayout.addComponent(devicePanel);
        leftVerticalLayout.addComponent(deviceDescription);
        leftVerticalLayout.setExpandRatio(devicePanel, 5);
        leftVerticalLayout.setExpandRatio(deviceDescription, 3);

        //Таймлайн
        timeLine = new VTimeLine();
//        timeLine.setSizeFull();
        timeLine.setWidth(107.8f, Unit.PERCENTAGE);
//        timeLine.setStyleName("timeline-marg");
        
        //Список задач
        rightVerticalLayout = new VerticalLayout();
        rightVerticalLayout.setSizeFull();
        
        //Описание задач
        taskDescription = new TextArea("Описание устройства");
        taskDescription.setSizeFull();
        taskDescription.setReadOnly(true);
        taskDescription.setWordwrap(false);
        
        //Панель задач
        Panel taskPanel = new Panel("Задачи");
        taskPanel.setSizeFull();
        VerticalLayout panelVL = new VerticalLayout();
        panelVL.setWidth(100, Unit.PERCENTAGE);
        panelVL.setHeightUndefined();//для скролла ?
        taskPanel.setContent(panelVL);
        
        //Кнопки задач
        addTask = new Button("Добавить");
        addTask.setSizeFull();
        addTask.setStyleName("green-button");
        removeTask = new Button("Удалить");
        removeTask.setSizeFull();
        removeTask.setStyleName("red-button");
        
        HorizontalLayout buttonHL = new HorizontalLayout();
        buttonHL.setWidth(100, Unit.PERCENTAGE);
        buttonHL.setHeightUndefined();
        buttonHL.addComponent(addTask);
        buttonHL.addComponent(removeTask);
        buttonHL.setExpandRatio(addTask, 1);
        buttonHL.setExpandRatio(removeTask, 1);
        
        panelVL.addComponent(buttonHL);
        
        rightVerticalLayout.addComponent(taskPanel);
        rightVerticalLayout.addComponent(taskDescription);
        rightVerticalLayout.setExpandRatio(taskPanel, 5);
        rightVerticalLayout.setExpandRatio(taskDescription, 3);
        
        //Компоновка всего интерфейса
		this.addComponent(leftVerticalLayout);
	    this.addComponent(timeLine);
		this.addComponent(rightVerticalLayout);
		
		this.setExpandRatio(leftVerticalLayout, 1.5f);
		this.setExpandRatio(timeLine, 4);
		this.setExpandRatio(rightVerticalLayout, 1.5f);
	}
	
	private class DeviceChangeTask extends TimerTask{

		private int count = 1;
		private boolean avai = false;
		
		@Override
		public void run() {
			ui.access(new Runnable() {
				
				@Override
				public void run() {
					
//					ДОБАВЛЕНИЕ ПУШЕМ
//					final Device device = new Device("Device " + count, "Address " + count, true);
//					device.setDescription("Description " +count);
//					
//					device.getButton().addClickListener(new ClickListener() {
//						
//						@Override
//						public void buttonClick(ClickEvent event) {
//							deviceDescription.setReadOnly(false);
//							deviceDescription.setValue("Ip adress : " + device.getAddress() + 
//											"\n\n" + device.getDescription());
//							deviceDescription.setReadOnly(true);
//						}
//					});
//					
//					deviceLayout.addComponent(device.getButton());
					
//					ОБНОВЕЛЕНИЕ ПУШЕМ
					if(count > 3) count = 1;
					Device device =  devices.get("TestDevice " + count + " address");
					device.setAvailability(!device.getAvailability());
					
					ui.push();
					
//					crutch
//					Object key = deviceContainer.addItem();
//					deviceContainer.getContainerProperty(key, "name").setValue("");
//					deviceContainer.setChildrenAllowed(key, false);
//					
//					ui.push();
//					
//					deviceContainer.removeItem(key);
//					end of crutch
				}
			});
			count++;
		}
		
	}
	
	private void updateTaskPlan(String deviceIp){
		List<Map<String,String>> timeLineData = new ArrayList<>();
		
		for(PlannedTask task : planner.getPlan()){
			timeLineData.add(VTimeLine.createRow
					(task.getPlannedInterval().getStartMillis(),
					 task.getPlannedInterval().getEndMillis(), task.getId(), deviceIp));
		}
		
		timeLine.setDatatable(timeLineData);
		
//		ui.push();
		
	}
	
	private void setTestData(){
		
		long currentTime = System.currentTimeMillis();
		Random r = new Random();
		
		for(int i = 0 ; i < 5; i++){
			Task task = new Task("Task " + i);
			task.setDuration(r.nextInt(30) + 20);
			long endTime = currentTime + r.nextInt(80)*60*1000 + 10*60*1000;
			Interval interval = new Interval(currentTime, endTime);
			task.setIntervalAllowed(interval);
			
			planner.addTask(task);
		}
		
		updateTaskPlan("sdfsd");

		System.out.println(planner.getTasks()  );
		System.out.println(planner.getPlan());
		
//		Device device = new Device("TestDevice 1", "TestDevice 1 address", true);
//		device.setDescription("TestDevice 1 description");
//		Button button = device.getButton();
//		
//		deviceLayout.addComponent(button);
//		devices.put(device.getAddress(), device);
//		
//		device = new Device("TestDevice 2", "TestDevice 2 address", false);
//		device.setDescription("TestDevice 2 description");
//		button = device.getButton();
//		
//		deviceLayout.addComponent(button);
//		devices.put(device.getAddress(), device);
//		
//		device = new Device("TestDevice 3", "TestDevice 3 address", true);
//		device.setDescription("TestDevice 3 description");
//		button = device.getButton();
//		
//		deviceLayout.addComponent(button);
//		devices.put(device.getAddress(), device);
//		
//		for(String key : devices.keySet()){
//			final Device dev = devices.get(key);
//			
//			dev.getButton().addClickListener(new ClickListener() {
//				
//				@Override
//				public void buttonClick(ClickEvent event) {
//					deviceDescription.setReadOnly(false);
//					deviceDescription.setValue("Ip adress : " + dev.getAddress() + 
//									"\n\n" +dev.getDescription());
//					deviceDescription.setReadOnly(true);
//				}
//			});
//		}
	}

	@Override
	public void fireAddedDevice(Device device) {
		
	}
}





























