package ru.smartsolutions.mesgi.planner;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.http.HttpService;

import ru.smartsolutions.mesgi.planner.logic.NodeReciever;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private HttpService httpService;

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		
//		получение Http сервиса 
		ServiceReference reference = 
				context.getServiceReference(HttpService.class);
		httpService = (HttpService) context.getService(reference);
		System.out.println(httpService);
		
//		регистрация сервлета с UI
		PlannerUI.PlannerUIServlet servlet = new PlannerUI.PlannerUIServlet();
		httpService.registerServlet("/*", servlet,
				null,null);

//		запуск слушателя сообщений о состоянии устройтсв 
		NodeReciever nodeReciever = new NodeReciever();
		context.registerService(EventHandler.class, 
				nodeReciever, 
				getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
		
		System.out.println("Planner started");
	}
	
	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.context = null;
		httpService.unregister("/*");
		System.out.println("Planner stopped");
	}
	
	public static BundleContext getContext(){
		return context;
	}
}
