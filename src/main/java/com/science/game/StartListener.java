package com.science.game;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.IConfigCache;
import com.science.game.service.AbstractService;

import game.quick.window.GameWindows;

@Component
public class StartListener implements ApplicationListener<ContextStartedEvent> {

	@Autowired
	private GameWindows win;

	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		try {

			ApplicationContext ctx = event.getApplicationContext();
			// 加载配置表
			for (IConfigCache configCache : ctx.getBeansOfType(IConfigCache.class).values()) {
				ConfigLoader.load("./config/" + configCache.getFileName(), configCache);
			}

			Map<String, AbstractService> services = ctx.getBeansOfType(AbstractService.class);
			for (AbstractService service : services.values()) {
				service.initCache();
			}

			win.setHandler(ctx.getBean(ScienceHandler.class));
			win.setView(ctx.getBean(ScienceView.class));
			win.build().startConsoleThread();

		} catch (InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
	}

}
