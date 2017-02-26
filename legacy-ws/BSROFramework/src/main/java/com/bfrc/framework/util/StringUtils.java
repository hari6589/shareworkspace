package com.bfrc.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class StringUtils {
	public static final  DecimalFormat PRICE_FORMAT = new DecimalFormat("#,##0.00");
	public static final  String PRICE_FORMAT_STRING = "#,##0.00";
	public static final String PRIVATE_KEY = "1DEeeAp-N64RwbEmH2DdJxKjcJI=";
	public static final String NAME_FILTER_REGX = "[^a-zA-Z0-9\\.]";
	public static final String MAKEMODEL_NAME_FILTER_REGX = "[^a-zA-Z0-9\\.\\-\\,\\'_\\(\\)#]";
	
	public static String lowerCaseUnderlineToCamelCase(String name)
    {
      StringBuffer newName = new StringBuffer();
      boolean nextIsUpper = false;
      
      name = name.toLowerCase();
      
      for (int i = 0; i < name.length(); i++)
        {
          if ((name.charAt(i)) == '_')
            nextIsUpper = true;
          else
            {
              newName.append(nextIsUpper ? Character.toUpperCase(name.charAt(i)) : name.charAt(i));
              
              nextIsUpper = false;
            }
        }
      
      return newName.toString();
    }
  
  public static String camelCaseToLowerCaseUnderline(String name)
    {
      StringBuffer newName = new StringBuffer();

      for (int i = 0; i < name.length(); i++)
        if (Character.isUpperCase(name.charAt(i)))
          {
            if (i == 0)
              newName.append(Character.toLowerCase(name.charAt(i)));
            else
              newName.append("_").append(Character.toLowerCase(name.charAt(i)));
          }
        else
          newName.append(name.charAt(i));

      return newName.toString();
    }
  
  public static String upperCaseUnderlineToCamelCase(String name)
    {
      return lowerCaseUnderlineToCamelCase(name);
    }
  
  public static String camelCaseToUpperCaseUnderline(String name)
    {
      StringBuffer newName = new StringBuffer();

      for (int i = 0; i < name.length(); i++)
        if (Character.isUpperCase(name.charAt(i)))
          {
            if (i == 0)
              newName.append(Character.toUpperCase(name.charAt(i)));
            else
              newName.append("_").append(Character.toUpperCase(name.charAt(i)));
          }
        else
          newName.append(Character.toUpperCase(name.charAt(i)));

      return newName.toString();
    }
  public static <T> T convertObject(Class<T> parameterClass, Object value)
  {
    Object newValue = value;

    if (value != null)
      {
        if (!parameterClass.isAssignableFrom(value.getClass()))
          if (parameterClass.isAssignableFrom(String.class))
            newValue = value.toString();
          else if (parameterClass.isAssignableFrom(int.class) || parameterClass.isAssignableFrom(Integer.class))
            newValue = value instanceof Number ? new Integer(((Number)value).intValue()) : new Integer(value.toString());
          else if (parameterClass.isAssignableFrom(short.class) || parameterClass.isAssignableFrom(Short.class))
            newValue = value instanceof Number ? new Short(((Number)value).shortValue()) : new Short(value.toString());
          else if (parameterClass.isAssignableFrom(long.class) || parameterClass.isAssignableFrom(Long.class))
            newValue = value instanceof Number ? new Long(((Number)value).longValue()) : new Long(value.toString());
          else if (parameterClass.isAssignableFrom(float.class) || parameterClass.isAssignableFrom(Float.class))
            newValue = value instanceof Number ? new Float(((Number)value).floatValue()) : new Float(value.toString());
          else if (parameterClass.isAssignableFrom(double.class) || parameterClass.isAssignableFrom(Double.class))
            newValue = value instanceof Number ? new Double(((Number)value).doubleValue()) : new Double(value.toString());
          else if (parameterClass.isAssignableFrom(BigDecimal.class))
                newValue = value instanceof BigDecimal ? new BigDecimal(((Number)value).doubleValue()) : new BigDecimal(new Double(value.toString()));
          else if (parameterClass.isAssignableFrom(boolean.class) || parameterClass.isAssignableFrom(Boolean.class))
            {
              String str = value.toString().toLowerCase();

              newValue = str.equals("on") || str.equals("true") || str.equals("yes") || str.equals("1") ? Boolean.TRUE : Boolean.FALSE;
            }
          else if (parameterClass.isAssignableFrom(byte.class) || parameterClass.isAssignableFrom(Byte.class))
            newValue = new Byte(value.toString());
          else if ((parameterClass.isAssignableFrom(char.class) || parameterClass.isAssignableFrom(Character.class)) && value.toString().length() > 0)
            newValue = new Character(value.toString().charAt(0));
      }
    else if (parameterClass.isPrimitive())
      {
        if (parameterClass.isAssignableFrom(int.class))
          newValue = new Integer(0);
        else if (parameterClass.isAssignableFrom(short.class))
          newValue = new Short((short)0);
        else if (parameterClass.isAssignableFrom(long.class))
          newValue = new Long(0);
        else if (parameterClass.isAssignableFrom(float.class))
          newValue = new Float(0);
        else if (parameterClass.isAssignableFrom(double.class))
          newValue = new Double(0);
        else if (parameterClass.isAssignableFrom(boolean.class))
          newValue = Boolean.FALSE;
        else if (parameterClass.isAssignableFrom(byte.class))
          newValue = new Byte((byte)0);
        else if (parameterClass.isAssignableFrom(char.class))
          newValue = new Character((char)0);
      }
    
    return (T)newValue;
  }
  
  //--- move from ServerUtil --//
  public static boolean isNullOrEmpty(String str) {
      
      if (str == null || str.trim().equals("") || str.trim().equals("null")) {
          return true;
      } 
      return false;
  }
   public static boolean isNullOrEmpty(Object obj) {
      
      if(obj == null) return true;
      return isNullOrEmpty(obj.toString());
  }
   
   public static String replace(String string, String from, String to) {
       if (string ==null || from ==null || from.equals("") || to ==null ) 
           return string;
       StringBuffer buf = new StringBuffer(2*string.length());
       int previndex=0;
       int index=0;
       int flen = from.length();
       while (true) { 
           index = string.indexOf(from, previndex);
           if (index == -1) {
               buf.append(string.substring(previndex));
               break;
           }
           buf.append( string.substring(previndex, index) + to );
           previndex = index + flen;
       }
       return buf.toString();
   }
   
   public static String formatDecimal(double d,String pattern){
       java.text.DecimalFormat nf= new java.text.DecimalFormat(pattern);
       return nf.format(new Double(d));
   }
   
   public static String[] tokenToArray(String s,String delim) {
       if(s==null) return null;
       java.util.StringTokenizer st = new java.util.StringTokenizer(s,delim);
       List<String> l = new ArrayList<String>();
       String t=null;
       while(st.hasMoreTokens()){
          t=st.nextToken();
          l.add(t);
               //LogUtil.log("elemt="+t);
       }
       String[] rt = new String[l.size()];
       l.toArray(rt);
       return rt;
   }
   public static String dateFormat(java.util.Date date, String pattern)
   {
       if(date == null)
       {
           return "";
       } else
       {
           SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
           return simpledateformat.format(date);
       }
   }
   public static String join(List l, String delimiter) {
	    if (l == null && l.isEmpty()) return "";
	    Iterator<String> iter = l.iterator();
	    StringBuffer buffer = new StringBuffer(iter.next());
	    while (iter.hasNext()) buffer.append(delimiter).append(iter.next());
	    return buffer.toString();
	}

   
   public static Date parseDate(String s, String fomat)
   {
       if(s == null)
       {
           return null;
       } else
       {
           SimpleDateFormat simpledateformat = new SimpleDateFormat(fomat);
           Date dt= null;
           try{
               dt = simpledateformat.parse(s);
           }catch(Exception ex){}
           return dt;
       }
   }
   
   public static String arrayToString(String[] a, String separator) {
	    
	    return join(a,separator);
	 }
   public static String join(String[] a, String separator) {
	   if (a == null || separator == null) {
	        return null;
	    }
	    StringBuffer result = new StringBuffer();
	    if (a.length > 0) {
	        result.append(a[0]);
	        for (int i=1; i < a.length; i++) {
	            result.append(separator);
	            result.append(a[i]);
	        }
	    }
	    return result.toString();
   }
   
   public static String nameFilter(String s){
       if(s == null) return "";
       return s.replaceAll(NAME_FILTER_REGX, "");
   }
   
   public static String makeModelNameFilter(String s){
	   return makeModelNameFilter(s,null);
   }
   
   public static String makeModelNameFilter(String s, String spaceReplacement){
       if(s == null) return "";
       if(spaceReplacement != null){
    	   s = s.replace(" ", spaceReplacement);
    	   return s.replaceAll(MAKEMODEL_NAME_FILTER_REGX, "").replace(spaceReplacement+spaceReplacement, spaceReplacement);
       }
       return s.replaceAll(MAKEMODEL_NAME_FILTER_REGX, "");
   }
   
   public static String categoryNameFilter(String s){
	   return categoryNameFilter(s,null);
   }
   
   public static String categoryNameFilter(String s, String spaceReplacement){
	   String MAKEMODEL_NAME_FILTER_REGX = "[^a-zA-Z0-9\\.\\-\\,\\'_\\(\\)#]";
       if(s == null) return "";
	   s = s.toLowerCase();
	   s = s.replace("/", "-");
       if(spaceReplacement != null){
    	   s = s.replace(" ", spaceReplacement);
    	   return s.replaceAll(MAKEMODEL_NAME_FILTER_REGX, "").replace(spaceReplacement+spaceReplacement, spaceReplacement);
       }
       return s.replaceAll(MAKEMODEL_NAME_FILTER_REGX, "");
   }
   
   public static boolean testSerializable(Object obj){
	   if( obj == null )
		   return true;
       try{
    // test = obj2;
     // serialize
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bout);
      oos.writeObject(obj);
      oos.close();

      //deserialize
      byte[] pickled = bout.toByteArray();
      InputStream in = new ByteArrayInputStream(pickled);
      ObjectInputStream ois = new ObjectInputStream(in);
      Object o = ois.readObject();
      //com.mastercareusa.selector.FullResults copy = (com.mastercareusa.selector.FullResults) o;
      }catch(Exception ex){
          ex.printStackTrace();
          return false;
      }
      return true;
  }
   
  public static String base64Encode(String s){
	  if(s == null) return null;
	  try{
	      //byte[] bytes= org.apache.commons.codec.binary.Base64.encodeBase64(s.getBytes("utf-8"));
	      return new sun.misc.BASE64Encoder().encodeBuffer(s.getBytes("utf-8"));
	      //return new String(bytes, "utf8");
	     //bytes = org.apache.commons.codec.binary.Base64.decodeBase64(SPACER_IMAGE); 
	  }catch(Exception ex){
		  
	  }
	  return null;
  }
  
  public static String base64Encode(byte[] bytes){
	  if(bytes == null) return null;
	  try{
	      //byte[] bytes= org.apache.commons.codec.binary.Base64.encodeBase64(s.getBytes("utf-8"));
	      return new sun.misc.BASE64Encoder().encodeBuffer(bytes);
	      //return new String(bytes, "utf8");
	     //bytes = org.apache.commons.codec.binary.Base64.decodeBase64(SPACER_IMAGE); 
	  }catch(Exception ex){
		  
	  }
	  return null;
  }
  
  
  public static String base64Decode(String s){
	  if(s == null) return null;
	  try{
	      //byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(s); 
		  byte[] bytes= new sun.misc.BASE64Decoder().decodeBuffer(s);
	      return new String(bytes, "utf8");
	  }catch(Exception ex){
		  
	  }
	  return null;
  }
  
  public static byte[] base64DecodeBinary(String s){
	  if(s == null) return null;
	  try{
	      //byte[] bytes = org.apache.commons.codec.binary.Base64.decodeBase64(s); 
		  return new sun.misc.BASE64Decoder().decodeBuffer(s);
	  }catch(Exception ex){
		  
	  }
	  return null;
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
  
  public static String dumpBeanInfo(Object bean){
	  return dumpBeanInfo(bean,true);
  }
		  
  public static String dumpBeanInfo( Object bean, boolean tabs) {
	    StringBuffer buf = new StringBuffer();
	    if( bean != null ) {
	      try {
	        java.beans.BeanInfo binfo = java.beans.Introspector.getBeanInfo( bean.getClass() );
	        java.beans.PropertyDescriptor[] properties = binfo.getPropertyDescriptors();
	        if( properties != null ) {
	          for( int i = 0; i < properties.length; i++ ) {
	            java.lang.reflect.Method readMethod = properties[i].getReadMethod();
	            if( readMethod != null ) {
	              if(tabs){
	                  buf.append( "\t"+properties[i].getName() );
	              }else{
	                  buf.append( properties[i].getName() );
	              }
	              buf.append( " = " );
	              Object obj = readMethod.invoke( bean, (Object[])null );
	              if( obj != null ) {
	                buf.append( obj.toString() );
	              }
	              else {
	                buf.append( "<empty>" );
	              }
	              buf.append( "\n" );
	            }
	          }
	        }
	      }
	      catch( Exception e ) {
	        // ignore exceptions thrown, this is a development aid
	      }
	    }
	    return buf.toString();
	  }
  
  public static String inspectBeanInfo(Object bean){
	  return dumpBeanInfo(bean,true);
  }
		  
  public static String inspectBeanInfo( Object bean, boolean tabs) {
	    StringBuffer buf = new StringBuffer();
	    if( bean != null ) {
	      try {
	        java.beans.BeanInfo binfo = java.beans.Introspector.getBeanInfo( bean.getClass() );
	        java.beans.PropertyDescriptor[] properties = binfo.getPropertyDescriptors();
	        if( properties != null ) {
	          for( int i = 0; i < properties.length; i++ ) {
	            java.lang.reflect.Method readMethod = properties[i].getReadMethod();
	            if( readMethod != null ) {
	              if(tabs){
	                  buf.append( "\t"+properties[i].getName() );
	              }else{
	                  buf.append( properties[i].getName() );
	              }
	              buf.append( " = " + readMethod.getReturnType().getName());
	              buf.append( "\n" );
	            }
	          }
	        }
	      }
	      catch( Exception e ) {
	        // ignore exceptions thrown, this is a development aid
	      }
	    }
	    return buf.toString();
	  }
    public static boolean isValidPhone(String phone) {
		if(phone!=null)
			return phone.matches("^\\(?(\\d{3})\\)?[- \\.]?(\\d{3})[- \\.]?(\\d{4})$");
		return false;
	}
    /*
     *removes -_(). and spaces from given string
     * 
     */
    public static String stripToPhoneNumber(String phone) {
		if(phone!=null){
			phone = phone.replace("-", "");
			phone = phone.replace("_", "");
			phone = phone.replace("(", "");
			phone = phone.replace(")", "");
			phone = phone.replace(".", "");
			phone = phone.replace(" ", "");
		}
		return phone;
	}
    /*
     * returns XXX-XXX-XXXX with given XXXXXXXXXX format(10 digits)
     * call stripToPhoneNumber to get this format.
     * 
     */
    public static String getFormattedPhoneNumber(String phoneNumber){
	    if(phoneNumber!=null&& phoneNumber.length()==10){
			char[] digs= phoneNumber.toCharArray();
			// quick fix for the fact that it was doing ascii decimal value addition on the first 3 digits
			phoneNumber=""+digs[0]+digs[1]+digs[2]+"-"+digs[3]+digs[4]+digs[5]+"-"+digs[6]+digs[7]+digs[8]+digs[9];
		}
	    return phoneNumber;
    }
    
	public static boolean isValidEmail(String email) {
		if(email!=null)
			return email.trim().matches("^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$");
		return false;
	}
	
	public static String formatDateRange(Date startDate, Date endDate, String monthPattern){
		if(monthPattern == null)
			monthPattern="MMMM";
		if(startDate != null && endDate != null){
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(startDate);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(endDate);
			if(calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)){
				SimpleDateFormat df = new SimpleDateFormat(monthPattern+" d, yyyy");
				return df.format(startDate)+" - "+df.format(endDate);
			}else if(calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)){
				SimpleDateFormat df = new SimpleDateFormat(monthPattern+" d");
				return df.format(startDate)+" - "+df.format(endDate)+", "+calendar1.get(Calendar.YEAR);
			}else {
				SimpleDateFormat df = new SimpleDateFormat(monthPattern+" d");
				SimpleDateFormat df2 = new SimpleDateFormat("d");
				return df.format(startDate)+" - "+df2.format(endDate)+", "+calendar1.get(Calendar.YEAR);
			}
		}
		if(startDate != null && endDate == null){
			SimpleDateFormat df = new SimpleDateFormat(monthPattern+" d, yyyy");
			return df.format(startDate)+" - ? ";
			
		}
		if(startDate == null && endDate != null){
			SimpleDateFormat df = new SimpleDateFormat(monthPattern+" d, yyyy");
			return "? - "+df.format(endDate);
		}
		return "";
		
	}
	
	public static String formatDateRange(Date startDate, Date endDate){
		return formatDateRange(startDate, endDate, null);
		
	}
	
	public static String formatDateRangeShortMonth(Date startDate, Date endDate){
		return formatDateRange(startDate, endDate, "MMM");
		
	}
	
	public static String formatTireSize(String value) {
		return formatTireSize(value, false);
	}
	
	public static String formatTireSize(String value, boolean rim) {
			Integer test = null;
			try{
				test = Integer.valueOf(value);
			}catch(Exception ex){
				return value;
			}
			if(value.length() == 4) {
				char end = value.charAt(3);
				value = value.substring(0, 2) + "." + value.charAt(2);
				if(!rim)
					value += end;
			} else if(test != null && Integer.valueOf(value).compareTo(new Integer(500)) >= 0)
				value = value.charAt(0) + "." + value.substring(1,3);
			return value;
	}
	
	public static String userNameFilter(String name, int maxLength){
		if(name == null)
			return null;
		if(maxLength >0)
		    name = name.length() > maxLength ? name.substring(0,maxLength) : name;
		name =     ("First Name".equalsIgnoreCase(name) || "FirstName".equalsIgnoreCase(name) || "Last Name".equalsIgnoreCase(name) || "LastName".equalsIgnoreCase(name)) ? "" : name;
		return name.replaceAll("\\|", "");
	}
	
	public static String userNameFilter(String name){
		return userNameFilter(name, -1);
	}
	
	public static String truncate(String input,int maxLength){
		if(input == null)
			return null;
		if(maxLength >0)
			input = input.length() > maxLength ? input.substring(0,maxLength) : input;
		return input;
	}
	public static boolean validEmail(String email, boolean validateEmailName) {
		try {
			EmailValidatorUtil evu = new EmailValidatorUtil();
			String rawEmail = "";
			String emailName = "";
			if (email.contains("<")) {
				rawEmail = email.substring(email.indexOf("<") + 1,email.indexOf(">"));
				emailName = email.substring(0, email.indexOf("<")).trim();
			} else
				rawEmail = email;
//			Util.debug("==========This should be an email address " + rawEmail + "======end");
			if (!evu.isValidEmail(rawEmail)) {
//				Util.debug("==========validation of  " + rawEmail + "======failed");
				return false;
			} else if (emailName.length() > 0 && validateEmailName) {
				if (!evu.isValidUser(emailName)){
//					Util.debug("==========validation of  " + emailName	+ "======failed");
					return false;
				} else
					return true;
			} else
				return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static String unescapeHtmlLight(String value){
		if(StringUtils.isNullOrEmpty(value))
			return value;
		value = value
			 .replaceAll("&#040;", "(")
			 .replaceAll("&#041;", ")")
			 .replaceAll("&#40;", "(")
			 .replaceAll("&#41;", ")")
			 .replaceAll("&#047;", "/")
			 .replaceAll("&#47;", "/")
			 
			 .replaceAll("&#34;", "\"")
			 .replaceAll("&#034;", "\"")
			 .replaceAll("&quot;", "\"")
			 
			 .replaceAll("&#35;", "#")
			 .replaceAll("&#035;", "#")
			 
			 .replaceAll("&#37;", "%")
			 .replaceAll("&#037;", "%")
			 
			 .replaceAll("&#39;", "'")
			 .replaceAll("&#039;", "'")
			 
			 .replaceAll("&#43;", "+")
			 .replaceAll("&#043;", "+")
			 
			 .replaceAll("&#45;", "-")
			 .replaceAll("&#045;", "-")
			 
			 .replaceAll("&#46;", ".")
			 .replaceAll("&#046;", ".")
			 
			 .replaceAll("&#95;", "_")
			 .replaceAll("&#095;", "_")
			 
			 .replaceAll("&#038;", "&")
			 .replaceAll("&#38;", "&")
			 .replaceAll("&amp;", "&");  
		return value;
	}
	
	public static String md5(String message)
	  {
	    String digest = "";
	    MessageDigest md5 = null;
	    try
	    {
	      md5 = MessageDigest.getInstance("MD5");
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	      
	    }
	    byte[] dig = md5.digest((byte[]) message.getBytes());
	    StringBuffer code = new StringBuffer();
	    for (int i = 0; i < dig.length; ++i)
	    {
	      code.append(Integer.toHexString(0x0100 + (dig[i] & 0x00FF)).substring(1));
	    }
	    return code.toString();

	    //System.out.println("Message: " + message);
	    //System.out.println("Digest: " + digest);

	  }
	
	public static String signUrl(String keyString, String urlString) throws Exception {
		if(isNullOrEmpty(urlString) || isNullOrEmpty(keyString)) return null;
		else {
			if(urlString.indexOf("http://")<0 && urlString.indexOf("https://")<0)
				return null;
		}
		URL url = new URL(urlString);
		String path = url.getPath();
		String query = url.getQuery();
		String resource = path + '?' + query;
		byte[] key;
		keyString = keyString.replace('-', '+');
	    keyString = keyString.replace('_', '/');
	    key = base64DecodeBinary(keyString);
	    // Get an HMAC-SHA1 signing key from the raw key bytes
	    SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
	    // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(sha1Key);
	    // compute the binary signature for the request
	    byte[] sigBytes = mac.doFinal(resource.getBytes());		
	    // base 64 encode the binary signature
	    String signature = base64Encode(sigBytes);
	    // convert the signature to 'web safe' base 64
	    signature = signature.replace('+', '-');
	    signature = signature.replace('/', '_');

	    return url.getProtocol() + "://" + url.getHost() + resource + "&signature=" + signature;
	}
	
	public static String signUrl(String urlString) throws Exception {
		return signUrl(PRIVATE_KEY, urlString);
	}
	
	public static String replaceParameterInUrl(String parameter, String value, String url, boolean add) {
		StringBuffer returnUrl = new StringBuffer(url);
		boolean hasParameter = returnUrl.indexOf(parameter+"=") != -1;
		if (hasParameter) {
			int indexStore = returnUrl.indexOf("&"+parameter+"=");
			if (indexStore == -1)
				indexStore = returnUrl.indexOf(parameter+"=");
			int end = returnUrl.length();
			int indexAnd = returnUrl.indexOf("&", indexStore + 1);
			int indexHash = returnUrl.indexOf("#", indexStore);
			if (indexAnd != -1)
				end = indexAnd;
			else if (indexHash != -1)
				end = indexHash;
			returnUrl.replace(indexStore, end, "");
		}

		if(hasParameter || add){
			String append = "";
			if (returnUrl.indexOf("?") == -1) {
				append = "?"+parameter+"=" + value;
			} else if (returnUrl.indexOf("?") == returnUrl.length() - 1 || returnUrl.indexOf("?#") != -1) {
				append = parameter+"=" + value;
			} else {
				append = "&"+parameter+"=" + value;
			}
	
			if (returnUrl.indexOf("#") == -1)
				returnUrl.append(append);
			else
				returnUrl.insert(returnUrl.indexOf("#"), append);
		}
		
		return returnUrl.toString();
	}
	
	public static String trimBraces(String parameter) {
		String out = "";
		if (parameter != null && !parameter.isEmpty()) {
			for(int i=0;i < parameter.length();i++) {
				char currentChar = parameter.charAt(i);
				if(currentChar != '(' && currentChar != ')')
					out += currentChar;
				if(currentChar == ')' && ((i+1) < parameter.length()) && (parameter.charAt(i+1) == '(')) {
					out += " ";
				}
			}
		}
		
		return out;
	}
	
	public static String getTitleByVehicle(String year, String makeName, String modelName, String submodel) {
    	String title = "";
    	title += year + " " + makeName + " " + modelName + " " + trimBraces(submodel);
    	
    	return title;
    }
    
    public static String getTitleBySize(String cross, String aspect, String rim) {
    	String title = "";
    	aspect = formatTireSize(aspect);
    	rim = formatTireSize(rim, true);
    	title += cross + "/" + aspect + " R" + rim;
    	
    	return title;
    }
    
    public static String getMetaDescriptionByVehicle(String year, String makeName, String modelName, String submodel) {
    	String metaDesc = "Tires for ";
    	metaDesc += year + " " + makeName + " " + modelName + " " + trimBraces(submodel)+". ";
    	metaDesc += year + " " + makeName + " " + modelName;
    	metaDesc += " tires new at Firestone Complete Auto Care stores.";
    	
    	return metaDesc;
    }
    
    public static String getMetaDescriptionBySize(String cross, String aspect, String rim) {
    	String metaDesc = "";
    	aspect = formatTireSize(aspect);
    	rim = formatTireSize(rim, true);
    	
    	metaDesc += cross + "/" + aspect + " R" + rim;
    	metaDesc += " tires available for install at a local Firestone Complete Auto Care store. ";
    	metaDesc += cross + "/" + aspect + "/" + rim +" tire reviews & prices. ";
    	metaDesc += cross + " width " + aspect + " sidewall height " + rim +"\'\' tire.";
    	
    	return metaDesc;
    }
    
    public static String getKeywordByVehicle(String year, String makeName, String modelName, String submodel) {
    	String keyword = year + " " + makeName + " " + modelName + " Tires, ";
    	keyword += makeName + " " + modelName + " Tires, ";
    	keyword += modelName + " Tires, ";
    	keyword += trimBraces(submodel) + " Tires, Tires, Firestone Complete Auto Care";
    	
    	return keyword;
    }
    
    public static String getKeywordBySize(String cross, String aspect, String rim) {
    	aspect = formatTireSize(aspect);
    	rim = formatTireSize(rim, true);
    	
    	String keyword = cross + "/" + aspect + " R" + rim + " Tires, ";
    	keyword += cross + "/" + aspect + "/" + rim + " Tires, ";
    	keyword += cross + "/" + aspect + "-" + rim + " Tires, ";
    	keyword += cross + " " + aspect + " " + rim + ", ";
    	keyword += cross + "/" + aspect + " R" + rim + ", ";
    	keyword += cross + "/" + aspect + " " + rim + ", ";
    	keyword += cross + "/" + aspect + "-R" + rim + ", ";
    	keyword += cross + "/" + aspect + "R" + rim + ", ";
    	keyword += cross + "/" + aspect + "/" + rim + ", ";
    	keyword += rim + " Inch Tire, ";
    	keyword += cross + aspect + rim + ", ";
    	keyword += cross + " Tire" + ", ";
    	keyword += cross + " Width" + ", ";
    	keyword += aspect + " Sidewall Height" + ", ";
    	keyword += rim + " Tire, ";
    	keyword += "Tires, Firestone Complete Auto Care";
    	
    	return keyword;
    }
    
    public static String getParaContentByVehicle(String year, String makeName, String modelName, String submodel) {
    	String paraContent = "Select tires for a " + year + " " + makeName + " " + modelName + " " + trimBraces(submodel) 
    		+ " and get a tire quote at a local Firestone Complete Auto Care Store. Compare tires for a " + year + " " + makeName + " " + modelName
    		+ " that include warranty, performance, speed rating, load index and sidewall specifications.";
    	
    	return paraContent;
    }
    
    public static String getParaContentByVehicle_T1(String year, String makeName, String modelName, String submodel) {
    	String paraContent = "Below is a list of tires matching your search criteria. If you feel there may have been a mistake with your search,"
    		+ " please click the Edit button below to change your search options.";
    	
    	return paraContent;
    }
    
    public static String getParaContentBySize(String cross, String aspect, String rim) {
    	aspect = formatTireSize(aspect);
    	rim = formatTireSize(rim, true);
    	
    	String paraContent = "Select a " + cross + "/" + aspect + " R" + rim
		+ " tire below and get a tire quote at your nearest Firestone Complete Auto Care Store. Compare " + cross + "/" + aspect + "-" + rim
		+ " tires performance, warranty, load index, speed rating and sidewall specifications.";
	
    	return paraContent;
    }
    
    public static String formatTireSizeDisplay(String tireSize) {
		String out = "";
		if (tireSize != null && !tireSize.isEmpty()) {
			int slashIndex = tireSize.indexOf("/");
			String cross = tireSize.substring(0, slashIndex);
			out = cross;
			
			String aspect_rim = tireSize.substring(slashIndex+1);
			int rIndex = aspect_rim.indexOf("R");
			String aspect = aspect_rim.substring(0, rIndex);
			out += "/"+aspect;
				
			String rim = aspect_rim.substring(rIndex);
			out += " "+rim;
		}
		return out;
	}
}