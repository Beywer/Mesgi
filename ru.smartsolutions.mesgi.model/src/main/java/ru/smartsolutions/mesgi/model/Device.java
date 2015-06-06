package ru.smartsolutions.mesgi.model;


public class Device {

	private String name;
	private String address;
	private String Description;
	private boolean availability;

	public Device(String name) {
		this.name = name;
		address = null;
	}

	public Device(String name, String address, boolean availability) {
		this.name = name;
		this.address = address;
		this.availability = availability;
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

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
}
