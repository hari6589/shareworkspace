package com.ses.sample.email;

import java.util.Map;

public class StringUtils {
	
	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().equals("") || str.trim().equals("null")) {
			return true;
		} 
		return false;
	}
	
	public static String populateEmailMessageContent(String content, Map<String, String> data) {
		try {
			for (String key : data.keySet()) {
				String value = data.get(key);
				if (content.contains("%%" + key + "%%") && value != null) {
					try {
						content = content.replace("%%" + key + "%%", value);
					} catch (Exception ex) {
						System.out.println("Exception : " + ex.getMessage());
					}
				}
			}
			return content;
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return "";
	}
	
	public static String padStoreNumber(String storeNumber){
		StringBuffer paddedNumber = new StringBuffer();
		for(int j=0; j<6-storeNumber.length(); j++)
			paddedNumber.append("0");
		paddedNumber.append(storeNumber);
		
		return paddedNumber.toString();
	}
	
}
