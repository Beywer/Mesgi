package ru.smartsolutions.mesgi.planner.logic;

import java.util.List;
import java.util.TimerTask;

import ru.smartsolutions.mesgi.model.DeviceConnection;
import ru.smartsolutions.mesgi.model.INodeScanner;
import ru.smartsolutions.mesgi.model.Task;
import ru.smartsolutions.mesgi.planner.uicomponents.DataProvider;

/***
 * Отвечает за пересылку незапланированных задач на устройства.
 * @author beywer
 *
 */
public class TaskSender extends TimerTask {

	private static final String OWN_IP = "fc20:db84:e22d:785f:3d40:730c:6594:1b4c";
	
	private INodeScanner nodeScanner;
	
	public TaskSender(INodeScanner nodeScanner) {
		this.nodeScanner = nodeScanner;
	}
	
	@Override
	public void run() {
		
		//списки задач для отправки и метрик доступных устройств
		List<Task> tasks = (List<Task>) DataProvider.getUnplannedTasks().values();
		List<DeviceConnection> deviceConnections = nodeScanner.getDeviceConnections();
		
//		выбирается первое самое близкое устройство, кроме себя самого
		DeviceConnection connection = null;
		int i = 0;
		while(i < deviceConnections.size() && deviceConnections.get(i).getIp().equals(OWN_IP))
			i++;
		try{ connection = deviceConnections.get(i);	} catch (IndexOutOfBoundsException e){}
		
		if(connection != null){
			String ip = connection.getIp();
		}
	}

}
