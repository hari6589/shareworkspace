package com.bfrc.security;

import com.bfrc.Base;
import com.bfrc.framework.util.*;
public class Encode extends Base {
	public static String html(String value){
		if(ServerUtil.isNullOrEmpty(value))
			return value;
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");  
		//value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");  
		value = value.replaceAll("(?i)onblur\\s*=", "");
		value = value.replaceAll("(?i)onchange\\s*=", "");
		value = value.replaceAll("(?i)onclick\\s*=", "");
		value = value.replaceAll("(?i)ondblclick\\s*=", "");
		value = value.replaceAll("(?i)onfocus\\s*=", "");
		value = value.replaceAll("(?i)onkeydown\\s*=", "");
		value = value.replaceAll("(?i)onkeypress\\s*=", "");
		value = value.replaceAll("(?i)onkeyup\\s*=", "");
		value = value.replaceAll("(?i)onload\\s*=", "");
		
		value = value.replaceAll("(?i)onmousedown\\s*=", "");
		value = value.replaceAll("(?i)onmousemove\\s*=", "");
		value = value.replaceAll("(?i)onmouseout\\s*=", "");
		value = value.replaceAll("(?i)onmouseover\\s*=", "");
		value = value.replaceAll("(?i)onmouseup\\s*=", "");
		value = value.replaceAll("(?i)onreset\\s*=", "");
		value = value.replaceAll("(?i)onselect\\s*=", "");
		value = value.replaceAll("(?i)onunload=\\s*", "");      
		value = value.replaceAll("eval\\((.*)\\)", "");  
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");  
		//value = value.replaceAll("script", "");
		value = value.replaceAll("(?i)Location: http", "");//remove header injection
		value = value.replaceAll("(?i)Document.write", "");//remove client out put
		value = value.replaceAll("(?i)Set-Cookie: ", "");//remove cookie injection
		return value;
	}
	
	public static String[] html(String[] params) {
		if(params == null)
			return null;
		for(int i=0;i<params.length;i++)
			params[i] = Encode.html(params[i]);
		return params;
	}
	public static String escapeDb(String in) {
		return escapeDb(in, false);
	}
	
	
	public final static char[] CHAR_ALPHANUMERICS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	
	private final static char[] IMMUNE_JAVASCRIPT = { ',', '.', '-', '_', ' ','/','(',')' ,'+','@','&'};
	
	public static String encodeForJavaScript(String input) {
		if( input == null ) return null;
		StringBuffer sb = new StringBuffer();
		for ( int i=0; i<input.length(); i++ ) {
			char c = input.charAt(i);
			sb.append( encode( c, CHAR_ALPHANUMERICS, IMMUNE_JAVASCRIPT ) );
		}
		return sb.toString();
	}
	
	private static String encode( char c, char[] baseImmune, char[] specialImmune ) {
		if (isContained(baseImmune, c) || isContained(specialImmune, c)) {
			return ""+c;
		} else {
			return encodeCharacter( new Character( c ) );
		}
	}

	public static String encodeCharacter( Character c ) {
		char ch = c.charValue();
		switch(ch)
		{
			case 0x00:
				return "\\0";
			case 0x08:
				return "\\b";
			case 0x09:
				return "\\t";
			case 0x0a:
				return "\\n";
			case 0x0b:
				return "\\v";
			case 0x0c:
				return "\\f";
			case 0x0d:
				return "\\r";
			case 0x27:
				return "\\'";
			case 0x5c:
				return "\\\\";
		}

		// encode up to 255 with \\xHH
		String temp = Integer.toHexString((int)ch);
		String out = "";
		if ( ch <= 255 ) {
			String pad = "00".substring(temp.length() );
			out = "\\x" + pad + temp.toUpperCase();
			return out;
		}

		// otherwise encode with \\uHHHH
		String pad = "0000".substring(temp.length() );
		out = "\\u" + pad + temp.toUpperCase();
		return out;
	}
	
	protected static boolean isContained(char[] haystack, char c) {
		for (int i = 0; i < haystack.length; i++) {
			if (c == haystack[i])
				return true;
		}
		return false;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (11/27/2000 11:18:44 AM)
	 * @return java.lang.String
	 * @param param java.lang.String
	 */
	public static String escapeDb(String in, boolean isACH) {
		String out = "";
		String oraHack = "&\'||\'";
		if(!isBlankOrNull(in))
			if(isACH) {
				for(int i=0;i < in.length();i++) {
					char currentChar = in.charAt(i);
	// begin security code
	        	    if (
	                 	// A-Z, a-z 0-9 are all OK
	            	     	(currentChar >= 'A' && currentChar <= 'Z') ||
	                	 	(currentChar >= 'a' && currentChar <= 'z') ||
	                 		(currentChar >= '0' && currentChar <= '9')) {
		                out += currentChar;
	    	        }
	        	    else {
	            		out += oraHack + "#" + (int)currentChar + ";";
		            }
	// end security code
				}
			}
			else {
	// regular escape of ' with ''
				for(int i=0;i < in.length();i++) {
					char currentChar = in.charAt(i);
					if(currentChar == '\'')
						out += "\'\'";
					else if(currentChar == '&')
						out += oraHack;
					else out += currentChar;
				}
			}
		return out;
	}
}
