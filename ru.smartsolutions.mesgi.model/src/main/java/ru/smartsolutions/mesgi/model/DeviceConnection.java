package ru.smartsolutions.mesgi.model;

public class DeviceConnection implements Comparable<DeviceConnection> {
	
	private String ip;
	private long connection;

	public DeviceConnection(String ip) {
		this.ip = ip;
	}

	public long getConnection() {
		return connection;
	}

	public void setConnection(long connection) {
		this.connection = connection;
	}

	public String getIp() {
		return ip;
	}

	public int compareTo(DeviceConnection o) {
		int result = 0;
		
		if(connection < o.getConnection()) result = -1;
		else if (connection > o.getConnection()) result = 1; 
		
		return result;
	}
}
