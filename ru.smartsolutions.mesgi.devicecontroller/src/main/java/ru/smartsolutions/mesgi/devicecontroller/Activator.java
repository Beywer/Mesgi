package ru.smartsolutions.mesgi.devicecontroller;

import org.eclipse.californium.core.CoapServer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ru.smartsolutions.mesgi.devicecontroller.coap.resource.PlanResource;

public class Activator implements BundleActivator {

	private CoapServer coapServer;
	
	public void start(BundleContext context) throws Exception {
		
		Planner planner = new Planner();
		
		coapServer = new CoapServer();
		PlanResource taskresource = new PlanResource("plan", planner);

		coapServer.add(taskresource);
		coapServer.start();
		
		System.out.println("Device controller started");
	}

	public void stop(BundleContext context) throws Exception {
		coapServer.stop();
		System.out.println("Device controller stopped");
	}

}
