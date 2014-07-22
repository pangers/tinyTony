package com.pangers.tinyTony;

public class TaskData {

	private String task;
	private String time;
	private String importance;

	public TaskData(String task, String time, String importance) {
		this.task = task;
		this.time = time;
		this.importance = importance;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

}
