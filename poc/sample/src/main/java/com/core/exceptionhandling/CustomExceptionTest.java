package com.core.exceptionhandling;

public class CustomExceptionTest {

	public static String name;
	
	public static void main(String[] args) {
		name = "";
		try {
			displayName();
		} catch (CustomException e) {
			e.printStackTrace();
		}
	}

	public static void displayName() throws CustomException {
		if(name.equals("")) {
			throw new CustomException("Name value is empty!");
		}
	}
}
