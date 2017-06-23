package com.bsro.core.validation;

public class Error {

	private String field;
	private String message;
	
	public Error(){}
	public Error(String fieldName, String errorMessage) {
		setField(fieldName);
		setMessage(errorMessage);
	}
	
	public void setField(String field) {
		this.field = field;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getField() {
		return field;
	}
	public String getMessage() {
		return message;
	}


	public String toString() {
		return "Error [field=" + field + ", message=" + message + "]";
	}
	
	
}
