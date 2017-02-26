package com.bsro.exception.errors;

import com.bsro.errors.Errors;

public class ErrorMessageException extends Exception {
	private Errors errors;
	
	public ErrorMessageException(Errors errors) {
		super();
		this.errors = errors;
	}
	
	public Errors getErrors() {
		return errors;
	}
	
	public String printErrors() {
		StringBuilder errorOutput = new StringBuilder("[");
		if (errors != null) {
			if (errors.getGlobalErrors() != null) {
				errorOutput.append("Global Errors: \n");
				for (String globalError : errors.getGlobalErrors()) {
					errorOutput.append(globalError).append("\n");
				}
			}
			if (errors.getFieldErrors() != null && !errors.getFieldErrors().isEmpty()) {
				errorOutput.append("Field Errors: \n");
				for (String fieldName : errors.getFieldErrors().keySet()) {
					errorOutput.append(fieldName).append(": \n");
					if (!errors.getFieldErrors().get(fieldName).isEmpty()) {
						for (String fieldError : errors.getFieldErrors().get(fieldName)) {
							errorOutput.append(fieldError).append("\n");
						}
					}
				}
			}
		}
		errorOutput.append("]");
		return errorOutput.toString();
	}
}
