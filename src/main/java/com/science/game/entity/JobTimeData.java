package com.science.game.entity;

import game.quick.window.Task;

public class JobTimeData {
	private long total;
	private long velocity;
	private Task task;

	public void setTotal(long total) {
		this.total = total;
	}

	public long getTotal() {
		return total;
	}

	public void setVelocity(long velocity) {
		this.velocity = velocity;
	}

	public long getVelocity() {
		return velocity;
	}

	public long getDelayTime() {
		return total / velocity;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
