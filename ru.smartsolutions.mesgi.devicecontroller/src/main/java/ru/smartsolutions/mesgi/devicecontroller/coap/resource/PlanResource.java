package ru.smartsolutions.mesgi.devicecontroller.coap.resource;

import java.util.HashMap;

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
	
//	private Queue<Task> plannedTasks;
	private Task lastTask;
	
	public PlanResource(String name, Planner planner) {
		super(name);
		
		getAttributes().setObservable();
		setObservable(true);
		setObserveType(Type.CON);
		
		this.planner = planner;
		gson = new Gson();
	}
	
	@Override
	public void handleDELETE(CoapExchange exchange) {
		
		planner.removeAllTasks();
		System.out.println("Plan clean");
		
		exchange.respond(ResponseCode.CONTENT);
		
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
		
		if(result.getPlannedInterval() != null){
			exchange.respond("planned");
		}else {
			exchange.respond("unplanned");
			System.out.println("Task was not planned" + result.getName());
		}
		
		exchange.respond(ResponseCode.CONTENT);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		
		System.out.println(lastTask);
		
		if(lastTask != null){
			String paramsJSON = gson.toJson(lastTask.getParameters());
			exchange.respond(paramsJSON);
			lastTask = null;
		}else exchange.respond("none");
		
		exchange.respond(ResponseCode.CONTENT);
	}
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		
		String mess = exchange.getRequestText();
		String req = mess.substring(0, 5);
		
		switch(req){
			case "allal" :
				break;
			case "check":
				
				Task task = null;
				
				String paramsJSON = mess.substring(5);
				HashMap<String, Object> params = gson.fromJson(paramsJSON, HashMap.class);
				
				task = new Task(params);
				
				Task result = planner.planTask(task);
				
				if(result.getPlannedInterval() != null){
					exchange.respond("planned");
					System.out.println("Check task good " + task.getName());
					lastTask = task;
					changed();
				}else {
					exchange.respond("unplanned");
					planner.removeUnplannedTask(task.getId());
				}
				break;
			default :
				Task plannedTask = planner.getPlan().get(mess);
				System.out.println("Controller  return planned task "+ plannedTask.getName());
				
				String answer = gson.toJson(plannedTask.getParameters());
				
				exchange.respond(answer);
				break;
		}

		exchange.respond(ResponseCode.CONTENT);
	}
}
