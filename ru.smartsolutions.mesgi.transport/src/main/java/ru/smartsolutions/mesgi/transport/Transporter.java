package ru.smartsolutions.mesgi.transport;

import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import ru.smartsolutions.mesgi.model.ITransporter;
import ru.smartsolutions.mesgi.model.Task;

import com.google.gson.Gson;

public class Transporter implements ITransporter {
	
	private Gson gson;
	
	public Transporter() {
		gson = new Gson();
	}

	public String sendTask(String ip, String path, Task task) {

//		создание клиента, упаковка в JSON, отправка
		CoapClient client = new CoapClient("["+ip+"]/" + path);
		String taskJSON = gson.toJson(task.getParameters());
		CoapResponse resp = client.put(taskJSON, MediaTypeRegistry.APPLICATION_JSON);

//		если ответ пришел
		if(resp != null)  return resp.getResponseText();
		
		return "";
	}

	public String checkTask(String ip, Task task) {
		
		CoapClient client = new CoapClient("["+ip+"]/plan");
		
		String paramsJSON = gson.toJson(task.getParameters());
		
		CoapResponse response = client.post("check"+paramsJSON,MediaTypeRegistry.TEXT_PLAIN);
		
		if(response != null)
			return response.getResponseText();
		return null;
		
	}

	public Task getPlannedTask(String ip, String path, Task task) {
		
		CoapClient client = new CoapClient("["+ip+"]/" + path);
		CoapResponse response = client.post(task.getId(), MediaTypeRegistry.TEXT_PLAIN);
		
		String paramsStr = response.getResponseText();
		
		HashMap<String, Object> params = gson.fromJson(paramsStr, HashMap.class);
		
		return new Task(params);
			
	}
}
