package com.bfrc.framework.businessdata;

import java.util.Map;

import com.bfrc.framework.AbstractOperator;

public abstract class BusinessOperatorSupport extends AbstractOperator implements BusinessOperator {

	public String operate(Map objects) throws Exception {
		Object o = operate((Object)objects);
		if(o == null)
			return null;
		return o.toString();
	}

}
