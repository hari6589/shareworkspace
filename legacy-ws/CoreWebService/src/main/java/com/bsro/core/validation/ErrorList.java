package com.bsro.core.validation;

import java.util.ArrayList;
import java.util.List;

public class ErrorList {

	private List<Error> errors;
	public void addError(Error error) {
		
		if(errors == null) errors = new ArrayList<Error>();
		errors.add(error);
	}
	
	public boolean hasErrors() {
		return (errors != null && errors.size() > 0);
	}
	
	public List<Error> getErrors() {
		return this.errors;
	}
}
