package com.bfrc.dataaccess.svc.mail;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import map.States;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.FeedbackDAO;
import com.bfrc.dataaccess.dao.generic.StoreDAO;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.appointment.AppointmentChoice;
import com.bfrc.dataaccess.model.contact.Feedback;
import com.bfrc.dataaccess.model.vehicle.GenericVehicle;
import com.bfrc.dataaccess.svc.vehicle.InterstateBatteryService;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.svc.webdb.StoreService;
import com.bfrc.dataaccess.svc.webdb.TireQuoteService;
import com.bfrc.dataaccess.util.Sites;
import com.bfrc.dataaccess.util.Sites.SiteTypes;
import com.bfrc.framework.dao.FleetCareDAO;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.alignment.AlignmentPricingQuote;
import com.bfrc.pojo.pricing.TpArticleLog;
import com.bfrc.pojo.pricing.TpUserLog;
import com.bfrc.pojo.fleetcare.NaApplication;
import com.bfrc.pojo.interstatebattery.InterstateBatteryQuote;
import com.bfrc.pojo.fleetcare.NaManager;

import app.bsro.model.battery.BatteryQuote;
import app.bsro.model.error.Errors;
import app.bsro.model.tire.QuoteItem;
import app.bsro.model.tire.Tire;
import app.bsro.model.tire.TireQuote;
import app.bsro.model.tire.TireSize;
import app.bsro.model.tire.VehicleFitment;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.map.ObjectMapper;
import com.bfrc.framework.dao.ContactDAO;

