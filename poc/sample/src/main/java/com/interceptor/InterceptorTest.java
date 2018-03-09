package com.interceptor;

class Intercept {
//	@AroundInvoke
	public void display() {
		System.out.println("hello interceptor!");
	}
}

public class InterceptorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s1 = "2";
		String s2 = "3";
		System.out.println(s1.compareTo(s2));
	}

}
