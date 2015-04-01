package ru.beywer.vaadin.ru.beywer.vaadin.consoleecho;

import javax.servlet.ServletException;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class ConsoleEchoComponent {
	
	private HttpService httpService;
	
	private ComponentContext context;
	
	public void activate(ComponentContext context) {
		this.context=context;
		System.out.println(ConsoleEchoComponent.class.getCanonicalName() + " Started11");
		registerservlet();
	}
	
	public void deactivate(ComponentContext context) {
		unregisterservlet();
		System.out.println(ConsoleEchoComponent.class.getCanonicalName() + " Stopped");
		this.context=null;
	}
	
	
	public void bindHttpService(HttpService service) {
		this.httpService = service;
		if (context!=null) registerservlet();
	}
	
	public void unbindHttpService(HttpService service) {
		// unregister the servlet from the http service
		unregisterservlet();
	}
	
	private void unregisterservlet(){
		httpService.unregister("/*");
	}
	
	private void registerservlet(){
		try {
			// register the servlet at the http service
			this.httpService.registerServlet("/*", new ConsoleEchoUI.ConsoleEchoUIServlet(), null,
					null);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
	}

	
}
