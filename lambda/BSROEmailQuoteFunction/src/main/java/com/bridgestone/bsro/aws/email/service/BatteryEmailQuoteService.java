package com.bridgestone.bsro.aws.email.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;

import com.bridgestone.bsro.aws.email.model.battery.BatteryQuote;
import com.bridgestone.bsro.aws.email.model.tire.Store;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class BatteryEmailQuoteService {
	public String generateBatteryQuoteMessage(MailMessage mailMessage, String baseURL, String batteryChoiceId, String quoteId, String siteName, String firstName, String lastName, String email) throws JsonParseException, JsonMappingException, IOException, JSONException {
		
		Quote quote = new Quote();
		Store store = quote.getStoreDetail("11940");
		BatteryQuote batteryQuote = quote.getBatteryQuote(quoteId);
		String messageTemplate = mailMessage.getBatteryQuoteMailMessage();
		
		Map<String,String> data = new LinkedHashMap<String,String>();
		Map<String,String> staticData = new LinkedHashMap<String,String>();
		
		String customerName = "";
		
		String appointmentURL = baseURL
				+ "/appointment/index.htm?storeNumber="
				+ "11940"  //+ store.getStoreNumber()
				+ "&amp;pricing=false&amp;maintenanceChoices="
				+ batteryChoiceId
				+ "&amp;appointment.batteryQuoteId="
				+ java.net.URLEncoder.encode(String.valueOf(batteryQuote.getBatteryQuoteId()), "utf-8")
				+ "&amp;nav=battery&amp;appointment.comments="
				+ java.net.URLEncoder.encode(getAppointmentComments(batteryQuote), "utf-8");

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
		
		if (batteryQuote.getVehicle() != null) {
			data.put("USER_VEHILE", batteryQuote.getVehicle().getYear() + " " +batteryQuote.getVehicle().getMake() + " " + batteryQuote.getVehicle().getModel() + " " + batteryQuote.getVehicle().getEngine());
			appointmentURL += "&amp;vehicle.year="
					+ batteryQuote.getVehicle().getYear()
					+ "&amp;vehicle.make="
					+ batteryQuote.getVehicle().getMake()
					+ "&amp;vehicle.model="
					+ batteryQuote.getVehicle().getModel()
					+ "&amp;vehicle.submodel="
					+ batteryQuote.getVehicle().getSubmodel();
		}
		System.out.println("1");
		data.put("QUOTE_URL", mailMessage.getBatteryQuoteURL());
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
		//data.put("STORE_DETAIL_URL", mailMessage.getStoreDetailURL(statesMap, store)); // yet to be done
		data.put("STORE_HOLIDAY", "");	//Todo
		data.put("STORE_LAT", store.getLatitude().toString());
		data.put("STORE_LNG", store.getLongitude().toString());
		data.put("BING_MAPS_KEY", mailMessage.getBingMapsKey());
		
		/// mapBatteryQuoteDetails(batteryQuote, data);
		data.put("QUOTE_ID", String.valueOf(batteryQuote.getBatteryQuoteId()));
		data.put("BATTERY_DESCRIPTION", batteryQuote.getBattery().getProductName());
		data.put("BATTERY_WARRANTY_YEARS", batteryQuote.getBattery().getPerformanceWarrantyYearsOrMonths());
		data.put("BATTERY_ARTICLE_NO", batteryQuote.getBattery().getPartNumber().toString());
		data.put("BATTERY_PRICE", batteryQuote.getBattery().getWebPrice().toString());
		data.put("BATTERY_INSTALLATION_PRICE", batteryQuote.getBattery().getInstallationAmount().toString());
		data.put("QUOTE_SUBTOTAL_PRICE", batteryQuote.getSubtotal().toString());
		data.put("QUOTE_TOTAL_PRICE", batteryQuote.getTotal().toString());
		data.put("COPY_RIGHT_YEAR", new SimpleDateFormat("yyyy").format(new java.util.Date()));
		
		/// mapBatteryQuoteHTML(customerName, staticdata);
		staticData.put("BRAND_BANNER_IMAGE", mailMessage.getBannerHTML());
		staticData.put("PREFERED_VEHILE", mailMessage.getVehicleDetailsHTML());
		staticData.put("PREFERED_STORE", mailMessage.getStoreDetailsHTML());
		staticData.put("STORE_BING_MAP", mailMessage.getBingStoreMapHtml());
		staticData.put("CFNA_HTML", mailMessage.getCfnaHtml());
		staticData.put("TIRE_SPEC_INFO", "");
		staticData.put("QUOTE_HEADER_HTML", mailMessage.getBatteryQuoteHeaderHTML());
		staticData.put("QUOTE_DETAILS_HTML", mailMessage.getBatteryQuoteDetailsHTML());
		staticData.put("QUOTE_PRICE_DISCLAIMER", mailMessage.getBatteryQuoteDisclaimerHTML());
		staticData.put("QUOTE_APPOINTMENT_HTML", mailMessage.getBatteryQuoteScheduleAppointmentHTML());
		staticData.put("QUOTE_FOOTER", mailMessage.getQuoteFooterHTML());
		
		String mailContent = StringUtils.populateEmailMessageContent(messageTemplate, staticData);
		mailContent = StringUtils.populateEmailMessageContent(mailContent, data);
		
		return mailContent;
	}
	
	public static String getAppointmentComments(BatteryQuote quote) {
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
}
