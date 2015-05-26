package ru.smartsolutions.mesgi.dumptable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

public class NodeScanner implements Runnable {

	private final String FUNCTION = "NodeStore_dumpTable";
	private Map<String, Long> dumptable;
	private boolean interrupt;
	private EventAdmin eventAdmin;
	
	public NodeScanner(EventAdmin eventAdmin){
		this.eventAdmin = eventAdmin;
		interrupt = true;
		dumptable = new HashMap<String, Long>();
	}
	
	public void run() {
		while(interrupt){
			scanNodes();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, Long> getDumptable(){
		return dumptable;
	}
	
	public void stop(){
		interrupt = false;
	}
	
	private void scanNodes(){

		InetAddress address = null;
		try{
			address = InetAddress.getByName("127.0.0.1");
		}
		catch(UnknownHostException e) { } 
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("page", 0);
		
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("q", FUNCTION); 
		request.put("args",args);
		
		AdminAPI adminAPI = new AdminAPI(address, 11234, null);
		Map<String, Object> response = null;
		
		response = adminAPI.perform(request);
				
		List<Map<String, Object>> rooteTable = (List<Map<String, Object>>) response.get("routingTable");
		
		for(Map<String, Object> node : rooteTable){
			String availability = null;
			String ip = (String) node.get("ip");
			long newLink = (Long) node.get("link");
			if(dumptable.containsKey(ip)){
				if(newLink != 0){
					availability = "aviable";
				}
				else availability = "unaviable";
			}
			dumptable.put(ip, newLink);
			if(availability != null){
				eventAdmin.postEvent(new Event("ru/smartsolutions/mesgi/nodescanner",getEventProperties(ip,availability)));
			}
//			eventAdmin.postEvent(new Event("ru/smartsolutions/mesgi/nodescanner",getEventProperties("IP","true")));
		}	
	}
	
	private Dictionary<String, Object> getEventProperties(String property1) {
	    Dictionary<String, Object> result = new Hashtable<String, Object>();
	    result.put("IPv6", property1);
	    return result;
	  }
	
	private Dictionary<String, Object> getEventProperties(String ... properties) {
	    Dictionary<String, Object> result = new Hashtable<String, Object>();
	    result.put("IPv6", properties[0]);
	    result.put("Availability", properties[1]);
	    return result;
	  }
}
