package ru.smartsolutions.mesgi.ru.smartsolutions.mesgi.nodelist;

import javax.servlet.ServletException;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class NodeListComponent {
	
private HttpService httpService;
	
	private ComponentContext context;
	
	public void activate(ComponentContext context) {
		this.context=context;
		System.out.println(NodeListComponent.class.getCanonicalName() + " Started11");
		registerservlet();
	}
	
	public void deactivate(ComponentContext context) {
		unregisterservlet();
		System.out.println(NodeListComponent.class.getCanonicalName() + " Stopped");
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
			this.httpService.registerServlet("/*", new NodeListUI.NodeListUIServlet(), null,
					null);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
	}
}