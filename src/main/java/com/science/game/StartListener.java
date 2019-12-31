package com.science.game;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import com.science.game.cache.config.ConfigCache;
import com.science.game.service.AbstractService;

import game.quick.window.GameWindows;

@Component
public class StartListener implements ApplicationListener<ContextStartedEvent>, ApplicationContextAware {

	@Autowired
	private GameWindows win;

	@Override
	public void onApplicationEvent(ContextStartedEvent event) {
		try {
			ConfigLoader.load("./config/item.csv", ConfigCache.item);
			ConfigLoader.load("./config/res.csv", ConfigCache.res);
			ConfigLoader.load("./config/consist.csv", ConfigCache.consist);

			ApplicationContext ctx = event.getApplicationContext();

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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		Map<String, AbstractService> services = applicationContext.getBeansOfType(AbstractService.class);
//		for (AbstractService service : services.values()) {
//			service.setGameWindows(win);
//		}
	}

}
