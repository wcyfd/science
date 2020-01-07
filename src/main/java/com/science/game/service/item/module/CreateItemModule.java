package com.science.game.service.item.module;

import java.util.LinkedList;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Place;

@Component
public class CreateItemModule {

	public void createItemIfAbsent(int itemId) {
		Data.itemMap.putIfAbsent(itemId, new LinkedList<>());
		Data.itemPlace.putIfAbsent(itemId, Place.create(itemId));// 资源型item不需要创建道具位
	}

	public void createItemPlace(int itemId) {
		Data.itemPlace.putIfAbsent(itemId, Place.create(itemId));// 资源型item不需要创建道具位
	}
}
