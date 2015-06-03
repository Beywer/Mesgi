package ru.smartsolutions.mesgi.planner.uicomponents;

import ru.smartsolutions.mesgi.planner.model.Device;

public class Main {

	public static void main(String[] args){
		
		Device device = new Device("Dev 1","Adr 1",true);
		
		DataProvider.addDevice(device);
		DataProvider.addDevice(device);
		System.out.println(DataProvider.getDevices());
		
	}
	
}
