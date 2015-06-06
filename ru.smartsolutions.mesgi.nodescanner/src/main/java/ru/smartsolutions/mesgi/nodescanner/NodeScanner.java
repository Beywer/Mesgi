package ru.smartsolutions.mesgi.nodescanner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import ru.smartsolutions.mesgi.model.DeviceConnection;

public class NodeScanner implements Runnable {

	private final String FUNCTION = "NodeStore_dumpTable";
	private EventAdmin eventAdmin;
	
	private Map<String, Boolean> dumptable;
	
	private List<DeviceConnection> deviceConnections;
	private ReentrantLock deviceConnectionsLock;

	private boolean interrupt;
	
	public NodeScanner(EventAdmin eventAdmin){
		this.eventAdmin = eventAdmin;
		interrupt = true;
		deviceConnectionsLock = new ReentrantLock();
		deviceConnections = new ArrayList<DeviceConnection>();
		dumptable = new HashMap<String, Boolean>();
	}
	
	public void run() {
		while(interrupt){
//			периодическое сканирование сети
			scanNodes();
//			после сканирования метрики сортируются 
//			и к ним открывается доступ
			Collections.sort(deviceConnections);
			deviceConnectionsLock.unlock();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		interrupt = false;
	}
	
	@SuppressWarnings("unchecked")
	private void scanNodes(){

//		никто не должен получать метрики устройств
//		пока список обновляется
		deviceConnectionsLock.lock();
		
//		очищение списка устройств
		deviceConnections.clear();
		
//		адрес AdminAPI
		InetAddress address = null;
		try{
			address = InetAddress.getByName("127.0.0.1");
		}
		catch(UnknownHostException e) { } 
		
//		создание запроса(страница 0) в виде карты
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("page", 0);
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("q", FUNCTION); 
		request.put("args",args);

//		создание объекта AdminAPI
		AdminAPI adminAPI = new AdminAPI(address, 11234, null);
		
//		запрос
		Map<String, Object> response = null;
		response = adminAPI.perform(request);
				
//		получение списка устройств из ответа
		List<Map<String, Object>> rooteTable = (List<Map<String, Object>>) response.get("routingTable");
		
//		аналзи списка
		for(Map<String, Object> node : rooteTable){
			
//			получение ip и метрики из ответа
			String ip = (String) node.get("ip");
			long newLink = (Long) node.get("link");
			
//			в зависимости от метрики устройство 
//			либо доступно, либо нет
			boolean availability = false;
			if(newLink != 0)
				availability = true;
			
//			получение старой доступности
			Boolean oldAvailability = null;
			oldAvailability = dumptable.get(ip);
			
//			если старая и новая доступности совпадают,
//			то eventAdmin не кидает событие
			if(oldAvailability != null){
				if(oldAvailability.booleanValue()^availability){
					eventAdmin.postEvent(
							new Event("ru/smartsolutions/mesgi/nodescanner", getEventProperties(ip,availability)));
					System.out.println(ip + "  " + availability);
				}
//			если ранне небыло узла, кидается событие
			} else{ eventAdmin.postEvent(
					new Event("ru/smartsolutions/mesgi/nodescanner", getEventProperties(ip,availability)));
				System.out.println(ip + "  " + availability);
			}

//			запоминается новая доступность
			dumptable.put(ip, availability);
			
//			создается объект, хранящий информацию о метрике 
//			и сохраниние его в списке
			DeviceConnection connection = new DeviceConnection(ip);
			connection.setConnection(newLink);
			deviceConnections.add(connection);
		}	
	}
	
	private Dictionary<String, Object> getEventProperties(String ip, boolean availability) {
	    Dictionary<String, Object> result = new Hashtable<String, Object>();
	    result.put("IPv6", ip);
	    result.put("Availability", availability);
	    return result;
	  }
}
