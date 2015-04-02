package ru.smartsolutions.mesgi;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class NodeReciver implements EventHandler {

	private Map<String, String> nodes;
	
	public void handleEvent(Event event) {
		String ip = (String) event.getProperty("IPv6");
		String availability = (String) event.getProperty("Availability");
		if(!nodes.containsKey(ip)){
			nodes.put(ip, availability);
			System.out.println(ip + " " + availability);
		} else {
			String oldAvailability = nodes.get(ip);
			if(!oldAvailability.equals(availability)){
				nodes.put(ip, availability);
				System.out.println(ip + " " + availability);
			}
		}
	}
	
	public NodeReciver() {
		nodes = new HashMap<String, String>();
	}

}
