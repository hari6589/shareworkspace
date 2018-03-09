package com.core.customexception;

public class CheckAccount {

	private int number;
	private double balance;
	
	public int getNumber() {
		return number;
	}
	public double getBalance() {
		return balance;
	}
		
	public CheckAccount(int number) {
		this.number = number;
	}

	public void depositBalance(double balance) {
		this.balance = balance;
	}

	public void withdraw(double amount) throws InsufficientException {
		if(amount > balance) {
			double needs = amount - balance; 
			throw new InsufficientException(needs);
		} else {
			balance -= amount;
		}
	}
	
	public void withdraw2(double amount) throws InsufficientException2 {
		if(amount > balance) { 
			throw new InsufficientException2();
		} else {
			balance -= amount;
		}
	}
}
