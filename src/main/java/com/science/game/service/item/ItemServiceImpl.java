package com.science.game.service.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.service.AbstractService;
import com.science.game.service.item.module.AddItemModule;
import com.science.game.service.item.module.CreateItemModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItemServiceImpl extends AbstractService implements ItemService, ItemInternal {

	@Autowired
	private AddItemModule addItemModule;

	@Autowired
	private CreateItemModule createItemModule;

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
	public Item createItemIfAbsent(int itemId) {
		return createItemModule.createItemIfAbsent(itemId);
	}

	@Override
	public void addItem(int itemId, int count) {
		addItemModule.addItem(itemId, count);
	}

}
