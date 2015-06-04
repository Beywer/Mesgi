package ru.smartsolutions.mesgi.model;

public class Result {
	
	private String taskId;
	private String result;
	
	public Result(String taskId) {
		super();
		this.taskId = taskId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTaskId() {
		return taskId;
	}
}
