package com.science.game;

public interface ParamReader {
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

	default int i() {
		increaseIdx();
		return i(getIdx());
	}

	default long l() {
		increaseIdx();
		return l(getIdx());
	}

	default String str() {
		increaseIdx();
		return v(getIdx());
	}

	default short s() {
		increaseIdx();
		return Short.valueOf(v(getIdx()));
	}

	default byte b() {
		increaseIdx();
		return Byte.valueOf(v(getIdx()));
	}

	default float f() {
		increaseIdx();
		return Float.valueOf(v(getIdx()));
	}

	default double d() {
		increaseIdx();
		return Double.valueOf(v(getIdx()));
	}

	String v(int idx);

	void increaseIdx();

	void resetIdx();

	int getIdx();
}
