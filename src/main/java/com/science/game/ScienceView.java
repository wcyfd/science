package com.science.game;

import org.springframework.stereotype.Component;

import com.science.game.cache.Data;
import com.science.game.entity.Item;
import com.science.game.entity.Village;

import game.quick.window.IView;

@Component
public class ScienceView implements IView {

	private StringBuilder sb = new StringBuilder();

	@Override
	public String render() {
		sb.delete(0, sb.length());
		sb.append(Data.cmd).append("\n");
		sb.append("===========\n");
		village();
		sb.append("\n");
		item();
		sb.append("\n");
		sb.append("area\n");
		for (int i = 0; i < Data.areaId; i++) {
			sb.append(Data.areaList.get(i).getName()).append(" ");
		}
		sb.append("\n");

		return sb.toString();
	}

	private void village() {
		sb.append("Village").append("\n");
		for (Village village : Data.villages.values()) {
			sb.append(village.toString()).append("\t");
		}
	}

	private void item() {
		sb.append("Item").append("\n");
		for (Item item : Data.itemMap.values()) {
			sb.append(item.getProto().getItemId()).append(item.getProto().getName()).append(item.getNum()).append("\n");
		}
	}
}
