package ru.smartsolutions.mesgi.planner;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Timer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.http.HttpService;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;
import ru.smartsolutions.mesgi.planner.data.DataProvider;
import ru.smartsolutions.mesgi.planner.logic.NodeReciever;
import ru.smartsolutions.mesgi.planner.logic.TaskSender;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private HttpService httpService;
	
	private INodeScanner nodeScanner;
	private ITransporter transporter;
	
	public static final String PROPERTY_FILE = "mesgi.properties";
	public static final String KARAF_FOLDER_PROPERTY = "user.dir";

	private Timer timer;
	
	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		
//		получение Http сервиса 
		ServiceReference reference = 
				context.getServiceReference(HttpService.class);
		httpService = (HttpService) context.getService(reference);
		System.out.println(httpService);
		
//		регистрация сервлета с UI
		PlannerUI.PlannerUIServlet servlet = new PlannerUI.PlannerUIServlet();
		httpService.registerServlet("/*", servlet,
				null,null);

//		запуск слушателя сообщений о состоянии устройтсв 
		NodeReciever nodeReciever = new NodeReciever();
		context.registerService(EventHandler.class, 
				nodeReciever, 
				getHandlerServiceProperties("ru/smartsolutions/mesgi/nodescanner"));
		
//		получение транспортного сервиса
		ServiceReference transporterReference =
				context.getServiceReference(ITransporter.class);
		transporter = (ITransporter) context.getService(transporterReference);
		
//		получение сервиса обнаружения устройств
		ServiceReference nodeScannerReference =
				context.getServiceReference(INodeScanner.class);
		nodeScanner = (INodeScanner) context.getService(nodeScannerReference);
		
		System.out.println("Planner got services:");
		System.out.println("\t\t" + nodeScanner);
		System.out.println("\t\t" + transporter);

//		добавление в DATA PROVIDR уже существующих узлов
		HashMap<String, Boolean> dumptable = 
				(HashMap<String, Boolean>) nodeScanner.getDumptable();
		int i = 1;
		for(String ip : dumptable.keySet()){
			Device device = new Device(ip, dumptable.get(ip));
			//TODO получить нормальное описание
			device.setName("Dev prov " + i);
			device.setDescription("Descr prov " + i);
			i++;
			DataProvider.addDevice(device);
		}
		
		
//		создание и запуск распределителя задач
		TaskSender sender = new TaskSender(nodeScanner, transporter);
		timer = new Timer();
		timer.schedule(sender, 5000, 5000);
		
		System.out.println("Planner started");
	}
	
	private Dictionary<String, Object> getHandlerServiceProperties(String... topics) {
		Dictionary<String, Object> eventHandlerProperties = new Hashtable<String, Object>();
		eventHandlerProperties.put(EventConstants.EVENT_TOPIC, topics);
		return eventHandlerProperties;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
		httpService.unregister("/*");
		timer.cancel();
		System.out.println("Planner stopped");
	}
	
	public static BundleContext getContext(){
		return context;
	}
}
