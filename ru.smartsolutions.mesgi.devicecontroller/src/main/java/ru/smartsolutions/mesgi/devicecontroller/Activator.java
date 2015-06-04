package ru.smartsolutions.mesgi.devicecontroller;

import org.eclipse.californium.core.CoapServer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private CoapServer coapServer;
	
	public void start(BundleContext context) throws Exception {
		
		coapServer = new CoapServer();
		PlanResource taskresource = new PlanResource("plan");

		coapServer.add(taskresource);
		coapServer.start();
		
		System.out.println("Device controller2 started");
	}

	public void stop(BundleContext context) throws Exception {
		coapServer.stop();
		System.out.println("Device controller3 stopped");
	}

}
