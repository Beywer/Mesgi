package ru.smartsolutions.mesgi.planner;

import java.util.TimerTask;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private HttpService httpService;
	private DeviceObserver deviceObserver;
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		
		ServiceReference reference = 
				context.getServiceReference(HttpService.class);
		httpService = (HttpService) context.getService(reference);
		System.out.println(httpService);
		
		PlannerUI.PlannerUIServlet servlet = new PlannerUI.PlannerUIServlet();
		httpService.registerServlet("/*", servlet,
				null,null);

//		deviceObserver = new DeviceObserver();
//		Component component  = servlet.getUI().getContent();
//		if(component instanceof MainComponent){
//			System.out.println("MainComponent");
//			deviceObserver.addDeviceObservable((IDeviceObservable) component);
//		}
		
		System.out.println("Planner started");
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
