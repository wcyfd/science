package com.science.game.service.item;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aimfd.game.tool.reserve.Reserve;
import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Place;
import com.science.game.service.AbstractService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public Item createItemIfAbsent(int itemId) {
		if (!Data.itemMap.containsKey(itemId)) {
			Data.itemMap.putIfAbsent(itemId, Item.create(itemId));
			Data.itemPlace.put(itemId, Place.create(itemId));// 资源型item不需要创建道具位
		}

		return Data.itemMap.get(itemId);
	}

	@Override
	public void addItem(int itemId, int count) {
		Item item = Data.itemMap.get(itemId);
		if (item != null) {
			Reserve reserve = Reserve.builder().delta(count).store(item.getNum()).build();
			if (reserve.transfer()) {
				log.debug("扣除{} {}", itemId, count);
				item.setNum(reserve.getRemainCount());
			}
		}

	}

}
