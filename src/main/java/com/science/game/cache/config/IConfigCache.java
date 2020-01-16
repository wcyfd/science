package com.science.game.cache.config;

import java.util.List;

public interface IConfigCache {
	ThreadLocal<List<String>> threadLocals = new ThreadLocal<List<String>>();

	void load();

	String getFileName();

	void afterLoad();

	default int i(int idx) {
		return Integer.valueOf(v(idx));
	}

	default long l(int idx) {
		return Long.valueOf(v(idx));
	}

	default String str(int idx) {
		return v(idx);
	}

	default short s(int idx) {
		return Short.valueOf(v(idx));
	}

	default byte b(int idx) {
		return Byte.valueOf(v(idx));
	}

	default float f(int idx) {
		return Float.valueOf(v(idx));
	}

	default double d(int idx) {
		return Double.valueOf(v(idx));
	}

	default String v(int idx) {
		List<String> list = threadLocals.get();
		return list.get(idx);
	}
}
