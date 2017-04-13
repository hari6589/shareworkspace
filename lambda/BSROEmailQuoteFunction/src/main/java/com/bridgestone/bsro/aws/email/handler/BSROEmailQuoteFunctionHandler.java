package com.bridgestone.bsro.aws.email.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.bridgestone.bsro.aws.appointment.util.AuthenticateService;
import com.bridgestone.bsro.aws.appointment.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.appointment.webservice.BSROWebServiceResponseCode;
import com.bridgestone.bsro.aws.email.service.AlignmentEmailQuoteService;
import com.bridgestone.bsro.aws.email.service.BatteryEmailQuoteService;
import com.bridgestone.bsro.aws.email.service.FCACMailMessage;
import com.bridgestone.bsro.aws.email.service.HTMailMessage;
import com.bridgestone.bsro.aws.email.service.MailMessage;
import com.bridgestone.bsro.aws.email.service.Mailer;
import com.bridgestone.bsro.aws.email.service.StringUtils;
import com.bridgestone.bsro.aws.email.service.TPMailMessage;
import com.bridgestone.bsro.aws.email.service.TireEmailQuoteService;
import com.bridgestone.bsro.aws.email.service.WWTMailMessage;

public class BSROEmailQuoteFunctionHandler implements RequestHandler<Object, Object> {
//public class BSROEmailQuoteFunctionHandler {

	private static MailMessage mailMessage;
    public static final String TIRE_REPLACEMENT_CHOICE_ID = "2745";
	public static final String MVAN_TIRE_REPLACEMENT_CHOICE_ID = "2782";
	public static final String BATTERY_CHOICE_ID = "2750";
	public static final String ALIGNMENT_CHOICE_ID = "2749";
	public static Properties properties = null;
    
	static {
		InputStream inputStream = BSROEmailQuoteFunctionHandler.class.getClassLoader().getResourceAsStream("application.properties");
		properties = new Properties();
		try {
			properties.load(inputStream);
			inputStream.close();			
		} catch (IOException e) {
			System.out.println("IOException  in BSROScheduleAppointmentFunctionHandler Constructor!");
		} catch (Exception e) {
			System.out.println("Exception  in BSROScheduleAppointmentFunctionHandler Constructor!");
		}
	}
	
    public static MailMessage getInstance(String siteName) {
		if (siteName.equalsIgnoreCase("TP")) {
			return new TPMailMessage();
		} else if (siteName.equalsIgnoreCase("WWT")) {
			return new WWTMailMessage();
		} else if (siteName.equalsIgnoreCase("HT")) {
			return new HTMailMessage();
		}
		return new FCACMailMessage();
	}
    
    public static String getWebSiteAppRoot(String siteName) {
    	String appRoot = "";
    	if (!StringUtils.isNullOrEmpty(siteName)) {
			appRoot = "http://";
			
//			if (ServerUtil.isProduction()) {
//				appRoot += "www.";
//			} else if (ServerUtil.isBSROQa()) {
//				appRoot += "cwh-qa.";
//			} else {
//				appRoot += "cwh-uat.";
//			}
			appRoot += "cwh-uat.";
			
	    	if ("FCAC".equalsIgnoreCase(siteName)) {
				appRoot += "firestonecompleteautocare.com";
			} else if ("TP".equalsIgnoreCase(siteName)) {
				appRoot += "tiresplus.com";
			} else if ("HT".equalsIgnoreCase(siteName)) {
				appRoot += "hibdontire.com";
			} else if ("WWT".equalsIgnoreCase(siteName)) {
				appRoot += "wheelworks.net";
			}
    	}
    	return appRoot;
    }
    
	
	public Object handleRequest(Object input, Context context) {
		//public static void main(String[] args) {
		
		HashMap<String, String> params = (HashMap<String, String>) input;
		String resourcePath = params.get("resourcePath");
		String siteName = params.get("siteName");
		String sourceType = params.get("sourceType");
		String quoteId = params.get("quoteId");
		String firstName = params.get("firstName");
		String lastName = params.get("lastName");
		String email = params.get("email");
		String tokenId = params.get("tokenId");
		String appName = params.get("appName");
		
		System.out.println("Resource Path: " + resourcePath);
		System.out.println("SiteName: " + siteName);
		System.out.println("Source Type: " + sourceType);
		System.out.println("Quote Id: " + quoteId);
		System.out.println("FirstName: " + firstName);
		System.out.println("LastName: " + lastName);
		System.out.println("Email: " + email);
		System.out.println("AppName: " + appName);
		System.out.println("Token Id: " + tokenId);
		
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
    	AuthenticateService security = new AuthenticateService();
    	try {
			if(!security.authenticateService(properties, tokenId,appName)){
				bsroWebServiceResponse.setStatusCode(BSROWebServiceResponseCode.UNAUTHORIZED_ERROR.toString());
				return bsroWebServiceResponse;
			}
		} catch (IOException e) {
			System.out.println("Exception while Authentication : " + e.getMessage());
		}
		
		String messageContent = "";
		Mailer mailer = new Mailer();
		try {
			MailMessage mailMessage = getInstance(siteName);
			String baseURL = getWebSiteAppRoot(siteName);
			if(sourceType.equals("tire")) {
				TireEmailQuoteService tireEmailQuoteService = new TireEmailQuoteService();
				messageContent = tireEmailQuoteService.generateTireQuoteMessage(mailMessage, baseURL, TIRE_REPLACEMENT_CHOICE_ID, quoteId, siteName, firstName, lastName, email);
			} else if(sourceType.equals("battery")) {
				BatteryEmailQuoteService batteryEmailQuoteService = new BatteryEmailQuoteService();
				messageContent = batteryEmailQuoteService.generateBatteryQuoteMessage(mailMessage, baseURL, BATTERY_CHOICE_ID, quoteId, siteName, firstName, lastName, email);
			} else if(sourceType.equals("alignment")) {
				AlignmentEmailQuoteService alignmentEmailQuoteService = new AlignmentEmailQuoteService();
				messageContent = alignmentEmailQuoteService.generateAlignmentQuoteMessage(mailMessage, baseURL, ALIGNMENT_CHOICE_ID, quoteId, siteName, firstName, lastName, email);
			}
			
			// Mail prepared content
			mailer.sendMail(messageContent, email);
			
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
			System.out.println("FAILURE");
		}
		System.out.println("SUCCESS");
		return "SUCCESS";
	}

}
