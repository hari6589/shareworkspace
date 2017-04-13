package com.bridgestone.bsro.aws.email.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;

import com.bridgestone.bsro.aws.email.model.tire.Store;
import com.bridgestone.bsro.aws.email.model.tire.Tire;
import com.bridgestone.bsro.aws.email.model.tire.TireQuote;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TireEmailQuoteService {
	public String generateTireQuoteMessage(MailMessage mailMessage, String baseURL, String tireReplacementChoiceId, String quoteId, String siteName, String firstName, String lastName, String email) throws JsonParseException, JsonMappingException, IOException, JSONException {
		
		Quote quote = new Quote();
		Store store = quote.getStoreDetail("11940");
		TireQuote tireQuote = quote.getTireQuote(quoteId);
		String messageTemplate = mailMessage.getTireQuoteMailMessage();
		
		Map<String,String> data = new LinkedHashMap<String,String>();
		Map<String,String> staticData = new LinkedHashMap<String,String>();
		
		String customerName = "";
		// String baseURL = getWebSiteAppRoot(siteName); //Need to be dynamic based on siteName and Environment(www/chw-qa/cwh-uat)
		String appointmentURL = baseURL
				+ "/appointment/index.htm?storeNumber="
				+ "11940"  //+ store.getStoreNumber()
				+ "&amp;pricing=false&amp;maintenanceChoices="
				+ tireReplacementChoiceId //if(MVAN) 2782 else 2745 // maintenanceChoice
				+ "&amp;appointment.tireQuoteId="
				+ java.net.URLEncoder.encode(tireQuote.getTireQuoteId().toString(), "utf-8")
				+ "&amp;nav=tire&amp;appointment.comments="
				+ java.net.URLEncoder.encode(getAppointmentComments(tireQuote.getTire(), tireQuote), "utf-8");
		
		if(siteName.toLowerCase().contains("fcac")) {
			appointmentURL+= "&amp;lw_cmp=int-em_fcac_eg-fcac-tires-other_em-link";
		}
		if(siteName.toLowerCase().contains("tp")) {
			appointmentURL+= "&amp;lw_cmp=int-em_tp_eg-tp-tires-other_em-link";
		}
		if(siteName.toLowerCase().contains("ht")) {
			appointmentURL+= "&amp;lw_cmp=int-em_hib_eg-hib-tires-other_em-link";
		}
		if(siteName.toLowerCase().contains("wwt")) {
			appointmentURL+= "&amp;lw_cmp=int-em_ww_eg-ww-tires-other_em-link";
		}
		
		if (!StringUtils.isNullOrEmpty(firstName)) {
			customerName = firstName + " ";
		}
		if (!StringUtils.isNullOrEmpty(lastName)) {
			customerName += lastName;
		}
		
		if (tireQuote.getVehicleFitment() != null) {
			data.put("USER_VEHILE", tireQuote.getVehicleFitment().getYear() + " " +tireQuote.getVehicleFitment().getMake() + " " + tireQuote.getVehicleFitment().getModel() + " " + tireQuote.getVehicleFitment().getSubmodel());
			appointmentURL += "&amp;vehicle.year="
					+ tireQuote.getVehicleFitment().getYear()
					+ "&amp;vehicle.make="
					+ java.net.URLEncoder.encode(tireQuote.getVehicleFitment().getMake(), "utf-8")
					+ "&amp;vehicle.model="
					+ java.net.URLEncoder.encode(tireQuote.getVehicleFitment().getModel(), "utf-8")
					+ "&amp;vehicle.submodel="
					+ java.net.URLEncoder.encode(tireQuote.getVehicleFitment().getSubmodel(), "utf-8");
		} else {
			data.put("USER_VEHILE", tireQuote.getTireSize().getCrossSection()+"/"+tireQuote.getTireSize().getAspectRation()+"/"+tireQuote.getTireSize().getRimSize()+" Tires");				
		}
		
		data.put("QUOTE_URL", mailMessage.getTireQuoteURL());
		data.put("IMAGE_PATH", mailMessage.getImagePath());
		data.put("CUSTOMER_NAME", (!StringUtils.isNullOrEmpty(customerName)) ? customerName : "");
		data.put("PUNCHMARK", (!StringUtils.isNullOrEmpty(customerName)) ? "&#8217;s" : "");
		data.put("CUSTOMER_FIRST_NAME", firstName);
		data.put("CUSTOMER_LAST_NAME", lastName);
		data.put("CURRENT_DATE", (new SimpleDateFormat("MMMM d, yyyy")).format(new Date()));
		data.put("APPOINTMENT_URL", appointmentURL);
		data.put("BASE_URL", baseURL);
		
		// Map Store Data
		/// mapStoreDetails(store, data);
		System.out.println("before Store section...");
		data.put("STORE_NAME", store.getStoreName());
		System.out.println("1");
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
		
		// Map Tire Quote Detail
		/// mapTireQuoteDetails(tire, rearTire, quote, quoteItem, baseURL, data);
		DecimalFormat mf = new DecimalFormat("###,###");
		DecimalFormat decimalValueFormatter = new DecimalFormat("##.00");
		data.put("QUOTE_ID", tireQuote.getTireQuoteId().toString());
		if (tireQuote.getRearTire() != null) {
			data.put("TIRE_SIZE_INFO", "Front Size: " + tireQuote.getTire().getTireSize() + " | Rear Size: " + tireQuote.getRearTire().getTireSize());
			data.put("ARTICLE_NO", tireQuote.getTire().getArticle() + ";" + tireQuote.getRearTire().getArticle());
		} else {
			data.put("TIRE_SIZE_INFO", "Size: " + tireQuote.getTire().getTireSize());
			data.put("ARTICLE_NO", tireQuote.getTire().getArticle().toString());
		}
		
		data.put("TIRE_SIZE", "Size: "+tireQuote.getTire().getTireSize());
		data.put("SPEED_RATING", tireQuote.getTire().getSpeedRating());
		data.put("SPEED_RATING_MPH", tireQuote.getTire().getSpeedRatingMPH());
		data.put("LOAD_RANGE", (tireQuote.getTire().getLoadRange() != null) ? tireQuote.getTire().getLoadRange() : "");
		data.put("LOAD_INDEX", (tireQuote.getTire().getLoadIndex() != null) ? tireQuote.getTire().getLoadIndex().toString() : "");
		data.put("LOAD_INDEX_POUNDS", (tireQuote.getTire().getLoadIndexPounds() != null) ? tireQuote.getTire().getLoadIndexPounds().toString() : "");
		data.put("SIDE_WALL", tireQuote.getTire().getSidewallDescription());
		data.put("MILEAGE", (tireQuote.getTire().getMileage() != null) ? mf.format(Integer.parseInt(tireQuote.getTire().getMileage().toString())) : "No");
		data.put("TIRE_BRAND", tireQuote.getTire().getBrand());
		data.put("TIRE_NAME", tireQuote.getTire().getTireName());
		data.put("TIRE_TYPE", tireQuote.getTire().getTireType());
		data.put("STD_OPT", ("O".equalsIgnoreCase(tireQuote.getTire().getStandardOptional())) ? "Optional" : "Standard");
		data.put("PROMO_DISPLAY_NAME", (tireQuote.getTirePromotion() != null) ? tireQuote.getTirePromotion().getPromoDisplayName() : "");
		data.put("PROMO_URL", (tireQuote.getTirePromotion() != null) ? mailMessage.getTirePromoURL() : "");
		
		if (tireQuote.getTirePromotion() != null) {
			if (tireQuote.getTirePromotion().getPromoId() != null) {
				// if (SiteTypes.TP.getSiteType().equalsIgnoreCase(getSiteName())) { /// Check
				if(true) {
					data.put("TIRE_PROMO_ID", tireQuote.getTirePromotion().getPromoId().toString());
				} else {
					data.put("TIRE_PROMO_ID", tireQuote.getTirePromotion().getPromoName());
				}
			}
		}
		
		data.put("TIRE_PROMO_DISCOUNT_AMOUNT", (tireQuote.getQuoteItem().getDiscount() > 0) ? String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getDiscount())) : "");
		data.put("TIRE_QTY", tireQuote.getQuantity().toString());
		data.put("TIRE_UNIT_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getUnitPrice())));
		data.put("TIRE_TOTAL_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getTotalTirePrice())));
		if (tireQuote.getRearTire() != null) {
			data.put("REAR_TIRE_QTY", tireQuote.getRearQuantity().toString());
			if (tireQuote.getQuoteItem().getRearUnitPrice() > 0) {
				data.put("REAR_TIRE_UNIT_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getRearUnitPrice())));
				data.put("REAR_TIRE_TOTAL_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getRearTotalTirePrice())));
			} else {
				data.put("REAR_TIRE_UNIT_PRICE", "");
				data.put("REAR_TIRE_TOTAL_PRICE", "");
			}
		}
		
		if (tireQuote.getRearTire() != null) {
			data.put("SUBTOTAL_PRICE", decimalValueFormatter.format((tireQuote.getQuoteItem().getTotalTirePrice() + tireQuote.getQuoteItem().getRearTotalTirePrice()) - tireQuote.getQuoteItem().getDiscount()));
		} else {
			data.put("SUBTOTAL_PRICE", decimalValueFormatter.format(tireQuote.getQuoteItem().getTotalTirePrice() - tireQuote.getQuoteItem().getDiscount()));
		}
		data.put("WHEEL_BALANCE_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getWheelBalance())));
		if (tireQuote.getQuoteItem().getTpmsValveServiceKit() > 0) {
			data.put("TPMS_KIT_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getTpmsValveServiceKit())));
			data.put("TPMS_KIT_LABOR_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getTpmsValveServiceKitLabor())));
			data.put("VALVE_STEM_PRICE", "");
		} else {
			data.put("VALVE_STEM_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getValveStem())));
			data.put("TPMS_KIT_PRICE", "");
			data.put("TPMS_KIT_LABOR_PRICE", "");
		}
		data.put("STATE_ENVIRONMENT_FEE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getStateEnvironmentalFee())));
		data.put("SCRAP_TIRE_RECYCLE_FEE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getScrapTireRecyclingCharge())));
		data.put("SHOP_SUPPLY_FEE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getShopSupplies())));
		data.put("TAX", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getTax())));
		data.put("SUB_TOTAL_PRICE", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getTotal())));
		data.put("TOTAL", String.valueOf(decimalValueFormatter.format(tireQuote.getQuoteItem().getTotal())));
		data.put("COPY_RIGHT_YEAR", new SimpleDateFormat("yyyy").format(new java.util.Date()));
		
		///mapTireQuoteHTML(customerName, staticdata);
		staticData.put("BRAND_BANNER_IMAGE", mailMessage.getBannerHTML());
		staticData.put("PREFERED_VEHILE", (tireQuote.getVehicleFitment() != null) ? mailMessage.getVehicleDetailsHTML() : "");
		staticData.put("PREFERED_STORE", mailMessage.getStoreDetailsHTML());
		staticData.put("STORE_BING_MAP", mailMessage.getBingStoreMapHtml());
		staticData.put("TIRE_SPEC_INFO", mailMessage.getTireSpecDetailsHTML());
		staticData.put("QUOTE_HEADER", mailMessage.getTireQuoteHeaderHTML());
		staticData.put("CUSTOMER_INFO", (!StringUtils.isNullOrEmpty(customerName)) ? mailMessage.getCustomerInfoHTML() : "");
		staticData.put("QUOTE_TIRE_DETAIL_INFO", mailMessage.getTireDetailsHTML());
		staticData.put("QUOTE_LINE_ITEMS", mailMessage.getQuoteLineItemsHTML());
		staticData.put("QUOTE_SERVICE_GUARANTY", mailMessage.getQuoteServiceGuaranty());
		staticData.put("CFNA_HTML", mailMessage.getCfnaHtml());
		if (tireQuote.getRearTire() != null) {
			staticData.put("QUOTE_TIRE_QTY_PRICE", "");
			staticData.put("QUOTE_MATCHED_SET_TIRE_PRICES", mailMessage.getMatchedSetTirePriceHTML());
			if (tireQuote.getQuoteItem().getUnitPrice() > 0) {
				staticData.put("FRONT_TIRE_PRICE", mailMessage.getFrontTirePriceHTML());
			} else {
				staticData.put("FRONT_TIRE_PRICE", mailMessage.getContactStorePricingHTML());
			}				
			if (tireQuote.getQuoteItem().getRearUnitPrice() > 0) {
				staticData.put("REAR_TIRE_PRICE", mailMessage.getRearTirePriceHTML());
			} else {
				staticData.put("REAR_TIRE_PRICE", mailMessage.getContactStorePricingHTML());
			}				
		} else {
			staticData.put("QUOTE_TIRE_QTY_PRICE", mailMessage.getTirePriceHTML());
			staticData.put("QUOTE_MATCHED_SET_TIRE_PRICES", "");
		}
		staticData.put("QUOTE_TIRE_DISCOUNTS", (tireQuote.getQuoteItem().getDiscount() > 0) ? mailMessage.getTirePromotionHTML() : "");
		staticData.put("QUOTE_TIRE_SUBTOTAL", mailMessage.getTireSubTotalHTML());
		staticData.put("QUOTE_WHEEL_BALANCE_PRICE", (tireQuote.getQuoteItem().getWheelBalance() > 0) ? mailMessage.getTireWheelBalanceHTML() : "");
		staticData.put("QUOTE_TIREMOUNTING_PRICE", mailMessage.getTireMountingHTML());
		staticData.put("QUOTE_WHEEL_ALIGNMENT_CHECK_PRICE", mailMessage.getWheelAlignmentHTML());
		staticData.put("QUOTE_TPMS_KIT_PRICE", (tireQuote.getQuoteItem().getTpmsValveServiceKit() > 0) ? mailMessage.getTPMSValveServiceKitHTML() : "");
		staticData.put("QUOTE_TPMS_KIT_LABOR_PRICE", (tireQuote.getQuoteItem().getTpmsValveServiceKitLabor() > 0) ? mailMessage.getTPMSValveServiceKitLaborHTML() : "");
		staticData.put("QUOTE_VALVE_STEM_PRICE", (tireQuote.getQuoteItem().getValveStem() > 0) ? mailMessage.getValveStemPriceHTML() : "");
		staticData.put("QUOTE_STATE_ENVIRONMENT_FEE_PRICE", (tireQuote.getQuoteItem().getStateEnvironmentalFee() > 0) ? mailMessage.getStateEnvironmentFeeHTML() : "");
		staticData.put("QUOTE_SCRAP_TIRE_RECYCLING_CHARGE_PRICE", (tireQuote.getQuoteItem().getScrapTireRecyclingCharge() > 0) ? mailMessage.getScrapTireRecyclingChargeHTML() : "");
		staticData.put("QUOTE_SHOP_SUPPLIES_PRICE", (tireQuote.getQuoteItem().getShopSupplies() > 0) ? mailMessage.getShopSuppliesHTML() : "");
		staticData.put("QUOTE_TAX_AMOUNT", (tireQuote.getQuoteItem().getTax() > 0) ? mailMessage.getTaxHTML() : "");
		staticData.put("QUOTE_SUBTOTAL_PRICE", (tireQuote.getQuoteItem().getTotal() > 0) ? mailMessage.getTireQuoteSubTotalHTML() : "");
		staticData.put("QUOTE_TOTAL_PRICE", (tireQuote.getQuoteItem().getTotal() > 0) ? mailMessage.getTireQuoteTotalHTML() : "");
		staticData.put("QUOTE_DISCLAIMER", mailMessage.getTireQuoteDisclaimerHTML());
		staticData.put("QUOTE_FOOTER", mailMessage.getQuoteFooterHTML());
		
		String mailContent = StringUtils.populateEmailMessageContent(messageTemplate, staticData);
		mailContent = StringUtils.populateEmailMessageContent(mailContent, data);
		
		return mailContent;
	}
	
	public static String getAppointmentComments(Tire tire, TireQuote quote) {
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
}
