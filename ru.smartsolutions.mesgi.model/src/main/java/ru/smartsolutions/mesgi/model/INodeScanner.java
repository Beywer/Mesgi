package ru.smartsolutions.mesgi.model;

import java.util.List;
import java.util.Map;

public interface INodeScanner {

	public List<DeviceConnection> getDeviceConnections();
	public Map<String, Boolean> getDumptable();

}
