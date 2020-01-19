package com.science.game.service.equip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.science.game.I;
import com.science.game.entity.Item;
import com.science.game.entity.Village;
import com.science.game.service.AbstractService;
import com.science.game.service.item.ItemInternal;
import com.science.game.service.village.VillageInternal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EquipServiceImpl extends AbstractService implements EquipService {

	@Autowired
	private ItemInternal itemInternal;

	@Autowired
	private VillageInternal villageInternal;

	@Override
	protected void dispatch(String cmd, I i) {
		switch (cmd) {
		case "equip":
			this.equip(i.i(), i.i());
			break;
		case "unequip":
			this.unequip(i.i(), i.i());
			break;
		}
	}

	@Override
	public void equip(int vid, int onlyId) {
		Village v = villageInternal.getVillage(vid);
		Item item = itemInternal.removeItemByOnlyId(onlyId);
		if (item == null) {
			log.info("没有该道具，或者该道具已经被某人装备");
			return;
		}

		unequip(vid, item.getProto().getItemId());

		v.getItemData().getEquips().put(item.getProto().getItemId(), item);
	}

	@Override
	public void unequip(int vid, int itemId) {
		Village v = villageInternal.getVillage(vid);
		Item item = v.getItemData().getEquips().remove(itemId);
		if (item != null) {
			itemInternal.insertItem(item);
		}
	}

}
