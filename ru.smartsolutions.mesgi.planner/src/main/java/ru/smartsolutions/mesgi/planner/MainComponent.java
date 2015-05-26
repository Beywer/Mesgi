package ru.smartsolutions.mesgi.planner;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import ru.jnanovaadin.widgets.timeline.VTimeLine;
import ru.smartsolutions.mesgi.planner.model.Device;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Tree;
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

		if(context == null){
			context = Activator.getContext();
			nodeReciver = new NodeReciver(ui, deviceTree, deviceContainer);
			
			context.registerService(EventHandler.class, 
			nodeReciver, 
			getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
			System.out.println("Crated eventHandler in component");
		}
	}

	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
	
	private void buildInterface(){

		deviceTree = new Tree("Устройства");
		deviceContainer = new HierarchicalContainer();
		deviceTree.setContainerDataSource(deviceContainer);
		
		System.out.println("Planner :" + deviceTree);
		System.out.println("Planner :" + deviceContainer);
		
		taskTree = new Tree("Задачи");
		taskContainer = new HierarchicalContainer();
		taskTree.setContainerDataSource(taskContainer);
		
        VTimeLine timeLine = new VTimeLine();
        timeLine.setSizeFull();

//    	List<Map<String,String>> timeLineData = new ArrayList<>();
//    	timeLineData.add(VTimeLine.createRow(new Date().getTime(),
//    			new Date().getTime() + 1000000, "Task 0", "Device 0"));
//        
//        timeLine.setDatatable(timeLineData);
//        timeLine.setCurrentInterval(new Date().getTime() - 1000, new Date().getTime() + 1000000);
        
		this.addComponent(deviceTree);
	    this.addComponent(timeLine);
		this.addComponent(taskTree);
		
		this.setExpandRatio(deviceTree, 1);
		this.setExpandRatio(timeLine, 3);
		this.setExpandRatio(taskTree, 1);
	}
}
