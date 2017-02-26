package com.bfrc.framework.util;

import com.bfrc.framework.util.*;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;  

import org.apache.commons.validator.EmailValidator;
import org.apache.commons.validator.GenericTypeValidator;

public class RequestValidatorUtils {
	
	public static final String VALIDATION_FIELDS="_VALIDATION_FIELDS";
	public static final String VALIDATION_ERROR="_VALIDATION_ERROR";
	public static final String VALIDATION_FIELDS_MAP="_VALIDATION_FIELDS_MAP";
	public static final String VALIDATION_FIELDS_FLAG="_VALIDATION_FIELDS_FLAG";
	@SuppressWarnings("unchecked")
	public static void jsonToObject(HttpServletRequest request)
    {
		Map<String, String> fields = (Map<String, String>)request.getAttribute(VALIDATION_FIELDS);
		if(fields == null || fields.size() == 0)
			return;
		Map<String, Object> id_map = new HashMap<String, Object>();
		
		for(Iterator<String> it = fields.keySet().iterator(); it.hasNext();){
			String id = (String)it.next();
			String json = (String)fields.get(id);
			if(json != null){
				try{
					Map<String, Object> m = new HashMap<String, Object>();
				    org.json.JSONObject vals = new org.json.JSONObject(json);
				    Iterator<String> pi = vals.keys();
				    while (pi.hasNext()) {
				        String key = (String)pi.next();
				        String value = (String)vals.get(key.toString());
				        if("error_codes".equalsIgnoreCase(key)){
				        	Map<String, Map<String, Object>> check_errorCodes = new HashMap<String, Map<String, Object>>();
				        	String[] customErrors = value.split(";");
				        	for(int i =0; customErrors != null && i< customErrors.length; i++){
				        		String s = customErrors[i];
				        		String[] tokens = s.split(":");
				        		
				        		if(tokens != null && tokens.length >= 2){
				        		    String errorFor = tokens[0];
				        		    String erroCode = tokens[1];
				        		    Map<String, Object> error_sub = new HashMap<String, Object>();
				        		    error_sub.put("error_code", erroCode);
				        		    if(tokens.length > 2){
				        		    	String ss = tokens[2];
				        		    	String[] args = ss.split("\\|");
				        		    	error_sub.put("error_args", args);
				        		    }
				        		    check_errorCodes.put(errorFor, error_sub);
				        		}
				        	}
				        	m.put(key, check_errorCodes);
				        }else{
				            m.put(key, value);
				        }
				    }
				    id_map.put(id, m);
				}catch(Exception ex){
					ex.printStackTrace();
				}	
			}
		}
		request.setAttribute(VALIDATION_FIELDS_MAP, id_map);
		request.setAttribute(VALIDATION_FIELDS_FLAG, "1");
    }
	@SuppressWarnings("unchecked")
	public static boolean validate(HttpServletRequest request)
    {
		ServletContext ctx = request.getSession().getServletContext();
		//--- validate form inputs ---//
		
		//--- if no validation needed ---//
		if(!RequestUtil.doProcessForm(request))
			return true;
		if(!"1".equals((String)request.getAttribute(VALIDATION_FIELDS_FLAG))){
			jsonToObject(request);
		}
		org.springframework.context.MessageSource
        messageSource = (org.springframework.context.MessageSource)com.bfrc.Config.locate(ctx, "messageSource");
       
		Map<String, String> errorMsg = new HashMap<String, String>();
		Map<String, Map> id_map = (Map<String, Map>)request.getAttribute(VALIDATION_FIELDS_MAP);
		if(id_map == null || id_map.size() == 0)
			return true;
		/**
		 * 
		 *  in the format
		 *  firstname = "{name: 'First Name', validations: 'required date', min:'1', max:'567', error_codes: 'required:user.passwordHint.empty:arg0|arg1;date:user.dob.format;'}";
		 */
		boolean valid = true;

		for(Iterator it = id_map.keySet().iterator(); it.hasNext();){
			    String id = (String)it.next();
			    ServerUtil.debug("\t\tid : "+id);
			    Map pairs = (Map)id_map.get(id);
			    String name = null;
			    Object val = null;
			    String validations = null;
			    String errorCode = null;
			    String[] erroArgs = null;
			    String max = null;
			    String min = null;
			    String maxLength = null;
			    String minLength = null;
			    String regExpValue = null;
			    Map<String, Object> check_errorCodes = new HashMap<String, Object>();
			    boolean checkNumber = false;
			    boolean checkMin = false;
			    boolean checkMax = false;
			    boolean checkRangeNumber = false;
			    boolean checkRangeString = false;
			    for(Iterator keys = pairs.keySet().iterator(); keys.hasNext();){
			        Object key = keys.next();
			        val = (String)pairs.get(key.toString());
			        if("name".equalsIgnoreCase((String)key)){
			        	name= (String)val;
			        }else if("validations".equalsIgnoreCase((String)key)){
			        	validations = (String)val;
			        	if(validations != null && validations.indexOf("number") >=0){
			        		checkNumber = true;
			        	}
			        }else if("error_codes".equalsIgnoreCase((String)key)){
			        	check_errorCodes = (Map)val;
			        }else if("min".equalsIgnoreCase((String)key)){
			        	checkMin = true;
			        	if(checkMax)
			        		checkRangeNumber = true;
			        	min = (String)val;
			        }else if("max".equalsIgnoreCase((String)key)){
			        	checkMax = true;
			        	if(checkMin)
			        		checkRangeNumber = true;
			        	max = (String)val;
			        }else if("minlength".equalsIgnoreCase((String)key)){
			        	checkMin = true;
			        	if(checkMax)
			        		checkRangeString = true;
			        	minLength = (String)val;
			        }else if("maxlength".equalsIgnoreCase((String)key)){
			        	checkMax = true;
			        	if(checkMin)
			        		checkRangeString = true;
			        	maxLength = (String)val;
			        }else if("regexp_value".equalsIgnoreCase((String)key)){
			        	regExpValue = (String)val;
			        }
			    }
			    
			    for(Iterator keys = pairs.keySet().iterator(); keys.hasNext();){
			        Object key = keys.next();
			        ServerUtil.debug("\t\tkey : "+key);
			        val = (String)pairs.get(key.toString());
			        if("name".equalsIgnoreCase((String)key)){
			        	name= (String)val;
			        }else if("validations".equalsIgnoreCase((String)key)){
			        	validations = (String)val;
			        }else if("error_codes".equalsIgnoreCase((String)key)){
			        	check_errorCodes = (Map)val;
			        }
			        if(!ServerUtil.isNullOrEmpty(validations)){
			        	String[] checks =  ServerUtil.tokenToArray(validations, " ");
			        	//-- loop all the validations ---//
			        	String input = request.getParameter(id);
			        	ServerUtil.debug("\t\tinput : "+input);
			        	for(int i=0; checks != null && i < checks.length; i++){
			        		String check = checks[i];
			        		boolean checkMsg = false;
			        		String msgKey = null;
			        		ServerUtil.debug("\t\tcheck : "+check);
			        		if("required".equalsIgnoreCase(check)){
			        			if(ServerUtil.isNullOrEmpty(input)){
			        				valid = false;
			        				checkMsg = true;
			        				msgKey = "global.error.required";
			        			}
			        		}else if("date".equalsIgnoreCase(check)){
			        			msgKey = "global.error.date";
			        			if(!ServerUtil.isNullOrEmpty(input)){
				        			Date date = GenericTypeValidator.formatDate(input, Locale.getDefault());
				        			if(date == null){
				        				valid = false;
				        				checkMsg = true;
				        			}
			        			}
			        		}else if("number".equalsIgnoreCase(check)){
			        			msgKey = "global.error.number";
			        			if(!ServerUtil.isNullOrEmpty(input)){
				        			Long l = GenericTypeValidator.formatLong(input);
				        			if(l == null){
				        				valid = false;
				        				checkMsg = true;
				        			}
			        			}
			        		}else if("email".equalsIgnoreCase(check)){
			        			msgKey = "global.error.email";
			        			if(!ServerUtil.isNullOrEmpty(input)){
				        			if(!EmailValidator.getInstance().isValid(input)){
				        				valid = false;
				        				checkMsg = true;
				        			}
			        			}
			        		}else if("regexp".equalsIgnoreCase(check)){
			        			msgKey = "global.error.regex";
			        			if(!ServerUtil.isNullOrEmpty(input)){
				        			if(regExpValue != null){
				        				java.util.regex.Pattern patern = java.util.regex.Pattern.compile(regExpValue);
				        				java.util.regex.Matcher m = patern.matcher(input);
				        				if(!m.find()){
				        					valid = false;
					        				checkMsg = true;
				        				}
				        			}
			        			}
			        		}
			        		if("min".equalsIgnoreCase((String)key)){
			        			msgKey = "global.error.number.min";
			        			
			        		}else if("max".equalsIgnoreCase((String)key)){
			        			msgKey = "global.error.number.max";
			        			
			        		}else if("minLength".equalsIgnoreCase((String)key)){
			        			msgKey = "global.error.string.min";
			        			
			        		}else if("maxLength".equalsIgnoreCase((String)key)){
			        			msgKey = "global.error.string.max";
			        			
			        		}
			        		if(valid){
			        		if(checkRangeString){
			        			ServerUtil.debug("\t\t checkRangeString "+minLength+" <--> "+maxLength);
			        			msgKey = "global.error.string.range";
			        			if(!ServerUtil.isNullOrEmpty(input)){
			        				try{
			        				    long i_minLength = Long.parseLong(minLength);
			        				    long i_maxLength = Long.parseLong(maxLength);
			        				    if(input.length() <i_minLength || input.length() > i_maxLength){
			        				    	checkMsg = true;
			        				    	valid = false;
			        				    }
			        				}catch(Exception ex){
			        					ex.printStackTrace();
			        				}
			        			}
			        		}
			        		if(checkRangeNumber){
			        			msgKey = "global.error.number.range";
			        			ServerUtil.debug("\t\t checkRangeNumber "+min+" <--> "+max);
			        			if(!ServerUtil.isNullOrEmpty(input)){
			        				try{
			        				    long i_min = Long.parseLong(min);
			        				    long i_max = Long.parseLong(max);
			        				    long num = Long.parseLong(input);
			        				    if(num <i_min || num > i_max){
			        				    	checkMsg = true;
			        				    	valid = false;
			        				    }
			        				}catch(Exception ex){
			        					
			        				}
			        			}
			        		}
			        		}
			        		if(checkMsg){
			        			String[] arg1 = new String[]{name};
			        			if("global.error.number.range".equals(msgKey)){
			        				arg1 = new String[]{min,max};
			        			}else if("global.error.string.range".equals(msgKey)){
			        				arg1 = new String[]{minLength,maxLength};
			        			}else if("global.error.string.min".equals(msgKey)){
			        				arg1 = new String[]{minLength};
			        			}else if("global.error.string.max".equals(msgKey)){
			        				arg1 = new String[]{maxLength};
			        			}
		        				Map customError = (Map)check_errorCodes.get(check);
		        				String msg = null;
		        				if(StringUtils.isNullOrEmpty(msgKey)){
		        					try{
		        				        msg = messageSource.getMessage("global.error."+check, arg1,Locale.getDefault());
			        				}catch(Exception ex){
			        					StringBuffer buff = new StringBuffer();
			        					for (int j=0;j<arg1.length;j++)
			        						buff.append(arg1[j]+" ");
			        					msg = "Error getting message text for global.error."+check+" "+buff.toString();
		        						//return false;
		        					}
		        				}else{
		        					try{
		        					    msg = messageSource.getMessage(msgKey, arg1,Locale.getDefault());
		        					}catch(Exception ex){
			        					StringBuffer buff = new StringBuffer();
			        					for (int j=0;j<arg1.length;j++)
			        						buff.append(arg1[j]+" ");
			        					msg = "Error getting message text for "+msgKey+" "+buff.toString();
		        						//return false;
		        					}
		        				}
		        				ServerUtil.debug("\t\t 1"+check+" <--> "+msg);
		        				if(customError != null){
		        					errorCode = (String)customError.get("error_code");
		        					String[] argErr = (String[])customError.get("error_args");
		        					try{
		        					    msg = messageSource.getMessage(errorCode, argErr,Locale.getDefault());
		        					}catch(Exception ex){
			        					StringBuffer buff = new StringBuffer();
			        					for (int j=0;j<argErr.length;j++)
			        						buff.append(argErr[j]+" ");
			        					msg = "Error getting message text for "+errorCode+" "+buff.toString();
		        						//return false;
		        					}
		        				}
		        				//--- only hold one message each field ---//
		        				if(!errorMsg.containsKey(id)){
		        					ServerUtil.debug("\t\t 2"+id+" <--> "+msg);
		        				    errorMsg.put(id, msg);
		        				}
			        		}
			        		
			        	}
			        }  
			    }
		}
		if(!valid){
			//ServerUtil.debug("\t\t set error Map");
			request.setAttribute(VALIDATION_ERROR, errorMsg);
		}
        return valid;
    }
	
	public static void addFieldError(HttpServletRequest request,String fieldName, String error){
		Map m = (Map)request.getAttribute(VALIDATION_ERROR);
		if(m==null){
			m = new HashMap();
			request.setAttribute(VALIDATION_ERROR,m);
	    }
		m.put(fieldName, error);
	}
}
