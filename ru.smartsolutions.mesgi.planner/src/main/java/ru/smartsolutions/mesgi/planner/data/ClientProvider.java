package ru.smartsolutions.mesgi.planner.data;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.Task;

import com.google.gson.Gson;

public class ClientProvider {

	private static Gson gson = new Gson();
	private static Map<String, CoapClient> clients = new HashMap<String, CoapClient>();
	
	public static void addClient(final String ip){
		
		CoapClient client = new CoapClient("["+ip+"]/plan");
		
		client.observe(new CoapHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onLoad(CoapResponse response) {
				
				String responseStr = response.getResponseText();
				
				if(responseStr != null && !responseStr.equals("") && !responseStr.equals("none")){
					
					HashMap<String, Object> params = gson.fromJson(response.getResponseText(),
							HashMap.class);
					
					Task task = new Task(params);
					Device device = DataProvider.getDevice(ip);
					DataProvider.addTaskToDevicePlan(device, task);
				}
			}
			
			@Override
			public void onError() {
				
			}
		});
		
		clients.put(ip, client);
	}
	
}
