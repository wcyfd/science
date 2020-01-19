package com.science.game;

import java.util.LinkedList;
import java.util.List;

public class DefaultI implements I {
	private ThreadLocal<List<String>> SAFE_REQUEST = new ThreadLocal<>();
	private ThreadLocal<Integer> SAFE_REQUEST_IDX = new ThreadLocal<>();

	@Override
	public String v(int idx) {
		return SAFE_REQUEST.get().get(idx);
	}

	@Override
	public void increaseIdx() {
		Integer i = SAFE_REQUEST_IDX.get();
		if (i == null) {
			resetIdx();
		}

		SAFE_REQUEST_IDX.set(SAFE_REQUEST_IDX.get() + 1);
	}

	@Override
	public void resetIdx() {
		SAFE_REQUEST_IDX.set(-1);
	}

	@Override
	public int getIdx() {
		return SAFE_REQUEST_IDX.get();
	}

	private void createThreadSafeParams() {
		List<String> list = SAFE_REQUEST.get();
		if (list == null) {
			SAFE_REQUEST.set(new LinkedList<>());
		}
	}

	public List<String> getParams() {
		createThreadSafeParams();
		return SAFE_REQUEST.get();
	}

	public void cleanParams() {
		resetIdx();
		SAFE_REQUEST.get().clear();
	}

	public void relaseMemory() {
		SAFE_REQUEST.set(null);
		SAFE_REQUEST_IDX.set(null);
	}
}
