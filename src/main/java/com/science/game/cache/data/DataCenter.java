package com.science.game.cache.data;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.science.game.entity.Scene;

import lombok.Getter;

/**
 * 数据中心
 * 
 * @author heyue
 *
 */
@Component
public class DataCenter {

	@Getter
	private Scene scene;

	@PostConstruct
	public void init() {
		scene = new Scene();
	}

}
