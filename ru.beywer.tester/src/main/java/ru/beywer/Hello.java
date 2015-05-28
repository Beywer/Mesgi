package ru.beywer;

import org.osgi.service.component.annotations.Component;

@Component
public class Hello implements IHello {

	public void sayHello() {
		System.out.println("Hello World!");
	}

}
