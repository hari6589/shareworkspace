package com.core.customexception;

public class DemoBank {

	public static void main(String[] args) {
		CheckAccount checkAccount = new CheckAccount(1234);
		
		System.out.println("Balance : " + checkAccount.getBalance());
		
		checkAccount.depositBalance(500.00);
		System.out.println("Balance : " + checkAccount.getBalance());
		
		try {
			checkAccount.withdraw(300.00);
			System.out.println("Balance : " + checkAccount.getBalance());
		
			checkAccount.withdraw2(300.00); // checkAccount.withdraw(300.00);
			System.out.println("Balance : " + checkAccount.getBalance());
			
		} catch (InsufficientException e) {
			System.out.println("Sorry, but you are short $" + e.getAmount());
			e.printStackTrace();
		} catch (InsufficientException2 e) {
			System.out.println("Sorry, but you are short of some amount");
			e.printStackTrace();
		}
	}

}
