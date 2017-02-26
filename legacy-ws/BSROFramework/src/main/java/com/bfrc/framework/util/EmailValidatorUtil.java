package com.bfrc.framework.util;

import java.util.regex.Pattern;

import org.apache.commons.validator.EmailValidator;

public class EmailValidatorUtil extends EmailValidator {

	private static final String SPECIAL_CHARS = "\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]";
	private static final String VALID_CHARS = "[^" + SPECIAL_CHARS + "]";
	private static final String QUOTED_USER = "(\"[^\"]*\")";
	private static final String WORD = "((" + VALID_CHARS + "|')+|" + QUOTED_USER + ")";

	private static final String USER_REGEX = "^" + WORD + "(\\." + WORD + ")*$";

	private static final Pattern USER_PATTERN = Pattern.compile(USER_REGEX);

	protected EmailValidatorUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isValidUser(String user) {
		return USER_PATTERN.matcher(user).matches();
	}

	public boolean isValidEmail(String email) {
		return super.isValid(email);
	}

}