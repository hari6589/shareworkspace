package com.core.exceptionhandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CheckedException {

	public static void main(String[] args) {
		File file = new File("D://prog1.txt");
		try {
			FileReader fr = new FileReader(file);
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CheckedException ce = new CheckedException();
		ce.testThrow();
	}
	
	void testThrow() {
		try {
			int a[] = new int[2];
			System.out.println(a[0]);
		} catch(Exception e) {
			System.out.println("Exception!");
		}
	}

}
