package ru.smartsolutions.mesgi.devicecontroller.coap.resource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;

import ru.smartsolutions.mesgi.devicecontroller.Planner;
import ru.smartsolutions.mesgi.model.Task;

import com.google.gson.Gson;

public class PlanResource extends CoapResource {

	private Gson gson;
	private Planner planner;
	private Queue<Task> plannedTasks;
	private ReentrantLock plannedTasksLock;
	
	public PlanResource(String name, Planner planner) {
		super(name);
		
		getAttributes().setObservable();
		setObservable(true);
		setObserveType(Type.CON);
		
		plannedTasks = new LinkedList<Task>();
		plannedTasksLock = new ReentrantLock();
		
		this.planner = planner;
		gson = new Gson();
	}
	
//	Получение задачи
	@SuppressWarnings("unchecked")
	@Override
	public void handlePUT(CoapExchange exchange) {

		Task task = null;
		
		String paramsJSON = exchange.getRequestText();
		HashMap<String, Object> params = gson.fromJson(paramsJSON, HashMap.class);
		
		task = new Task(params);
		
		Task result = planner.planTask(task);
		
		plannedTasksLock.lock();
		plannedTasks.add(result);
		changed();
		plannedTasksLock.unlock();
		
		exchange.respond(ResponseCode.CONTENT);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		plannedTasksLock.lock();
		Task task = plannedTasks.peek();
		plannedTasksLock.unlock();
		if(task != null){
			String params = gson.toJson(task.getParameters());
			exchange.respond(params);
		} exchange.respond("none");
		
		exchange.respond(ResponseCode.CONTENT);
	}
	
	@Override
	public void handleDELETE(CoapExchange exchange) {
		
		plannedTasksLock.lock();
		plannedTasks.poll();
		changed();
		plannedTasksLock.unlock();
		
		exchange.respond(ResponseCode.CONTENT);
	}
}
