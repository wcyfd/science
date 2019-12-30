package com.science.game;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.science.game.service.AbstractService;
import com.science.game.service.ServiceInterface;

import game.quick.window.IView;

@Component
public class CommandView implements IView, ApplicationContextAware {

	private StringBuilder sb = new StringBuilder();

	@Override
	public String render() {
		return sb.toString();
	}

//	private void command() {
//		sb.append("Command").append("\n");
//		List<Class<?>> list = AbstractService.getServices();
//
//		for (Class<?> clazz : list) {
//			String name = clazz.getSimpleName().toLowerCase().replace("service", "");
//			Method[] methods = clazz.getMethods();
//			sb.append(name).append("\t");
//			for (Method method : methods) {
//				sb.append(method.getName().toLowerCase()).append("  ");
//			}
//			sb.append("\n");
//		}
//	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		sb.append("Command").append("\n");
		Map<String, AbstractService> services = ctx.getBeansOfType(AbstractService.class);
		for (AbstractService service : services.values()) {
			Class<?>[] clazzes = service.getClass().getInterfaces();
			for (Class<?> c : clazzes) {
				if (ServiceInterface.class.isAssignableFrom(c)) {
					String name = c.getSimpleName().toLowerCase().replace("service", "");
					Method[] methods = c.getMethods();
					sb.append(name).append("\t");
					for (Method method : methods) {
						sb.append(method.getName().toLowerCase()).append("  ");
					}
					sb.append("\n");
				}
			}
		}

	}

}
