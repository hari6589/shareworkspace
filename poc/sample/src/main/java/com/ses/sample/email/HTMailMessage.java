package com.ses.sample.email;

import tirequote.States;
import tirequote.Store;

public class HTMailMessage extends MailMessage {
	
	public static final String BING_MAPS_KEY = "ApUWlg1gz_OPJbPnSbLwvB5uFTP9JitfD9f39EWadzRu1mdTHbLXpdP2iZVrhB0o";
	public static final String EMAIL_FROM =  "\"DO-NOT-REPLY\" <webmaster@tiresplus.com>";
	public static final String IMAGE_PATH =  "images-tp";
	public static final String HTP_LOCAL_PAGE_URL =  "http://local.hibdontire.com";
		
	@Override
	public String getBannerHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr valign=\"top\">\n");
		mailMessage.append("<td bgcolor=\"#FFFFFF\" colspan=\"13\">\n");
		mailMessage.append("<a href=\"%%BASE_URL%%/\" style=\"color:#C00;text-decoration:none;\" target=\"_blank\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/content/dam/bsro/global/images/email/header-ht.jpg\" width=\"675\" height=\"150\" border=\"0\" alt=\"Hibdon Tires Plus.\" />\n");
		mailMessage.append("</a>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr valign=\"top\">\n");
		mailMessage.append("<td bgcolor=\"#FFFFFF\" colspan=\"5\" align=\"center\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("This message contains graphics. If you do not see the graphics <a href=\"%%QUOTE_URL%%\" style=\"color:#C00;\" target=\"_blank\">view the HTML version.</a>\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getTireDetailsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"4\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>%%TIRE_BRAND%% %%TIRE_NAME%%</strong><br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("- %%MILEAGE%% mileage warranty<br />\n");
		mailMessage.append("- %%TIRE_TYPE%%<br />\n");
		mailMessage.append("- Size: %%TIRE_SIZE_INFO%%<br />\n");
		mailMessage.append("- Article Number: %%ARTICLE_NO%%<br />\n");
		mailMessage.append("- Load Index: %%LOAD_INDEX%% (%%LOAD_INDEX_POUNDS%% lbs)<br />\n");
		mailMessage.append("- Speed Rating: %%SPEED_RATING%% (%%SPEED_RATING_MPH%% mph)<br />\n");
		mailMessage.append("- Sidewall: %%SIDE_WALL%%\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getQuoteLineItemsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<table width=\"100%\" border=\"1\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("%%QUOTE_TIRE_QTY_PRICE%% \n");
		mailMessage.append("%%QUOTE_MATCHED_SET_TIRE_PRICES%% \n");
		mailMessage.append("%%QUOTE_TIRE_DISCOUNTS%% \n");
		mailMessage.append("%%QUOTE_TIRE_SUBTOTAL%% \n");
		mailMessage.append("%%QUOTE_WHEEL_BALANCE_PRICE%% \n");
		mailMessage.append("%%QUOTE_TIREMOUNTING_PRICE%% \n");
		mailMessage.append("%%QUOTE_WHEEL_ALIGNMENT_CHECK_PRICE%% \n");
		mailMessage.append("%%QUOTE_TPMS_KIT_PRICE%% \n");
		mailMessage.append("%%QUOTE_TPMS_KIT_LABOR_PRICE%% \n");
		mailMessage.append("%%QUOTE_VALVE_STEM_PRICE%% \n");
		mailMessage.append("%%QUOTE_STATE_ENVIRONMENT_FEE_PRICE%% \n");
		mailMessage.append("%%QUOTE_SCRAP_TIRE_RECYCLING_CHARGE_PRICE%% \n");
		mailMessage.append("%%QUOTE_SHOP_SUPPLIES_PRICE%% \n");
		mailMessage.append("%%QUOTE_TAX_AMOUNT%% \n");
		mailMessage.append("%%QUOTE_SUBTOTAL_PRICE%% \n");	
		mailMessage.append("%%QUOTE_TOTAL_PRICE%% \n");
		mailMessage.append("</table>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getTireQuoteSubTotalHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr bgcolor=\"#E9E9E9\" align=\"right\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>TOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%SUB_TOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getTireQuoteTotalHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr bgcolor=\"#E9E9E9\" align=\"right\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td bgcolor=\"#cccccc\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Best Tire Price</strong><br />\n");
		mailMessage.append("We will match any locally advertised price on tires. If you find a better price within 30 days of purchase, We&#8217;ll give you twice the amount of the difference back.\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("<td bgcolor=\"#fefefe\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Free Lifetime Tire Rotation</strong><br />\n");
		mailMessage.append("When you buy tires from Tires Plus you&rsquo;ll get free lifetime rotations based on manufacturers mileage.\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("<td bgcolor=\"#cccccc\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Road Hazard Protection</strong><br />\n");
		mailMessage.append("Potholes, or hazards? For a one-time charge you can protect your new tires from whatever hazards you face, as long as you own the vehicle. Just ask your Tire Advisor.\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>TOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%TOTAL%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getBingStoreMapHtml() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<div id=\"map\" style=\"background-image:url('http://dev.virtualearth.net/REST/v1/Imagery/Map/Road/%%STORE_LAT%%%2C%%STORE_LNG%%/13?ms=265,160&amp;pp=%%STORE_LAT%%,%%STORE_LNG%%;7&amp;ml=TrafficFlow&amp;fmt=jpeg&amp;mmd=0&amp;key=%%BING_MAPS_KEY%%'); width:220px; height:133px; background-position:-22px -13px;\"></div>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getQuoteFooterHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr bgcolor=\"#E6E6E6\" align=\"center\">\n");
		mailMessage.append("<td colspan=\"5\">\n");
		mailMessage.append("<font color=\"#666666\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("&copy;%%COPY_RIGHT_YEAR%% Tires Plus Total Car Care | <a href=\"%%BASE_URL%%/privacy/\" style=\"color:#C00;\" target=\"_blank\">Privacy Statement</a> | <a href=\"%%BASE_URL%%/legal/\" style=\"color:#C00;\" target=\"_blank\">Legal Notice</a> | <a href=\"%%BASE_URL%%/sign-up-for-email-offers/unsubscribe/\" style=\"color:#C00;\" target=\"_blank\">Unsubscribe</a><br />\n");
		mailMessage.append("Written inquiries may be sent to: Bridgestone Retail Operations, LLC 333 E. Lake Street Bloomingdale, Illinois 60108\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<table border=\"0\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#666666\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<a href=\"http://m.tiresplus.com\" style=\"color:#C00;text-decoration:none;\" target=\"_blank\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/iphone.gif\" width=\"10\" height=\"20\" border=\"0\" alt=\"icon: iphone\" />\n");
		mailMessage.append("</a>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#666666\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<a href=\"%%BASE_URL%%/\" style=\"color:#C00;\" target=\"_blank\">\n");
		mailMessage.append("View Our Mobile Site\n");
		mailMessage.append("</a>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/footer.gif\" width=\"675\" height=\"7\" border=\"0\" alt=\"\" />\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getTireQuoteDisclaimerHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>CODE #%%QUOTE_ID%%</strong>.\n");
		mailMessage.append("Prices valid for 30 days.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("&dagger; Original equipment tires are not available at all stores. Please call store to confirm availability.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}

	@Override
	public String getCfnaHtml() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<a href=\"https://www.cfna.com/web/personalservices/hibdontiresplus\" target=\"_blank\" id=\"cfna1\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/content/dam/bsro/global/images/email/email-quote-ht.png\" width=\"220\" height=\"54\" alt=\"Convenient Credit. Quick online applications.\" border=\"0\" />\n");
		mailMessage.append("</a>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getBatteryQuoteHeaderHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%CURRENT_DATE%%<br /> \n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<font size=\"4\">%%CUSTOMER_NAME%%%%PUNCHMARK%% Battery Quote</font><br />\n");
		mailMessage.append("%%USER_VEHILE%%<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("This is a Special Internet Price. Battery installation and exchange is required.<br />\n");
		mailMessage.append("<font size=\"1\"><strong>CODE #%%QUOTE_ID%%</strong></font><br />\n");
		mailMessage.append("<br />\n");
		mailMessage.append("<strong>Print this quote</strong> and bring it to the store to receive this price.<br />\n");
		mailMessage.append("<br />\n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getAlignmentQuoteHeaderHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%CURRENT_DATE%%<br /> \n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<font size=\"4\">%%CUSTOMER_NAME%%%%PUNCHMARK%% Alignment Quote</font><br />\n");
		mailMessage.append("%%USER_VEHILE%%<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("This is a Special Internet Price. Please be sure to <strong>print this quote</strong> and bring it to the store to receive this price. Here is the Alignment Pricing Quote you requested for your vehicle.\n");
		mailMessage.append("<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<hr />\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getAlignmentCheckArticleNo() {
		return "7004109";
	}
	
	@Override
	public String getTireQuoteURL() {
		return "%%BASE_URL%%/content/bsro/hibdontire/en/tires/tire-quote.html?quoteId=%%QUOTE_ID%%&lw_cmp=int-em_hib_eg-hib-tires-other_em-link";
	}
	
	@Override
	public String getTirePromoURL() {
		return "%%BASE_URL%%/tire_promotion/show_tire_promotion.jsp?id=%%TIRE_PROMO_ID%%";
	}
	
	@Override
	public String getBatteryQuoteURL() {
		return "%%BASE_URL%%/content/bsro/hibdontire/en/repair/batteries/battery-quote.html?quoteId=%%QUOTE_ID%%&lw_cmp=int-em_hib_eg-hib-btry-other_em-link";
	}
	
	@Override
	public String getAlignmentQuoteURL() {
		return "%%BASE_URL%%/content/bsro/hibdontire/en/maintenance/alignment/alignment-quote.html?quoteId=%%QUOTE_ID%%&lw_cmp=int-em_hib_eg-hib-align-other_em-link";
	}
	
	@Override
	public String getStoreDetailURL(States statesMap, Store store) {
		return store.getLocalPageURL();
	}
	
	@Override
	public String getBingMapsKey() {
		return BING_MAPS_KEY;
	}

	@Override
	public String getEmailFrom() {
		return EMAIL_FROM;
	}

	@Override
	public String getImagePath() {
		return IMAGE_PATH;
	}

}
