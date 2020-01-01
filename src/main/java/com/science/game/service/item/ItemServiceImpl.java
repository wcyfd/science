package com.science.game.service.item;

import java.util.List;

import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.service.AbstractService;

@Service
public class ItemServiceImpl extends AbstractService implements ItemService, ItemInternal {

	@Override
	protected void dispatch(String cmd, List<String> args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initCache() {
	}

	@Override
	public void gc() {
		// TODO Auto-generated method stub

	}

	@Override
	public void equip() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unequip() {
		// TODO Auto-generated method stub

	}

	@Override
	public Item getItem(int itemId) {
		if (!Data.itemMap.containsKey(itemId)) {
			Data.itemMap.putIfAbsent(itemId, Item.create(itemId));
			Data.itemPlace.put(itemId, Place.create(itemId));// 资源型item不需要创建道具位
		}

		return Data.itemMap.get(itemId);
	}

}
