package com.science.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.IConfigCache;
import com.science.game.service.AbstractService;

import game.quick.window.GameWindows;

@Component
@Order(1)
public class StartListener implements ApplicationListener<ContextStartedEvent> {

	@Autowired
	private GameWindows win;

	@Override
	public void onApplicationEvent(ContextStartedEvent event) {

		ApplicationContext ctx = event.getApplicationContext();
		// 加载配置表
		ctx.getBeansOfType(IConfigCache.class).values()
				.forEach(cache -> ConfigLoader.load("./config/" + cache.getFileName(), cache));
		ctx.getBeansOfType(AbstractService.class).values().forEach(service -> service.initService());

		win.setHandler(ctx.getBean(ScienceHandler.class));
		win.setView(ctx.getBean(ScienceView.class));
		win.build().startConsoleThread();
	}

}
