package com.pangers.tinyTony;

public class taskData {

	private String task;
	private String time;
	private String importance;

	public taskData(String task, String time) {
		this.task = task;
		this.time = time;
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
