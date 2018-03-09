package com.java8;

public class DefaultMethodTest implements DefaultMethodInterface1, DefaultMethodInterface2 {
	
	// If this method is not defined then default method will be called from Interface
	/*public void print() {
		System.out.println("Hello non-default!");
	}*/

	// If more than one Interface has same default-method then we could specify like below
	public void print() {
		DefaultMethodInterface1.super.print();
		DefaultMethodInterface2.super.print();
	}
	
	public static void main(String[] args) {
		DefaultMethodTest dmt = new DefaultMethodTest();
		dmt.print();
	}

}
