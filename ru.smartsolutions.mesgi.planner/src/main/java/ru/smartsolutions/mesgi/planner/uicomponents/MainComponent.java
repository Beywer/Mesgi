package ru.smartsolutions.mesgi.planner.uicomponents;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.planner.logic.NodeReciever;
import ru.smartsolutions.mesgi.planner.logic.Planner;
import ru.smartsolutions.mesgi.planner.model.Device;
import ru.smartsolutions.mesgi.planner.model.PlannedTask;
import ru.smartsolutions.mesgi.planner.model.Task;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class MainComponent extends HorizontalLayout{

	private static final long serialVersionUID = -7389175932554905939L;

//	Список устройств
	private VerticalLayout leftVerticalLayout;
	private Map<String, Button> deviceList;
	private VerticalLayout devicePanelLayout;
	//Описание устройств
	private TextArea deviceDescription;
	
//	Список задач
	private VerticalLayout rightVerticalLayout;
	private VerticalLayout taskPanelVL;
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
	
	private Button lastSelectedDevice;
	
	private Map<String, Device> devices;
	
	public MainComponent(UI ui) {
		
		this.ui = ui;
		this.setSizeFull();
		
		planner = new Planner();
		lastSelectedDevice = null;
		
		devices = new HashMap<String, Device>();

		buildInterface();
		createListeners();

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
		Timer timer = new Timer();
		timer.schedule(new TestUiTask(), 1000, 1000);
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
        devicePanelLayout = new VerticalLayout();
        devicePanelLayout.setSizeUndefined();
		devicePanelLayout.setWidth(100, Unit.PERCENTAGE);
		
        deviceList = new HashMap<String, Button>();

        Panel devicePanel = new Panel(devicePanelLayout);
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
        timeLine.setWidth(107.8f, Unit.PERCENTAGE);
        
        //Список задач
        rightVerticalLayout = new VerticalLayout();
        rightVerticalLayout.setSizeFull();
        
        tasks = new HashMap<String, Task>();
        taskList = new HashMap<String, Button>();
        
        //Описание задач
        taskDescription = new TextArea("Описание устройства");
        taskDescription.setSizeFull();
        taskDescription.setReadOnly(true);
        taskDescription.setWordwrap(false);
        
        //Панель задач
        Panel taskPanel = new Panel("Задачи");
        taskPanel.setSizeFull();
        taskPanelVL = new VerticalLayout();
        taskPanelVL.setWidth(100, Unit.PERCENTAGE);
        taskPanelVL.setHeightUndefined();//для скролла ?
        taskPanel.setContent(taskPanelVL);
        
        //Кнопки задач
        addTask = new Button("Добавить");
        addTask.setSizeFull();
        addTask.setStyleName("friendly");
        removeTask = new Button("Удалить");
        removeTask.setSizeFull();
        removeTask.setStyleName("danger");
        
        HorizontalLayout buttonHL = new HorizontalLayout();
        buttonHL.setWidth(100, Unit.PERCENTAGE);
        buttonHL.setHeightUndefined();
        buttonHL.addComponent(addTask);
        buttonHL.addComponent(removeTask);
        buttonHL.setExpandRatio(addTask, 1);
        buttonHL.setExpandRatio(removeTask, 1);
        
        taskPanelVL.addComponent(buttonHL);
        
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
	
	private void createListeners(){
		
		addTask.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if(lastSelectedDevice != null){
					final TaskCreaterWindow window = new TaskCreaterWindow();
					UI.getCurrent().addWindow(window);
					
					window.addCloseListener(new CloseListener() {
						
						@Override
						public void windowClose(CloseEvent e) {
							System.out.println(window);
							addTask(window.getTask());
						}
					});
					
				} else UI.getCurrent().addWindow(new MessageBox(
						"\nНе было выбрано ни одно устройство."
						+ "\nНажмите по одному из устройств из списка, чтобы выбрать его."));
			}
		});
		
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
		
