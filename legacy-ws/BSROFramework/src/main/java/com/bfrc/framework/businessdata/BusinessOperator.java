package com.bfrc.framework.businessdata;

import java.util.Map;

import com.bfrc.framework.Operator;

public interface BusinessOperator extends Operator {
	String operate(Map objects) throws Exception;
}
