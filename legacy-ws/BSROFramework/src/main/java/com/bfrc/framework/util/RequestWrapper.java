package com.bfrc.framework.util;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletRequestWrapper;  
import java.util.StringTokenizer;

import com.bfrc.framework.dao.StoreScheduleDAO;
import com.bfrc.security.*;
public final class RequestWrapper extends HttpServletRequestWrapper implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3810601964568136085L;
	private static final String PSID = "psid";

	public RequestWrapper() {  
        //super();  
		super(null);  
    }
	
	public RequestWrapper(HttpServletRequest servletRequest) {  
        super(servletRequest);  
    }  
      
    public String[] getParameterValues(String parameter) {  
  
      String[] values = super.getParameterValues(parameter);  
      if (values==null)  {  
          return null;  
      }  
      int count = values.length;  
      String[] encodedValues = new String[count];  
      for (int i = 0; i < count; i++) {  
    	  if(ServerUtil.isNullOrEmpty(values[i])){
    		  encodedValues[i] =""; 
    	  }else{
              encodedValues[i] = Encode.html(values[i]); 
    	  }
      }    
      return encodedValues;   
    }  
      
    public String getParameter(String parameter) {  
          String value = super.getParameter(parameter);  
          if (value == null) {  
                 return null;   
          }
          String encoded = Encode.html(value.trim());
          ServerUtil.debug("parameter "+parameter+" encoded: "+encoded);
          return encoded;  
    }
    
    public String getQueryString() {  
        String value = super.getQueryString();
        String out = "";
        if (value == null) {  
               return null;   
        }
        String encoded = Encode.html(value.trim());
        String psidParam = super.getParameter(PSID);
        if (psidParam != null) {
        	int tokCount = 0;
        	ServerUtil.debug("input query string: "+encoded);
    		StringTokenizer token = new StringTokenizer(encoded, "&");
    		while(token.hasMoreTokens()) {
    			String parameter = token.nextToken();
    			int eqIndex = parameter.indexOf("=");
    			if(eqIndex != -1){
    				String paramName = parameter.substring(0, eqIndex);
    				String paramValue = parameter.substring(eqIndex+1);

    				if (tokCount > 0)
    				{
    					out += "&";
    				}
    				if (PSID.equalsIgnoreCase(paramName))
    				{
    					out += paramName + "=";
    					for(int i=0;i < paramValue.length();i++) {
    						char currentChar = paramValue.charAt(i);
    						if(currentChar == '\\')
    							out += Encode.encodeCharacter(currentChar);
    						else out += currentChar;
    					}
    				} else {
    					out += paramName+"="+paramValue;
    				}
    				tokCount = tokCount+1;
    			}
    		}

    		ServerUtil.debug("encoded query string = "+out);
        } else {
        	out = encoded;
        }
        return out;
  }    
      
    public String getHeader(String name) {  
        String value = super.getHeader(name);  
        if (value == null)  
            return null;  
        return Encode.html(value);  
          
    }  
}
