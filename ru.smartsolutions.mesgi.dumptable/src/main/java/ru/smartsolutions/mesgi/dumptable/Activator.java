package ru.smartsolutions.mesgi.dumptable;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	private BundleContext context;
	private ServiceTracker serviceTracker;
	private NodeScanner scanner;
	
	public void start(BundleContext context) throws Exception {
		this.context = context;
		
		EventAdmin eventAdmin = bindEventAdmin(this.context);
		
//		scanner = new NodeScanner(eventAdmin);
//		Thread thr = new Thread(scanner);
//		thr.setDaemon(true);
//		thr.start();
		
		Timer timer = new Timer();
		timer.schedule(new DeviceAddTask(eventAdmin), 5000, 2000);
		
		System.out.println("Started dumptable service");
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
//		scanner.stop();
		System.out.println("Stoped dumptable service");
	}

	private EventAdmin bindEventAdmin(BundleContext context) {
	    serviceTracker = new ServiceTracker(context, EventAdmin.class.getName(), null);
	    serviceTracker.open();

	    return (EventAdmin) serviceTracker.getService();
	  }
	
	private class DeviceAddTask extends TimerTask{

		private EventAdmin eventAdmin;
		private int count = 0;

		public DeviceAddTask(EventAdmin eventAdmin) {
			this.eventAdmin = eventAdmin;
		}
		
		@Override
		public void run() {
			String ip = "IPv6 address N " + count;
			String availability = "aviable";
			eventAdmin.postEvent(new Event("ru/smartsolutions/mesgi/nodescanner",
					getEventProperties(ip,availability)));
			count++;
		}
		
		private Dictionary<String, Object> getEventProperties(String ... properties) {
		    Dictionary<String, Object> result = new Hashtable<String, Object>();
		    result.put("IPv6", properties[0]);
		    result.put("Availability", properties[1]);
		    return result;
		  }
	}
}
