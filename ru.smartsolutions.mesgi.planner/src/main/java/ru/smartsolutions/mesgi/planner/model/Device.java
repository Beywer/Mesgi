package ru.smartsolutions.mesgi.planner.model;

public class Device {

	private String name;
	private String address;
	private Boolean availability;
	private String Description;

	public Device(String name) {
		this.name = name;
		address = null;
		availability = null;
	}

	public Device(String name, String address, Boolean availability) {
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

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
}
