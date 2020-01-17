package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 进度数据
 * 
 * @author aimfd
 *
 */
public class ProgressData {
	@Getter
	private AtomicInteger current = new AtomicInteger();
	@Getter
	@Setter
	private int total;
}
