package com.report;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class MailReport {

    static final String FROM = "aravindhan.jayakumar@csscorp.com";   // Replace with your "From" address. This address must be verified.
    static final String TO = "aravindhan.jayakumar@csscorp.com";  // Replace with a "To" address. If your account is still in the 
                                                       // sandbox, this address must be verified.
    
    static final String BODY = "This email was sent through the Amazon SES SMTP interface by using Java.";
    static final String SUBJECT = "Drop in Appointments - Comparisons - Previous Hour and Avg. of 4 Weeks - Appointments by Store Type";
    
    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static final String SMTP_USERNAME = "AKIAI2HIRXY2Z2ZCUTBA";  // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "AvHHbHqg6Ss8j7U6t9d5Y+uQpWKiGEMvv35onR1n87Bk";  // Replace with your SMTP password.
    
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";    
    
    // The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;

    public void checkAndMail(String message) {
    	mailReportData(message);
    }
    
    public void mailReportData(String bodyMessage) {

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
    	
		// Create a transport.        
        Transport transport = null; 
        		
    	try {        
            transport = session.getTransport();
            
    		
	    	
	        // Create a message with the specified information. 
	        MimeMessage msg = new MimeMessage(session);
	        msg.setFrom(new InternetAddress(FROM));
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
	        msg.setSubject(SUBJECT);
	        msg.setContent(bodyMessage,"text/plain");

	        System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
            System.out.println("1");
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            System.out.println("2");
            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally {            
            try {
            	// Close and terminate the connection.
				transport.close();
			} catch (MessagingException e) {
				System.out.println("MessagingException : " + e.getMessage() + " ::: " + e.getStackTrace());
			}        	
        }
    }
}