package com.core.exceptionhandling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ThrowExceptionTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ThrowExceptionTest tet = new ThrowExceptionTest();
		try {
			tet.test();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test() throws FileNotFoundException {
		
		FileReader fr = new FileReader("D://prog1.txt");
		
		new FileNotFoundException();
	}
}
