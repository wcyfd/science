package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.cache.config.ConfigCache;
import com.science.game.entity.config.ItemConfig;

public class Item {

	private static AtomicInteger ID = new AtomicInteger();
	private ItemConfig proto;
	private int num;
	private int id;

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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
