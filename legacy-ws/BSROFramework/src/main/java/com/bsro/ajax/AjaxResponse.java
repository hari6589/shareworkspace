package com.bsro.ajax;

import java.io.Serializable;

public class AjaxResponse implements Serializable {
	public static final String STATUS_OK = "OK";
	public static final String STATUS_VALIDATION_FAILURE = "VALIDATION_FAILURE";
	public static final String STATUS_UNEXPECTED_ERROR = "ERROR";
	
	public AjaxResponse() {
		errors = new AjaxErrors();
	}
	
	//This is the POJO to be transformed to JSON
	private Object data;
	
	//This status code is an easy way of determining if 
	//an error has occurred
	private String status;
	
	//This is the list of errors that have occurred.
	//These could be validation errors or actual exceptions
	private AjaxErrors errors;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AjaxErrors getErrors() {
		return errors;
	}

	public void setErrors(AjaxErrors errors) {
		this.errors = errors;
	}
}
