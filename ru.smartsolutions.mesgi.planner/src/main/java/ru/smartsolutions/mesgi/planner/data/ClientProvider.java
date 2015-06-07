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
	
	private static Map<String, CoapClient> planObservers = new HashMap<String, CoapClient>();
	
	public static void addPlanObserver(final String ip){
		
		final CoapClient client = new CoapClient("["+ip+"]/plan");
		
		client.observe(new CoapHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onLoad(CoapResponse response) {
				
				String result = response.getResponseText();
				System.out.println("Result [" + result +"]");
				if(!result.equals("none") && !result.equals("")){
					HashMap<String, Object> params = gson.fromJson(result, HashMap.class);

					Task plannedTask = new Task(params);

					if(plannedTask.getPlannedInterval() != null){
						Device device = DataProvider.getDevice(ip);
						DataProvider.addTaskToDevicePlan(device, plannedTask);
					}
					
					client.delete();
				}
			}

			@Override
			public void onError() {
			}
		});
		
		planObservers.put(ip, client);
		
	}
}
