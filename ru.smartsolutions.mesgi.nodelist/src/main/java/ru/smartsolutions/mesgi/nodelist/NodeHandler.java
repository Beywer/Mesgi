package ru.smartsolutions.mesgi.nodelist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class NodeHandler implements EventHandler {

	private static TextArea textArea;
	private static VerticalLayout layout;
	private static MainPage mainPage;
	private Map<String, String> nodes;
	
	private long time;
	
	public NodeHandler() {
		nodes = new HashMap<String, String>();
		textArea = null;
		time = System.currentTimeMillis();
	}	
	
	public static void setTextArea(TextArea textArea){
		NodeHandler.textArea = textArea;
		System.out.println("Setting tam >>>>>> area  " + NodeHandler.textArea);
	}
	
	public static void setMainPage(MainPage mainPage){
		NodeHandler.mainPage = mainPage;
	}
	
	@Override
	public void handleEvent(final Event event) {

		String ip = (String) event.getProperty("IPv6");
		String avilability = (String) event.getProperty("Availability");
		nodes.put(ip, avilability);
		System.out.println(mainPage.getUI());
		if(System.currentTimeMillis() - time > 6000){
			System.out.println(getText());
			time = System.currentTimeMillis();
		}
		
		if(mainPage != null){
			mainPage.setData(getText());
		}
	}
	
	private String getText(){
		StringBuilder text = new StringBuilder();
		for(String key : nodes.keySet()) {
			text.append(key + nodes.get(key) + "\n");
		}
		return text.toString();
	}
}
