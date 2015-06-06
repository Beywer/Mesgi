package ru.smartsolutions.mesgi.model;


public class Device {

	private String name;
	private String address;
	private String description;
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
	
	public Device(String address, boolean availability) {
		super();
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
		
		StringBuilder sb = new StringBuilder();
		sb.append("Наименование: " + name + "\n");
		sb.append("IPv6: " + address);
		sb.append("\n\n" + description);
		
		return sb.toString();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
}
