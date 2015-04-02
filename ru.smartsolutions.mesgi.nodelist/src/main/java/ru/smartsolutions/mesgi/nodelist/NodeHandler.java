package ru.smartsolutions.mesgi.nodelist;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class NodeHandler implements EventHandler {

	private static TextArea textArea;
	private static VerticalLayout layout;
	private Map<String, String> nodes;
	
	public NodeHandler() {
		nodes = new HashMap<String, String>();
		textArea = null;
		System.out.println("Constr  " + NodeHandler.textArea);
	}	
	
	public static void setTextArea(TextArea textArea){
		NodeHandler.textArea = textArea;
		System.out.println("Setting area  " + NodeHandler.textArea);
	}
	
	public static void setLayout(VerticalLayout layout){
		NodeHandler.layout = layout;
	}
	
	@Override
	public void handleEvent(Event event) {
		String ip = (String) event.getProperty("IPv6");
		String avilability = (String) event.getProperty("Avilability");
		nodes.put(ip, avilability);
		layout.addComponent(new Label(ip +  " " + avilability));
		if(textArea != null){
			System.out.println("not null  " + NodeHandler.textArea + "  " + layout.getComponentCount());
			textArea.setValue(nodes.toString());
		}
	}

}
