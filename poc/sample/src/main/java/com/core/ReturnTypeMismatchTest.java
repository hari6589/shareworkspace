package com.core;

public class ReturnTypeMismatchTest {

	public static void main(String[] args) {
		ReturnTypeMismatchTest t = new ReturnTypeMismatchTest();
		System.out.println(t.test1());
		System.out.println(t.test2());
		System.out.println(t.test3());
		System.out.println(t.test4());
	}

	public int test1() {
		long l = 2;
		//return l; // Type mismatch: cannot convert from long to int
		return 2;
	}
	
	public long test2() {
		int i = 2;
		return i;
	}
	
	public float test3() {
		double d = 2L;
		//return d; // Type mismatch: cannot convert from double to float
		return 2;
	}
	
	public double test4() {
		float f = 2L;
		return f;
	}
}
