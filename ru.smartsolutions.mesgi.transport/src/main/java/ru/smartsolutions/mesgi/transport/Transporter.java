package ru.smartsolutions.mesgi.transport;

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

	public boolean sendTask(String ip, String path, Task task) {

//		создание клиента, упаковка в JSON, отправка
		CoapClient client = new CoapClient("["+ip+"]/" + path);
		String taskJSON = gson.toJson(task.getParameters());
		CoapResponse resp = client.put(taskJSON, MediaTypeRegistry.APPLICATION_JSON);

//		если ответ пришел
		if(resp != null)  return true;
		
		return false;
	}

	public String checkTask(String ip, Task task) {
		return "";
	}
}
