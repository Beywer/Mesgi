package ru.beywer.tester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private BundleContext context;
	
	public void start(BundleContext context) throws Exception {
		this.context = context;
		System.out.println("Tester started");
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
		System.out.println("Tester stopped");
		
	}

}
