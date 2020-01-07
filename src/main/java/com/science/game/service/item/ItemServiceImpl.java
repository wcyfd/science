package com.science.game.service.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.service.AbstractService;
import com.science.game.service.item.module.AddItemModule;
import com.science.game.service.item.module.CreateItemModule;

@Service
public class ItemServiceImpl extends AbstractService implements ItemService, ItemInternal {

	@Autowired
	private AddItemModule addItemModule;

	@Autowired
	private CreateItemModule createItemModule;

	@Override
	protected void dispatch(String cmd, List<String> args) {

	}

	@Override
	public void initCache() {

	}

	@Override
	public void gc() {

	}

	@Override
	public void equip(int vid, int onlyId) {

	}

	@Override
	public void unequip() {

	}

	@Override
	public void createItemIfAbsent(int itemId) {
		createItemModule.createItemIfAbsent(itemId);
	}

	@Override
	public void addItem(int itemId, int count) {
		addItemModule.addItem(itemId, count);
	}

	@Override
	public void createItemPlace(int itemId) {
		createItemModule.createItemPlace(itemId);
	}

	@Override
	public boolean itemIsDeveloped(int itemId) {
		return Data.itemMap.containsKey(itemId);
	}

}
