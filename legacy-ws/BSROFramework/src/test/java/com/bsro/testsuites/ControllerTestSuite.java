package com.bsro.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({})
public class ControllerTestSuite {
	@BeforeClass
	public void setup() {
		System.out.println("Setup for Controller suite.");
	}
	
	@AfterClass
	public void teardown() {
		System.out.println("Tearing down all setup items.");
	}
}
