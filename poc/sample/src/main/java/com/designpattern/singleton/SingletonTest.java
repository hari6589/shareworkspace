package com.designpattern.singleton;

public class SingletonTest {

	public static void main(String[] args) {
		System.out.println(Singleton.getInstance().hashCode());
		System.out.println(Singleton.getInstance().hashCode());
		System.out.println(Singleton.getInstance().hashCode());
		System.out.println(Singleton.getInstance().hashCode());
	}

}
