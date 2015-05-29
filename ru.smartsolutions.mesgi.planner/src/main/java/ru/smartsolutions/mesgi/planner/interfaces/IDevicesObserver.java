package ru.smartsolutions.mesgi.planner.interfaces;

import ru.smartsolutions.mesgi.planner.model.Device;
import ru.smartsolutions.mesgi.planner.model.Task;

public interface IDevicesObserver {
	
	public void planTask(Task task ,Device device);
	
}
