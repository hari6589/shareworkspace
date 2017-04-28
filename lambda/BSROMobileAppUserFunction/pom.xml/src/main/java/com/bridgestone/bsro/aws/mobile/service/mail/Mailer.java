package com.bridgestone.bsro.aws.mobile.service.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	// Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static final String SMTP_USERNAME = "AKIAI2HIRXY2Z2ZCUTBA";  // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "AvHHbHqg6Ss8j7U6t9d5Y+uQpWKiGEMvv35onR1n87Bk";  // Replace with your SMTP password.
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";
    
	// The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;
    
	public static String getDoNotReply(String siteName) {
		/*
		WebSite site = websiteService.getBySiteName(siteName);
		if(site != null) {
			String from = "DO-NOT-REPLY<" + site.getWebmaster() + ">";
			return from;
		}
		return "";*/
		return "DO-NOT-REPLY<aravindhan.jayakumar@csscorp.com>";
	}
	
	public static String getSiteNameString(String userType){
		/*
		if(userType.equalsIgnoreCase(SiteTypes.TP.name())){
			return "Tires Plus Mobile App";
		}else if(userType.equals(SiteTypes.FCAC.name())){
			return "Firestone Mobile App";
		}
		return "";*/
		return "Firestone Mobile App";
	}

	public static String generateEmailBody(String newPassword, String appName) {
		StringBuffer sb = new StringBuffer();
		sb.append("You recently requested that your "+ appName +" password be reset.\r\n");
		sb.append("Your new password is "+newPassword+".\n\n");
		sb.append("If you have not requested this change, please contact administrator using Contact Us feature.\n\n");
		sb.append("Thank You,\n");
		sb.append(getSiteNameString(appName) + " Team.\r\n");
		return sb.toString();
	}
	
	public static void sendEmail(String emailTo, String emailFrom, String emailSubject, String emailBody) throws AddressException, MessagingException {
		// Create a Properties object to contain connection configuration information.
    	Properties props = System.getProperties();
    	props.put("mail.transport.protocol", "smtps");
    	props.put("mail.smtp.port", PORT);
    	
    	// Set properties indicating that we want to use STARTTLS to encrypt the connection.
    	// The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
    	props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.starttls.required", "true");

        // Create a Session object to represent a mail session with the specified properties. 
    	Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information. 
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailFrom));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        
        String subject = "";
        
        msg.setSubject(subject);
        msg.setContent(emailBody,"text/html");
            
        // Create a transport.
        Transport transport = session.getTransport();
        
        // Send the message.
        try {
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
        	
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();        	
        }
	}
}
