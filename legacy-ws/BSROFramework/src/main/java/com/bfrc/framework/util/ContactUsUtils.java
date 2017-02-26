/**
 * 
 * Modified from jWebApp 
 * 
 * URL and Licenses
 * go to http://www.softwaresensation.com
 */

package com.bfrc.framework.util;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import com.bfrc.Config;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.CustomerDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Feedback;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.customer.CustomerContactusemailLog;
import com.bfrc.pojo.store.Store;
import com.bfrc.storelocator.util.LocatorUtil;

public class ContactUsUtils {
	static CustomerDAO customerDAO;
	static ServletContext ctx;
	static ContactDAO contactDAO;
	static StoreDAO storeDAO;
	static Config thisConfig;
	static MailManager mailManager;
	private static Map mainSiteNames;

	public static Map getSiteNames() {
		if (mainSiteNames == null) {
			mainSiteNames = new LinkedHashMap();
			mainSiteNames.put("FCAC", "Firestone Complete Auto Care");
			mainSiteNames.put("TP", "Tires Plus Total Car Care");
			mainSiteNames.put("ET", "ExpertTire");
			mainSiteNames.put("WW", "Wheel Works");
			mainSiteNames.put("HTP", "Hibdon Tires Plus");
		}
		return mainSiteNames;
	}

	private static void locateBeans(HttpServletRequest request) {
		if (ctx == null)
			ctx = request.getSession().getServletContext();
		if (contactDAO == null)
			contactDAO = (ContactDAO) Config.locate(ctx, "contactDAO");
		if (storeDAO == null)
			storeDAO = (StoreDAO) Config.locate(ctx, "storeDAO");
		if (customerDAO == null)
			customerDAO = (CustomerDAO) Config.locate(ctx, "customerDAO");
		if (mailManager == null)
			mailManager = (MailManager) Config.locate(ctx, "mailManager");
		if (thisConfig == null)
			thisConfig = (Config) Config.locate(ctx, "config");
	}

