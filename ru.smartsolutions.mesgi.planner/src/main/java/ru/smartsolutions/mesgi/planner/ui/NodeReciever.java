package ru.smartsolutions.mesgi.planner.ui;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.smartsolutions.mesgi.planner.model.Device;

import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;

public class NodeReciever implements EventHandler {

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
				changedDeviceAvailability(ip, availability);
			}
		}
	}
	
	private void changedDeviceAvailability(final String ip, final String availability){
		ui.accessSynchronously(new Runnable() {
			
			@Override
			public void run() {
				Object key = keys.get(ip);
				Property<Device> prop = deviceContainer.getContainerProperty(key, "device");
				Device device = prop.getValue();
				
				boolean available = false;
				if(availability.equals("available")) available = true;
				device.setAvailability(available);

				deviceContainer.getContainerProperty(key, "device").setValue(device);

//				crutch
				key = deviceContainer.addItem();
				deviceContainer.getContainerProperty(key, "name").setValue("");
				deviceContainer.setChildrenAllowed(key, false);
				
				ui.push();
				
				deviceContainer.removeItem(key);
//				end of crutch
			}
		});
	}
	
	private void addDeviceAvailability(final String ip, final String availability){
		ui.accessSynchronously(new Runnable() {
			
			@Override
			public void run() {
				
				boolean available = false;
				if(availability.equals("available")) available = true;
				Device device = new Device(ip, ip, available);
				device.setDescription("ip : " + ip + "\n\n" + "Some description of this device");
				
				Object ipKey = deviceContainer.addItem();
				deviceContainer.getContainerProperty(ipKey, "name").setValue(device.getName());
				deviceContainer.getContainerProperty(ipKey, "device").setValue(device);
				deviceContainer.setChildrenAllowed(ipKey, false);
				
				keys.put(ip, ipKey);
				
				ui.push();
			}
		});
	}
	
	public NodeReciever(UI ui, Tree deviceTree,
			HierarchicalContainer deviceContainer) {

		this.ui = ui;
		this.deviceTree = deviceTree;
		this.deviceContainer = deviceContainer;

		nodes = new HashMap<String, String>();
		keys = new HashMap<String, Object>();
	}
}
