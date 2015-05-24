package ru.smartsolutions.mesgi.coap.simpleservice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;

public class TimeResource extends CoapResource {
    
	private String time;
	
    public TimeResource() {
        
        super("time");
        getAttributes().setTitle("Time Resource");
        getAttributes().setObservable();
        setObservable(true);
        setObserveType(Type.CON);
        
		Timer timer = new Timer();
		timer.schedule(new TimeTask(), 0, 1000);
    }
    
    @Override
    public void handleGET(CoapExchange exchange) {
        
        // respond to the request
    	exchange.setMaxAge(3);
    	exchange.respond(time);
        
    }
    
    private class TimeTask extends TimerTask {

		@Override
		public void run() {
			time = getTime() + "  and hello from RPI";
			changed();
		}
	}

	private String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		return dateFormat.format(time);
	}
}