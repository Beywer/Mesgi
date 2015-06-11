package ru.smartsolutions.mesgi.nodescanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import ru.smartsolutions.mesgi.model.Constants;
import ru.smartsolutions.mesgi.model.DeviceConnection;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;

public class NodeScanner implements Runnable, INodeScanner {

	private final String FUNCTION = "NodeStore_dumpTable";
	private EventAdmin eventAdmin;
	
	private Map<String, Boolean> dumptable;
	
	private volatile List<DeviceConnection> deviceConnections;
//	private ReentrantLock deviceConnectionsLock;
	
	private List<String> blackList;

	private boolean interrupt;
	
	private ITransporter transporter;
	private Timer timer;
	private String ownIp;
	
	public NodeScanner(EventAdmin eventAdmin, ITransporter transporter){
		this.eventAdmin = eventAdmin;
		this.transporter = transporter;
		
		interrupt = true;
//		deviceConnectionsLock = new ReentrantLock();
		deviceConnections = Collections.synchronizedList(new ArrayList<DeviceConnection>());
		dumptable = new HashMap<String, Boolean>();
		
		blackList = new ArrayList<String>();
		
//		получение собсветнного адреса из файла конфигурации
		String karafFolder = System.getProperty(Constants.KARAF_FOLDER_PROPERTY);
		Properties properties = new Properties();
		
		try {
			File file = new File(karafFolder+"/"+Constants.PROPERTY_FILE);
			FileInputStream inputStream = new FileInputStream(file);
			
			properties.load(inputStream);
			ownIp = (String) properties.get("ip");
			blackList.add(ownIp);
			
		} catch (FileNotFoundException e1) {e1.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		
		
//		periodicaly remove nodes from blacklist
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				blackList.clear();
				blackList.add(ownIp);
			}
		}, 5000, 2000);
	}
	
	public void run() {
		while(interrupt){
//			периодическое сканирование сети
			scanNodes();
//			после сканирования метрики сортируются 
//			и к ним открывается доступ
			Collections.sort(deviceConnections);
//			deviceConnectionsLock.unlock();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public List<DeviceConnection> getDeviceConnections() {
		return deviceConnections;
	}
	
	public void stop(){
		timer.cancel();
		interrupt = false;
	}
	
	public Map<String, Boolean> getDumptable() {
		return dumptable;
	}

	@SuppressWarnings("unchecked")
	private void scanNodes(){

//		никто не должен получать метрики устройств
//		пока список обновляется
//		deviceConnectionsLock.lock();
		
//		очищение списка устройств
//		deviceConnections.clear();
		
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

			String type = null;
//			если узел обнаружен первый раз(нигде не сохранен) прверка его типа
			if(!blackList.contains(ip) && !dumptable.containsKey(ip)){
				type = transporter.getNodeType(ip);
				if(type != null)
					switch(type){
						case "station" :
							blackList.add(ip);
							break;
						case "mobile" :
							break;
					}
				else blackList.add(ip);
			}
			
//			игнороирует себя и не мобильные узлы
			if(!blackList.contains(ip)){
				
//				в зависимости от метрики устройство 
//				либо доступно, либо нет
				boolean availability = false;
				if(newLink != 0)
					availability = true;
				
//				получение старой доступности
				Boolean oldAvailability = null;
				oldAvailability = dumptable.get(ip);
				
//				если старая и новая доступности совпадают,
//				то eventAdmin не кидает событие
				if(oldAvailability != null){
					if(oldAvailability.booleanValue()^availability){
						eventAdmin.postEvent(
								new Event("ru/smartsolutions/mesgi/nodescanner", getEventProperties(ip,availability)));
						System.out.println(ip + "  " + availability);
					}
//				если ранне небыло узла, кидается событие
				} else{ eventAdmin.postEvent(
						new Event("ru/smartsolutions/mesgi/nodescanner", getEventProperties(ip,availability)));
					System.out.println(ip + "  " + availability);
				}
	
//				запоминается новая доступность
				dumptable.put(ip, availability);
				
//				создается объект, хранящий информацию о метрике 
//				и сохраниние его в списке
				DeviceConnection connection = new DeviceConnection(ip);
				connection.setConnection(newLink);
				DeviceConnection connection2 = null;
				
				for(DeviceConnection deviceConnection : deviceConnections){
					if(deviceConnection.getIp().equals(ip)){
						connection2 = deviceConnection;
						break;
					}
				}

				if(connection2 == null) deviceConnections.add(connection);
				else {
					deviceConnections.remove(connection2);
					deviceConnections.add(connection);
				}
				
			} 
			
		}
	}
	
	private Dictionary<String, Object> getEventProperties(String ip, boolean availability) {
	    Dictionary<String, Object> result = new Hashtable<String, Object>();
	    result.put("IPv6", ip);
	    result.put("Availability", availability);
	    return result;
	  }

}
