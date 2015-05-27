package ru.smartsolutions.mesgi.planner.model;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.Reindeer;

public class Device {

	private String name;
	private String address;
	private Boolean availability;
	private String Description;
	private Button button;

	public Device(String name) {
		this.name = name;
		address = null;
		availability = null;
		button = new Button(name);
		button.setPrimaryStyleName(Reindeer.BUTTON_LINK);
		button.setWidth(100, Unit.PERCENTAGE);
	}

	public Device(String name, String address, Boolean availability) {
		this.name = name;
		this.address = address;
		this.availability = availability;
		button = new Button(name);
		button.setPrimaryStyleName(Reindeer.BUTTON_LINK);
		button.setWidth(100, Unit.PERCENTAGE);
		
		if(availability) 
			button.setStyleName("available-device");
		else
			button.setStyleName("unavailable-device");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		button.setCaption(name);
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
		if(availability) 
			button.setStyleName("available-device");
		else
			button.setStyleName("unavailable-device");
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

}
