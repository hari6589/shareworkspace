package com.core.overriding;

class ClassA {
	
	ClassA() {
		System.out.println("ClassA Constructor");
	}
	
	public int test() {
		System.out.println("ClassA");
		return 1;
	}
}

class OverridingTest extends ClassA {

	public int test() {
		//super.test();
		System.out.println("OverridingTest");
		return 1;
	}
	
	public static void main(String[] args) {
		OverridingTest ort = new OverridingTest();
		ort.test();
		
	}

}
