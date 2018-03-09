package com.core.inheritance;

public class ClassB extends ClassA {
	public void dispB() {
		System.out.println("ClassB dispB method!");
	}
	
	public static void main(String []args) {
		ClassB b = new ClassB();
		b.dispB();
		b.dispA();
	}
}
