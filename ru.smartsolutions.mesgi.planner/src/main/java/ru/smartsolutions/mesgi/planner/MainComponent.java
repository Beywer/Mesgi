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
import ru.smartsolutions.mesgi.planner.model.Device;

import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
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
	private NodeReciever nodeReciver;
	
	private TextArea deviceDescription;
	
	private Timer timer;
	
	private List<Object> keys;
	private Object key0;
	
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

		deviceContainer = new HierarchicalContainer();
		deviceContainer.addContainerProperty("name", String.class, "undefinded");
		deviceContainer.addContainerProperty("device", Device.class, null);
		
		deviceTree = new Tree("Устройства");
		deviceTree.setItemCaptionPropertyId("name");
		ItemStyleGenerator styleGenerator = new ItemStyleGenerator() {
			
			@Override
			public String getStyle(Tree source, Object itemId) {
				
				Property<Device> prop = source.getContainerProperty(itemId, "device");
				Device device = prop.getValue();
				
				if(device != null)
					if(device.getAvailability()){
//						System.out.println("\tplanner: "+device.getName()+"  will green");
						return "text-green";
					} else {
//						System.out.println("\tplanner: "+device.getName()+" will red");
						return "text-red";
					}
				else return "";
			}
		};
		deviceTree.setItemStyleGenerator(styleGenerator);
		deviceTree.setContainerDataSource(deviceContainer);
		
		taskTree = new Tree("Задачи");
		taskContainer = new HierarchicalContainer();
		taskTree.setContainerDataSource(taskContainer);
		
        VTimeLine timeLine = new VTimeLine();
        timeLine.setSizeFull();
        timeLine.setStyleName("timeline-marg");
        
        deviceDescription = new TextArea("Описание устройства");
        deviceDescription.setSizeFull();
        deviceDescription.setWordwrap(false);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();

        Panel panel = new Panel(deviceTree);
        panel.setCaption("Устройства");
        panel.setSizeFull();
        
        verticalLayout.addComponent(panel);
        verticalLayout.addComponent(deviceDescription);
        
        verticalLayout.setExpandRatio(panel, 5);
        verticalLayout.setExpandRatio(deviceDescription, 3);
        
		this.addComponent(verticalLayout);
	    this.addComponent(timeLine);
		this.addComponent(taskTree);
		
		this.setExpandRatio(verticalLayout, 1.5f);
		this.setExpandRatio(timeLine, 3);
		this.setExpandRatio(taskTree, 1);
		
		deviceTree.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				Property<Device> devicePropertty = event.getItem().getItemProperty("device");
				Device device = devicePropertty.getValue();
		        deviceDescription.setReadOnly(false);
				deviceDescription.setValue("Ip : " + device.getAddress() + "\n\n"
						+ device.getDescription());
		        deviceDescription.setReadOnly(true);
			}
		});
		
		keys = new ArrayList<>();
		setTreeData();
		timer = new Timer();
		timer.schedule(new DeviceChangeTask(), 1000, 1000);
	}
	
	private class DeviceChangeTask extends TimerTask{

		private int count = 0;
		private boolean avai = false;
		
		@Override
		public void run() {
			if(count > 4) count = 0;
			ui.access(new Runnable() {
				
				@Override
				public void run() {
					Property<Device> prop = deviceContainer.getContainerProperty(keys.get(count), "device");
					Device device = prop.getValue();
					
					if(device.getAvailability())  device.setAvailability(false);
					else device.setAvailability(true);
					
					deviceContainer.getContainerProperty(keys.get(count), "device").setValue(device);
					
//					System.out.println("\tTimerTask: inverrt prop at device "  + (count + 1));
					
//					crutch
					Object key = deviceContainer.addItem();
					deviceContainer.getContainerProperty(key, "name").setValue("");
					deviceContainer.setChildrenAllowed(key, false);
					
					ui.push();
					
					deviceContainer.removeItem(key);
//					end of crutch
				}
			});
			count++;
		}
		
	}
	
	private void setTreeData(){
		Object key = deviceContainer.addItem();
		Device device = new Device("SmartSputnik 1", "fe80:feef:ba27:ebff:fe4c:f09d", true);
		device.setDescription("Description 1 \n Между прочим, это высокотехнологичное устройство"
				+ ", которому не следует переходить дорогу.");
		deviceContainer.getContainerProperty(key, "name").setValue(device.getName());
		deviceContainer.getContainerProperty(key, "device").setValue(device);
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);

		key = deviceContainer.addItem();
		device = new Device("Device 2", "Address 2", false);
		device.setDescription("Description 2 \n New string");
		deviceContainer.getContainerProperty(key, "name").setValue(device.getName());
		deviceContainer.getContainerProperty(key, "device").setValue(device);
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
		
		key = deviceContainer.addItem();
		device = new Device("Device 3", "Address 3", true);
		device.setDescription("Description 3 \n New string");
		deviceContainer.getContainerProperty(key, "name").setValue(device.getName());
		deviceContainer.getContainerProperty(key, "device").setValue(device);
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
		
		key = deviceContainer.addItem();
		device = new Device("Device 4", "Address 4", false);
		device.setDescription("Description 4 \n New string");
		deviceContainer.getContainerProperty(key, "name").setValue(device.getName());
		deviceContainer.getContainerProperty(key, "device").setValue(device);
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
		
		key = deviceContainer.addItem();
		device = new Device("Device 5", "Address 5", true);
		device.setDescription("Description 5 \n New string");
		deviceContainer.getContainerProperty(key, "name").setValue(device.getName());
		deviceContainer.getContainerProperty(key, "device").setValue(device);
		deviceContainer.setChildrenAllowed(key, false);
		keys.add(key);
	}
}





























