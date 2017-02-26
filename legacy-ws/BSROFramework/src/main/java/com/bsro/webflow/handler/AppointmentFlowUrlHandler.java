/**
 * 
 */
package com.bsro.webflow.handler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.DefaultFlowUrlHandler;
import org.springframework.webflow.core.collection.AttributeMap;

/**
 * @author schowdhury
 *
 */
public class AppointmentFlowUrlHandler extends DefaultFlowUrlHandler {
		
	@Override
    public String createFlowDefinitionUrl(String flowId, AttributeMap input, HttpServletRequest request) {
		return appendRequestParam(super.createFlowDefinitionUrl(flowId, input, request), request);
	
    }

    @Override
    public String createFlowExecutionUrl(String flowId, String flowExecutionKey, HttpServletRequest request) {
    	return appendRequestParam(super.createFlowExecutionUrl(flowId, flowExecutionKey, request), request);
    }

    protected String appendRequestParam(String url, HttpServletRequest request) {
    	StringBuilder mUrl = new StringBuilder(url);
    	//System.out.println("url init==================="+url);
    	try{
    		String navParam = request.getParameter("nav");
    		//System.out.println("navParam1 ========="+navParam);
    		if(navParam != null && !navParam.isEmpty() && !navParam.equalsIgnoreCase("null")){
    			request.getSession().setAttribute("nav", navParam);
    		} else {
    			navParam = (String)request.getSession().getAttribute("nav");
    			if(navParam == null || navParam.isEmpty() || navParam.equalsIgnoreCase("null")){
    				navParam = "top";
    				request.getSession().setAttribute("nav", navParam);
    			}  			
    		}
    		//System.out.println("navParam2 ========="+navParam);
    		if(navParam != null && !navParam.isEmpty() && !navParam.equalsIgnoreCase("null")){
    			if(url.indexOf("?") == -1)
    				mUrl.append("?");
    			else
    				mUrl.append("&");
    			mUrl.append("nav=");
    			mUrl.append(URLEncoder.encode(navParam, "utf-8"));
    		}
    		String intCmp = (String) request.getParameter("int_cmp");
			if(intCmp != null && intCmp.equalsIgnoreCase("pandora_appt")) { 
				mUrl.append("&int_cmp=pandora_appt");
			}
    		String stepParam = (String)request.getSession().getAttribute("step");
    		//System.out.println("stepParam ========="+stepParam);
    		if(stepParam != null && !stepParam.isEmpty()&& !stepParam.equalsIgnoreCase("null")){
    			mUrl.append("&step=");
    			mUrl.append(URLEncoder.encode(stepParam,  "utf-8"));
    		}
    		
    		String extParams = (String)request.getSession().getAttribute("extParams");
    		//System.out.println("extParams ========="+extParams);
    		if(extParams != null && !extParams.isEmpty() && !extParams.equalsIgnoreCase("null")){
    			mUrl.append(extParams);
    		}else{
    			@SuppressWarnings("unchecked")
				Enumeration<String> currParams = request.getParameterNames();
    			extParams = "";
    			while (currParams.hasMoreElements()){
    				String key = currParams.nextElement();
    				//System.out.println("key==="+key +" and step="+stepParam);
    				if(!key.equalsIgnoreCase("execution") && !key.equalsIgnoreCase("nav") 
    						&& !key.startsWith("step") && !key.equalsIgnoreCase("enteredZip")
    						&& !key.startsWith("_eventId") &&!key.equalsIgnoreCase("storeNumber")){
    					if(key.contains("lw_") || key.contains("utm_") || key.contains("k_")
    							|| key.equalsIgnoreCase("psid")|| key.equalsIgnoreCase("gclid")
    							|| key.equalsIgnoreCase("_vsrefdom")){
	    					String value = (String)request.getParameter(key);
	    					//System.out.println("keyValue="+value);
	    					if(value != null && !value.isEmpty()){
	    						extParams += "&"+key+"="+URLEncoder.encode(value,  "utf-8");
	    					}
    					}
    				}
    			}
    			if(extParams != null && !extParams.isEmpty() && !extParams.equalsIgnoreCase("null")){
    				request.getSession().setAttribute("extParams", extParams);
    				mUrl.append(extParams);
    			}
    		}

    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return mUrl.toString();
    }
}
