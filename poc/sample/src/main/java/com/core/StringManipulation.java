package com.core;

public class StringManipulation {

	public static void main(String[] args) {
		String a = "Hello";
		String b = "Hello1";
		System.out.println(a);
		System.out.println(b);
		System.out.println(a.hashCode());
		System.out.println(b.hashCode());
		
		//
		System.out.println("------------1-------------");
		System.out.println(a==b);
		System.out.println(a.equals(b));
		
		//
		System.out.println("------------2-------------");
		StringBuffer strBuf = new StringBuffer("Hello");
		System.out.println(strBuf);
		System.out.println(strBuf.hashCode());
		strBuf.append(" World!");
		System.out.println(strBuf.hashCode());
		strBuf.reverse();
		System.out.println(strBuf.toString());
		
		//
		System.out.println("------------3-------------");
		String str1 = new String("test");
		String str2 = new String("test");
		
		System.out.println(str1.hashCode());
		System.out.println(str2.hashCode());
		
		//
		System.out.println("------------4-------------");
		StringManipulation sm = new StringManipulation();
		System.out.println(sm.hashCode());
		
		//
		System.out.println("------------5-------------");
		String myString = new String( "old String" );
	    System.out.println( myString );
	    myString.replaceAll( "old", "new" );
	    System.out.println( myString );
	    myString = "jhgg";
	    System.out.println( myString );

	    // .toString() vs String.valueOf()
	    System.out.println("------------6-------------");
	    Object s = null;
		System.out.println(String.valueOf(s));
		System.out.println((String.valueOf(s) instanceof String));
		System.out.println(s.toString());
	}

}
