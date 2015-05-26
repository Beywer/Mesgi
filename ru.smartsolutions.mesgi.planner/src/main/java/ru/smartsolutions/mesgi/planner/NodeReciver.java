package ru.smartsolutions.mesgi.planner;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

public class NodeReciver implements EventHandler {

	private Map<String, String> nodes;
	
	private UI ui;
	private Tree deviceTree;
	private HierarchicalContainer deviceContainer;
	
	public void handleEvent(Event event) {
		String ip = (String) event.getProperty("IPv6");
		String availability = (String) event.getProperty("Availability");
		//Если узел еще не был в сети
		if(!nodes.containsKey(ip)){ 
			nodes.put(ip, availability);
			System.out.println("Planner: " + ip + " " + availability);
			changeDeviceAvailability(ip, availability);
			
		} else {
			String oldAvailability = nodes.get(ip);
			//Если у уже известного узла именилась доступность
			if(!oldAvailability.equals(availability)){
				nodes.put(ip, availability);
				System.out.println("Planner: " + ip + " " + availability);
				changeDeviceAvailability(ip, availability);
			}
		}
	}
	
	private void changeDeviceAvailability(final String ip, final String availability){
		ui.access(new Runnable() {
			
			@Override
			public void run() {
				
				Object ipKey = deviceContainer.addItem();
				deviceTree.setItemCaption(ipKey, ip);
				
				Object availabilityKey = deviceContainer.addItem();
				deviceTree.setItemCaption(availabilityKey, availability);
				
				deviceTree.setParent(availabilityKey, ipKey);
				
				ui.push();
				
				System.out.println("Planner: pushed changes");
			}
		});
	}
	
	public NodeReciver(UI ui, Tree deviceTree,
			HierarchicalContainer deviceContainer) {

		System.out.println("Planner : NodeRecieverConstrunctor");
		this.ui = ui;
		this.deviceTree = deviceTree;
		this.deviceContainer = deviceContainer;

		System.out.println("Planner :" + deviceTree);
		System.out.println("Planner :" + deviceContainer);

		nodes = new HashMap<String, String>();
	}
}
