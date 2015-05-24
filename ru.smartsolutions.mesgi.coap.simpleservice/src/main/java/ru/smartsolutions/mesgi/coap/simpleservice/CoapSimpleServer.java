package ru.smartsolutions.mesgi.coap.simpleservice;

import java.net.SocketException;

import org.eclipse.californium.core.CoapServer;

public class CoapSimpleServer extends CoapServer {
    
    public CoapSimpleServer() throws SocketException {
        
    	TimeResource res = new TimeResource();
    	add(res);
    }
}
