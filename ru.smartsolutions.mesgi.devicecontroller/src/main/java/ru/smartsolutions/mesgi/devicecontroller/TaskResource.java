package ru.smartsolutions.mesgi.devicecontroller;

import org.eclipse.californium.core.CoapResource;

public class TaskResource extends CoapResource {

	public TaskResource(String name) {
		super(name);
	}

}
