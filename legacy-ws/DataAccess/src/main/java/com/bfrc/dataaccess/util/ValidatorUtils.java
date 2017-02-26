package com.bfrc.dataaccess.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.validator.EmailValidator;

import sun.misc.BASE64Encoder;
public class ValidatorUtils {

	public static boolean doPasswordsMatch(String password1, String password2){
		try{
			if(password1!=null && password2!=null && password1.equals(password2)){
				return true;
			}
		}catch(Exception e){
			return false;
		}
		return false;

	}

	public static boolean validateUserEncryptedPassword(String currentPasswd, String userEnteredPasswd){
		String userEncryptedPasswd;
		try {
			userEncryptedPasswd = ValidatorUtils.encryptPassword(userEnteredPasswd);
		} catch (Exception e) {
			System.err.println("Exception thrown while encrypting password");
			return false;
		}
		if(currentPasswd!=null && userEncryptedPasswd!=null && currentPasswd.equals(userEncryptedPasswd)){
			return true;
		}
		return false;
	}

	public static boolean isEmailValid(String email) {
		return (email!=null&&EmailValidator.getInstance().isValid(email)&&email.length()<=255);
	}
	/*
	 * only ',.- are allowed, and name must be at least two letters and no more than 50
	 */
	public static boolean isFirstNameValid(String firstName) {
		return(firstName.length() > 2 && firstName.matches("[a-zA-Z]+(([\\'\\,\\.\\-\\s][a-zA-Z\\.\\s])?[a-zA-Z]*([\\.\\s])?)*") && firstName.length()<=50);

	}
	/*
	 * only ',.- are allowed, and name must be at least two letters and no more than 80
	 */
	public static boolean isLastNameValid(String firstName) {
		return(firstName.length() > 2 && firstName.matches("[a-zA-Z]+(([\\'\\,\\.\\-\\s][a-zA-Z\\.\\s])?[a-zA-Z]*([\\.\\s])?)*") && firstName.length()<=80);

	}

	/*
	 * only '#,.- are allowed, and name must be at least two letters and no more than 100
	 */
	public static boolean isAddressValid(String address) {

		return(address.length() > 2 && address.matches("[a-zA-Z\\d]+(([\\'\\#\\,\\.\\-\\s][a-zA-Z\\d\\.\\s])?[a-zA-Z\\d]*([\\.\\s])?)*") && address.length()<=100);
	}

	public static boolean isCityValid(String city) {

		return(city.length() > 2 && city.matches("[a-zA-Z]+(([\\'\\,\\.\\-\\s][a-zA-Z\\.\\s])?[a-zA-Z]*([\\.\\s])?)*") && city.length()<=50);
	}

	public static boolean isStateValid(String state) {
		return(state.length() == 2 && state.matches("[\\s]?[a-zA-Z]*[\\s]?[a-zA-Z]*")); 

	}

	/*
	 * Accepts 53045, 53045-4100, 530454100
	 * 
	 */
	public static boolean isZipCodeValid(String zip) {

		return(zip.matches("\\d{5}([- ]?\\d{4})?")) ;
	}
	/*
	 * 
	 * Enter a valid phone number 555-555-5555,1-555555555
	 */
	public static boolean isPhoneValid(String phone) {
		// TODO Auto-generated method stub
		return(phone.matches("(1(-|\\.)?)?(\\([2-9]\\d{2}\\)|[2-9]\\d{2})(-|\\.)?[2-9]\\d{2}(-|\\.)?\\d{4}")) ;
	}
	/*
	 * 
	 * checks for 3 of the following criteria
	 * uppercase
	 * lowercase
	 * digits
	 * special characters !@#$%^*()_+-={}[]|/
	 */
	public static boolean isPasswordValid(String passwordOne) {
		// TODO Auto-generated method stub		
		if(passwordOne!=null&&passwordOne.length() >= 8 ){
			//System.out.println("is at least 8 char.");
			int x=0;
			//check for digit
			if(passwordOne.matches("((?=.*\\d).{1,})"))
				x++;
			//check for lowercase
			if(passwordOne.matches("((?=.*[a-z]).{1,})"))
				x++;
			//check for upper case
			if(passwordOne.matches("((?=.*[A-Z]).{1,})"))
				x++;
			//check for characters !@#$%^*()_+-={}[]|/
			if(passwordOne.matches("((?=.*[@#%/_=\\-\\(\\)\\!\\+\\^\\*\\[\\]\\{\\}\\|\\$]).{1,})"))
				x++;

			if(x<3){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}

	public static String encryptPassword(String password){
		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(password.getBytes("UTF-8"));
			byte raw[] = md.digest(); 
			hash = (new BASE64Encoder()).encode(raw);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	} 
	
	public static boolean lockUserAccount(int numAttempts){
		if(numAttempts < ValidationConstants.MAX_PASSWORD_FAILURE_ATTEMPTS){
			return false;
		}
		return true;
	}
}
