package ru.smartsolutions.mesgi.planner;

import ru.smartsolutions.mesgi.planner.model.Device;

public interface IDeviceObservable {
	
	public void addedDevice(Device device);
	public void removedDevice(Device device);
	
}
