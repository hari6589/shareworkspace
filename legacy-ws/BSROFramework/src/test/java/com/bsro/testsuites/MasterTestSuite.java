package com.bsro.testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;



@RunWith(Suite.class)
@SuiteClasses({ControllerTestSuite.class, DAOTestSuite.class, ServiceTestSuite.class})
public class MasterTestSuite {

}
