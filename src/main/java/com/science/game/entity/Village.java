package com.science.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.science.game.entity.village.DevelopData;
import com.science.game.entity.village.ItemData;
import com.science.game.entity.village.PlaceData;
import com.science.game.entity.village.ProductData;
import com.science.game.entity.village.WorkData;

import lombok.Getter;

public class Village {
	private static AtomicInteger ID = new AtomicInteger(1);
	private int id;

	@Getter
	private WorkData workData;
	@Getter
	private PlaceData placeData;
	@Getter
	private DevelopData developData;
	@Getter
	private ProductData productData;
	@Getter
	private ItemData itemData;

	public Village() {
		id = ID.getAndIncrement();
		this.workData = new WorkData(id);
		this.placeData = new PlaceData(id);
		this.developData = new DevelopData(id);
		this.productData = new ProductData(id);
		this.itemData = new ItemData(id);
	}

	public int getId() {
		return id;
	}

}
