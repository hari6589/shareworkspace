package com.core.abstracttest;

abstract class Shape {
	
	abstract void draw();
	
	void draw1() {
		System.out.println("Draw Shape!!");
	}
}

class Circle extends Shape {
	@Override
	void draw() {
		System.out.println("Draw Circle!");
	}
}

class Rectangle extends Shape {
	@Override
	void draw() {
		System.out.println("Draw Rectangle!");
	}
}

class TestAbstract {
	public static void main(String []args) {
	
		Shape c = new Circle();
		c.draw();
		
		Shape r = new Rectangle();
		r.draw();
		
	}
}