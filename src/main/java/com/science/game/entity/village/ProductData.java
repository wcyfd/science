package com.science.game.entity.village;

import lombok.Getter;
import lombok.Setter;

public class ProductData {
	@Getter
	private int vid;
	@Getter
	@Setter
	private int itemId;

	@Getter
	@Setter
	private boolean needCostItem;

	public ProductData(int vid) {
		this.vid = vid;
		this.needCostItem = false;
	}

}
