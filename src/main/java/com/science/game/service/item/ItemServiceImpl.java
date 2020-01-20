package com.science.game.service.item;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.ParamReader;
import com.science.game.cache.data.DataCenter;
import com.science.game.entity.Item;
import com.science.game.entity.scene.ItemData;
import com.science.game.service.AbstractService;
import com.science.game.service.item.module.AddItemModule;
import com.science.game.service.item.module.CreateItemModule;
import com.science.game.service.item.module.ExtractItemModule;
import com.science.game.service.item.module.ItemInfoModule;

@Service
public class ItemServiceImpl extends AbstractService implements ItemService, ItemInternal {

	@Autowired
	private AddItemModule addItemModule;

	@Autowired
	private CreateItemModule createItemModule;

	@Autowired
	private ItemInfoModule ItemInfoModule;

	@Autowired
	private ExtractItemModule extractItemModule;

	@Autowired
	private DataCenter dataCenter;

	@Override
	protected void dispatch(String cmd, ParamReader i) {

	}

	@Override
	public void initCache() {

	}

	@Override
	public void gc() {

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

	@Override
	public boolean hasItemRecord(int itemId) {
		Map<Integer, List<Item>> map = dataCenter.getScene().getItemData().getAllItemsByItemId();
		return map.containsKey(itemId);
	}

	@Override
	public Item getItemByOnlyId(int onlyId) {
		return dataCenter.getScene().getItemData().getItemByOnlyId(onlyId);
	}

	@Override
	public List<Item> getItemList(int itemId) {
		return dataCenter.getScene().getItemData().getItemsByItemId(itemId);
	}

	@Override
	public Item removeItemByOnlyId(int onlyId) {
		ItemData itemData = dataCenter.getScene().getItemData();
		Item item = itemData.getItemByOnlyId(onlyId);
		if (item == null) {
			return null;
		}

		int itemId = item.getProto().getItemId();
		if (itemData.getItemsByItemId(itemId).remove(item)) {
			return item;
		}
		return null;
	}

	@Override
	public List<Item> extractItem(int itemId, int count) {
		return extractItemModule.extractItemByItemId(itemId, count);
	}

}
