package com.bfrc;

import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import map.States;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bfrc.framework.dao.AdminDAO;
import com.bfrc.framework.dao.AppointmentDAO;
import com.bfrc.framework.dao.BsroCorpUsersDAO;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.CustomerDAO;
import com.bfrc.framework.dao.DistrictDAO;
import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.framework.dao.EventDAO;
import com.bfrc.framework.dao.GeoDataDAO;
import com.bfrc.framework.dao.GrandOpeningDAO;
import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.RealEstateDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.StoreScheduleDAO;
import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.framework.dao.UserDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.ZoneDAO;
import com.bfrc.framework.dao.appointment.ListAppointmentTimesOperator;
import com.bfrc.framework.dao.catalog.ViewProductOperator;
import com.bfrc.framework.dao.district.DistrictInfoOperator;
import com.bfrc.framework.dao.district.ListEventsOperator;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.framework.dao.pricing.PricingOperator;
import com.bfrc.framework.dao.pricing.ProductListOperator;
import com.bfrc.framework.dao.pricing.SegmentListOperator;
import com.bfrc.framework.dao.store.GeocodeOperator;
import com.bfrc.framework.dao.store.ListStoresOperator;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.dao.store.MapOperator;
import com.bfrc.framework.dao.vehicle.ListVehiclesOperator;
import com.bfrc.pojo.User;
import com.bfrc.pojo.contact.Feedback;
import com.bfrc.pojo.store.Store;
import com.hibernate.dao.base.BaseDao;

