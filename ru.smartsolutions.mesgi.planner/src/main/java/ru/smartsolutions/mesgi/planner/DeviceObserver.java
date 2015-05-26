package ru.smartsolutions.mesgi.planner;

import java.util.ArrayList;
import java.util.List;

import ru.smartsolutions.mesgi.planner.model.Device;

public class DeviceObserver implements IDeviceObservable {
	
	private List<IDeviceObservable> deviceObservables;
	
	public DeviceObserver() {
		deviceObservables = new ArrayList<IDeviceObservable>();
	}
	
	public void addDeviceObservable(IDeviceObservable deviceObservable){
		deviceObservables.add(deviceObservable);
	}

	public void removeDeviceObservable(IDeviceObservable deviceObservable){
		deviceObservables.remove(deviceObservable);
	}

	@Override
	public void addedDevice(Device device) {
		for(IDeviceObservable deviceObservable : deviceObservables)
			deviceObservable.addedDevice(device);
	}

	@Override
	public void removedDevice(Device device) {
		for(IDeviceObservable deviceObservable : deviceObservables)
			deviceObservable.removedDevice(device);
	}
}
