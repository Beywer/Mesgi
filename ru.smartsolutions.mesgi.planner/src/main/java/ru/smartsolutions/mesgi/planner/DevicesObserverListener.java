package ru.smartsolutions.mesgi.planner;

import ru.smartsolutions.mesgi.planner.model.Device;

public interface DevicesObserverListener {
	
	public void fireAddedDevice(Device device);

}
