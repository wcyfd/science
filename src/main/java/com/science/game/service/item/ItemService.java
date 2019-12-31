package com.science.game.service.item;

import com.science.game.service.ServiceInterface;

public interface ItemService extends ServiceInterface {

	void equip();

	void unequip();

	void gc();
}