/**
 * @author smoorthy 
 *
 */

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private States statesMap;
	
	@Autowired
	TireQuoteService tireQuoteService;
	
	@Autowired
	InterstateBatteryService interstateBatteryService;
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FeedbackDAO feedbackDao;
	
	@Autowired
	private PricingDAO pricingDAO;
	
	@Autowired
	private InterstateBatteryDAO batteryDAO;
	
	@Autowired
	private ContactDAO contactDAO;
	
	@Autowired
	FleetCareDAO fleetCareDAO;
	
	@Resource(name="emailSender")
	private JavaMailSender mailSender;
	
	private String siteName;
	private MailMessage mailMessage;
	public static final String QUOTE_SOURCE_TIRE = "tire";
	public static final String QUOTE_SOURCE_BATTERY = "battery";
	public static final String QUOTE_SOURCE_ALIGNMENT = "alignment";
	public static final String TIRE_REPLACEMENT_CHOICE_ID = "2745";
	public static final String MVAN_TIRE_REPLACEMENT_CHOICE_ID = "2782";
	public static final String BATTERY_CHOICE_ID = "2750";
	public static final String ALIGNMENT_CHOICE_ID = "2749";
	
	public MailMessage getInstance() {
		if (SiteTypes.TP.getSiteType().equalsIgnoreCase(getSiteName())){
			return new TPMailMessage();
		}else if (SiteTypes.WWT.getSiteType().equalsIgnoreCase(getSiteName())) {
			return new WWTMailMessage();
		}else if (SiteTypes.HT.getSiteType().equalsIgnoreCase(getSiteName())){
			return new HTMailMessage();
		}
		return new FCACMailMessage(); 
	}
	
	private void saveTireQuoteEMail(Long quoteId, String firstName, String lastName, String emailAddress){
		 if (quoteId != null && Long.parseLong(""+quoteId) > 0L) {
			 	TpArticleLog tpArticleLog = null;
				TpUserLog tpUserLog = null;
		    	tpArticleLog = pricingDAO.findTpArticleLogById(quoteId);
		    	tpUserLog = pricingDAO.findTpUserLogByUserId(new Long(tpArticleLog.getTpUserId()));
		    	if (tpUserLog != null) {
		    		try {
			            tpUserLog.setFirstName(firstName);
			            tpUserLog.setLastName(lastName);
			            tpUserLog.setEmailAddress(emailAddress);
			            pricingDAO.updateTpUserLog(tpUserLog);
		    		} catch (Exception e) {
						e.printStackTrace();
					}
		        }
		 }
	}
	
	private void saveBatteryQuoteEMail(Long quoteId, String firstName, String lastName, String emailAddress){
		 if (quoteId != null && Long.parseLong(""+quoteId) > 0L) {
			 InterstateBatteryQuote interstateBatteryQuote = batteryDAO.getInterstateBatteryQuote(quoteId);
				 try {	
					 interstateBatteryQuote.setFirstName(firstName);			
					 interstateBatteryQuote.setLastName(lastName);
					 interstateBatteryQuote.setEmailAddress(emailAddress);
						batteryDAO.updateInterstateBatteryQuote(interstateBatteryQuote);
					} catch (Exception e) {
						e.printStackTrace();
					}
		      }
		 }
	
	private void saveAlignmentQuoteEMail(Long quoteId, String firstName, String lastName, String emailAddress){
		 if (quoteId != null && Long.parseLong(""+quoteId) > 0L) {
			 try {  
				 AlignmentPricingQuote alignmentQuote = pricingDAO.getAlignmentQuotebyQuoteId(quoteId);
			 	alignmentQuote.setFirstName(firstName);
			 	alignmentQuote.setLastName(lastName);
			 	alignmentQuote.setEmailAddress(emailAddress);
		        pricingDAO.updateAlignmentPricingQuote(alignmentQuote);
			 } catch (Exception e) {
					e.printStackTrace();
				}
		       
		 }
	}
	
	public BSROWebServiceResponse postEmailQuote(String source, String siteName, Long quoteId,
			String rebateId, String firstName, String lastName, String emailAddress) {
		BSROWebServiceResponse response = null;
		String mailBody = null;
		boolean mailSent = false;
		setSiteName(siteName);
		switch (getSourceType(source)) {
			case 1:
				BSROWebServiceResponse tireQuote = tireQuoteService.getQuote(quoteId);
				TireQuote tireQuote2 = null;
				if(tireQuote.getPayload() != null && tireQuote.getPayload().getClass() != TireQuote.class){
					JsonNode jsonNode = (JsonNode)tireQuote.getPayload();
					ObjectNode o = (ObjectNode) jsonNode;
					o.put("firstName", firstName);
					o.put("lastName", lastName);
					o.put("emailAddress", emailAddress);
					tireQuoteService.updateQuote(quoteId,jsonNode);
					try{
						ObjectMapper objectMapper = new ObjectMapper();
						tireQuote2 = objectMapper.readValue(jsonNode, TireQuote.class);
						tireQuote.setPayload(tireQuote2);
					}catch(IOException exception){
						exception.printStackTrace();
						response = getErrorMessage("Error while parsing JSON :" + exception);
					}
				}
				mailMessage = getInstance();
				mailBody = getTireQuoteMailMessage(tireQuote, siteName, firstName, lastName);
				if (mailBody != null) {
					mailSent = sendMail(Sites.getSiteFullName(siteName)+" Tire Quote",
							 				emailAddress, mailMessage.getEmailFrom(), true, mailBody);
				}
				if(mailSent){
					saveTireQuoteEMail(quoteId,firstName,lastName,emailAddress);
					response = getSuccessMessage("Tire quote email sent successfully");
				}else{
					response = getErrorMessage("Error while sending tire quote email.");
				}
				break;
			case 2:
				BSROWebServiceResponse batteryQuote = interstateBatteryService.getBatteryQuote(quoteId.toString(),rebateId,siteName);
				mailMessage = getInstance();
				mailBody = getBatteryQuoteMailMessage(batteryQuote, siteName, firstName, lastName);
				if (mailBody != null) {
					//System.out.println(mailBody);
					mailSent = sendMail(Sites.getSiteFullName(siteName)+" Battery Quote", 
											emailAddress, mailMessage.getEmailFrom(), true, mailBody);
				}
				
				if(mailSent){
					saveBatteryQuoteEMail(quoteId,firstName,lastName,emailAddress);
					response = getSuccessMessage("Battery quote email sent successfully");
				}else{
					response = getErrorMessage("Error while sending battery quote email.");
				}
				break;
			case 3:
				AlignmentPricingQuote alignmentQuote = tireVehicleService.getAlignmentQuote(quoteId, siteName);
				mailMessage = getInstance();
				mailBody = getAlignmentQuoteMailMessage(alignmentQuote, siteName, firstName, lastName);
				if (mailBody != null) {
					//System.out.println(mailBody);
					mailSent = sendMail(Sites.getSiteFullName(siteName)+" Alignment Quote", 
											emailAddress, mailMessage.getEmailFrom(), true, mailBody);
				}
				
				if(mailSent){
					saveAlignmentQuoteEMail(quoteId,firstName,lastName,emailAddress);
					response = getSuccessMessage("Alignment quote email sent successfully");
				}else{
					response = getErrorMessage("Error while sending alignment quote email.");
				}
				break;
			default:
				response = getErrorMessage("Invalid email quote source. Valid values [tire | battery | alignment]");
				break;
		}
		return response;
	}
	
	public void sendAppointmentRetryFailedEmail(Appointment appointment, String services) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEEE', 'MMM dd', 'yyyy' at 'hh:mm aaa");
		Integer retryFeedbackId = 191;
		String siteName="BFRC";
		String feedbackTypeName="Appointment";
		String subject = "";
		
		String from = "webmaster@bfrc.com";
		List<String> to = emailService.getToEmail(siteName, feedbackTypeName, retryFeedbackId);
		List<String> cc = emailService.getCcEmail(siteName, feedbackTypeName, retryFeedbackId);
		List<String> bcc = emailService.getBccEmail(siteName, feedbackTypeName, retryFeedbackId);
		Feedback feedback = feedbackDao.get(retryFeedbackId);
		subject = feedback.getSubject();
		
		StringBuilder sb = new StringBuilder("\nThe attempt to send information to Appointment Plus system was unsuccessful.");
		sb.append("\n\nHere are the appointment details:");
		sb.append("\n\nStore #: "+appointment.getStoreNumber());
		sb.append("\nWebSite : "+appointment.getWebSite());
		sb.append("\nSource : "+appointment.getWebSiteSource());
		sb.append("\n\nDigital Appointment Id : "+appointment.getAppointmentId());
		sb.append("\n\nCustomer First Name: "+appointment.getFirstName());
		sb.append("\nCustomer Last Name: "+appointment.getLastName());
		sb.append("\nCustomer Email: "+ appointment.getEmailAddress());
		sb.append("\nCustomer Phone: "+ appointment.getDaytimePhone());
		sb.append("\n\nVehicle(Year-Make-Model-Submodel): "+appointment.getVehicleYear()+
				"-"+appointment.getVehicleMake()+"-"+appointment.getVehicleModel()+"-"+appointment.getVehicleSubmodel());
		if(!ServerUtil.isNullOrEmpty(appointment.getMileage())){
			sb.append("\nMileage : "+appointment.getMileage());
		}
		sb.append("\nServices: "+services);
		if(!ServerUtil.isNullOrEmpty(appointment.getComments()))
				sb.append("\nComments : "+appointment.getComments());
		for(int i=0; i<appointment.getChoices().size(); i++){
			AppointmentChoice choice = (AppointmentChoice)appointment.getChoices().get(i);
			if(choice != null && choice.getDatetime()!=null)
				sb.append("\n\nTime: "+sdf.format(choice.getDatetime()));
		}
		sb.append("\n\nPlease re-schedule this appointment on behalf of the customer.");
		sb.append("The customer can be contacted for any missing information.\n");			
		sb.append("\nThanks,");
		sb.append("\nBridgestone Digital Marketing Team\n");
		
		sendMail(subject, (String[])to.toArray(new String[0]), (String[])cc.toArray(new String[0]), from, false, sb.toString());
	} 
	
	public void sendAppointmentRetryFailedCountEmail(Integer retryCount) {
		Integer retryCountFeedbackId = 192;
		String siteName="BFRC";
		String feedbackTypeName="Appointment";
		String subject = "";
		
		String from = "webmaster@bfrc.com";
		List<String> to = emailService.getToEmail(siteName, feedbackTypeName, retryCountFeedbackId);
		List<String> cc = emailService.getCcEmail(siteName, feedbackTypeName, retryCountFeedbackId);
		List<String> bcc = emailService.getBccEmail(siteName, feedbackTypeName, retryCountFeedbackId);
		Feedback feedback = feedbackDao.get(retryCountFeedbackId);
		subject = feedback.getSubject();
		
		StringBuilder sb = new StringBuilder("\nThere are "+retryCount);
		sb.append(" number of records that need to be retried and sent to Appointment Plus");
				
		sb.append("\nThanks,");
		sb.append("\nBridgestone Digital Marketing Team\n");
		
		sendMail(subject, (String[])to.toArray(new String[0]), (String[])cc.toArray(new String[0]), from, false, sb.toString());
		
	}
	
	public void sendNewFleetRegistrationEmail(NaApplication naApplication) {
		
		
		String[] tos = null;
		String[] dbTo = null;
		String[] ccAddresses = null;
		int feedbackId = 55;
		
		List l = contactDAO.getToAddresses(new Integer(feedbackId));
		dbTo = new String[l.size()];
		l.toArray(dbTo);
		l = contactDAO.getCCAddresses(new Integer(feedbackId));
		if(!ServerUtil.isNullOrEmpty(l)){
			ccAddresses = new String[l.size()];
			l.toArray(ccAddresses);
		}
		tos = dbTo;
		
		try{
			//Send E-mail
			NaManager naManager = fleetCareDAO.getManagerByState(naApplication.getCompanyState()); 
	
			//Mail mail = new Mail();
			String[] nato = new String[] {naManager.getAccountManagerEmailAddress()};
			if(nato != null && dbTo != null && "NA_MANAGER".equalsIgnoreCase(dbTo[0])){
				tos = nato;
			}

			String from = naApplication.getApplcntFirstName() + " " + naApplication.getApplcntLastName() + "<" + naApplication.getApplcntEmail() + ">";
			String subject = contactDAO.getSubject(new Integer(feedbackId));
			StringBuffer body = new StringBuffer();
			body.append("The New Fleet Registration information was submitted to the FirestoneCompleteFleetcare.com website.\r\n\r\n");
			
			//Company Info
			body.append("Company Information\r\n");
			body.append("-------------------\r\n");
			body.append("Company Name: " + naApplication.getCompanyName() + "\r\n");
			if (naApplication.getParentCompanyName() != null && !"".equals(naApplication.getParentCompanyName()))
				body.append("Subsidiary Of: " + naApplication.getParentCompanyName() + "\r\n");
			body.append("Type of Business: " + naApplication.getNatureOfBusiness() + "\r\n");
			body.append("Address: " + naApplication.getCompanyAddress1() + "\r\n");
			if (naApplication.getCompanyAddress2() != null && !"".equals(naApplication.getCompanyAddress2()))
				body.append("Address Line 2: " + naApplication.getCompanyAddress2() + "\r\n");
			body.append("City: " + naApplication.getCompanyCity() + "\r\n");
			body.append("State: " + naApplication.getCompanyState() + "\r\n");
			body.append("ZIP Code: " + naApplication.getCompanyZipCode() + "\r\n");
			body.append("Phone Number: " + naApplication.getCompanyPhoneNumber() + "\r\n");
			if (naApplication.getCompanyFaxNumber() != null && !"".equals(naApplication.getCompanyFaxNumber()))
				body.append("Fax Number: " + naApplication.getCompanyFaxNumber() + "\r\n");
			
			//Contact Info
			body.append("\r\nContact Information\r\n");
			body.append("-------------------\r\n");
			body.append("First Name: " + naApplication.getApplcntFirstName() + "\r\n");
			body.append("Middle Initial: " + naApplication.getApplcntMiddleInitial() + "\r\n");
			body.append("Last Name: " + naApplication.getApplcntLastName() + "\r\n");
			body.append("E-mail Address: " + naApplication.getApplcntEmail() + "\r\n");
			body.append("Phone Number: " + naApplication.getApplcntPhoneNumber() + "\r\n");
			body.append("Fax Number: " + naApplication.getApplcntFaxNumber() + "\r\n");

			if(naApplication.isOtherAccounts())
				body.append("Other National Account Programs: " + naApplication.getOtherNationalAccntDesc() + "\r\n");
			
			if ("C".equals(naApplication.getPurchasingMngmntType()))
				body.append("Purchasing Is: Centralized\r\n");
			else
				body.append("Purchasing Is: Decentralized\r\n");
			if ("Y".equals(naApplication.getPurchasingContactIsApplcnt()))
				body.append("Is Purchasing Contact?: Yes\r\n");
			else {
				body.append("Is Purchasing Contact?: No\r\n");
				body.append("Purchasing Contact Name: " + naApplication.getPurchasingContactName() + "\r\n");
				body.append("Purchasing Contact Phone Number: " + naApplication.getPurchasingContactPhone() + "\r\n");
			}
			
			//Number of Vehicles
			body.append("\r\nNumber of Vehicles\r\n");
			body.append("-------------------\r\n");
			body.append("Passenger: " + naApplication.getNumVehiclesPassenger().toString() + "\r\n");
			body.append("Light Truck: " + naApplication.getNumVehiclesLighttruck().toString() + "\r\n");
			if (naApplication.getNumVehiclesOther() != null && naApplication.getNumVehiclesOther().longValue() > 0) {
				body.append("Other: " + naApplication.getNumVehiclesOther().toString() + "\r\n");
				body.append("Description of Other: " + naApplication.getVehiclesOtherDesc() + "\r\n");
			}
			
			//Annual Purchases
			body.append("\r\nEstimated Annual Purchases\r\n");
			body.append("-------------------\r\n");
			body.append("Tires: $" + naApplication.getEstAnnualPurchasesTires().toString() + "\r\n");
			body.append("Services: $" + naApplication.getEstAnnualPurchasesServices().toString() + "\r\n");
	
			sendMail(subject, tos, ccAddresses, from, false, body.toString());
			
			
		}catch(Exception ex){
			// log it, but return success so we don't bother user with email problems
			ex.printStackTrace();
			
		}
		
	}
	
	private String getTireQuoteMailMessage(BSROWebServiceResponse tireQuote,
			String siteName, String firstName, String lastName) {
		String mailBody = null;
		if (tireQuote != null && tireQuote.getPayload() != null) {
			try {
				Map<String,String> data = new LinkedHashMap<String,String>();
				Map<String,String> staticdata = new LinkedHashMap<String,String>();
				String customerName = "";
				String mailContent = mailMessage.getTireQuoteMailMessage();
				TireQuote quote = (TireQuote) tireQuote.getPayload();
				VehicleFitment vehicleFitment = quote.getVehicleFitment();
				TireSize tireSize = quote.getTireSize();			
				Tire tire = quote.getTire();
				Tire rearTire = quote.getRearTire();
				QuoteItem quoteItem = quote.getQuoteItem();
				Long storeNumber = quote.getStoreNumber();			
				Store store = findStoreById(storeNumber);
				String baseURL = Sites.getWebSiteAppRoot(siteName);
				String maintenanceChoice = ("MVAN".equalsIgnoreCase(store.getStoreType())) ? MVAN_TIRE_REPLACEMENT_CHOICE_ID : TIRE_REPLACEMENT_CHOICE_ID;
				String appointmentURL = baseURL
						+ "/appointment/index.htm?storeNumber="
						+ store.getStoreNumber()
						+ "&amp;pricing=false&amp;maintenanceChoices="
						+ maintenanceChoice
						+ "&amp;appointment.tireQuoteId="
						+ java.net.URLEncoder.encode(quote.getTireQuoteId().toString(), "utf-8")
						+ "&amp;nav=tire&amp;appointment.comments="
						+ java.net.URLEncoder.encode(getAppointmentComments(tire, quote), "utf-8");
				
				if(siteName.toLowerCase().contains("fcac")){
					appointmentURL+= "&amp;lw_cmp=int-em_fcac_eg-fcac-tires-other_em-link";
				}
				if(siteName.toLowerCase().contains("tp")){
					appointmentURL+= "&amp;lw_cmp=int-em_tp_eg-tp-tires-other_em-link";
				}
				if(siteName.toLowerCase().contains("ht")){
					appointmentURL+= "&amp;lw_cmp=int-em_hib_eg-hib-tires-other_em-link";
				}
				if(siteName.toLowerCase().contains("wwt")){
					appointmentURL+= "&amp;lw_cmp=int-em_ww_eg-ww-tires-other_em-link";
				}
				
				if (!StringUtils.isNullOrEmpty(firstName)) {
					customerName = firstName + " ";
				}
				if (!StringUtils.isNullOrEmpty(lastName)) {
					customerName += lastName;
				}
				
				if (vehicleFitment != null) {
					data.put("USER_VEHILE", vehicleFitment.getYear() + " " +vehicleFitment.getMake() + " " + vehicleFitment.getModel() + " " + vehicleFitment.getSubmodel());
					appointmentURL += "&amp;vehicle.year="
							+ vehicleFitment.getYear()
							+ "&amp;vehicle.make="
							+ java.net.URLEncoder.encode(vehicleFitment.getMake(), "utf-8")
							+ "&amp;vehicle.model="
							+ java.net.URLEncoder.encode(vehicleFitment.getModel(), "utf-8")
							+ "&amp;vehicle.submodel="
							+ java.net.URLEncoder.encode(vehicleFitment.getSubmodel(), "utf-8");
				} else {
					data.put("USER_VEHILE", tireSize.getCrossSection()+"/"+tireSize.getAspectRation()+"/"+tireSize.getRimSize()+" Tires");				
				}
				
				data.put("QUOTE_URL", mailMessage.getTireQuoteURL());
				data.put("IMAGE_PATH", mailMessage.getImagePath());
				data.put("CUSTOMER_NAME", (!StringUtils.isNullOrEmpty(customerName)) ? customerName : "");
				data.put("PUNCHMARK", (!StringUtils.isNullOrEmpty(customerName)) ? "&#8217;s" : "");
				data.put("CUSTOMER_FIRST_NAME", firstName);
				data.put("CUSTOMER_LAST_NAME", lastName);
				data.put("CURRENT_DATE", (new SimpleDateFormat("MMMM d, yyyy")).format(new Date()));
				data.put("APPOINTMENT_URL", appointmentURL);
				mapStoreDetails(store, data);
				mapTireQuoteDetails(tire, rearTire, quote, quoteItem, baseURL, data);
				mapTireQuoteHTML(quoteItem, rearTire, vehicleFitment, customerName, staticdata);
				data.put("BASE_URL", baseURL);
				
				mailContent = ServerUtil.populateEmailMessageContent(mailContent, staticdata);
				mailBody = ServerUtil.populateEmailMessageContent(mailContent, data);
			} catch(Exception exp) {
				exp.printStackTrace();
				return null;
			}
		}
		
		return mailBody;
	}
	
	private String getBatteryQuoteMailMessage(BSROWebServiceResponse batteryQuote,
			String siteName, String firstName, String lastName) {
		String mailBody = null;
		if (batteryQuote != null && batteryQuote.getPayload() != null) {
			try {
				Map<String,String> data = new LinkedHashMap<String,String>();
				Map<String,String> staticdata = new LinkedHashMap<String,String>();
				String customerName = "";
				String mailContent = mailMessage.getBatteryQuoteMailMessage();
				
				BatteryQuote quote = (BatteryQuote) batteryQuote.getPayload();
				GenericVehicle genericVehicle = quote.getVehicle();
				
				Long storeNumber = quote.getStoreNumber();			
				Store store = findStoreById(storeNumber);
				String baseURL = Sites.getWebSiteAppRoot(siteName);
				String appointmentURL = baseURL
						+ "/appointment/index.htm?storeNumber="
						+ store.getStoreNumber()
						+ "&amp;pricing=false&amp;maintenanceChoices="
						+ BATTERY_CHOICE_ID
						+ "&amp;appointment.batteryQuoteId="
						+ java.net.URLEncoder.encode(String.valueOf(quote.getBatteryQuoteId()), "utf-8")
						+ "&amp;nav=battery&amp;appointment.comments="
						+ java.net.URLEncoder.encode(getAppointmentComments(quote), "utf-8");
				
				if(siteName.toLowerCase().contains("fcac")){
					appointmentURL+= "&amp;lw_cmp=int-em_fcac_eg-fcac-btry-other_em-link";
				}
				if(siteName.toLowerCase().contains("tp")){
					appointmentURL+= "&amp;lw_cmp=int-em_tp_eg-tp-btry-other_em-link";
				}
				if(siteName.toLowerCase().contains("ht")){
					appointmentURL+= "&amp;lw_cmp=int-em_hib_eg-hib-btry-other_em-link";
				}
				if(siteName.toLowerCase().contains("wwt")){
					appointmentURL+= "&amp;lw_cmp=int-em_ww_eg-ww-btry-other_em-link";
				}
				
				if (!StringUtils.isNullOrEmpty(firstName)) {
					customerName = firstName + " ";
				}
				if (!StringUtils.isNullOrEmpty(lastName)) {
					customerName += lastName;
				}
				
				if (genericVehicle != null) {
					data.put("USER_VEHILE", genericVehicle.getYear() + " " +genericVehicle.getMake() + " " + genericVehicle.getModel() + " " + genericVehicle.getEngine());
					appointmentURL += "&amp;vehicle.year="
							+ genericVehicle.getYear()
							+ "&amp;vehicle.make="
							+ genericVehicle.getMake()
							+ "&amp;vehicle.model="
							+ genericVehicle.getModel()
							+ "&amp;vehicle.submodel="
							+ genericVehicle.getSubmodel();
				}
				
				data.put("QUOTE_URL", mailMessage.getBatteryQuoteURL());
				data.put("BASE_URL", baseURL);
				data.put("IMAGE_PATH", mailMessage.getImagePath());
				data.put("CUSTOMER_NAME", (!StringUtils.isNullOrEmpty(customerName)) ? customerName : "");
				data.put("PUNCHMARK", (!StringUtils.isNullOrEmpty(customerName)) ? "&#8217;s" : "");
				data.put("CUSTOMER_FIRST_NAME", firstName);
				data.put("CUSTOMER_LAST_NAME", lastName);
				data.put("CURRENT_DATE", (new SimpleDateFormat("MMMM d, yyyy")).format(new Date()));
				data.put("APPOINTMENT_URL", appointmentURL);
				mapStoreDetails(store, data);
				mapBatteryQuoteDetails(quote, data);
				mapBatteryQuoteHTML(customerName, staticdata);
				
				mailContent = ServerUtil.populateEmailMessageContent(mailContent, staticdata);
				mailBody = ServerUtil.populateEmailMessageContent(mailContent, data);
			} catch(Exception exp) {
				exp.printStackTrace();
				return null;
			}
		}
		return mailBody;
	}
	
	private String getAlignmentQuoteMailMessage(AlignmentPricingQuote alignmentQuote,
			String siteName, String firstName, String lastName) {
		String mailBody = null;
		if (alignmentQuote != null) {
			try {
				Map<String,String> data = new LinkedHashMap<String,String>();
				Map<String,String> staticdata = new LinkedHashMap<String,String>();
				String customerName = "";
				String mailContent = mailMessage.getAlignmentQuoteMailMessage();
				
				Long storeNumber = alignmentQuote.getStoreNumber();			
				Store store = findStoreById(storeNumber);
				String baseURL = Sites.getWebSiteAppRoot(siteName);
				String appointmentURL = baseURL
						+ "/appointment/index.htm?storeNumber="
						+ store.getStoreNumber()
						+ "&amp;maintenanceChoices="
						+ ALIGNMENT_CHOICE_ID
						+ "&amp;pricing=false&amp;nav=alignment"
						+ "&amp;appointment.comments="
						+ java.net.URLEncoder.encode(getAppointmentComments(alignmentQuote), "utf-8");
				
				if(siteName.toLowerCase().contains("fcac")){
					appointmentURL+= "&amp;lw_cmp=int-em_fcac_eg-fcac-align-other_em-link";
				}
				if(siteName.toLowerCase().contains("tp")){
					appointmentURL+= "&amp;lw_cmp=int-em_tp_eg-tp-align-other_em-link";
				}
				if(siteName.toLowerCase().contains("ht")){
					appointmentURL+= "&amp;lw_cmp=int-em_hib_eg-hib-align-other_em-link";
				}
				if(siteName.toLowerCase().contains("wwt")){
					appointmentURL+= "&amp;lw_cmp=int-em_ww_eg-ww-align-other_em-link";
				}
				
				if (alignmentQuote.getVehicleModel() != null) {
					data.put("USER_VEHILE", alignmentQuote.getVehicleYear() + " " +alignmentQuote.getVehicleMake() + " " + alignmentQuote.getVehicleModel() + " " + alignmentQuote.getVehicleSubmodel());
					appointmentURL += "&amp;vehicle.year="
										+ alignmentQuote.getVehicleYear()
										+ "&amp;vehicle.make="
										+ java.net.URLEncoder.encode(alignmentQuote.getVehicleMake(), "utf-8")
										+ "&amp;vehicle.model=" 
										+ java.net.URLEncoder.encode(alignmentQuote.getVehicleModel(), "utf-8");
				}
				
				data.put("QUOTE_URL", mailMessage.getAlignmentQuoteURL());
				data.put("BASE_URL", baseURL);
				data.put("IMAGE_PATH", mailMessage.getImagePath());
				data.put("CUSTOMER_NAME", (!StringUtils.isNullOrEmpty(customerName)) ? customerName : "");
				data.put("PUNCHMARK", (!StringUtils.isNullOrEmpty(customerName)) ? "&#8217;s" : "");
				data.put("CUSTOMER_FIRST_NAME", firstName);
				data.put("CUSTOMER_LAST_NAME", lastName);
				data.put("CURRENT_DATE", (new SimpleDateFormat("MMMM d, yyyy")).format(new Date()));
				data.put("APPOINTMENT_URL", appointmentURL);
				
				mapStoreDetails(store, data);
				mapAlignmentQuoteDetails(alignmentQuote, data);
				mapAlignmentQuoteHTML(customerName, staticdata);
				
				mailContent = ServerUtil.populateEmailMessageContent(mailContent, staticdata);
				mailBody = ServerUtil.populateEmailMessageContent(mailContent, data);
			} catch(Exception exp) {
				exp.printStackTrace();
				return null;
			}
		}
		return mailBody;		
	}
	
	private Store findStoreById(Long storeNumber) {
		Store store = storeDAO.get(storeNumber);
		store.setStoreHour(storeService.getStoreHourHTML(store.getHours(), true));
		return store;
	}

	private void mapStoreDetails(Store store, Map<String, String> data) {
		data.put("STORE_NAME", store.getStoreName());
		data.put("STORE_NUMBER", com.bfrc.storelocator.util.LocatorUtil.padStoreNumber(String.valueOf(store.getStoreNumber())));
		data.put("STORE_ADDRESS", store.getAddress());
		data.put("STORE_CITY", store.getCity());
		data.put("STORE_STATE", store.getState());
		data.put("STORE_ZIP", store.getZip());
		data.put("STORE_PHONE", store.getPhone());
		data.put("STORE_HOUR", ((store.getStoreHour() != null) ? store.getStoreHour() : ""));
		data.put("STORE_DETAIL_URL", mailMessage.getStoreDetailURL(statesMap, store));
		data.put("STORE_HOLIDAY", "");	//ToDO
		data.put("STORE_LAT", store.getLatitude().toString());
		data.put("STORE_LNG", store.getLongitude().toString());
		data.put("BING_MAPS_KEY", mailMessage.getBingMapsKey());
	}
	
	private void mapTireQuoteDetails(Tire tire, Tire rearTire, TireQuote quote,
			QuoteItem quoteItem, String baseURL, Map<String, String> data) {
		DecimalFormat mf = new DecimalFormat("###,###");
		DecimalFormat decimalValueFormatter = new DecimalFormat("##.00");
		
		data.put("QUOTE_ID", quote.getTireQuoteId().toString());
		if (rearTire != null) {
			data.put("TIRE_SIZE_INFO", "Front Size: "+tire.getTireSize() + " | Rear Size: "+rearTire.getTireSize());
			data.put("ARTICLE_NO", tire.getArticle()+";"+rearTire.getArticle());
		} else {
			data.put("TIRE_SIZE_INFO", "Size: "+tire.getTireSize());
			data.put("ARTICLE_NO", tire.getArticle().toString());
		}
		data.put("TIRE_SIZE", "Size: "+tire.getTireSize());
		data.put("SPEED_RATING", tire.getSpeedRating());
		data.put("SPEED_RATING_MPH", tire.getSpeedRatingMPH());
		data.put("LOAD_RANGE", (tire.getLoadRange() != null) ? tire.getLoadRange() : "");
		data.put("LOAD_INDEX", (tire.getLoadIndex() != null) ? tire.getLoadIndex().toString() : "");
		data.put("LOAD_INDEX_POUNDS", (tire.getLoadIndexPounds() != null) ? tire.getLoadIndexPounds().toString() : "");
		data.put("SIDE_WALL", tire.getSidewallDescription());
		data.put("MILEAGE", (tire.getMileage() != null) ? mf.format(Integer.parseInt(tire.getMileage().toString())) : "No");
		data.put("TIRE_BRAND", tire.getBrand());
		data.put("TIRE_NAME", tire.getTireName());
		data.put("TIRE_TYPE", tire.getTireType());
		data.put("STD_OPT", ("O".equalsIgnoreCase(tire.getStandardOptional())) ? "Optional" : "Standard");
		data.put("PROMO_DISPLAY_NAME", (quote.getTirePromotion() != null) ? quote.getTirePromotion().getPromoDisplayName() : "");
		data.put("PROMO_URL", (quote.getTirePromotion() != null) ? mailMessage.getTirePromoURL() : "");
		if (quote.getTirePromotion() != null) {
			if (SiteTypes.TP.getSiteType().equalsIgnoreCase(getSiteName())) {
				data.put("TIRE_PROMO_ID", quote.getTirePromotion().getPromoId().toString());
			} else {
				data.put("TIRE_PROMO_ID", quote.getTirePromotion().getPromoName());
			}
		}
		data.put("TIRE_PROMO_DISCOUNT_AMOUNT", (quoteItem.getDiscount() > 0) ? String.valueOf(decimalValueFormatter.format(quoteItem.getDiscount())) : "");
		data.put("TIRE_QTY", quote.getQuantity().toString());
		data.put("TIRE_UNIT_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getUnitPrice())));
		data.put("TIRE_TOTAL_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getTotalTirePrice())));
		if (rearTire != null) {
			data.put("REAR_TIRE_QTY", quote.getRearQuantity().toString());
			if (quoteItem.getRearUnitPrice() > 0) {
				data.put("REAR_TIRE_UNIT_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getRearUnitPrice())));
				data.put("REAR_TIRE_TOTAL_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getRearTotalTirePrice())));
			} else {
				data.put("REAR_TIRE_UNIT_PRICE", "");
				data.put("REAR_TIRE_TOTAL_PRICE", "");
			}
		}
		data.put("SUBTOTAL_PRICE", decimalValueFormatter.format(quoteItem.getTotalTirePrice() - quoteItem.getDiscount()));
		data.put("WHEEL_BALANCE_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getWheelBalance())));
		if (quoteItem.getTpmsValveServiceKit() > 0) {
			data.put("TPMS_KIT_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getTpmsValveServiceKit())));
			data.put("TPMS_KIT_LABOR_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getTpmsValveServiceKitLabor())));
			data.put("VALVE_STEM_PRICE", "");
		} else {
			data.put("VALVE_STEM_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getValveStem())));
			data.put("TPMS_KIT_PRICE", "");
			data.put("TPMS_KIT_LABOR_PRICE", "");
		}
		data.put("STATE_ENVIRONMENT_FEE", String.valueOf(decimalValueFormatter.format(quoteItem.getStateEnvironmentalFee())));
		data.put("SCRAP_TIRE_RECYCLE_FEE", String.valueOf(decimalValueFormatter.format(quoteItem.getScrapTireRecyclingCharge())));
		data.put("SHOP_SUPPLY_FEE", String.valueOf(decimalValueFormatter.format(quoteItem.getShopSupplies())));
		data.put("TAX", String.valueOf(decimalValueFormatter.format(quoteItem.getTax())));
		data.put("SUB_TOTAL_PRICE", String.valueOf(decimalValueFormatter.format(quoteItem.getTotal())));
		data.put("TOTAL", String.valueOf(decimalValueFormatter.format(quoteItem.getTotal())));
		data.put("COPY_RIGHT_YEAR", new SimpleDateFormat("yyyy").format(new java.util.Date()));
	}
	
	private void mapTireQuoteHTML(QuoteItem quoteItem, Tire rearTire, VehicleFitment vehicleFitment, String customerName, Map<String, String> staticdata) {
		staticdata.put("BRAND_BANNER_IMAGE", mailMessage.getBannerHTML());
		staticdata.put("PREFERED_VEHILE", (vehicleFitment != null) ? mailMessage.getVehicleDetailsHTML() : "");
		staticdata.put("PREFERED_STORE", mailMessage.getStoreDetailsHTML());
		staticdata.put("STORE_BING_MAP", mailMessage.getBingStoreMapHtml());
		staticdata.put("TIRE_SPEC_INFO", mailMessage.getTireSpecDetailsHTML());
		staticdata.put("QUOTE_HEADER", mailMessage.getTireQuoteHeaderHTML());
		staticdata.put("CUSTOMER_INFO", (!StringUtils.isNullOrEmpty(customerName)) ? mailMessage.getCustomerInfoHTML() : "");
		staticdata.put("QUOTE_TIRE_DETAIL_INFO", mailMessage.getTireDetailsHTML());
		staticdata.put("QUOTE_LINE_ITEMS", mailMessage.getQuoteLineItemsHTML());
		staticdata.put("QUOTE_SERVICE_GUARANTY", mailMessage.getQuoteServiceGuaranty());
		staticdata.put("CFNA_HTML", mailMessage.getCfnaHtml());
		if (rearTire != null) {
			staticdata.put("QUOTE_TIRE_QTY_PRICE", "");
			staticdata.put("QUOTE_MATCHED_SET_TIRE_PRICES", mailMessage.getMatchedSetTirePriceHTML());
			if (quoteItem.getUnitPrice() > 0) {
				staticdata.put("FRONT_TIRE_PRICE", mailMessage.getFrontTirePriceHTML());
			} else {
				staticdata.put("FRONT_TIRE_PRICE", mailMessage.getContactStorePricingHTML());
			}				
			if (quoteItem.getRearUnitPrice() > 0) {
				staticdata.put("REAR_TIRE_PRICE", mailMessage.getRearTirePriceHTML());
			} else {
				staticdata.put("REAR_TIRE_PRICE", mailMessage.getContactStorePricingHTML());
			}				
		} else {
			staticdata.put("QUOTE_TIRE_QTY_PRICE", mailMessage.getTirePriceHTML());
			staticdata.put("QUOTE_MATCHED_SET_TIRE_PRICES", "");
		}
		staticdata.put("QUOTE_TIRE_DISCOUNTS", (quoteItem.getDiscount() > 0) ? mailMessage.getTirePromotionHTML() : "");
		staticdata.put("QUOTE_TIRE_SUBTOTAL", mailMessage.getTireSubTotalHTML());
		staticdata.put("QUOTE_WHEEL_BALANCE_PRICE", (quoteItem.getWheelBalance() > 0) ? mailMessage.getTireWheelBalanceHTML() : "");
		staticdata.put("QUOTE_TIREMOUNTING_PRICE", mailMessage.getTireMountingHTML());
		staticdata.put("QUOTE_WHEEL_ALIGNMENT_CHECK_PRICE", mailMessage.getWheelAlignmentHTML());
		staticdata.put("QUOTE_TPMS_KIT_PRICE", (quoteItem.getTpmsValveServiceKit() > 0) ? mailMessage.getTPMSValveServiceKitHTML() : "");
		staticdata.put("QUOTE_TPMS_KIT_LABOR_PRICE", (quoteItem.getTpmsValveServiceKitLabor() > 0) ? mailMessage.getTPMSValveServiceKitLaborHTML() : "");
		staticdata.put("QUOTE_VALVE_STEM_PRICE", (quoteItem.getValveStem() > 0) ? mailMessage.getValveStemPriceHTML() : "");
		staticdata.put("QUOTE_STATE_ENVIRONMENT_FEE_PRICE", (quoteItem.getStateEnvironmentalFee() > 0) ? mailMessage.getStateEnvironmentFeeHTML() : "");
		staticdata.put("QUOTE_SCRAP_TIRE_RECYCLING_CHARGE_PRICE", (quoteItem.getScrapTireRecyclingCharge() > 0) ? mailMessage.getScrapTireRecyclingChargeHTML() : "");
		staticdata.put("QUOTE_SHOP_SUPPLIES_PRICE", (quoteItem.getShopSupplies() > 0) ? mailMessage.getShopSuppliesHTML() : "");
		staticdata.put("QUOTE_TAX_AMOUNT", (quoteItem.getTax() > 0) ? mailMessage.getTaxHTML() : "");
		staticdata.put("QUOTE_SUBTOTAL_PRICE", (quoteItem.getTotal() > 0) ? mailMessage.getTireQuoteSubTotalHTML() : "");
		staticdata.put("QUOTE_TOTAL_PRICE", (quoteItem.getTotal() > 0) ? mailMessage.getTireQuoteTotalHTML() : "");
		staticdata.put("QUOTE_DISCLAIMER", mailMessage.getTireQuoteDisclaimerHTML());
		staticdata.put("QUOTE_FOOTER", mailMessage.getQuoteFooterHTML());
	}
	
	private void mapBatteryQuoteDetails(BatteryQuote quote,
			Map<String, String> data) {
		data.put("QUOTE_ID", String.valueOf(quote.getBatteryQuoteId()));
		data.put("BATTERY_DESCRIPTION", quote.getBattery().getProductName());
		data.put("BATTERY_WARRANTY_YEARS", quote.getBattery().getPerformanceWarrantyYearsOrMonths());
		data.put("BATTERY_ARTICLE_NO", quote.getBattery().getPartNumber().toString());
		data.put("BATTERY_PRICE", quote.getBattery().getWebPrice().toString());
		data.put("BATTERY_INSTALLATION_PRICE", quote.getBattery().getInstallationAmount().toString());
		data.put("QUOTE_SUBTOTAL_PRICE", quote.getSubtotal().toString());
		data.put("QUOTE_TOTAL_PRICE", quote.getTotal().toString());
		data.put("COPY_RIGHT_YEAR", new SimpleDateFormat("yyyy").format(new java.util.Date()));
	}
	
	private void mapBatteryQuoteHTML(String customerName, Map<String, String> staticdata) {
		staticdata.put("BRAND_BANNER_IMAGE", mailMessage.getBannerHTML());
		staticdata.put("PREFERED_VEHILE", mailMessage.getVehicleDetailsHTML());
		staticdata.put("PREFERED_STORE", mailMessage.getStoreDetailsHTML());
		staticdata.put("STORE_BING_MAP", mailMessage.getBingStoreMapHtml());
		staticdata.put("CFNA_HTML", mailMessage.getCfnaHtml());
		staticdata.put("TIRE_SPEC_INFO", "");
		staticdata.put("QUOTE_HEADER_HTML", mailMessage.getBatteryQuoteHeaderHTML());
		staticdata.put("QUOTE_DETAILS_HTML", mailMessage.getBatteryQuoteDetailsHTML());
		staticdata.put("QUOTE_PRICE_DISCLAIMER", mailMessage.getBatteryQuoteDisclaimerHTML());
		staticdata.put("QUOTE_APPOINTMENT_HTML", mailMessage.getBatteryQuoteScheduleAppointmentHTML());
		staticdata.put("QUOTE_FOOTER", mailMessage.getQuoteFooterHTML());
	}
	
	private void mapAlignmentQuoteDetails(AlignmentPricingQuote quote,
			Map<String, String> data) {
		data.put("QUOTE_ID", String.valueOf(quote.getAlignmentQuoteId()));
		data.put("ALIGNMENT_TYPE", getAlignmentType(quote.getPricingName()));
		data.put("ALIGNMENT_CHECK_ARTICLE", mailMessage.getAlignmentCheckArticleNo());
		data.put("ALIGNMENT_TYPE_ARTICLE", quote.getArticle().toString());
		data.put("QUOTE_SUBTOTAL_PRICE", quote.getPrice().toString());
		data.put("QUOTE_TOTAL_PRICE", quote.getPrice().toString());
		data.put("COPY_RIGHT_YEAR", new SimpleDateFormat("yyyy").format(new java.util.Date()));
	}
	
	private void mapAlignmentQuoteHTML(String customerName, Map<String, String> staticdata) {
		staticdata.put("BRAND_BANNER_IMAGE", mailMessage.getBannerHTML());
		staticdata.put("PREFERED_VEHILE", mailMessage.getVehicleDetailsHTML());
		staticdata.put("PREFERED_STORE", mailMessage.getStoreDetailsHTML());
		staticdata.put("STORE_BING_MAP", mailMessage.getBingStoreMapHtml());
		staticdata.put("CFNA_HTML", mailMessage.getCfnaHtml());
		staticdata.put("TIRE_SPEC_INFO", "");
		staticdata.put("QUOTE_HEADER_HTML", mailMessage.getAlignmentQuoteHeaderHTML());
		staticdata.put("QUOTE_APPOINTMENT_HTML", mailMessage.getAlignmentQuoteScheduleAppointmentHTML());
		staticdata.put("QUOTE_DETAILS_HTML", mailMessage.getAlignmentQuoteDetailsHTML());
		staticdata.put("QUOTE_PRICE_DISCLAIMER", mailMessage.getAlignmentQuoteDisclaimerHTML());
		staticdata.put("QUOTE_FOOTER", mailMessage.getQuoteFooterHTML());
	}

	private boolean sendMail(String subject, String emailTo,
			String emailFrom, boolean isHtml, String mailBody) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(new String[]{emailTo});
			helper.setFrom(emailFrom);
			
			helper.setText(mailBody, isHtml);

			this.mailSender.send(message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void sendMail(String subject, String[] emailTo, String[] emailCC,
			String emailFrom, boolean isHtml, String mailBody) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(emailTo);
			helper.setCc(emailCC);
			helper.setFrom(emailFrom);
			
			helper.setText(mailBody, isHtml);

			this.mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getSourceType(String source) {
		if (QUOTE_SOURCE_TIRE.equalsIgnoreCase(source)) {
			return 1;
		} else if (QUOTE_SOURCE_BATTERY.equalsIgnoreCase(source)) {
			return 2;
		} else if (QUOTE_SOURCE_ALIGNMENT.equalsIgnoreCase(source)) {
			return 3;
		}
		return -1;
	}
	
	public String getAppointmentComments(Tire tire, TireQuote quote) {
		String cmtArticle = String.valueOf(tire.getArticle());
        String cmtDsc = tire.getBrand() + " " + tire.getTireName();
        String cmtSize = tire.getTireSize();
        String cmtPrice = "$" + tire.getRetailPrice();
        
		String comments = "tire quote id:  " + quote.getTireQuoteId()
                + ",  tire article number:  " + cmtArticle
                + ",  tire description: " + cmtDsc
                + ",  tire size: " + cmtSize
                + ",  tire quantity: " + quote.getQuantity()
                + ",  tire unit price: " + cmtPrice
                + ",  tire quote End;";
		
		return comments;
	}
	
	public String getAppointmentComments(BatteryQuote quote) {
		java.text.DecimalFormat decimalFormatter = new java.text.DecimalFormat("000000");
		String comments = "battery quote id:  " 
				+ decimalFormatter.format(quote.getBatteryQuoteId()) 
				+ ",  battery article number:  " 
				+ quote.getBattery().getPartNumber() 
				+ ",  battery description: " 
				+ quote.getBattery().getProductName() 
				+ ",  battery warranty: " 
				+ quote.getBattery().getTotalWarrantyMonths() 
				+ ",  battery unit price: $" 
				+ quote.getBattery().getWebPrice() 
				+ ",  battery quote End;";
		return comments;
	}
	
	public String getAppointmentComments(AlignmentPricingQuote quote) {
		String comments = "alignment quote id:  " + quote.getAlignmentQuoteId()
	            + ",  alignment article number:  " + quote.getArticle()
	            + ",  alignment description: " + quote.getPricingName()
	            + ",  alignment unit price: " + quote.getPrice()
	            + ",  alignment quote End;";
		return comments;
	}
	
	public String getAlignmentType(String type) {
		String alType = "";
		if (type.startsWith("Standard")) {
			alType="Standard";
		} else if (type.startsWith("Three")) {
			alType="Three Year";
		} else if(type.startsWith("Life")) {
			alType="Life Time";
		}
		return alType;
	}
	
	private BSROWebServiceResponse getErrorMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}
	
	private BSROWebServiceResponse getSuccessMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());	
		response.setMessage(message);
		response.setPayload(null);
		return response;
	}
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

}
