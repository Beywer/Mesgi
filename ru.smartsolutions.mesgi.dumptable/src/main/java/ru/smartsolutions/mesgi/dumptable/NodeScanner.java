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
			String message = null;
			String ip = (String) node.get("ip");
			long newLink = (Long) node.get("link");
			if(dumptable.containsKey(ip)){
				long oldLink = dumptable.get(ip);
				if(oldLink != 0 && newLink == 0){
//					System.out.println("Node " + ip + " is unaviable now");
					message = "Node " + ip + " is unaviable now";
				}
				else if(oldLink ==0 && newLink != 0){
//					System.out.println("Node " + ip + " is aviable again");
					message = "Node " + ip + " is aviable again";
				}
			}
			else{
//				System.out.println("New node finded: " + ip);
				message = "New node finded: " + ip;
			}
			dumptable.put(ip, newLink);
			if(message != null){
				getEventProperties(message);
				eventAdmin.postEvent(new Event("ru/smartsol/spectrum/rpi/nodescanner", getEventProperties(message)));
//				System.out.println("Posted message " + message);
			}
		}	
	}
	
	private Dictionary<String, Object> getEventProperties(String property1) {
	    Dictionary<String, Object> result = new Hashtable<String, Object>();
	    result.put("property1", property1);
	    return result;
	  }
}
