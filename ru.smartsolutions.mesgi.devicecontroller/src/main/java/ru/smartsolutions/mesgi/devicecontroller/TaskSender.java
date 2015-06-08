package ru.smartsolutions.mesgi.devicecontroller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import ru.smartsolutions.mesgi.model.DeviceConnection;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;
import ru.smartsolutions.mesgi.model.Task;

public class TaskSender extends TimerTask {

	private Planner planner;
	private INodeScanner nodeScanner;
	private ITransporter transporter;

	
	public TaskSender(Planner planner, INodeScanner nodeScanner,ITransporter transporter) {
		this.planner = planner;
		this.nodeScanner = nodeScanner;
		this.transporter = transporter;
	}

	@Override
	public void run(){
		
		 Map<String, Task> tasks = planner.getUnplannedTasks();
		 List<DeviceConnection> connections = nodeScanner.getDeviceConnections();
		 
		 Collection<Task> objects = tasks.values();
		 if(objects.size() > 0){
			 System.out.println("Sender whant to send tasks :  " + objects);
			 System.out.println("Available connections :  " + connections);
		 }
		 
		 try{
			 for(DeviceConnection connection : connections){
				 if(connection.getConnection() > 0)
				 for(Task task : objects){
					 String ip = connection.getIp();
					 if(!ip.equals("fc20:db84:e22d:785f:3d40:730c:6594:1b4c")){
						 String answer = transporter.checkTask(ip, task);
						 if(answer != null)
							 switch(answer){
							 	case "planned" :
							 		objects.remove(task);
							 		planner.removeUnplannedTask(ip);
							 		break;
							 }
					 }
				 }
			 }
		 } catch(Exception e){}
		 
	}

}
