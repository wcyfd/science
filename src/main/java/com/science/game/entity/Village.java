package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;

@Data
public class Village {
	private static AtomicInteger ID = new AtomicInteger(1);
	private int id;
	private String job;

	public Village() {
		id = ID.getAndIncrement();
	}

	public int getId() {
		return id;
	}

	public String getJob() {
		return job;
	}

	@Override
	public String toString() {
		return "Village [id=" + id + ", job=" + job + "]";
	}

}
