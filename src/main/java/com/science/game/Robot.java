package com.science.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;

import game.quick.window.GameWindows;

@Component
@Order(2)
public class Robot implements ApplicationListener<ContextStartedEvent> {

	@Autowired
	private GameWindows win;

	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		new Thread() {
			public void run() {
				w(1000);
				System.out.println("robot");

				c("village.recruite");
				c("village.recruite");
				c("job.assart 1");
				c("job.collect 2 1");
				c("job.collect 3 2");
			};
		}.start();

	}

	private void c(String line) {
		win.command(line);
	}

	private void w(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
