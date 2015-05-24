package ru.smartsolutions.mesgi.coap.simpleservice;

import java.net.SocketException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private BundleContext context;
	private CoapSimpleServer server;
	
	public void start(BundleContext context) throws Exception {
		this.context = context;
		
		//Create CoAP Server
        try {
        	server = new CoapSimpleServer();
            server.start();
            
        } catch (SocketException e) {
            
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
	}

	public void stop(BundleContext context) throws Exception {
		this.context = null;
		server.stop();
		server.destroy();
	}
	
}
