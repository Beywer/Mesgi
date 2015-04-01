package ru.smartsolutions.mesgi.ru.smartsolutions.mesgi.nodelist;

import javax.servlet.ServletException;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class NodeListComponent{

	private BundleContext context;
	private HttpService httpService;
	private static final String ALIAS = "/nodelist";
	
	public void bindHttpService(HttpService httpService){
		this.httpService = httpService;
	}
	
	public void unbindHttpService(HttpService httpService){
		this.httpService = httpService;
	}
	
	public void activate(BundleContext context){
		this.context = context;
		System.out.println(NodeListComponent.class.getCanonicalName() + " Started");
	}	
	
	public void deactivate(BundleContext context){
		this.context = null;
		System.out.println(NodeListComponent.class.getCanonicalName() + " Started");
	}
	

	private void unregisterservlet(){
		httpService.unregister(ALIAS);
	}
	
	private void registerservlet(){
		try {
			this.httpService.registerServlet(ALIAS, new NodeListUI.NodeListUIServlet(), null,
					null);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (NamespaceException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void handleEvent(Event event) {
//		
//	}

}
