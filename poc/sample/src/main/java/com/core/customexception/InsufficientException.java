package com.core.customexception;

public class InsufficientException extends Exception {
	
	public double amount;
	
	public InsufficientException(double amount) {
		this.amount = amount;
	}
	
	public double getAmount() {
		return this.amount;
	}
}
