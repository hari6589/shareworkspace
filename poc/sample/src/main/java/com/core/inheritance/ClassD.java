package com.core.inheritance;

public class ClassD extends ClassB{
	
	public void dispD() {
		System.out.println("ClassD dispD method!");
	}
	
	public static void main(String []args) {
		ClassD d = new ClassD();
		d.dispA();
		d.dispB();
		d.dispD();
	}
}