//		DEVICES
		for(int i = 0 ; i < 5 ; i++){
			final Device device = new Device("Device " + (i + 1),
					"Address " + (i +1), true);
			device.setDescription("Device "+(i+1)+" decription");
			
			final Button deviceBut = new Button(device.getName());
			
			deviceBut.setSizeFull();
			
			if(device.getAvailability())
				deviceBut.setStyleName("available-device");
			else deviceBut.setStyleName("unavailable-device");
			
			deviceBut.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					deviceDescription.setReadOnly(false);
					deviceDescription.setValue(device.getDescription());
					deviceDescription.setReadOnly(true);
					
					lastSelectedDevice = deviceBut;
				}
			});
			
			deviceList.put(device.getAddress(), deviceBut);
			devices.put(device.getAddress(), device);
			devicePanelLayout.addComponent(deviceBut);
		}
		
//		PLANNER
//		long currentTime = System.currentTimeMillis();
//		Random r = new Random();
//		
//		for(int i = 0 ; i < 5; i++){
//			Task task = new Task("Task " + i);
//			task.setDuration(r.nextInt(30) + 20);
//			long endTime = currentTime + r.nextInt(80)*60*1000 + 10*60*1000;
//			Interval interval = new Interval(currentTime, endTime);
//			task.setIntervalAllowed(interval);
//			
//			planner.addTask(task);
//		}
//		
//		updateTaskPlan("sdfsd");
//
//		System.out.println(planner.getTasks()  );
//		System.out.println(planner.getPlan());
		
	}

	private void addTask(Task task){

		String ip = "";
		for(String key : deviceList.keySet()){
			System.out.println(key);
			if(deviceList.get(key).equals(lastSelectedDevice)){
				ip = key;
				break;
			}
		}
		
//		task.setDevice(nodeReciver.getDevice(ip));
		task.setDevice(devices.get(ip));

		tasks.put(task.getId(), task);
		final Button taskBut = new Button(task.getName());
		taskBut.setSizeFull();
		taskBut.setStyleName("black-task");
		
		taskList.put(task.getId(), taskBut);
		
		ui.access(new Runnable() {
			
			@Override
			public void run() {
				taskPanelVL.addComponent(taskBut);
				ui.push();
			}
		});
	}
	
	public void addDevice(final Device device){

		final Button deviceBut = new Button(device.getName());
		
		deviceBut.setSizeFull();
		
		if(device.getAvailability())
			deviceBut.setStyleName("available-device");
		else deviceBut.setStyleName("unavailable-device");
		
		deviceBut.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				deviceDescription.setReadOnly(false);
				deviceDescription.setValue(device.getDescription());
				deviceDescription.setReadOnly(true);
				
				lastSelectedDevice = deviceBut;
			}
		});
		
		ui.access(new Runnable() {
			
			@Override
			public void run() {
				devicePanelLayout.addComponent(deviceBut);
				ui.push();
			}
		});
	}
	
	public void updateDeviceAvailability(final Device device){
		String ip = device.getAddress();
		final Button deviceBut = deviceList.get(ip);
		
		ui.access(new Runnable() {
			
			@Override
			public void run() {
				if(device.getAvailability())
					deviceBut.setStyleName("available-device");
				else deviceBut.setStyleName("unavailable-device");
				ui.push();
			}
		});
	}
	
	private class TestUiTask extends TimerTask{

		private int count = 0;
		
		@Override
		public void run() {
			
//			DEVICE ADDING
//			Device device = new Device("Device " + count, "Address " + count, true);
//			addDevice(device);
			
//			CHANGE AVAILAILITY
//			if(count > 4) count = 0;
//			String ip = "Address " + (count +1);
//			Device device = new Device("Device " + (count+1),ip,false);
//			device.setDescription("Device "+(count+1)+" decription");
//			updateDeviceAvailability(device);
			
			
			count++;
		}
		
	}
}





























