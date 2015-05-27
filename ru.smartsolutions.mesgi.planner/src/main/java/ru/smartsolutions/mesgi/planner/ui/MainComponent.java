package ru.smartsolutions.mesgi.planner.ui;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.planner.model.Device;

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

public class MainComponent extends HorizontalLayout {

	private static final long serialVersionUID = -7389175932554905939L;


//	Список устройств
	private VerticalLayout verticalLayout;
	private Map<String, Device> devices;
	private VerticalLayout deviceLayout;
	//Описание устройств
	private TextArea deviceDescription;
	
//	Список задач
	private Tree taskTree;
	private HierarchicalContainer taskContainer;
	
//	Базовые компоненты
	private UI ui;
	private BundleContext context; 
	private NodeReciever nodeReciver; //eventadmin ЕventHandler
	
	
	public MainComponent(UI ui) {
		
		this.ui = ui;
		this.setSizeFull();

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
	}

	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
	
	private void buildInterface(){

        //Layout для списка устройств и описания
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        //Список устройств
        deviceLayout = new VerticalLayout();
        deviceLayout.setSizeUndefined();
		deviceLayout.setWidth(100, Unit.PERCENTAGE);
		
        devices = new HashMap<String, Device>();

        Panel panel = new Panel(deviceLayout);
        panel.setCaption("Устройства");
        panel.setSizeFull();
        
        //Описание устройтсва
        deviceDescription = new TextArea("Описание устройства");
        deviceDescription.setSizeFull();
        deviceDescription.setWordwrap(false);
        
        //Компоновка списка устройств и описания
        verticalLayout.addComponent(panel);
        verticalLayout.addComponent(deviceDescription);
        verticalLayout.setExpandRatio(panel, 5);
        verticalLayout.setExpandRatio(deviceDescription, 3);

        //Таймлайн
        VTimeLine timeLine = new VTimeLine();
        timeLine.setSizeFull();
        timeLine.setStyleName("timeline-marg");
        
        //Список задач
		taskTree = new Tree("Задачи");
		taskContainer = new HierarchicalContainer();
		taskTree.setContainerDataSource(taskContainer);
        
        //Компоновка всего интерфейса
		this.addComponent(verticalLayout);
	    this.addComponent(timeLine);
		this.addComponent(taskTree);
		
		this.setExpandRatio(verticalLayout, 1.5f);
		this.setExpandRatio(timeLine, 3);
		this.setExpandRatio(taskTree, 1);
		
//		Работа с тестовыми данными
		setTestData();
		Timer timer = new Timer();
		timer.schedule(new DeviceChangeTask(), 1000, 1000);
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
	
	private void setTestData(){
		
		Device device = new Device("TestDevice 1", "TestDevice 1 address", true);
		device.setDescription("TestDevice 1 description");
		Button button = device.getButton();
		
		deviceLayout.addComponent(button);
		devices.put(device.getAddress(), device);
		
		device = new Device("TestDevice 2", "TestDevice 2 address", false);
		device.setDescription("TestDevice 2 description");
		button = device.getButton();
		
		deviceLayout.addComponent(button);
		devices.put(device.getAddress(), device);
		
		device = new Device("TestDevice 3", "TestDevice 3 address", true);
		device.setDescription("TestDevice 3 description");
		button = device.getButton();
		
		deviceLayout.addComponent(button);
		devices.put(device.getAddress(), device);
		
		for(String key : devices.keySet()){
			final Device dev = devices.get(key);
			
			dev.getButton().addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					deviceDescription.setReadOnly(false);
					deviceDescription.setValue("Ip adress : " + dev.getAddress() + 
									"\n\n" +dev.getDescription());
					deviceDescription.setReadOnly(true);
				}
			});
		}
	}
}





























