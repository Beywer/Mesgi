package ru.smartsolutions.mesgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class Activator implements BundleActivator {

	private BundleContext context;
	
	public void start(BundleContext context) {
		this.context = context;
		context.registerService(EventHandler.class, 
				new NodeReciver(), 
				getHandlerServiceProperties("ru/smartsol/spectrum/rpi/nodescanner"));
		
		System.out.println("Started nodeReciver service");
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
		System.out.println("Stoped nodeReciver service");
	}
	
	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
}
