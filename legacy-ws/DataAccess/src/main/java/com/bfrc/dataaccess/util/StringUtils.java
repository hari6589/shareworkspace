package com.bfrc.dataaccess.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;


public class StringUtils {

	public static String listToString(List<? extends Object> list,
			String separator) {
		return listToString(list, "", separator, "");
	}

	public static String listToString(List<? extends Object> list,
			String header, String separator, String footer) {
		String delim = "";
		StringBuilder sb = new StringBuilder(header);

		for (int i = 0; i < list.size(); i++) {
			sb.append(delim).append("" + list.get(i));
			delim = separator;
		}

		return sb.append(footer).toString();
	}
	
	public static String[] listToStringArray(List<String> list) {
		if(list == null) return null;
		String[] result = new String[list.size()];
		int idx = 0;
		for(String s : list) {
			result[idx] = s;
			idx++;
		}
		return result;
	}
	private static java.util.Random random = null;
	  
	  public static String generatePassword(int charLen){
		  if(charLen <= 0)
			  charLen = 8; //default to 8
		  if(random==null){
				try{
					//Provided By sun, it will cause a lot garbage collection when startup
				    //random = java.security.SecureRandom.getInstance("SHA1PRNG");
					//random = new FastRandom();
					random = new java.util.Random();//use classic one which is fastter
				}catch(Exception ex){
				    System.out.println(ex.toString());
				}
		  }
		  byte charBytes[] = new byte[charLen];
	      
	      for(int i = 0; i < charLen; i++){
	          charBytes[i] = LETTER_AND_NUMBERS[(random.nextInt(128)) % 64];
	      }

	      return new String(charBytes);
	  }

	  public static String generatePassword(){
		  return generatePassword(-1);
	  }
	  private static final byte LETTER_AND_NUMBERS[] = {
	      65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 
	      75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 
	      85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 
	      101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 
	      111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 
	      121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 
	      56, 57, 50, 49
	  };
	  
	  public static String createForgotPasswordEmailString(){
		  StringBuffer sbuf = new StringBuffer();
		  sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n");
		  sbuf.append("<html>\n");
		  sbuf.append("<head>\n");
		  sbuf.append("<base href=\"%%BASE_URL%%\" />\n");
		  sbuf.append("<title>YOUR PASSWORD</title>\n");
		  sbuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\n");
		  sbuf.append("</head>\n");
		  sbuf.append("<body>\n");
		  sbuf.append("Hi, %%FIRST_NAME%%:<br />\n");
		  sbuf.append("Here is your login information: <br />\n");
		  sbuf.append("Login: %%NAME%%<br />\n");
		  sbuf.append("Click <a href=\"%%LINK%%\">here</a> to login.\n");
		  sbuf.append("</body>\n");
		  sbuf.append("</html>\n");
		  return sbuf.toString();
	  }
	  
	  public static String clobToString(Clob clb) throws IOException, SQLException{
		  if (clb == null)
			  return  "";

		  StringBuffer str = new StringBuffer();
		  String strng;

		  BufferedReader bufferRead = new BufferedReader(clb.getCharacterStream());

		  while ((strng=bufferRead .readLine())!=null)
			  str.append(strng);

		  return str.toString();
	  } 
	  
	  public static String responseToJson(String response){
		  JSONObject responseJson = new JSONObject();
		  if(org.apache.commons.lang.StringUtils.isNotBlank(response))
			  responseJson.put("STATUS", response);
		  else
			  responseJson.put("STATUS", "ResponseFormatError");
		  return responseJson.toString();
	  }
}
	  

