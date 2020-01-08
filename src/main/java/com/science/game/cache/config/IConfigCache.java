package com.science.game.cache.config;

import java.util.List;

public interface IConfigCache {

	void load(List<String> values);

	String getFileName();

	void afterLoad();

	default int getInt(List<String> values, int idx) {
		return Integer.valueOf(values.get(idx));
	}

	default long getLong(List<String> values, int idx) {
		return Long.valueOf(values.get(idx));
	}
}
