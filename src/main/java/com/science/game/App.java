package com.science.game;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import game.quick.window.GameWindows;

/**
 * Hello world!
 *
 */
@Configuration
public class App {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IOException {
		new ClassPathXmlApplicationContext("classpath:applicationContext.xml").start();

	}

	@Bean
	public GameWindows gameWindows() {
		GameWindows win = GameWindows.create();
		return win;
	}

}
