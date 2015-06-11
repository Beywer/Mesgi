package ru.smartsolutions.mesgi.nodescanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;

@SuppressWarnings("rawtypes")
public class Activator implements BundleActivator {

	private BundleContext context;
	private ServiceTracker serviceTracker;
	private NodeScanner scanner;
	private ITransporter transporter;
	
	public void start(BundleContext context) throws Exception {
		this.context = context;
		
		EventAdmin eventAdmin = bindEventAdmin(this.context);
		
//		получение транспортного сервиса
		ServiceReference transporterReference =
				context.getServiceReference(ITransporter.class);
		transporter = (ITransporter) context.getService(transporterReference);
		
		scanner = new NodeScanner(eventAdmin, transporter);
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