/**
 * @author jfang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Config extends Base {
	public final static String BFRC = "BFRC";
	public final static String ET = "ET";
	public final static String ETIRE = "ETIRE";
	public final static String FANEUIL = "fan";
	public final static String FCAC = "FCAC";
	public final static String FCFC = "FFC";
	public final static String PPS = "PPS";
	public final static String TP = "TP";
	public final static String WWT = "WWT";
	public final static String HT = "HT";
	public final static String FIVESTAR = "5STR";
	public final static String PWT = "PWT";
	private Integer maxLogoWidth = Integer.valueOf("110");
	private Integer maxLogoHeight = Integer.valueOf("45");
	private Integer thumbnailQuality = Integer.valueOf("95");
	private String siteName;
	private String siteFullName;
	private String contactUs = "CONTACT US";
	private String customerService = "Customer Service Comments";
	private String appointments = "Online Appointments";
	private String tires = "Questions About Tires";
	private String to = "TO";
	private String cc = "CC";
	private String bcc = "BCC";
	private LocatorConfig locator = new LocatorConfig();

	public int getUserType() {
		if (FCAC.equals(siteName)) {
			return User.FCAC;
		} else if (TP.equals(siteName)) {
			return User.TP;
		} else if (ET.equals(siteName)) {
			return User.ET;
		} else if (HT.equals(siteName)) {
			return User.HT;
		} else if (FCFC.equals(siteName)) {
			return User.FCFC;
		} else if (ETIRE.equals(siteName)) {
			return User.ETIRE;
		} else if (PPS.equals(siteName)) {
			return User.PPS;
		}
		return User.DEFAULT;
	}

	public LocatorConfig getLocator() {
		return this.locator;
	}

	public void setLocator(LocatorConfig locator) {
		this.locator = locator;
	}

	public static Config locate(WebApplicationContext ctx) {
		return (Config) locate(ctx, "config");
	}

	public static Object locate(WebApplicationContext ctx, String name) {
		return ctx.getBean(name);
	}

	private static Config config;

	public static Config locate(Servlet s) {
		if (config == null) {
			config = (Config) locate(s, "config");
		}
		return config;
	}

	public static Object locate(Servlet s, String name) {
		// pulls named bean out of root Spring web app context
		// will throw IllegalStateException if the context is not yet
		// initialized
		WebApplicationContext ctx = getCtx(s);
		return locate(ctx, name);
	}

	public static Config locate(ServletContext s) {
		return (Config) locate(s, "config");
	}

	public static Object locate(ServletContext s, String name) {
		WebApplicationContext ctx = getCtx(s);
		return locate(ctx, name);
	}

	public static WebApplicationContext getCtx(Servlet s) {
		return getCtx(s.getServletConfig().getServletContext());
	}

	public static WebApplicationContext getCtx(ServletContext s) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(s);
	}

	/**
	 * @return Returns the bcc.
	 */
	public String getBcc() {
		return this.bcc;
	}

	/**
	 * @param bcc
	 *            The bcc to set.
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return Returns the cc.
	 */
	public String getCc() {
		return this.cc;
	}

	/**
	 * @param cc
	 *            The cc to set.
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return Returns the contactUs.
	 */
	public String getContactUs() {
		return this.contactUs;
	}

	/**
	 * @param contactUs
	 *            The contactUs to set.
	 */
	public void setContactUs(String contactUs) {
		this.contactUs = contactUs;
	}

	/**
	 * @return Returns the customerService.
	 */
	public String getCustomerService() {
		return this.customerService;
	}

	/**
	 * @param customerService
	 *            The customerService to set.
	 */
	public void setCustomerService(String customerService) {
		this.customerService = customerService;
	}

	/**
	 * @return Returns the siteFullName.
	 */
	public String getSiteFullName() {
		return this.siteFullName;
	}

	/**
	 * @param siteFullName
	 *            The siteFullName to set.
	 */
	public void setSiteFullName(String siteFullName) {
		this.siteFullName = siteFullName;
	}

	/**
	 * @return Returns the siteName.
	 */
	public String getSiteName() {
		return this.siteName;
	}

	/**
	 * @param siteName
	 *            The siteName to set.
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return this.to;
	}

	/**
	 * @param to
	 *            The to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}

	public String getAppointments() {
		return appointments;
	}

	public void setAppointments(String appointments) {
		this.appointments = appointments;
	}

	public String getTires() {
		return tires;
	}

	public void setTires(String tires) {
		this.tires = tires;
	}

	public Integer getMaxLogoWidth() {
		return maxLogoWidth;
	}

	public void setMaxLogoWidth(Integer maxLogoWidth) {
		this.maxLogoWidth = maxLogoWidth;
	}

	public Integer getMaxLogoHeight() {
		return maxLogoHeight;
	}

	public void setMaxLogoHeight(Integer maxLogoHeight) {
		this.maxLogoHeight = maxLogoHeight;
	}

	public Integer getThumbnailQuality() {
		return thumbnailQuality;
	}

	public void setThumbnailQuality(Integer thumbnailQuality) {
		this.thumbnailQuality = thumbnailQuality;
	}

	public static String getStoreWebSource(Store store) {
		if (store == null ||store.getType() == null)
			return Config.FCAC;
		String type = store.getType().trim();
		if (type.startsWith(Config.TP))
			return Config.TP;
		else if (type.startsWith(Config.HT))
			return Config.HT;
		else if (type.startsWith("WW"))
			return Config.WWT;
		else if (type.startsWith("PW"))
			return Config.PWT;
		else
			return Config.FCAC;
	}

	public static String getContactDistributionWebSite(String storeType) {
		if (storeType == null)
			return Config.FCAC;
		String type = storeType.trim();
		if (type.startsWith(Config.TP))
			return Config.TP;
		else if (type.startsWith(Config.HT))
			return Config.TP;
		else if (type.startsWith("WW"))
			return Config.TP;
		else if (type.startsWith("PW"))
			return Config.PWT;
		else if (type.startsWith(Config.ET))
			return Config.ET;
		else if (type.startsWith(Config.FCFC))
			return Config.FCFC;
		else
			return Config.FCAC;
	}

	// ----- First Logic New -----//
	// private static String host;
	// private static String suffix;
	// private static int port;
	public static String getPrdFirstLogicHost() {
		return "mktds-ak-bfr";
	}

	public static String getDevFirstLogicHost() {
		return "mktfldev-ch-bfr";
	}

	public static int getFirstLogicPort() {
		return 80;
	}

	public static boolean isComplimentFeedBack(String s) {
		return (s != null && s.toLowerCase().indexOf("compliment") >= 0);
	}

	public static boolean isCustomerServiceFeedback(ContactDAO contactDAO,
			Config config, String s) {
		return isCustomerServiceFeedback(contactDAO.getAllFeedBacks(), config,
				s);
	}

	public static boolean isCustomerServiceFeedback(List feedbacks,
			Config config, String s) {
		// List l = contactDAO.getAllFeedBacks();
		if (feedbacks != null) {
			for (int i = 0; i < feedbacks.size(); i++) {
				Feedback item = (Feedback) feedbacks.get(i);
				if (item.getSubject().equalsIgnoreCase(s)) {
					if (item.getFeedbackId() == 1
							&& Config.FCAC.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 21
							&& Config.ET.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 44
							&& Config.FCFC.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 71
							&& Config.TP.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 121
							&& Config.HT.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 141
							&& Config.ETIRE.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 161
							&& Config.FIVESTAR.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					}
				}
			}

		}
		return false;
	}

	public static boolean isComplimentFeedback(ContactDAO contactDAO,
			Config config, String s) {
		return isComplimentFeedback(contactDAO.getAllFeedBacks(), config, s);
	}

	public static boolean isComplimentFeedback(List feedbacks, Config config,
			String s) {
		// List l = contactDAO.getAllFeedBacks();
		if (feedbacks != null) {
			for (int i = 0; i < feedbacks.size(); i++) {
				Feedback item = (Feedback) feedbacks.get(i);
				if (item.getSubject().equalsIgnoreCase(s)) {
					if (item.getFeedbackId() == 19
							&& Config.FCAC.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 39
							&& Config.ET.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 89
							&& Config.TP.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 139
							&& Config.HT.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 149
							&& Config.ETIRE.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 169
							&& Config.FIVESTAR.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					}
				}
			}

		}
		return false;
	}

	public static boolean isAppointmentsFeedBack(ContactDAO contactDAO,
			Config config, String s) {
		return isAppointmentsFeedBack(contactDAO.getAllFeedBacks(), config, s);
	}

	public static boolean isAppointmentsFeedBack(List feedbacks, Config config,
			String s) {
		if (feedbacks != null) {
			for (int i = 0; i < feedbacks.size(); i++) {
				Feedback item = (Feedback) feedbacks.get(i);
				if (item.getSubject().equalsIgnoreCase(s)) {
					if (item.getFeedbackId() == 31
							&& Config.ET.equalsIgnoreCase(config.getSiteName())) {
						return true;
					}
				}
			}

		}
		return false;
	}

	public static boolean isQuestionAboutTireFeedback(ContactDAO contactDAO,
			Config config, String s) {
		return isQuestionAboutTireFeedback(contactDAO.getAllFeedBacks(),
				config, s);
	}

	public static boolean isQuestionAboutTireFeedback(List feedbacks,
			Config config, String s) {
		// List l = contactDAO.getAllFeedBacks();
		if (feedbacks != null) {
			for (int i = 0; i < feedbacks.size(); i++) {
				Feedback item = (Feedback) feedbacks.get(i);
				if (item.getSubject().equalsIgnoreCase(s)) {
					if (item.getFeedbackId() == 3
							&& Config.FCAC.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 23
							&& Config.ET.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 47
							&& Config.FCFC.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 73
							&& Config.TP.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 123
							&& Config.HT.equalsIgnoreCase(config.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 143
							&& Config.ETIRE.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					} else if (item.getFeedbackId() == 163
							&& Config.FIVESTAR.equalsIgnoreCase(config
									.getSiteName())) {
						return true;
					}
				}
			}

		}
		return false;
	}

	public static boolean IsQuestionsAboutMaintenanceRepair(List feedbacks, Config config, String s) {
		// List l = contactDAO.getAllFeedBacks();
		if (feedbacks != null) {
			for (int i = 0; i < feedbacks.size(); i++) {
				Feedback item = (Feedback) feedbacks.get(i);
				if (item.getSubject().equalsIgnoreCase(s)) {
					if ((item.getFeedbackId() == 2 || item.getFeedbackId() == 13)  
							&& Config.FCAC.equalsIgnoreCase(config.getSiteName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// --- get all dao beans at one time ---//
	public static CustomerDAO customerDAO;
	public static EmailSignupDAO emailSignupDAO;
	public static AdminDAO adminDAO;
	public static CatalogDAO catalogDAO;
	public static ContactDAO contactDAO;
	public static DistrictDAO districtDAO;
	public static EventDAO eventDAO;
	public static LocatorDAO locatorDAO;
	public static PricingDAO pricingDAO;
	public static PromotionDAO promotionDAO;
	public static StoreDAO storeDAO;
	public static StoreScheduleDAO storeScheduleDAO;
	public static UserDAO userDAO;
	public static VehicleDAO vehicleDAO;
	public static AppointmentDAO appointmentDAO;
	public static DistrictInfoOperator districtInfo;
	public static ListEventsOperator listEvents;
	public static ListStoresOperator listStores;
	public static ListVehiclesOperator listVehicles;
	public static ListAppointmentTimesOperator listAppointmentTimes;
	public static GeoDataDAO geoDataDAO;
	public static GeocodeOperator geocodeOperator;
	public static MapOperator mapOperator;
	public static LocatorOperator locator2;
	public static PricingOperator pricing;
	public static PricingLocatorOperator pricingLocator;
	public static ProductListOperator productList;
	public static SegmentListOperator segmentList;
	public static ViewProductOperator viewProduct;
	public static States states;
	public static TirePromotionDAO tirePromotionDAO;
	public static InterstateBatteryDAO batteryDAO;
	public static RealEstateDAO realEstateDAO;

	public static BaseDao baseTpTpmsPriceDAO;
	public static GrandOpeningDAO grandOpeningDAO;
	public static ZoneDAO zoneDAO;
	public static BsroCorpUsersDAO bsroCorpUsersDAO;

	public static Config initBeans(ServletContext s) {
		locate(s);
		if (customerDAO == null)
			customerDAO = (CustomerDAO) locate(s, "customerDAO");

		if (emailSignupDAO == null)
			emailSignupDAO = (EmailSignupDAO) locate(s, "emailSignupDAO");

		if (adminDAO == null)
			adminDAO = (AdminDAO) locate(s, "adminDAO");

		if (catalogDAO == null)
			catalogDAO = (CatalogDAO) locate(s, "catalogDAO");

		if (contactDAO == null)
			contactDAO = (ContactDAO) locate(s, "contactDAO");

		if (districtDAO == null)
			districtDAO = (DistrictDAO) locate(s, "districtDAO");

		if (eventDAO == null)
			eventDAO = (EventDAO) locate(s, "eventDAO");

		if (locatorDAO == null)
			locatorDAO = (LocatorDAO) locate(s, "locatorDAO");

		if (pricingDAO == null)
			pricingDAO = (PricingDAO) locate(s, "pricingDAO");

		if (promotionDAO == null)
			promotionDAO = (PromotionDAO) locate(s, "promotionDAO");

		if (storeDAO == null)
			storeDAO = (StoreDAO) locate(s, "storeDAO");

		if (storeScheduleDAO == null)
			storeScheduleDAO = (StoreScheduleDAO) locate(s, "storeScheduleDAO");

		if (userDAO == null)
			userDAO = (UserDAO) locate(s, "userDAO");

		if (vehicleDAO == null)
			vehicleDAO = (VehicleDAO) locate(s, "vehicleDAO");

		if (appointmentDAO == null)
			appointmentDAO = (AppointmentDAO) locate(s, "appointmentDAO");

		if (districtInfo == null)
			districtInfo = (DistrictInfoOperator) locate(s, "districtInfo");

		if (listEvents == null)
			listEvents = (ListEventsOperator) locate(s, "listEvents");

		if (listStores == null)
			listStores = (ListStoresOperator) locate(s, "listStores");

		if (listVehicles == null)
			listVehicles = (ListVehiclesOperator) locate(s, "listVehicles");

		if (listAppointmentTimes == null)
			listAppointmentTimes = (ListAppointmentTimesOperator) locate(s,
					"listAppointmentTimes");

		if (geoDataDAO == null)
			geoDataDAO = (GeoDataDAO) locate(s, "geoDataDAO");

		if (geocodeOperator == null)
			geocodeOperator = (GeocodeOperator) locate(s, "geocodeOperator");

		if (mapOperator == null)
			mapOperator = (MapOperator) locate(s, "mapOperator");

		if (locator2 == null)
			locator2 = (LocatorOperator) locate(s, "locator");

		if (pricing == null)
			pricing = (PricingOperator) locate(s, "pricing");

		if (pricingLocator == null)
			pricingLocator = (PricingLocatorOperator) locate(s,
					"pricingLocator");

		if (productList == null)
			productList = (ProductListOperator) locate(s, "productList");

		if (segmentList == null)
			segmentList = (SegmentListOperator) locate(s, "segmentList");

		if (viewProduct == null)
			viewProduct = (ViewProductOperator) locate(s, "viewProduct");

		if (states == null)
			states = (States) locate(s, "states");

		if (tirePromotionDAO == null)
			tirePromotionDAO = (TirePromotionDAO) locate(s, "tirePromotionDAO");

		if (batteryDAO == null)
			batteryDAO = (InterstateBatteryDAO) locate(s, "batteryDAO");

		if (realEstateDAO == null)
			realEstateDAO = (RealEstateDAO) locate(s, "realEstateDAO");

		if (baseTpTpmsPriceDAO == null)
			baseTpTpmsPriceDAO = (BaseDao) locate(s, "baseTpTpmsPriceDAO");

		if (grandOpeningDAO == null)
			grandOpeningDAO = (GrandOpeningDAO) locate(s, "grandOpeningDAO");

		if (zoneDAO == null)
			zoneDAO = (ZoneDAO) locate(s, "zoneDAO");

		if (bsroCorpUsersDAO == null)
			bsroCorpUsersDAO = (BsroCorpUsersDAO) locate(s, "bsroCorpUsersDAO");

		return config;
	}

	public static Config initBeans(Servlet s) {
		return initBeans(s.getServletConfig().getServletContext());
	}

	public static Config initBeans(HttpServletRequest request) {
		return initBeans(request.getSession().getServletContext());
	}
}