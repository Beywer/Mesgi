package ru.smartsolutions.mesgi.transport;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;

import ru.smartsolutions.mesgi.model.ITransporter;
import ru.smartsolutions.mesgi.model.Task;

public class Transporter implements ITransporter {
	
	private Gson gson;
	
	public Transporter() {
		gson = new Gson();
	}

	public void sendTask(String ip, Task task) {
		
		CoapClient client = new CoapClient("["+ip+"]/plan");
		
		String payload = gson.toJson(task.getParameters());
		
//		client.put(payload, MediaTypeRegistry.APPLICATION_JSON);
		
		System.out.println(client.put(payload, MediaTypeRegistry.APPLICATION_JSON).getResponseText());
		
	}

	public boolean checkTask(String ip, Task task) {
		return false;
	}

	private String getAddress(String ip){
		return "["+ip+"]";
	}
}
