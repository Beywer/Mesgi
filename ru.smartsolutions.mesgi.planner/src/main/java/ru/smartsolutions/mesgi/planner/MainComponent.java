package ru.smartsolutions.mesgi.planner;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import ru.jnanovaadin.widgets.timeline.VTimeLine;

import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ItemStyleGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MainComponent extends HorizontalLayout {

	private static final long serialVersionUID = -7389175932554905939L;

	private VerticalLayout verticalLayout;
	
//	Список устройств
	private Tree deviceTree;
	private HierarchicalContainer deviceContainer;
//	Список задач
	private Tree taskTree;
	private HierarchicalContainer taskContainer;
	
	private UI ui;
	private BundleContext context;
	private NodeReciver nodeReciver;
	
	private List<Object> keys;
	
	public MainComponent(UI ui) {
		
		this.ui = ui;
		this.setSizeFull();

		buildInterface();

//		if(context == null){
//			context = Activator.getContext();
//			nodeReciver = new NodeReciver(ui, deviceTree, deviceContainer);
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

		deviceTree = new Tree("Устройства");
		deviceTree.setItemCaptionPropertyId("name");

		ItemStyleGenerator styleGenerator = new ItemStyleGenerator() {
			
			@Override
			public String getStyle(Tree source, Object itemId) {
				
				Property<String> availability = source.getContainerProperty(itemId, "availability");
				Property<String> name = source.getContainerProperty(itemId, "availability");
				
				if(availability.getValue().equals("available")){
					System.out.println("\tplanner: "+name.getValue()+" color green");
					return "text-green";
				} else {
					System.out.println("\tplanner: color red");
					return "text-red";
				}
			}
		};
		deviceTree.setItemStyleGenerator(styleGenerator);
		
		deviceContainer = new HierarchicalContainer();
		deviceContainer.addContainerProperty("name", String.class, "undefinded");
		deviceContainer.addContainerProperty("availability", String.class, "undefinded");
		
		deviceTree.setContainerDataSource(deviceContainer);
		
		taskTree = new Tree("Задачи");
		taskContainer = new HierarchicalContainer();
		taskTree.setContainerDataSource(taskContainer);
		
        VTimeLine timeLine = new VTimeLine();
        timeLine.setSizeFull();

		this.addComponent(deviceTree);
	    this.addComponent(timeLine);
		this.addComponent(taskTree);
		
		this.setExpandRatio(deviceTree, 2);
		this.setExpandRatio(timeLine, 3);
		this.setExpandRatio(taskTree, 1);
		
		keys = new ArrayList<>();
		setTreeData();
		Timer timer = new Timer();
		timer.schedule(new DeviceChangeTask(), 3000, 2000);
		
	}
	
	private class DeviceChangeTask extends TimerTask{

		private int count = 0;
		
		@Override
		public void run() {
			if(count > 4) count = 0;
			ui.access(new Runnable() {
				
				@Override
				public void run() {
					Property<String> prop = deviceContainer.getContainerProperty(keys.get(count), "availability");
					
					String value = prop.getValue(), newValue = "";
					if(value.equals("available"))  newValue = "unavailable";
					else newValue = "available";
					
					deviceContainer.getContainerProperty(keys.get(count), "availability").setValue(newValue);
					
					System.out.println("\tTimerTask: oldProp" + value + " newProp " + newValue );
					
//					ui.push();
				}
			});
			count++;
		}
		
	}
	
	private void setTreeData(){
		Object key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 1");
		deviceContainer.getContainerProperty(key, "availability").setValue("available");
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);

		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 2");
		deviceContainer.getContainerProperty(key, "availability").setValue("unavailable");
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 3");
		deviceContainer.getContainerProperty(key, "availability").setValue("available");
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 4");
		deviceContainer.getContainerProperty(key, "availability").setValue("unavailable");
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 5");
		deviceContainer.getContainerProperty(key, "availability").setValue("available");
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
	}
}





























