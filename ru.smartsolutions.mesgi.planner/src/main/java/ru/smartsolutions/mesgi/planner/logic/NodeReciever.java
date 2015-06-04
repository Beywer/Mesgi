package ru.smartsolutions.mesgi.planner.logic;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.planner.uicomponents.MainComponent;

public class NodeReciever implements EventHandler {

	private Map<String, Device> devices;
	
	private MainComponent mainComponent;
	
	public void handleEvent(Event event) {
		
//		String ip = (String) event.getProperty("IPv6");
//		String availabilityStr = (String) event.getProperty("Availability");
//		Boolean availability = true;
//		if(availabilityStr.equals("unavailable")) availability = false;
//		
//		//Если узел еще не был в сети
//		if(!devices.containsKey(ip)){ 
//			//Создается новое устройтсов
//			Device device = new Device("",ip,availability);
//			devices.put(ip, device);
//			//TODO получить информацию об устройстве
//			System.out.println("NodeReceiver: recieved new device " + ip + " " + availability);
////			mainComponent.addDevice(device);
//			
//		} else {
//			Boolean oldAvailability = devices.get(ip).getAvailability();
//			//Если у уже известного узла именилась доступность
//			if(oldAvailability == availability){
//				Device device = devices.get(ip);
//				device.setAvailability(availability);
//				System.out.println("NodeReceiver: recieved changed state" + ip + " " + availability);
////				mainComponent.updateDeviceAvailability(device);
//			}
//		}
	}
	
//	private void changedDeviceAvailability(final String ip, final String availability){
//		ui.accessSynchronously(new Runnable() {
//			
//			@Override
//			public void run() {
//				Object key = keys.get(ip);
//				Property<Device> prop = deviceContainer.getContainerProperty(key, "device");
//				Device device = prop.getValue();
//				
//				boolean available = false;
//				if(availability.equals("available")) available = true;
//				device.setAvailability(available);
//
//				deviceContainer.getContainerProperty(key, "device").setValue(device);
//
////				crutch
//				key = deviceContainer.addItem();
//				deviceContainer.getContainerProperty(key, "name").setValue("");
//				deviceContainer.setChildrenAllowed(key, false);
//				
//				ui.push();
//				
//				deviceContainer.removeItem(key);
////				end of crutch
//			}
//		});
//	}
//	
//	private void addDeviceAvailability(final String ip, final String availability){
//		ui.accessSynchronously(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				boolean available = false;
//				if(availability.equals("available")) available = true;
//				Device device = new Device(ip, ip, available);
//				device.setDescription("ip : " + ip + "\n\n" + "Some description of this device");
//				
//				Object ipKey = deviceContainer.addItem();
//				deviceContainer.getContainerProperty(ipKey, "name").setValue(device.getName());
//				deviceContainer.getContainerProperty(ipKey, "device").setValue(device);
//				deviceContainer.setChildrenAllowed(ipKey, false);
//				
//				keys.put(ip, ipKey);
//				
//				ui.push();
//			}
//		});
//	}
	
	public NodeReciever(MainComponent mainComponent) {
		
		this.mainComponent = mainComponent;
		devices = new HashMap<String, Device>();
	}
	
	public Device getDevice(String ip){
		return devices.get(ip);
	}
}
