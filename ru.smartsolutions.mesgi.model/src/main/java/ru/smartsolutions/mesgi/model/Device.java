package ru.smartsolutions.mesgi.model;


public class Device {

	private String name;
	private String address;
	private String Description;
	private long connection;

	public Device(String name) {
		this.name = name;
		address = null;
		connection = 0;
	}

	public Device(String name, String address, long connection) {
		this.name = name;
		this.address = address;
		this.connection = connection;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public long getConnection() {
		return connection;
	}

	public void setConnection(long connection) {
		this.connection = connection;
	}
}
