package com.science.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.science.game.service.village.VillageService;

/**
 * Unit test for simple App.
 */

@ContextConfiguration(locations = "classpath:applicationContext.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
public class AppTest {
	@Autowired
	private VillageService villageService;

	@Test
	public void recruite() {
		villageService.recruite();
	}
}
