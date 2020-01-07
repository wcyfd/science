package com.science.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.science.game.cache.Data;

import game.quick.window.GameWindows;

@Component
@Order(2)
public class Robot implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private GameWindows win;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		new Thread() {
			public void run() {
				w(1000);
				System.out.println("robot");
				script();
			};
		}.start();

	}

	private void script() {
		c("village.recruite");
		c("village.recruite");
		c("village.recruite");
		c("village.recruite");
		c("job.assart 1");
		c("job.collect 2 1");
		c("job.collect 3 2");
		until(() -> Data.thinkList.size() > 0);
		int itemId = Data.thinkList.get(0);
		c("job.develop 4 " + itemId);
		until(() -> Data.itemMap.containsKey(itemId));
		c("job.product 4 " + itemId);
	}

	private void c(String line) {
		win.command(line);
	}

	private void w(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void until(ConditionFunc func, long t) {
		boolean loopTag = true;
		while (loopTag) {
			if (func.conform()) {
				loopTag = false;
			} else {
				w(t);
			}
		}
	}

	private void until(ConditionFunc func) {
		until(func, 5000);
	}

	@FunctionalInterface
	private interface ConditionFunc {
		boolean conform();
	}
}
