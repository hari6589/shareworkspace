package com.bfrc.dataaccess.core.beans;

import java.util.HashMap;
import java.util.Map;

import com.bfrc.dataaccess.core.beans.PropertyAccessor;

public class PropertyAccessorImpl implements PropertyAccessor {
	private Map<String, String> propertyMap;

	public PropertyAccessorImpl() {
		propertyMap = new HashMap<String, String>();
	}
	
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<String, String> propertyMap) {
		this.propertyMap = propertyMap;
	}
	
	public String getStringProperty(String propertyName) {
		return propertyMap.get(propertyName);
	}
	
	public Integer getIntegerProperty(String propertyName) {
		return Integer.parseInt(propertyMap.get(propertyName));
	}
}