	public static boolean doContactUs(HttpServletRequest request) {
		locateBeans(request);
		List subjects = contactDAO.getSubjects();
		Map siteNames = getSiteNames();
		try {

			String nature = request.getParameter("nature");
			String storeType = request.getParameter("storeType");
			String distSite = Config.getContactDistributionWebSite(storeType);
			Long referenceNumber = new Long(-1);
			CustomerContactusemailLog contactLog
                = (CustomerContactusemailLog)request.getSession().getAttribute("contactus.contactLog");
			if(contactLog == null){
			    contactLog = new CustomerContactusemailLog();
			    RequestUtil.fillObject(contactLog, request);
		    }
			Mail mail = new Mail();
			String[] to = contactDAO.getTo(nature, distSite, contactLog.getStoreNumber(), contactLog.getState());
			// temporary
			mail.setTo(to);
			String[] cc = contactDAO.getCc(nature, distSite, contactLog.getStoreNumber(), contactLog.getState());
			mail.setCc(cc);
			String[] bcc = contactDAO.getBcc(nature, distSite, contactLog.getStoreNumber(), contactLog.getState());
			mail.setBcc(bcc);
			String from = contactLog.getFirstName() + " "
					+ contactLog.getLastName() + "<"
					+ contactLog.getEmailAddress() + ">";
			// --- 20101231 contact us by Id ---//
			java.text.DecimalFormat df8 = new java.text.DecimalFormat(
					"00000000");
			List feedbacks = contactDAO.getAllFeedBacks();
			if (Config.isCustomerServiceFeedback(feedbacks, thisConfig, nature)
					|| Config.isComplimentFeedback(feedbacks, thisConfig,
							nature)) {
				from = contactDAO.getFrom();
			}
			mail.setFrom(from);
			String storeBody = "", storeSuffix = "";
			Store store = null;
			Long storeNumber = contactLog.getStoreNumber();
			if (storeNumber!=null && storeNumber.intValue()!=0) {
				store = storeDAO.findStoreById(storeNumber);
				storeBody = "Store Visited:\r\n#"
					+ LocatorUtil.padStoreNumber(store.getStoreNumber().intValue()) + "\r\n" + store.getStoreName() + "\r\n" + store.getAddress() + "\r\n" + store.getCity() + ", " + store.getState() + "  " + store.getZip() + "\r\n";
				if(store.getStoreName().indexOf("Licensee") > -1)
					storeSuffix = " Licensee";
			}
			String subject = thisConfig.getSiteFullName()
					+ " Contact Feedback - " + contactLog.getFirstName() + " "
					+ contactLog.getLastName();
			if (Config.isComplimentFeedBack(nature)) {
				subject = "Compliment - " + subject;
			}
			mail.setSubject(subject);

			// --- Start Log Contact Us to Database 20101231 ---//
			Iterator it = subjects.iterator();
			Integer feedbackId = new Integer(-1);
			while (it.hasNext()) {
				Feedback feedback = (Feedback) it.next();
				if (nature.equalsIgnoreCase(feedback.getSubject())) {
					feedbackId = new Integer(feedback.getFeedbackId());
					break;
				}
			}
			contactLog.setFeedbackId(feedbackId);
			contactLog.setSiteId(new Integer(contactDAO.getSite().getSiteId()));
			contactLog.setUserAgentInfo(request.getHeader("User-Agent"));
			contactLog.setEmailTo(ServerUtil.arrayToString(to, ";"));
			contactLog.setEmailCc(ServerUtil.arrayToString(cc, ";"));
			contactLog.setEmailBcc(ServerUtil.arrayToString(bcc, ";"));
			contactLog.setEmailFrom(from);
			if (!StringUtils.isNullOrEmpty(request.getParameter("phone0"))
					&& !StringUtils.isNullOrEmpty(request
							.getParameter("phone1"))
					&& !StringUtils.isNullOrEmpty(request
							.getParameter("phone2"))) {
				contactLog.setDaytimePhone(request.getParameter("phone0") + "-"
						+ request.getParameter("phone1") + "-"
						+ request.getParameter("phone2"));
			}
			referenceNumber = customerDAO
					.createCustomerContactusemailLog(contactLog);
			if (to.length > 0) {
				StringBuffer body = new StringBuffer();
				body.append("The individual below has entered feedback on "
						+ contactDAO.getUrl() + " website.\r\n\r\n");
				// --- 20101231 contact us by Id ---//
				if (referenceNumber.longValue() != -1) {
					body.append("Reference # = " + df8.format(referenceNumber)
							+ "\r\n");
				}
				body.append("Nature of Inquiry = " + nature + "\r\n");
				body.append("First Name = " + contactLog.getFirstName()
						+ "\r\n");
				body.append("Last Name = " + contactLog.getLastName() + "\r\n");
				if (!ServerUtil.isNullOrEmpty(contactLog.getStreetAddress1()))
					body.append(contactLog.getStreetAddress1() + "\r\n");
				if (!ServerUtil.isNullOrEmpty(contactLog.getStreetAddress2()))
					body.append(contactLog.getStreetAddress2() + "\r\n");
				if (!ServerUtil.isNullOrEmpty(contactLog.getCity()))
					body.append(contactLog.getCity());
				if (!ServerUtil.isNullOrEmpty(contactLog.getState()))
					body.append(", " + contactLog.getState() + "  ");
				if (!ServerUtil.isNullOrEmpty(contactLog.getZipCode()))
					body.append(" "+contactLog.getZipCode());
				body.append("\r\n");
				if (!StringUtils.isNullOrEmpty(contactLog.getDaytimePhone())) {
					body.append("Phone Number = "
							+ contactLog.getDaytimePhone() + "\r\n");
				}
				if (!StringUtils.isNullOrEmpty(contactLog.getEveningPhone())) {
					body.append("Evening Number = "
							+ contactLog.getEveningPhone() + "\r\n");
				}
				if (!StringUtils.isNullOrEmpty(contactLog.getMobilePhone())) {
					body.append("Mobile Number = "
							+ contactLog.getMobilePhone() + "\r\n");
				}

				body.append("E-mail Address = " + contactLog.getEmailAddress()
						+ "\r\n");

				if (!StringUtils.isNullOrEmpty(storeType)) {
					body.append("Store Brand : " + siteNames.get(storeType)
							+ "\r\n");
				}
				if (!StringUtils.isNullOrEmpty(storeBody)) {
					body.append(storeBody);
				}
				if (!StringUtils.isNullOrEmpty(contactLog.getVehicleSubmodel())) {
					body.append("Vehicle = " + contactLog.getVehicleYear()
							+ " " + contactLog.getVehicleMake() + " "
							+ contactLog.getVehicleModel() + " "
							+ contactLog.getVehicleSubmodel() + "\r\n");
				}

				body.append("Message = " + contactLog.getComments() + "\r\n");
				body.append("\r\nUser Agent: "
						+ request.getHeader("User-Agent") + "\r\n");
				
				body.append(printSessionData(request));
				
				mail.setBody(body.toString());
				try{
			        if(to != null && to.length > 0){
				        mailManager.sendMail(mail);
			        }
				} catch (Exception ex) {
					ex.printStackTrace();
					//ignore send mail error if in dev01 and qa01
				}
				request.getSession().setAttribute("contactus.contactLog",
						contactLog);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 *  This method is intended to include debugging information in the Contact Us emails.
	 *  Currently, it returns a blank String, pending a redesign of the functionality.
	 * @param request
	 * @return
	 */
	public static String printSessionData(HttpServletRequest request) {
		return "";
		/*
		StringBuffer body = new StringBuffer();
		
		@SuppressWarnings(value="rawtypes")
		java.util.Enumeration e = request.getSession().getAttributeNames();	
		body.append("\n");
		if (e != null && e.hasMoreElements()) {
			while (e.hasMoreElements()) {
		        String name = (String)e.nextElement();
		        Object obj = request.getSession().getAttribute(name);
		        body.append(" " + name + ": " + obj+"\n");
		        //System.out.println("-----------------"+obj.getClass().getName());
		        if(obj instanceof com.mastercareusa.selector.GenericVehicle){
		        	com.mastercareusa.selector.GenericVehicle item = (com.mastercareusa.selector.GenericVehicle)obj;
		        	body.append(" \t\tbaseId: " + item.getBaseId()+"\n");
		        	body.append(" \t\tyear: " + item.getYear()+"\n");
		        	body.append(" \t\tmake: " + item.getMake()+"\n");
		        	body.append(" \t\tmodel: " + item.getModel()+"\n");
		        	body.append(" \t\tsubmodel: " + item.getSubmodel()+"\n");
		        	body.append(" \t\tisTpms: " + item.isTpms()+"\n");
		        }else if("com.bfrc.pojo.store.Store".equalsIgnoreCase(obj.getClass().getName())){
		        	com.bfrc.pojo.store.Store item = (com.bfrc.pojo.store.Store)obj;
		        	body.append(" \t\tphone: " + item.getPhone()+"\n");
		        	body.append(" \t\tstore number: " + item.getNumber()+"\n");
		        	body.append(" \t\tstore name: " + item.getStoreName()+"\n");
		        	body.append(" \t\tstore zip: " + item.getZip()+"\n");
		        }else if("com.bfrc.UserSessionData".equalsIgnoreCase(obj.getClass().getName())){
		        	body.append(StringUtils.dumpBeanInfo(obj));
	            }
		    }
		}
		
		return body.toString();
		*/
	}

}
