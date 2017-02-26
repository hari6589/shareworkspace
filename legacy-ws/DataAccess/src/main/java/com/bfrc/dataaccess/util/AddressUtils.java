package com.bfrc.dataaccess.util;

public class AddressUtils {

	public static boolean isValidZipCode(String str) {
		if (str == null)
			return false;
		String zipCodePattern = "\\d{5}(-\\d{4})?";
		return str.matches(zipCodePattern);
	}
}
