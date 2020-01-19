package com.science.game.cache.config;

import com.science.game.I;

public interface IConfigCache {

	void load(I i);

	String getFileName();

	void afterLoad();
}
