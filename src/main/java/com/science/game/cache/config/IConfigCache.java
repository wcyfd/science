package com.science.game.cache.config;

import com.science.game.ParamReader;

public interface IConfigCache {

	void load(ParamReader i);

	String getFileName();

	void afterLoad();
}
