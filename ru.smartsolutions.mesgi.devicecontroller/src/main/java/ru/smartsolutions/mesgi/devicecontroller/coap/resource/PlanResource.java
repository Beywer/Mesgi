package ru.smartsolutions.mesgi.devicecontroller.coap.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import ru.smartsolutions.mesgi.model.Task;

public class PlanResource extends CoapResource {

	public PlanResource(String name) {
		super(name);
	}
	
//	Получение задачи
	@Override
	public void handlePUT(CoapExchange exchange) {
		byte[] taskBytes = exchange.getRequestPayload();
		Task task = null;
		try {
			ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(taskBytes));
			Object object = stream.readObject();
			if(object instanceof Task)
				task = (Task) object;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(task);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond("Hello! This is plan. mb ^^");
	}
}
