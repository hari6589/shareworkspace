package com.bfrc;

public abstract class Base implements Bean {
	public static boolean isBlankOrNull(String in) {
		return in == null || "".equals(in);
	}
}
