package ru.smartsolutions.mesgi;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class Activator implements BundleActivator {

	private BundleContext context;
	
	public void start(BundleContext context) {

		switchPlatformEncodingToUTF8();
		
		this.context = context;
		context.registerService(EventHandler.class, 
				new NodeReciver(), 
				getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
		
//		try {
//			PrintStream stream = new PrintStream(System.out, false, "UTF-8");
//			stream.println("ФЫВА");
//			System.setOut(stream);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
		
		System.out.println("Started nodeReciver service");

		System.out.println("���� ���� ����");
		System.out.println("утф восемь");
	}

	
	
	public void stop(BundleContext context) throws Exception {
		this.context = null;
		System.out.println("Stoped nodeReciver service");
	}
	
    private void switchPlatformEncodingToUTF8() {
        try {
          System.setProperty("file.encoding","UTF-8");
          Field charset = Charset.class.getDeclaredField("defaultCharset");
          charset.setAccessible(true);
          charset.set(null,null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    }
	
	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}
}
