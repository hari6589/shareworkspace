package com.bfrc.storelocator.util;

import java.util.*;

public class ErrorUtil {
	public static String[] getFieldsForError(String error) {
		if(!check(error))
			throw new IllegalArgumentException("invalid error code");
		String code = error.substring(1,4);
		List out = new ArrayList();
		if(!isAddressValid(code))
			out.add("address");
		if(!isCityValid(code))
			out.add("city");
		if("213".equals(code))
			out.add("state");
		if(!isZipValid(code))
			out.add("zip");
		return (String[])out.toArray();
	}
	public static String getMessageForError(String error) {
		if(!check(error))
			throw new IllegalArgumentException("invalid error code");
		String code = error.substring(1,4);
		if("213".equals(code))
			return "Your city and state do not match";
		boolean city = isCityValid(code);
		boolean zip = isZipValid(code);
		if(!city && !zip)
			return "Your city and ZIP code do not match";
		if(!zip)
			return "Your ZIP code is not valid";
		if(!isAddressValid(code))
			return "Your address is not valid";
		if(!city)
			return "Your city is not valid";
		return null;
	}
	public static boolean check(String error) {
		return error != null && error.length() == 4 && error.charAt(0) == 'E';
	}
	public static boolean isAddressValid(String code) {
		if("020".equals(code))
			return false;
		if("021".equals(code))
			return false;
		if("022".equals(code))
			return false;
		if("024".equals(code))
			return false;
		if("025".equals(code))
			return false;
		if("026".equals(code))
			return false;
		if("027".equals(code))
			return false;
		if("429".equals(code))
			return false;
		if("430".equals(code))
			return false;
		if("505".equals(code))
			return false;
		if("600".equals(code))
			return false;
		return true;
	}
	public static boolean isCityValid(String code) {
		if("012".equals(code))
			return false;
		if("013".equals(code))
			return false;
		if("213".equals(code))
			return false;
		if("214".equals(code))
			return false;
		return true;
	}
	public static boolean isZipValid(String code) {
		if("011".equals(code))
			return false;
		if("212".equals(code))
			return false;
		if("214".equals(code))
			return false;
		if("216".equals(code))
			return false;
		if("428".equals(code))
			return false;
		if("503".equals(code))
			return false;
		return true;
	}
}
