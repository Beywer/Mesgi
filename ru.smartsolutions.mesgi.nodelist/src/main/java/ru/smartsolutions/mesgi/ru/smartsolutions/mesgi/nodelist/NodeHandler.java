package ru.smartsolutions.mesgi.ru.smartsolutions.mesgi.nodelist;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.vaadin.ui.TextArea;

public class NodeHandler implements EventHandler {

	private static TextArea textArea;
	private Map<String, String> nodes;
	
	public NodeHandler() {
		nodes = new HashMap<String, String>();
	}	
	
	public static void setTextArea(TextArea textArea){
		NodeHandler.textArea = textArea;
	}
	
	@Override
	public void handleEvent(Event event) {
		String ip = (String) event.getProperty("IPv6");
		String avilability = (String) event.getProperty("Avilability");
		nodes.put(ip, avilability);
		textArea.setValue(nodes.toString());
	}

}
