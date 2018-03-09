package com.core.overloading;

public class OverloadingTest {

	public static void main(String[] args) {
		OverloadingTest olt = new OverloadingTest();
		
		olt.test(1);
		olt.test("test");
		
		// Overloading by changing order of parameters
		olt.test(1, "test");
		olt.test("test", 1);
		
		// Overloading static methods
		OverloadingTest.test(1.2);
		OverloadingTest.test('a');
	}
	
	public void test(int a) {
		System.out.println(a);
	}
	public void test(String a) {
		System.out.println(a);
	}
	
	public void test(int a, String s) {
		System.out.println(a + " _ " + s);
	}
	public void test(String s, int a) {
		System.out.println(a + " _ " + s);
	}
	
	public static void test(double a) {
		System.out.println(a);
	}
	public static void test(char a) {
		System.out.println(a);
	}
}
