/*
 * Created on Sep 2, 2004
 */
package com.bfrc.framework.spring;

import org.springframework.core.io.*;
import org.springframework.mail.javamail.*;

import com.bfrc.pojo.contact.*;

/**
 * @author Jin Fang
 *  
 */
public class MailManager {
	private JavaMailSender mailSender;
	/**
	 * @param mailSender The mailSender to set.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(Mail mail) throws Exception {
        Attachment[] atts = mail.getAttachments();
		boolean multipart = atts != null && atts.length > 0;
    	MimeMessageHelper message = new MimeMessageHelper(this.mailSender.createMimeMessage(), multipart);
    	// to
    	String[] to = mail.getTo();
    	if(to == null)
    		to = new String[0];
        message.setTo(to);
		// cc
    	String[] cc = mail.getCc();
    	if(cc == null)
    		cc = new String[0];
        message.setCc(cc);
		// bcc
    	String[] bcc = mail.getBcc();
    	if(bcc == null)
    		bcc = new String[0];
        message.setBcc(bcc);
        // from
        message.setFrom(mail.getFrom());
		// reply to
        if(mail.getReplyTo() == null)
        	message.setReplyTo(mail.getFrom());
        else message.setReplyTo(mail.getReplyTo());
		// subject
        message.setSubject(mail.getSubject());
        // body
        message.setText(mail.getBody(), mail.isHtml());
        // attachments
        if(multipart)
        	for(int i=0; i<atts.length; i++)
        		message.addAttachment(atts[i].getName(), new ByteArrayResource(atts[i].getDocument()));
        // send email
       	MimeMailMessage msg = new MimeMailMessage(message);
       	this.mailSender.send(msg.getMimeMessage());
    }
}