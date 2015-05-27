package ru.smartsolutions.mesgi.planner.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class Main {

	public static void main(String[] args) {
		
		DateTime start = new DateTime();
		long startMillis = start.getMillis();
		List<Task> tasks = new ArrayList<Task>();
		Random r = new Random();
		
		for(int i =0; i < 5; i++){
			Task task = new Task("Task " +i);
			System.out.print(start + " -- ");
			task.setDuration(r.nextInt(20) + 20);
			long end = startMillis + Math.abs(r.nextInt(2000)*
					r.nextInt(2000)) + task.getDuration()*1000*60*3;
			System.out.println(end);
			Interval interval = new Interval(startMillis,end);
			task.setIntervalAllowed(interval);
			tasks.add(task);
		}

		System.out.println(tasks);
		Collections.sort(tasks);
		System.out.println(tasks);
		
		List<PlannedTask> plan = new ArrayList<PlannedTask>();
		
		startMillis = System.currentTimeMillis() + 10000;
		for(Task task : tasks){
			PlannedTask plannedTask = new PlannedTask(task.getId());
			//вемя завершения задачи - конец + длительность(из минут в милисекунды)
			long endMillis = startMillis + task.getDuration()*1000*60;
			Interval interval = new Interval(startMillis, endMillis);
			plannedTask.setPlannedInterval(interval);
			//если время завершения задачи >= запланированного завершения
			//планирование считается успешным
			if(task.getIntervalAllowed().getEndMillis() >= endMillis)
				plannedTask.setSuccessed(true);
			else plannedTask.setSuccessed(false);

			plan.add(plannedTask);
			//Интервал 10сек между задачами
			startMillis = endMillis + 10000;
		}
		
		System.out.println(plan);
	}

}
