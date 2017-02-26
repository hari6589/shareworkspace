package com.bfrc.timer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.bfrc.Config;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.contact.Attachment;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.contact.WebSite;
import com.bfrc.pojo.email.EmailSignup;
import com.bfrc.pojo.promotion.PromotionImages;

import org.springframework.web.context.WebApplicationContext;

public class CheckAndSendCoupons {
	Timer timer;
	boolean error = false;
	private EmailSignupDAO emailSignupDAO;
	private WebApplicationContext applicationCtx;
	private ServletContext application;
    private int delay = 30000;   // delay for ? sec.
    private String realPath;
    static private CheckAndSendCoupons _theInstance;
    Config thisConfig;
    ContactDAO contactDAO;
    PromotionDAO promotionDAO;
    MailManager mailManager;
	private void locateBeans(HttpServletRequest request) {
		application = request.getSession().getServletContext();
		realPath = application.getRealPath("");
		applicationCtx = Config
				.getCtx(application);
		emailSignupDAO = (EmailSignupDAO) applicationCtx
				.getBean("emailSignupDAO");
		thisConfig = (Config) applicationCtx
		        .getBean("config");
		contactDAO = (ContactDAO) applicationCtx
                .getBean("contactDAO");
		mailManager = (MailManager)applicationCtx
                .getBean("mailManager");
		promotionDAO = (PromotionDAO) applicationCtx
        .getBean("promotionDAO");
	}

	public void stop() {
		if (timer != null)
			timer.cancel();
	}
	public static CheckAndSendCoupons getInstance() {
		 if (_theInstance == null) {
		   synchronized (CheckAndSendCoupons.class) {
			 if (_theInstance == null) {
			   _theInstance = new CheckAndSendCoupons();
			 }
		   }
		 }
		 return _theInstance;
	  }
	private CheckAndSendCoupons() {
		timer = new Timer();
	}
	public void start(HttpServletRequest request) {
		start(request, -1);
	}
	
	public void checkEmails() {
		List items = emailSignupDAO.getDelayedSignups();
		if(items != null){
			for(int i=0; i< items.size(); i++){
				EmailSignup item = (EmailSignup)items.get(i);
				
				if("TPMobile".equals(item.getSource())){
					if("TP".equals(thisConfig.getSiteName())){
						//send TP email
						sendEmail(item);
					}
				}else if("FCACMobile".equals(item.getSource())){
					if("FCAC".equals(thisConfig.getSiteName())){
						//send FCAC email
						sendEmail(item);
					}
				}else if("ETMobile".equals(item.getSource())){
					if("ET".equals(thisConfig.getSiteName())){
						//send FCAC email
						sendEmail(item);
					}
				}
			}
		}
	}
	public void sendEmail(EmailSignup item){
		String emailFile = realPath+"/e/email_qr_coupon1.html";
		if("FCAC".equals(thisConfig.getSiteName())){
			emailFile = realPath+"/e/email_qr_coupon.html";
		}
		  String subject = "Big Savings from "+thisConfig.getSiteFullName()+" are here";
	    boolean delayed = false;
	    try {
	    	Mail mail = new Mail();
			Map emailData = new HashMap();
			emailData.put("PAGE_TITLE", subject);
			emailData.put("FIRST_NAME", item.getFirstName());
            emailData.put("LAST_NAME", item.getLastName());
			
			mail.setSubject(subject);
			mail.setTo(new String[]{item.getEmailAddress()});
			//mail.setFrom("\"DO-NOT-REPLY\" <webmaster@firestonecompleteautocare.com>");
            //-- dynamic email address --//
            //File coupon = new File(realPath+"/WEB-INF/images/qr_coupon1.jpg");
			PromotionImages promo = promotionDAO.getPromotionImagesById("tiresplus_mobile_qr_coupon_01");
			
            Map sites = contactDAO.getMappedWebSites();
            WebSite webSite = (WebSite)sites.get(thisConfig.getSiteName());
            mail.setFrom("\"DO-NOT-REPLY\" <"+webSite.getWebmaster()+">");

			mail.setHtml(true);
			String body = ServerUtil.populateEmailMessage(emailFile,emailData);
			mail.setBody(body);
			byte[] buffer = null;
            if(!delayed){
                Attachment attachment = new Attachment();
                attachment.setName("special_offer.jpg");
                //FileInputStream fis = new FileInputStream(coupon);
                //DataInputStream in = new DataInputStream(fis);

                //byte[] buffer = new byte[4096];
                //buffer = new byte[fis.available()];
                //System.out.println("sectionId : "+sectionId+ " LENGTH: "+in.available());
                //in.readFully(buffer);
                buffer = promo.getImage();
                attachment.setDocument(buffer);
                mail.setAttachments(new Attachment[]{attachment});
            }
            mailManager.sendMail(mail);
            if(!StringUtils.isNullOrEmpty(item.getFriendsEmail()) && StringUtils.isValidEmail(item.getFriendsEmail())){
            	subject = "A friend wants to share big savings from "+thisConfig.getSiteFullName();
            	if(!"blank@bsro.com".equals(item.getEmailAddress())){
                    emailData.put("FRIEND_EMAIL", "("+item.getEmailAddress()+")");
                }else{
                    emailData.put("FRIEND_EMAIL", "");
                }
                emailFile = realPath+"/e/email_qr_coupon1_f.html";
                if("FCAC".equals(thisConfig.getSiteName())){
    				emailFile = realPath+"/e/email_qr_coupon_f.html";
    			}
                if(delayed)
                    emailFile = realPath+"/e/email_qr_coupon_delayed_f.html";
                mail = new Mail();
                mail.setSubject(subject);
                mail.setTo(new String[]{item.getFriendsEmail()});
                //mail.setFrom("\"DO-NOT-REPLY\" <webmaster@firestonecompleteautocare.com>");
                //-- dynamic email address --//
                mail.setFrom("\"DO-NOT-REPLY\" <"+webSite.getWebmaster()+">");

                mail.setHtml(true);
                body = ServerUtil.populateEmailMessage(emailFile,emailData);
                mail.setBody(body);
                if(buffer != null){
                    Attachment attachment = new Attachment();
                    attachment.setName("special_offer.jpg");
                    
                    //FileInputStream fis = new FileInputStream(coupon);
                    //DataInputStream in = new DataInputStream(fis);

                    //byte[] buffer = new byte[4096];
                    //buffer = new byte[fis.available()];
                    //System.out.println("sectionId : "+sectionId+ " LENGTH: "+in.available());
                    //in.readFully(buffer);
                    buffer = promo.getImage();
                    attachment.setDocument(buffer);
                    mail.setAttachments(new Attachment[]{attachment});
                }
                mailManager.sendMail(mail);
            }
            item.setActionCode("SENT");
			emailSignupDAO.update(item);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void start(HttpServletRequest request, int seconds) {
		if (seconds < 0)
			seconds = 60;
		if (request == null) {
			return;
		} else {
			locateBeans(request);
			/*
			if(timer != null){
				timer.cancel();//cancel the if have one
				timer.purge();
				timer = new Timer();
			}
			*/
			timer.scheduleAtFixedRate(new RemindTask(), delay, seconds * 1000);
		}
	}

	class RemindTask extends TimerTask {
		public void run() {
			checkEmails();
		}
		
	}
}
