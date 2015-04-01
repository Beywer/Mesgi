package ru.smartsolutions.mesgi.ru.smartsolutions.mesgi.nodelist;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class Activator implements BundleActivator {

	private BundleContext context;
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		context.registerService(EventHandler.class, 
				new NodeHandler(), 
				getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.context = null;
	}

	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
}
