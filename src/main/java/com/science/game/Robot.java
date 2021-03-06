package com.science.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.science.game.service.lab.LabInternal;

import game.quick.window.GameWindows;

@Component
@Order(2)
public class Robot implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private GameWindows win;

	@Autowired
	private LabInternal labInternal;

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
		c("village.recruite");
		c("village.recruite");
		c("village.recruite");
//		c("assart.assart 1");
		c("mine.dig 1 1");
		c("mine.dig 2 1");
		c("forest.chop 3 2");
		c("forest.chop 4 2");
		c("build.apply 1");
		c("build.apply 1");
		until(() -> labInternal.getIdeaList().size() > 0);
		int itemId = labInternal.getIdeaList().get(0);
		c("lab.develop 5 " + itemId);
		until(() -> labInternal.isDeveloped(itemId));
		c("product.product 6 " + itemId);
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
