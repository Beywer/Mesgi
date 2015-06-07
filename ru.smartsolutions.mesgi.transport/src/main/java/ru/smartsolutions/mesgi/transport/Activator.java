package ru.smartsolutions.mesgi.transport;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ru.smartsolutions.mesgi.model.ITransporter;

public class Activator implements BundleActivator {

	
	public void start(BundleContext context) throws Exception {
		
		Transporter transporter = new Transporter();
		context.registerService(ITransporter.class, transporter, null);
		
		System.out.println("Mesgi Transporter service registred");		
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Mesgi Transporter service unregistred");	
	}
	
}
