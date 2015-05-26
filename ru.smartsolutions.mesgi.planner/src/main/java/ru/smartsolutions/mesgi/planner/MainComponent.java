package ru.smartsolutions.mesgi.planner;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;

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
				
				System.out.println("planner: availability " + availability.getValue());
				System.out.println("planner: name " + availability.getValue());
				
				if(availability.getValue().equals("available")){
					System.out.println("\tplanner: color green");
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
		
		setTreeData();
	}
	
	private void setTreeData(){
		Object key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 1");
		deviceContainer.getContainerProperty(key, "availability").setValue("available");
		deviceContainer.setChildrenAllowed(key, false);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 2");
		deviceContainer.getContainerProperty(key, "availability").setValue("unavailable");
		deviceContainer.setChildrenAllowed(key, false);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 3");
		deviceContainer.getContainerProperty(key, "availability").setValue("available");
		deviceContainer.setChildrenAllowed(key, false);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 4");
		deviceContainer.getContainerProperty(key, "availability").setValue("unavailable");
		deviceContainer.setChildrenAllowed(key, false);
		
		key = deviceContainer.addItem();
		deviceContainer.getContainerProperty(key, "name").setValue("Device 5");
		deviceContainer.getContainerProperty(key, "availability").setValue("available");
		deviceContainer.setChildrenAllowed(key, false);
	}
}





























