package ru.smartsolutions.mesgi.nodescanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import ru.smartsolutions.mesgi.model.INodeScanner;

@SuppressWarnings("rawtypes")
public class Activator implements BundleActivator {

	private BundleContext context;
	private ServiceTracker serviceTracker;
	private NodeScanner scanner;
	
	public void start(BundleContext context) throws Exception {
		this.context = context;
		
		EventAdmin eventAdmin = bindEventAdmin(this.context);
		
		scanner = new NodeScanner(eventAdmin);
		Thread thr = new Thread(scanner);
		thr.setDaemon(true);
		thr.start();
		
		context.registerService(INodeScanner.class, scanner, null);
		
		System.out.println("Mesgi Node Scanner started");
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
		scanner.stop();
		System.out.println("Mesgi Node Scanner stopped");
	}

	@SuppressWarnings("unchecked")
	private EventAdmin bindEventAdmin(BundleContext context) {
	    serviceTracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
	    serviceTracker.open();

	    return (EventAdmin) serviceTracker.getService();
	  }
}
