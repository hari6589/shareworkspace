package com.bsro.core.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class Validator {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String US_ZIP_PATTERN = "\\d{5}(-\\d{4})?";
	private static Pattern emailPattern;
	private static Pattern usZipPattern;
	
	static {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		usZipPattern = Pattern.compile(US_ZIP_PATTERN);
	}

	/**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param allowNull
	 * @param errors
	 * @return
	 */
	public static boolean isValidInteger(String fieldName, String fieldValue, boolean allowNull, ErrorList errors) {
		return isValidInteger(fieldName, fieldValue, Integer.MIN_VALUE, Integer.MAX_VALUE, allowNull, errors);
	}
	
	/**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param minValue
	 * @param maxValue
	 * @param allowNull
	 * @param errors
	 * @return
	 */
	public static boolean isValidInteger(String fieldName, String fieldValue, int minValue, int maxValue, boolean allowNull, ErrorList errors) {
		fieldValue = StringUtils.trimToNull(fieldValue);
		if(allowNull && fieldValue == null)
			return true;
		else if(!allowNull && fieldValue == null) {
			errors.addError(new Error(fieldName, "Value cannot be null"));
			return false;
		}
		
		if(!NumberUtils.isDigits(fieldValue)) {
			errors.addError(new Error(fieldName, "Value consists of non-digit characters"));
			return false;
		}
		try {
			int i = new Integer(fieldValue);
			if(i < minValue || i > maxValue) {
				errors.addError(new Error(fieldName, "Value is not within range ["+minValue+","+maxValue+"]"));
				return false;
			}
		}catch(Exception e) {
			errors.addError(new Error(fieldName, "Value consists of non-digit characters"));
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param minValue
	 * @param maxValue
	 * @param allowNull
	 * @param errors
	 * @return
	 */
	public static boolean isValidLong(String fieldName, String fieldValue, long minValue, long maxValue, boolean allowNull, ErrorList errors) {
		fieldValue = StringUtils.trimToNull(fieldValue);
		if(allowNull && fieldValue == null)
			return true;
		else if(!allowNull && fieldValue == null) {
			errors.addError(new Error(fieldName, "Value cannot be null"));
			return false;
		}
		
		if(!NumberUtils.isDigits(fieldValue)) {
			errors.addError(new Error(fieldName, "Value consists of non-digit characters"));
			return false;
		}
		try {
			long i = new Long(fieldValue);
			if(i < minValue || i > maxValue) {
				errors.addError(new Error(fieldName, "Value is not within range ["+minValue+","+maxValue+"]"));
				return false;
			}
		}catch(Exception e) {
			errors.addError(new Error(fieldName, "Value consists of non-digit characters"));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Validates the fieldValue to match the email format regex
	 * @param fieldName
	 * @param fieldValue
	 * @param allowNull
	 * @param errors
	 * @return
	 */
	public static boolean isValidEmail(String fieldName, String fieldValue, boolean allowNull, ErrorList errors) {
		boolean isValid = isValidString(fieldName, fieldValue, allowNull, errors);
		if(isValid) {
			Matcher matcher = emailPattern.matcher(fieldValue);
			if(matcher.matches()) {
				return true;
			} else {
				errors.addError(new Error(fieldName, "Invalid email format"));
				return false;
			}
		} else {
			return false;
		}
	}
	

	public static boolean isValidString(String fieldName, String fieldValue, int minLength, int maxLength, boolean allowNull, ErrorList errors, Whitelist whitelist) {
		fieldValue = StringUtils.trimToNull(fieldValue);
		if(allowNull && fieldValue == null)
			return true;
		else if(!allowNull && fieldValue == null) {
			errors.addError(new Error(fieldName, "Value cannot be null"));
			return false;
		}
		
		if(fieldValue.length() < minLength || fieldValue.length() > maxLength) {
			errors.addError(new Error(fieldName, "Length of value is not within range ["+minLength+","+maxLength+"]"));
			return false;
		}
		
		/* 
		Whitelist options
			none() -  This whitelist allows only text nodes: all HTML will be stripped.
			simpleText() - This whitelist allows only simple text formatting: b, em, i, strong, u.
			basic() -  This whitelist allows a fuller range of text nodes: a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, strike, strong, sub, sup, u, ul, and appropriate attributes.
			basicWithImages() -  This whitelist allows the same text tags as basic(), and also allows img tags, with appropriate attributes, with src pointing to http or https.
			relaxed() - This whitelist allows a full range of text and structural body HTML: a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul
		 */
		if(!Jsoup.isValid(fieldValue, whitelist)) {
			errors.addError(new Error(fieldName, "Invalid data in "+fieldName+"."));
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param allowNull
	 * @param errors
	 * @return
	 */
	public static boolean isValidString(String fieldName, String fieldValue, boolean allowNull, ErrorList errors) {
		return isValidString(fieldName, fieldValue, 0, 99999, allowNull, errors, Whitelist.none());
	}
	
	public static boolean isValidString(String fieldName, String fieldValue, int minLength, int maxLength, boolean allowNull, ErrorList errors) {
		return isValidString(fieldName, fieldValue, minLength, maxLength, allowNull, errors, Whitelist.none());
	}
}
