package com.bfrc.framework.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bfrc.Config;
import com.bfrc.framework.dao.AdminDAO;
import com.bfrc.framework.dao.BsroCorpUsersDAO;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.DistrictDAO;
import com.bfrc.framework.dao.IpAccessDAO;
import com.bfrc.framework.dao.StoreAdminDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.ZoneDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.admin.AdminApp;
import com.bfrc.pojo.admin.AdminIpAddress;
import com.bfrc.pojo.admin.GlobalAdmin;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.geo.DistrictManager;
import com.bfrc.pojo.geo.HrDistricts;
import com.bfrc.pojo.geo.ZoneList;
import com.bfrc.pojo.geo.ZoneManager;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.storeadmin.BsroCorpUsers;
import com.bfrc.pojo.storeadmin.StoreAdminOffer;
import com.bfrc.pojo.storeadmin.StoreAdminOfferCategory;
import com.bfrc.pojo.storeadmin.StoreAdminOfferTemplate;
import com.bfrc.pojo.storeadmin.StoreAdminPromotion;

public class AdminUtils {
	public final static String KEY = "com.bfrc.Admin";
	public final static String USER_KEY = "com.bfrc.admin.User";
	public final static int LOGGED_IN = 1;
	public final static int APP_TESTIMONIAL = 1;
	public final static int APP_WEBTIRESALES = 8;
	public final static int APP_TP = 6;
	public final static int APP_TIRE_PROMOTIONS = 31;
	public final static int APP_PROMOTIONS = 3;
	public final static int APP_CUSTOMER_EMAIL = 9;
	public final static int APP_TIRE_PRICING = 7;
	public final static int APP_STORE_ADMIN = 37;
	public final static String LOGIN_URL = "http://www.firestonecompleteautocare.com/cms/login.jsp";

	public boolean checkAuth(HttpServletRequest request, HttpSession session,
			HttpServletResponse response) {

		if (com.bfrc.framework.util.ServerUtil.isProduction()
				|| com.bfrc.framework.util.ServerUtil.isBSRODev())
			com.bfrc.framework.util.ServerUtil.forceHttps(request, response);
		if (!new Integer(LOGGED_IN).equals(session.getAttribute(KEY)))
			return false;
		return true;
	}

	public static boolean isAppAllowed(HttpSession session, int appId) {
		GlobalAdmin admin = (GlobalAdmin) session.getAttribute(USER_KEY);
		// --- 20091130 releases: add validation by CS --//
		if (admin == null)
			return false;
		Set s = admin.getAdminApps();
		Iterator iter = s.iterator();
		boolean allowed = false;
		while (iter.hasNext()) {
			AdminApp a = (AdminApp) iter.next();
			if ((appId + "").equals(a.getApplicationId() + ""))
				allowed = true;
		}
		return allowed;
	}
	
	public static Long getAdminId(HttpSession session) {
		GlobalAdmin admin = (GlobalAdmin) session.getAttribute(USER_KEY);

		if (admin == null) {
			return null;
		}
		return admin.getUserId();

	}

	public boolean checkAdmin(ServletContext application,
			HttpServletRequest request) {

		// check remote ip

		IpAccessDAO ipDAO = (IpAccessDAO) Config.locate(application,
				"ipAccessDAO");
		String remoteIP = request.getRemoteAddr();
		String forwardedFor = request.getHeader("X-Forwarded-For");
		AdminIpAddress allowableIp = ipDAO.checkIpAddress(remoteIP);
		boolean allowed = false;
		if (allowableIp != null && allowableIp.getIpAddress().length() > 0)
			allowed = true;
		else if(forwardedFor!=null&&forwardedFor.length()>0){
			AdminIpAddress allowableForwardedForIp = ipDAO.checkIpAddress(forwardedFor);
			if (allowableForwardedForIp != null && allowableForwardedForIp.getIpAddress().length() > 0)
				allowed = true;
			else
				allowed = false;
		}

		return allowed;
	}

