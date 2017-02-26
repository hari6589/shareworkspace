package com.bsro.testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bsro.service.battery.BatteryServiceImplTest;

@RunWith(Suite.class)
@SuiteClasses({BatteryServiceImplTest.class})
public class ServiceTestSuite {
	
}
