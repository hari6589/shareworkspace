package com.core.inheritance;

class ClassA {
	public void dispA() {
		System.out.println("ClassA dispA method!");
	}
}

public class SingleInheritance extends ClassA {
	public void dispB() {
		System.out.println("ClassB dispB method!");
	}
	
	public static void main(String []args) {
		ClassB b = new ClassB();
		b.dispB();
		b.dispA();
	}
}