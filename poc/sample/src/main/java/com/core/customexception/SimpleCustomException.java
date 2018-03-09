package com.core.customexception;

public class SimpleCustomException {

	public static void main(String[] args) {
		ExcepTest et = new ExcepTest();
		try {
			et.test();
		} catch (Excep e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RunExcepTest re = new RunExcepTest();
		re.test();
	}

}

//checked exception or compiletime exception
class Excep extends Exception {
	Excep() {
		
	}
}

//unchecked exception or runtime exception
class RunExcep extends RuntimeException {
	RunExcep() {
		
	}
}

class ExcepTest {
	public void test() throws Excep {
		throw new Excep();
	}
}

class RunExcepTest {
	public void test() throws RunExcep {
		throw new RunExcep();
	}
}