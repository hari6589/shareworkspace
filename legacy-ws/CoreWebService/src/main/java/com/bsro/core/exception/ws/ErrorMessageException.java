package com.bsro.core.exception.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMessageException extends RuntimeException {

	private List<String> globalErrors = new ArrayList<String>();
	private Map<String, List<String>> fieldErrors = new HashMap<String, List<String>>();
	
	private static final long serialVersionUID = 1L;

	public ErrorMessageException() {
		super();
	}
	
	public ErrorMessageException(List<String> globalErrors) {
		super();
		this.globalErrors = globalErrors;
	}
	
	public ErrorMessageException(Map<String, List<String>> fieldErrors) {
		super();
		this.fieldErrors = fieldErrors;
	}
	
	public ErrorMessageException(List<String> globalErrors, Map<String, List<String>> fieldErrors) {
		super();
		this.globalErrors = globalErrors;
		this.fieldErrors = fieldErrors;
	}

	public String getInternalErrorMessage() {
		Throwable cause = this.getCause();
		if (cause != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			cause.printStackTrace(pw);
			return sw.toString();
		}
		return null;
	}

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
