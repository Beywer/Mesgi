package ru.smartsolutions.mesgi.nodelist;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class Activator implements BundleActivator {

	private BundleContext context;
	public static MainPage mainPage;
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		mainPage = new MainPage();
		context.registerService(EventHandler.class, 
				mainPage, 
				getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
		System.out.println("Created handler  ");
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
