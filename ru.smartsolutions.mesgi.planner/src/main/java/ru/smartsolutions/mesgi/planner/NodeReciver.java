package ru.smartsolutions.mesgi.planner;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.smartsolutions.mesgi.planner.model.Device;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

public class NodeReciver implements EventHandler {

	private Map<String, String> nodes;
	private Map<String, Object> keys;
	
	private UI ui;
	private Tree deviceTree;
	private HierarchicalContainer deviceContainer;
	
	public void handleEvent(Event event) {
		
		String ip = (String) event.getProperty("IPv6");
		String availability = (String) event.getProperty("Availability");
		//Если узел еще не был в сети
		if(!nodes.containsKey(ip)){ 
			nodes.put(ip, availability);
			System.out.println("Planner: recieved new device " + ip + " " + availability);
			addDeviceAvailability(ip, availability);
			
		} else {
			String oldAvailability = nodes.get(ip);
			//Если у уже известного узла именилась доступность
			if(!oldAvailability.equals(availability)){
				nodes.put(ip, availability);
				System.out.println("Planner: recieved changed state" + ip + " " + availability);
//				addDeviceAvailability(ip, availability);
			}
		}
	}
	
	private void changedDeviceAvailability(final String ip, final String availability){
		ui.access(new Runnable() {
			
			@Override
			public void run() {
				Object key = keys.get(ip);
				deviceContainer.getContainerProperty(key, "availability").setValue(availability);
				
				ui.push();
			}
		});
	}
	
	private void addDeviceAvailability(final String ip, final String availability){
		ui.access(new Runnable() {
			
			@Override
			public void run() {
				
				Object ipKey = deviceContainer.addItem();
				deviceContainer.getContainerProperty(ipKey, "name").setValue(ip);
				deviceContainer.getContainerProperty(ipKey, "availability").setValue(availability);
				deviceContainer.setChildrenAllowed(ipKey, false);
				
				keys.put(ip, ipKey);
				
				ui.push();
				
				System.out.println("\tPlanner: pushed changes");
			}
		});
	}
	
	public NodeReciver(UI ui, Tree deviceTree,
			HierarchicalContainer deviceContainer) {

		this.ui = ui;
		this.deviceTree = deviceTree;
		this.deviceContainer = deviceContainer;

		nodes = new HashMap<String, String>();
		keys = new HashMap<String, Object>();
	}
}
