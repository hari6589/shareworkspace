package com.bridgestone.bsro.aws.email.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;

import com.bridgestone.bsro.aws.email.model.alignment.AlignmentPricingQuote;
import com.bridgestone.bsro.aws.email.model.tire.Store;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AlignmentEmailQuoteService {
	public String generateAlignmentQuoteMessage(MailMessage mailMessage, String baseURL, String alignmentChoiceId, String quoteId, String siteName, String firstName, String lastName, String email) throws JsonParseException, JsonMappingException, IOException, JSONException {

		Quote quote = new Quote();
		Store store = quote.getStoreDetail("11940");
		String messageTemplate = mailMessage.getAlignmentQuoteMailMessage();
		AlignmentPricingQuote alignmentQuote = quote.getAlignmentPricingQuote(quoteId);
		
		Map<String,String> data = new LinkedHashMap<String,String>();
		Map<String,String> staticData = new LinkedHashMap<String,String>();
		
		String customerName = "";
		String appointmentURL = baseURL
				+ "/appointment/index.htm?storeNumber="
				+ store.getStoreNumber()
				+ "&amp;maintenanceChoices="
				+ alignmentChoiceId
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
		
		/// mapStoreDetails(store, data);
		data.put("STORE_NAME", store.getStoreName());
		data.put("STORE_NUMBER", StringUtils.padStoreNumber(String.valueOf(store.getStoreNumber())));
		data.put("STORE_ADDRESS", store.getAddress());
		data.put("STORE_CITY", store.getCity());
		data.put("STORE_STATE", store.getState());
		data.put("STORE_ZIP", store.getZip());
		data.put("STORE_PHONE", store.getPhone());
		data.put("STORE_HOUR", ((store.getStoreHour() != null) ? store.getStoreHour() : ""));
		//data.put("STORE_DETAIL_URL", mailMessage.getStoreDetailURL(statesMap, store));  // yet to be done
		data.put("STORE_HOLIDAY", "");	//ToDO
		data.put("STORE_LAT", store.getLatitude().toString());
		data.put("STORE_LNG", store.getLongitude().toString());
		data.put("BING_MAPS_KEY", mailMessage.getBingMapsKey());
		
		/// mapAlignmentQuoteDetails(alignmentQuote, data);
		data.put("QUOTE_ID", String.valueOf(alignmentQuote.getAlignmentQuoteId()));
		data.put("ALIGNMENT_TYPE", getAlignmentType(alignmentQuote.getPricingName()));
		data.put("ALIGNMENT_CHECK_ARTICLE", mailMessage.getAlignmentCheckArticleNo());
		data.put("ALIGNMENT_TYPE_ARTICLE", alignmentQuote.getArticle().toString());
		data.put("QUOTE_SUBTOTAL_PRICE", alignmentQuote.getPrice().toString());
		data.put("QUOTE_TOTAL_PRICE", alignmentQuote.getPrice().toString());
		data.put("COPY_RIGHT_YEAR", new SimpleDateFormat("yyyy").format(new java.util.Date()));
		
		/// mapAlignmentQuoteHTML(customerName, staticdata);
		staticData.put("BRAND_BANNER_IMAGE", mailMessage.getBannerHTML());
		staticData.put("PREFERED_VEHILE", mailMessage.getVehicleDetailsHTML());
		staticData.put("PREFERED_STORE", mailMessage.getStoreDetailsHTML());
		staticData.put("STORE_BING_MAP", mailMessage.getBingStoreMapHtml());
		staticData.put("CFNA_HTML", mailMessage.getCfnaHtml());
		staticData.put("TIRE_SPEC_INFO", "");
		staticData.put("QUOTE_HEADER_HTML", mailMessage.getAlignmentQuoteHeaderHTML());
		staticData.put("QUOTE_APPOINTMENT_HTML", mailMessage.getAlignmentQuoteScheduleAppointmentHTML());
		staticData.put("QUOTE_DETAILS_HTML", mailMessage.getAlignmentQuoteDetailsHTML());
		staticData.put("QUOTE_PRICE_DISCLAIMER", mailMessage.getAlignmentQuoteDisclaimerHTML());
		staticData.put("QUOTE_FOOTER", mailMessage.getQuoteFooterHTML());
		
		String mailContent = StringUtils.populateEmailMessageContent(messageTemplate, staticData);
		mailContent = StringUtils.populateEmailMessageContent(mailContent, data);
		
		return mailContent;
	}
	
	public static String getAppointmentComments(AlignmentPricingQuote quote) {
		String comments = "alignment quote id:  " + quote.getAlignmentQuoteId()
	            + ",  alignment article number:  " + quote.getArticle()
	            + ",  alignment description: " + quote.getPricingName()
	            + ",  alignment unit price: " + quote.getPrice()
	            + ",  alignment quote End;";
		return comments;
	}
	
	public static String getAlignmentType(String type) {
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
}
