package ru.beywer.tester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	
	public void start(BundleContext context) throws Exception {
		
		String ip = "[fcec:16da:6714:7381:12aa:6db9:b10f:2fe7]";
		
//		CoapClient client = new CoapClient(ip);

//		System.out.println(client.ping());
		
		
//		Set<WebLink> webLinks = client.discover();
//		System.out.println(webLinks);
//		if(webLinks != null)
//			for(WebLink link : webLinks){
//				System.out.println(link);
//			}
		
//		Task task = new Task("TestTask");
//		task.setDuration(12);
//		long start = System.currentTimeMillis();
//		long end = start + 12*60*1000;
//		Interval aInterval = new Interval(start, end);
//		task.setIntervalAllowed(aInterval);
//		System.out.println("Original task : " + task);
//		System.out.println(task.getIntervalAllowed().getStartMillis());
//		
//		Map<String, Object> params = task.getParameters();
//		String bytes = new Gson().toJson(params);
//		System.out.println("Json: " + bytes);
//		
//		
//
////		Task task2 = new Task(params);s
////		System.out.println(task2);
//		
//		try{
//			HashMap t2 = new Gson().fromJson(bytes, HashMap.class);
//			System.out.println("Original params : " + params);
//			System.out.println("New params: " + t2);
//			
//			System.out.println("New task: " + new Task(t2));
//			System.out.println(new Task(t2).getIntervalAllowed().getStartMillis());
//			
//		} catch (Exception exception){
//			System.out.println("error");
//			exception.printStackTrace();
//		}
		
//		CoapResponse response = client.put(bytes, MediaTypeRegistry.APPLICATION_JSON);
//		System.out.println(new String(response.getPayload()));
		
		System.out.println("Tester started");
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Tester stopped");
		
	}

}
