package com.science.game.service.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.entity.Item;
import com.science.game.service.AbstractService;
import com.science.game.service.item.module.AddItemModule;
import com.science.game.service.item.module.CreateItemModule;
import com.science.game.service.item.module.EquipModule;
import com.science.game.service.item.module.ItemInfoModule;

@Service
public class ItemServiceImpl extends AbstractService implements ItemService, ItemInternal {

	@Autowired
	private AddItemModule addItemModule;

	@Autowired
	private CreateItemModule createItemModule;

	@Autowired
	private EquipModule equipModule;

	@Autowired
	private ItemInfoModule ItemInfoModule;

	@Override
	protected void dispatch(String cmd, List<String> args) {
		switch (cmd) {
		case "equip":
			this.equip(getInt(args, 0), getInt(args, 1));
			break;
		case "unequip":
			this.unequip(getInt(args, 0), getInt(args, 1));
			break;
		}
	}

	@Override
	public void initCache() {

	}

	@Override
	public void gc() {

	}

	@Override
	public void equip(int vid, int onlyId) {
		equipModule.equip(vid, onlyId);
	}

	@Override
	public void unequip(int vid, int itemId) {
		equipModule.unequip(vid, itemId);
	}

	@Override
	public void addItem(int itemId, int count) {
		addItemModule.addItem(itemId, count);
	}

	@Override
	public void insertItem(Item item) {
		addItemModule.insertItem(item);
	}

	@Override
	public int getItemCount(int itemId) {
		return ItemInfoModule.getItemCount(itemId);
	}

	@Override
	public void createResItemSpace(int itemId) {
		createItemModule.createResItemIfAbsent(itemId);
	}

	@Override
	public void createEquipItemSpace(int itemId) {
		createItemModule.createEquipItemIfAbsent(itemId);
	}

}
