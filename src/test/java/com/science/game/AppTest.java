package com.science.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import game.quick.window.GameWindows;

/**
 * Unit test for simple App.
 */

@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class AppTest {
	@Autowired
	private GameWindows win;

	@Test
	public void recruite() {
		win.command("village.recruite");
	}
}
