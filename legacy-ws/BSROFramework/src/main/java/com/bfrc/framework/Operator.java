package com.bfrc.framework;

import com.bfrc.*;

public interface Operator extends Bean {
	final static String ERROR = "error";
	final static String OPERATION = "operation";
	final static String RESULT = "result";
	final static String SUCCESS = "success";
Object operate(Object o) throws Exception;
void setConfig(Config c);
Config getConfig();
}
