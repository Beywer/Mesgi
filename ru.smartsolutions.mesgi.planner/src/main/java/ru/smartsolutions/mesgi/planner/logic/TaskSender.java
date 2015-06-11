package ru.smartsolutions.mesgi.planner.logic;

import java.util.List;
import java.util.TimerTask;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.DeviceConnection;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;
import ru.smartsolutions.mesgi.model.Task;
import ru.smartsolutions.mesgi.planner.data.DataProvider;

/***
 * Отвечает за пересылку незапланированных задач на устройства.
 * @author beywer
 *
 */
public class TaskSender extends TimerTask {

	private INodeScanner nodeScanner;
	private ITransporter transporter;
	
	public TaskSender(INodeScanner nodeScanner, ITransporter transporter) {
		this.nodeScanner = nodeScanner;
		this.transporter = transporter;
	}

	@Override
	public void run() {
		
		//списки задач для отправки и метрик доступных устройств
		List<Task> unsendedTasks = DataProvider.getUnsendedTasks();
		List<DeviceConnection> deviceConnections = nodeScanner.getDeviceConnections();
		
		if(unsendedTasks.size() > 0){
			System.out.print("Planner unsended tasks\n\t");
			for(Task task : unsendedTasks){
				System.out.print(task.getName() + "  ");
			}
			System.out.println();
		}
		
//		если задачи есть
		if(unsendedTasks.size() != 0){
			System.out.print("Есть задачи");
		
//			выбирается первое самое близкое устройство, которое достижимо
			DeviceConnection connection = null;
			int i = 0;
			while(i < deviceConnections.size() && deviceConnections.get(i).getConnection() == 0){
				i++;
			}
			try{ connection = deviceConnections.get(i);	} catch (IndexOutOfBoundsException e){}
			
//			Если есть устройства
			if(connection != null){
				System.out.print("Finedd Connection " + connection.getIp());
				
//				получение устройства, на которое отправятся задачи
				String ip = connection.getIp();
				Device device = DataProvider.getDevice(ip);
				
//				Отправка
				for(Task task : unsendedTasks){

					String answer = transporter.sendTask(ip, "plan", task);
					if(answer != null)
					switch(answer){
						case "planned":
							System.out.println("Planner: task " +task.getId()+" sended to   " + ip);
							DataProvider.addTaskToDevicePlan(device, 
									transporter.getPlannedTask(ip, "plan", task));
							DataProvider.setTaskSended(task, device);
							break;
						case "unplanned" :
							System.out.println("Planner: task " +task.getId()+" sended to   " + ip);
							DataProvider.setTaskSended(task, device);
							break;
					}
							
					
				}
			}
		}
		
	}

}

























