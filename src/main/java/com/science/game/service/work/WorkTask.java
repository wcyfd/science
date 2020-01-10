package com.science.game.service.work;

import com.science.game.service.AbstractService;

import game.quick.window.Task;

public abstract class WorkTask implements Task {
	private AbstractService service;
	private volatile boolean stop = false;

	public WorkTask(AbstractService service) {
		this.service = service;
	}

	@Override
	public void afterExecute() {
		if (stop)
			return;

		nextLoop();
	}

	private void nextLoop() {
		service.delay(this);
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isStop() {
		return stop;
	}

	public void start() {
		nextLoop();
	}

}
