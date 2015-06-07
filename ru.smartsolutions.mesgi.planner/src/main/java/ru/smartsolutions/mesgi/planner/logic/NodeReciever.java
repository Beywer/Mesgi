package ru.smartsolutions.mesgi.planner.logic;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.planner.data.DataProvider;

public class NodeReciever implements EventHandler {
	
	private int count;
	
	public NodeReciever() {
		count = 0;
	}

	public void handleEvent(Event event) {
		
		String ip = (String) event.getProperty("IPv6");
		Boolean availability = (Boolean) event.getProperty("Availability");
		
		//
		if(DataProvider.getDevice(ip) != null){
			DataProvider.changeDeviceAvailability(ip, availability);
		}
		else {
			Device device = new Device(ip, availability);
			//TODO получить настоящее описание и имя устройства
			device.setName("Device " + count);
			device.setDescription("Descr " + count);
			count ++;
			
			DataProvider.addDevice(device);
		}

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
	
}