	public static void sendAdminPassword(String email,
			ServletContext application, String emailFile) {
		// user.setDefaultFlag(new Boolean(true));
		Config thisConfig = Config.locate(application);
		AdminDAO adminDao = (AdminDAO) Config.locate(application, "adminDAO");
		ContactDAO contactDAO = (ContactDAO) Config.locate(application,
				"contactDAO");
		GlobalAdmin globalAdmin = adminDao.fetchByEmail(email);

		if (globalAdmin == null)
			return;
		globalAdmin.setToken(StringUtils.generatePassword());
		globalAdmin.setPasswordReset(true);
		adminDao.update(globalAdmin);
		sendPasswordEmail(application, emailFile, thisConfig, contactDAO,
				globalAdmin, LOGIN_URL + "?token=" + globalAdmin.getToken());
	}

	public static void sendPromoDenialEmail(
			StoreAdminPromotion storeAdminPromotion,
			ServletContext application, String emailFile) {
		// user.setDefaultFlag(new Boolean(true));
		Config thisConfig = Config.locate(application);
		Map data = new HashMap();
		data.put("FIRST_NAME", storeAdminPromotion.getCreatedBy());
		data.put("EMAIL_ADDRESS", storeAdminPromotion.getCreatedByEmail());
		data.put("LINK", LOGIN_URL);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String dates = "";
		try {
			dates = "Availability Online: "
					+ sdf.format(storeAdminPromotion.getOnlineStartDate())
					+ " - "
					+ sdf.format(storeAdminPromotion.getOnlineEndDate());
			dates += "<br/><br/>Valid in Store: "
					+ sdf.format(storeAdminPromotion.getStartDate()) + " - "
					+ sdf.format(storeAdminPromotion.getEndDate());
		} catch (Exception e) {
		}
		String description = storeAdminPromotion.getDescription() != null ? storeAdminPromotion
				.getDescription() : "";
		data.put(
				"PROMO_DETAILS",
				"Title: " + storeAdminPromotion.getTitle() + "<br/><br/>"
						+ "Description: " + description + "<br/><br/>" + dates
						+ "<br/><br/>Request Reason: "
						+ storeAdminPromotion.getRequestReason());

		data.put("COMMENTS", storeAdminPromotion.getDenyReason());
		String message = ServerUtil.populateEmailMessage(emailFile, data);
		MailManager mailManager = (MailManager) Config.locate(application,
				"mailManager");
		ContactDAO contactDAO = (ContactDAO) Config.locate(application,
				"contactDAO");
		try {
			Mail mail = new Mail();
			mail.setTo(new String[] { storeAdminPromotion.getCreatedByEmail() });
			mail.setSubject("Promotion Request Status(Denied)");
			String from = contactDAO.getFrom();
			mail.setFrom(from);
			mail.setHtml(true);
			mail.setBody(message);
			mailManager.sendMail(mail);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void sendPromoApprovalEmail(
			StoreAdminPromotion storeAdminPromotion,
			ServletContext application, String emailFile) {
		// user.setDefaultFlag(new Boolean(true));
		Config thisConfig = Config.locate(application);

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String dates = "";
		try {
			dates = "<b>Availability Online:</b> "
					+ sdf.format(storeAdminPromotion.getOnlineStartDate())
					+ " - "
					+ sdf.format(storeAdminPromotion.getOnlineEndDate());
			dates += "<br/><br/><b>Valid in Store:</b> "
					+ sdf.format(storeAdminPromotion.getStartDate()) + " - "
					+ sdf.format(storeAdminPromotion.getEndDate());
		} catch (Exception e) {
		}
		String createdBy = storeAdminPromotion.getCreatedBy() != null ? "<br/><b>Created By:</b> "
				+ storeAdminPromotion.getCreatedBy()
				+ " - "
				+ storeAdminPromotion.getCreatedByEmail() + "<br/><br/>"
				: "";
		String approvedBy = storeAdminPromotion.getModifiedBy() != null ? "<b>Approved By:</b> "
				+ storeAdminPromotion.getModifiedBy() + "<br/><br/>"
				: "";

		String description = storeAdminPromotion.getDescription() != null ? storeAdminPromotion
				.getDescription() : "";

		String offers = "";
		try {
			if (storeAdminPromotion.getOffers() != null
					&& storeAdminPromotion.getOffers().size() > 0) {

				StoreAdminDAO storeAdminDAO = (StoreAdminDAO) Config.locate(
						application, "storeAdminDAO");
				LinkedHashMap catMap = (LinkedHashMap) storeAdminDAO
						.getMappedOfferCategories();
				LinkedHashMap templateMap = (LinkedHashMap) storeAdminDAO
						.getMappedTemplates();
				List beanOffers = new ArrayList(storeAdminPromotion.getOffers());
				beansFor: for (int i = 0; i < beanOffers.size(); i++) {
					StoreAdminOffer storeAdminOffer = (StoreAdminOffer) beanOffers
							.get(i);
					StoreAdminOfferTemplate aot = (StoreAdminOfferTemplate) templateMap
							.get(storeAdminOffer.getTemplateId().toString());
					if (aot == null || aot.getCategoryId() == null)
						continue beansFor;
					StoreAdminOfferCategory aoc = (StoreAdminOfferCategory) catMap
							.get(aot.getCategoryId().toString());
					offers += "<br/>" + aoc.getName() + " - " + aot.getName();
				}
			}
		} catch (Exception e) {

		}
		String stores = "";
		try {
			stores = createStoresListHtml(application,storeAdminPromotion);
		}catch (Exception e) {

		}
		String storesString= stores.length() > 0 ? "<br/><br/><b>Store(s):</b> "
				+ stores + "<br/>"
				: "";
		String offersString = offers.length() > 0 ? "<br/><br/><b>Offer Name(s):</b> "
				+ offers + "<br/>"
				: "";
		Map<String, String> data = new HashMap<String, String>();
		data.put("LINK", LOGIN_URL);
		data.put("PROMO_DETAILS", createdBy + approvedBy + "<b>Brand:</b> "
				+ storeAdminPromotion.getBrand() + offersString +storesString
				+ "<br/><br/><b>Title:</b> " + storeAdminPromotion.getTitle()
				+ "<br/><br/>" + "<b>Description:</b> " + description + "<br/>"
				+ dates);
		try {
			if (storeAdminPromotion.getCreatedByEmail() != null)
				sendApprovedEmail(storeAdminPromotion.getCreatedByEmail(),
						storeAdminPromotion.getCreatedBy(), emailFile, data,
						application, storeAdminPromotion.getTitle());

			AdminDAO adminDao = (AdminDAO) Config.locate(application,
					"adminDAO");
			List<GlobalAdmin> globalAdmins = adminDao
					.getAllAdminWithEmail(storeAdminPromotion.getBrand());
			if (globalAdmins != null)
				for (GlobalAdmin ga : globalAdmins) {
					if (!ga.getEmailAddress().equals(
							storeAdminPromotion.getCreatedByEmail()))
						sendApprovedEmail(ga.getEmailAddress(),
								ga.getFirstName(), emailFile, data,
								application, storeAdminPromotion.getTitle());
				}
		} catch (Exception e) {
		}

	}

	private static String createStoresListHtml(ServletContext application, StoreAdminPromotion storeAdminPromotion){
		

	    StoreDAO storeDAO = (StoreDAO)Config.locate(application, "storeDAO");
	    ZoneDAO zoneDAO = (ZoneDAO)Config.locate(application, "zoneDAO");
	    DistrictDAO districtDAO = (DistrictDAO)Config.locate(application, "districtDAO");
	    List zones = new ArrayList();
	    List districts = new ArrayList();
	   
	         zones = zoneDAO.getAllZones();
	    for (int i = 0; i<zones.size();i++){
	        ZoneList zone = (ZoneList) zones.get(i);
	    }

	    String storeList = "";
	    // if we put an error message in storeList, don't bother trying to get the real store list
	    if (storeAdminPromotion!=null && storeAdminPromotion.getPromotionId()>0){
	        StoreAdminDAO storeAdminDAO = (StoreAdminDAO)Config.locate(application, "storeAdminDAO");
	        List stores = storeAdminDAO.findNationalPromotionStoresByPromoId(storeAdminPromotion.getPromotionId()+"");
	        boolean dispZone = false;
	        boolean dispDist = false;
	        boolean first = true;
	        // SuperUser
	        for (int i = 0; i < zones.size(); i++){
	            ZoneList zone = (ZoneList) zones.get(i);
	            dispZone = false;
	            districts = districtDAO.getDistrictIdsByZone(zone.getZoneId().toString());
	            for (int j = 0; j < districts.size(); j++){
	                HrDistricts district = (HrDistricts) districts.get(j);
	                dispDist = false;
	                for (int k=0; k < stores.size(); k++){
	                    Store store = (Store)stores.get(k);
	                    if (district.getDistrictId().equals(store.getDistrict().getDistrictId())){
	                        if (!dispZone){
	                            dispZone = true;
	                            if(first){
		                            storeList += "<div><ul>";
		                            first=false;
		                            }
		                         storeList += "<li><span class=\"folder\">Zone - "+zone.getZoneName()+"</span><ul>";
	                           
	                        }
	                        if (!dispDist){
	                            dispDist = true;
	                            storeList += "<li><span class=\"folder\">District - "+district.getDistrictName()+" - "+district.getDistrictId()+"</span><ul>";
	                        }
	                        storeList += "<li><span class=\"file\">Store #"+store.getNumber()+" "+store.getAddress()+"</span></li>";
	                    }
	                }
	                if (dispDist)
	                    storeList += "</ul></li>";
	            }
	            if (dispZone){
	                 storeList += "</ul></li>";
	            }
	        }
            if(!first){ storeList +="</ul></div>";}
	  }
		return storeList;
		
	}
	private static void sendApprovedEmail(String toEmail, String toName,
			String emailFile, Map<String, String> data,
			ServletContext application, String title) {

		MailManager mailManager = (MailManager) Config.locate(application,
				"mailManager");
		ContactDAO contactDAO = (ContactDAO) Config.locate(application,
				"contactDAO");
		try {
			Mail mail = new Mail();
			mail.setTo(new String[] { toEmail });
			mail.setSubject("New Store Level Promotion - " + title);
			String from = contactDAO.getFrom();
			mail.setFrom(from);
			mail.setHtml(true);
			data.put("FIRST_NAME", toName);
			data.put("EMAIL_ADDRESS", toEmail);
			String message = ServerUtil.populateEmailMessage(emailFile, data);
			mail.setBody(message);
			mailManager.sendMail(mail);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void sendPasswordEmail(ServletContext application,
			String emailFile, Config thisConfig, ContactDAO contactDAO,
			GlobalAdmin globalAdmin, String link) {
		Map data = new HashMap();
		data.put("FIRST_NAME", globalAdmin.getFirstName());
		data.put("EMAIL_ADDRESS", globalAdmin.getEmailAddress());
		data.put("NAME", globalAdmin.getName());
		if (link == null || link.length() == 0)
			link = LOGIN_URL;
		data.put("LINK", link);
		String message = ServerUtil.populateEmailMessage(emailFile, data);
		MailManager mailManager = (MailManager) Config.locate(application,
				"mailManager");

		try {
			Mail mail = new Mail();
			mail.setTo(new String[] { globalAdmin.getEmailAddress() });
			mail.setSubject(thisConfig.getSiteFullName() + " -  Password");
			String from = contactDAO.getFrom();
			mail.setFrom(from);
			mail.setHtml(true);
			mail.setBody(message);
			mailManager.sendMail(mail);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean signUpAdmin(String email, ServletContext application,
			String emailFile) {
		// user.setDefaultFlag(new Boolean(true));
		Config thisConfig = Config.locate(application);
		AdminDAO adminDao = (AdminDAO) Config.locate(application, "adminDAO");
		ContactDAO contactDAO = (ContactDAO) Config.locate(application,
				"contactDAO");
		GlobalAdmin globalAdmin = adminDao.fetchByEmail(email);
		// already exists, change password and send again
		if (globalAdmin != null) {
			sendAdminPassword(email, application, emailFile);
			return true;
		}
		BsroCorpUsersDAO corpUserDAO = (BsroCorpUsersDAO) Config.locate(
				application, "bsroCorpUsersDAO");
		BsroCorpUsers corpUser = corpUserDAO.getCorpUserByEmail(email);
		if (corpUser != null) {
			return buildNewGlobalAdmin(application, emailFile, thisConfig,
					adminDao, contactDAO, corpUser.getFirstName() + " "
							+ corpUser.getLastName(),
					corpUser.getEmailAddress(), true);
		}
		ZoneDAO zoneDao = (ZoneDAO) Config.locate(application, "zoneDAO");
		ZoneManager zoneManager = zoneDao.getZoneManagerByEmail(email);
		if (zoneManager != null && zoneManager.getId() != null) {
			return buildNewGlobalAdmin(application, emailFile, thisConfig,
					adminDao, contactDAO, zoneManager.getName(),
					zoneManager.getEmailAddress(), false);
		}
		DistrictDAO districtDao = (DistrictDAO) Config.locate(application,
				"districtDAO");
		DistrictManager districtManager = districtDao
				.getDistrictManagerByEmail(email);
		if (districtManager != null && districtManager.getId() != null) {
			return buildNewGlobalAdmin(application, emailFile, thisConfig,
					adminDao, contactDAO, districtManager.getId().getName(),
					districtManager.getEmailAddress(), false);
		}
		return false;
	}

	private static boolean buildNewGlobalAdmin(ServletContext application,
			String emailFile, Config thisConfig, AdminDAO adminDao,
			ContactDAO contactDAO, String name, String emailAddress,
			boolean superUser) {
		try {
			GlobalAdmin newGlobalAdmin = new GlobalAdmin();
			newGlobalAdmin.setCreatedDate(new Date());
			newGlobalAdmin.setEmailAddress(emailAddress);
			if (superUser) {
				newGlobalAdmin.setPermissionLevel("SU");
			}
			String[] names = name.split(" ");
			if (names.length > 1) {
				newGlobalAdmin.setFirstName(names[0]);
				newGlobalAdmin.setLastName(names[1]);
			} else
				newGlobalAdmin.setFirstName(name);
			newGlobalAdmin.setName(name.replace(" ", "").toLowerCase());
			newGlobalAdmin.setToken(StringUtils.generatePassword());
			newGlobalAdmin.setPassword(StringUtils.generatePassword());
			newGlobalAdmin.setPasswordReset(true);
			adminDao.save(newGlobalAdmin);
			sendPasswordEmail(application, emailFile, thisConfig, contactDAO,
					newGlobalAdmin,
					LOGIN_URL + "?token=" + newGlobalAdmin.getToken());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean saveNewPassword(String emailAddress, String token,
			String newPassword, ServletContext application) {
		// TODO make sure the new password passes validation.
		try {
			AdminDAO adminDao = (AdminDAO) Config.locate(application,
					"adminDAO");
			GlobalAdmin globalAdmin = adminDao.fetchByEmail(emailAddress);
			if (globalAdmin != null) {
				if (globalAdmin.getToken().equals(token)) {
					globalAdmin.setPassword(newPassword);
					globalAdmin.setPasswordReset(false);
					adminDao.update(globalAdmin);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
