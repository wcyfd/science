package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.config.ItemConfig;

public class Item {

	private static AtomicInteger ID = new AtomicInteger();
	private int id;
	private ItemConfig proto;
	private AtomicInteger age = new AtomicInteger();
	private AtomicInteger num = new AtomicInteger();

	public static Item create(int itemId) {
		Item item = new Item();
		item.id = ID.incrementAndGet();
		item.proto = ConfigCache.item.itemMap.get(itemId);
		return item;
	}

	public ItemConfig getProto() {
		return proto;
	}

	public int getId() {
		return id;
	}

	public AtomicInteger getAgeRef() {
		return age;
	}

	public int getAge() {
		return age.get();
	}

	public void setAge(int age) {
		this.age.set(age);
	}

	public int getNum() {
		return this.num.get();
	}

	public AtomicInteger getNumRef() {
		return this.num;
	}
}
