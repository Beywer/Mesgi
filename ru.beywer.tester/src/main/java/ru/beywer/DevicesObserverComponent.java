package ru.beywer;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import ru.beywer.IHello;

@Component(name=DevicesObserverComponent.NAME)
public class DevicesObserverComponent {

	public static final String NAME = "DevicesObserverComponent";
	
	private IHello helloService;
	
	@Activate
	private void start(BundleContext context){
		
		System.out.println(BundleContext.class.getName() + " started");
	}
	
	@Deactivate
	private void stop(BundleContext context){
		System.out.println(BundleContext.class.getName() + " stopped");
	}
	
    @Reference
    public void setHelloService(final IHello helloService) {
        this.helloService = helloService;
    }
 
    public void unsetHelloService(final IHello helloService) {
        this.helloService = null;
    }
}
