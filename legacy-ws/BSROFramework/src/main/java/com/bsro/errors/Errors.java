package com.bsro.errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Errors {
	private List<String> globalErrors = new ArrayList<String>();
	private Map<String, List<String>> fieldErrors = new HashMap<String, List<String>>();
	public List<String> getGlobalErrors() {
		return globalErrors;
	}
	public void setGlobalErrors(List<String> globalErrors) {
		this.globalErrors = globalErrors;
	}
	public Map<String, List<String>> getFieldErrors() {
		return fieldErrors;
	}
	public void setFieldErrors(Map<String, List<String>> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	public boolean hasErrors() {
		if ((fieldErrors != null && !fieldErrors.isEmpty()) || (globalErrors != null && !globalErrors.isEmpty())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addGlobalError(String errorMessage) {
		globalErrors.add(errorMessage);
	}
	
	public void addFieldError(String fieldName, String errorMessage) {
		if (!fieldErrors.containsKey(fieldName)) {
			fieldErrors.put(fieldName, new ArrayList<String>());
		}
		fieldErrors.get(fieldName).add(errorMessage);
	}
}
