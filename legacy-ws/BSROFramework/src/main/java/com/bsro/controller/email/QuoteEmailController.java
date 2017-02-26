package com.bsro.controller.email;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bfrc.Config;
import com.bfrc.framework.util.RequestUtil;
import com.bfrc.framework.util.RequestValidatorUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.security.Encode;

@Controller
public class QuoteEmailController {
	@Autowired
	Config thisConfig;
	
	@RequestMapping(value = "/email/quote/quote-email-form.htm", method = RequestMethod.GET)
	public String loadQuoteEmailForm(HttpServletRequest request, HttpSession session) {
	    String code = Encode.html(request.getParameter("code"));
	    String subject = thisConfig.getSiteFullName();
	    String quoteSource = Encode.html(request.getParameter("quoteSource"));

	    request.setAttribute("code", code);
	    request.setAttribute("subject", subject);
	    request.setAttribute("quoteSource", quoteSource);

		return "common/modals/quote-email-form";
	}
	
	@RequestMapping(value = {"/email/quote/send-quote-email.htm", "/email/quote/send-quote-email-t1.htm"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String sendQuoteEmail(HttpServletRequest request, HttpSession session) {
		Map<String, String> validationFields = new HashMap<String, String>();
		validationFields.put("emailto", "{name: 'To Email', validations: 'required email', maxLength:'255'}");
		
		// FCAC no longer requires this
		if (!Config.FCAC.equals(thisConfig.getSiteName())) {
			validationFields.put("emailfrom", "{name: 'From Email', validations: 'required email', maxLength:'255'}");
		}
		
		request.setAttribute(RequestValidatorUtils.VALIDATION_FIELDS,validationFields);
	
		RequestValidatorUtils.jsonToObject(request);
	    String code = Encode.encodeForJavaScript(request.getParameter("code"));
	    String emailto = Encode.html(request.getParameter("emailto"));
	    String emailfrom = Encode.html(request.getParameter("emailfrom"));
	    
	    String firstName = Encode.html(request.getParameter("firstName"));
	    String lastName = Encode.html(request.getParameter("lastName"));

	    String note = request.getParameter("note");
	    String quoteSource = Encode.encodeForJavaScript(request.getParameter("quoteSource"));
	    String tsRefererURL = request.getParameter("tsURL");
	    String appointmentRefererURL = request.getParameter("appointmentURL");
	    
	    try {
	    	if(!ServerUtil.isNullOrEmpty(firstName))
	    		appointmentRefererURL += "&amp;appointment.firstName="+java.net.URLEncoder.encode(firstName,"utf-8");
	    	if(!ServerUtil.isNullOrEmpty(lastName))
	    		appointmentRefererURL += "&amp;appointment.lastName="+java.net.URLEncoder.encode(lastName,"utf-8");
	    	if(!ServerUtil.isNullOrEmpty(emailto))
	    		appointmentRefererURL += "&amp;appointment.emailAddress="+java.net.URLEncoder.encode(emailto,"utf-8");
	    } catch (UnsupportedEncodingException e1) {

	    }

	    if(!ServerUtil.isNullOrEmpty(note)){
	    	try {
	    		String s = java.net.URLEncoder.encode(note,"utf-8");
	    		note=s.replace("'","&#39;");
	    	} catch (UnsupportedEncodingException e) {
	    		// this should never actually happen
	    		note = "";
	    	}
	     }
			
	    request.setAttribute("code", code);
	    request.setAttribute("emailto", emailto);
	    request.setAttribute("emailfrom", emailfrom);
	    request.setAttribute("note", note);
	    request.setAttribute("quoteSource", quoteSource);
	    request.setAttribute("tsRefererURL", tsRefererURL);
    	request.setAttribute("appointmentRefererURL", appointmentRefererURL);
	    /*try {
	    	request.setAttribute("tsRefererURL", java.net.URLDecoder.decode(tsRefererURL, "UTF-8"));
	    	request.setAttribute("appointmentRefererURL", java.net.URLDecoder.decode(appointmentRefererURL, "UTF-8"));
	    } catch(Exception exp) {	    	
	    }*/
	    
	    // This needs to go last - the form needs the request attributes to be set on success and on failure
		// what this amounts to, is that if we want to do this as a GET request (which we do, sometimes), we need _dopost=1 in the query string
		if(RequestUtil.doProcessForm(request)){
			boolean ok = true;
			ok = RequestValidatorUtils.validate(request);
			if (!ok) {
				return "common/modals/quote-email-form";
			}
		}
		
		return "common/modals/send-quote-email";
	}
}
