package ru.smartsolutions.mesgi.planner.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import ru.smartsolutions.mesgi.model.Device;
import ru.smartsolutions.mesgi.model.DeviceConnection;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.ITransporter;
import ru.smartsolutions.mesgi.model.Task;
import ru.smartsolutions.mesgi.planner.Activator;
import ru.smartsolutions.mesgi.planner.data.DataProvider;

/***
 * Отвечает за пересылку незапланированных задач на устройства.
 * @author beywer
 *
 */
public class TaskSender extends TimerTask {

	private String own_ip = "";
	
	private INodeScanner nodeScanner;
	private ITransporter transporter;
	
	public TaskSender(INodeScanner nodeScanner, ITransporter transporter) {
		this.nodeScanner = nodeScanner;
		this.transporter = transporter;
		
		//TODO в NodeReciever
		String karafFolder = System.getProperty(Activator.KARAF_FOLDER_PROPERTY);
		Properties properties = new Properties();
		InputStream stream = getClass().getClassLoader().getResourceAsStream(karafFolder+"/"+Activator.PROPERTY_FILE); 
		if(stream !=null){
			try {
				properties.load(stream);
				own_ip = (String) properties.get("ip");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		
		//списки задач для отправки и метрик доступных устройств
		List<Task> unsendedTasks = DataProvider.getUnsendedTasks();
		List<DeviceConnection> deviceConnections = nodeScanner.getDeviceConnections();
		
//		если задачи есть
		if(unsendedTasks.size() != 0){
		
//			выбирается первое самое близкое устройство, кроме себя самого
			DeviceConnection connection = null;
			int i = 0;
			while(i < deviceConnections.size() && deviceConnections.get(i).getIp().equals(own_ip)
					&& deviceConnections.get(i).getConnection() != 0)
				i++;
			try{ connection = deviceConnections.get(i);	} catch (IndexOutOfBoundsException e){}
			
//			Если есть устройства
			if(connection != null){
				
//				получение устройства, на которое отправятся задачи
				String ip = connection.getIp();
				Device device = DataProvider.getDevice(ip);
				
//				Отправка
				for(Task task : unsendedTasks){
					
					if(transporter.sendTask(ip, "plan", task)){
						DataProvider.setTaskSended(task, device);
					}
					
				}
			}
		}
		
	}

}

























