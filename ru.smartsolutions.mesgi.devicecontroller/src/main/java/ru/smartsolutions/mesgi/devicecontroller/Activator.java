package ru.smartsolutions.mesgi.devicecontroller;

import java.util.Timer;

import org.eclipse.californium.core.CoapServer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import ru.smartsolutions.mesgi.devicecontroller.coap.resource.PlanResource;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;

public class Activator implements BundleActivator {

	private CoapServer coapServer;
	private INodeScanner nodeScanner;
	
	private Timer timer;
	private ITransporter transporter;
	
	public void start(BundleContext context) throws Exception {

//		получение сервиса обнаружения устройств
		ServiceReference nodeScannerReference =
				context.getServiceReference(INodeScanner.class);
		nodeScanner = (INodeScanner) context.getService(nodeScannerReference);
		
//		получение транспортного сервиса
		ServiceReference transporterReference =
				context.getServiceReference(ITransporter.class);
		transporter = (ITransporter) context.getService(transporterReference);
		
		Planner planner = new Planner();
		
		TaskSender taskSender = new TaskSender(planner, nodeScanner, transporter);
		
		timer = new Timer();
		timer.schedule(taskSender, 5000, 3000);
		
		
		coapServer = new CoapServer();
		PlanResource taskresource = new PlanResource("plan", planner);

		coapServer.add(taskresource);
		coapServer.start();
		
		System.out.println("Device controller sdsd started");
	}

	public void stop(BundleContext context) throws Exception {
		coapServer.stop();
		timer.cancel();
		System.out.println("Device controller stopped");
	}

}
