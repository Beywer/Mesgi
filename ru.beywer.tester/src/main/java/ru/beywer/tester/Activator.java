package ru.beywer.tester;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.joda.time.Interval;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ru.smartsolutions.mesgi.model.Task;

public class Activator implements BundleActivator {

	
	public void start(BundleContext context) throws Exception {
		
		String ip = "[fcec:16da:6714:7381:12aa:6db9:b10f:2fe7]";
		
		CoapClient client = new CoapClient(ip);

		System.out.println(client.ping());
		
		
		Set<WebLink> webLinks = client.discover();
		System.out.println(webLinks);
		if(webLinks != null)
			for(WebLink link : webLinks){
				System.out.println(link);
			}
		
		Task task = new Task("TestTask");
		task.setDuration(12);
		long start = System.currentTimeMillis();
		long end = start + 12*60*1000;
		Interval aInterval = new Interval(start, end);
		task.setIntervalAllowed(aInterval);
		
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream);
		
		outputStream.writeObject(task);
		String bytes = arrayOutputStream.toString();
		System.out.println(bytes);
		
		
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes.getBytes());
		ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
		Object object = inputStream.readObject();
		if(object instanceof Task){
			Task task2 = (Task) object;
			System.out.println(object);
			System.out.println(task2);
		}
		
		
		CoapResponse response = client.put(bytes, MediaTypeRegistry.TEXT_PLAIN);
		System.out.println(new String(response.getPayload()));
		
		System.out.println("Tester started");
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Tester stopped");
		
	}

}
