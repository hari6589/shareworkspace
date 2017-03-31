package com.ses.sample.email;

import tirequote.States;
import tirequote.Store;

public class FCACMailMessage extends MailMessage {
	public static final String BING_MAPS_KEY = "AlGvGS0ffMJjgLqnheFD2z6MiUGHw9_QzsErZT9qJT21H_FW-mmSVjNh4Vq7MFwV";
	public static final String EMAIL_FROM =  "\"DO-NOT-REPLY\" <webmaster@firestonecompleteautocare.com>";
	public static final String IMAGE_PATH =  "static-fcac/images";
	
	@Override
	public String getBannerHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr valign=\"top\">\n");
		mailMessage.append("<td bgcolor=\"#FFFFFF\" colspan=\"13\">\n");
		mailMessage.append("<font color=\"#666666\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<a href=\"%%BASE_URL%%/?utm_source=email&amp;utm_medium=image&amp;utm_content=header&amp;utm_term=november_offers&amp;utm_campaign=fcac_2011\" style=\"color:#055BA5;text-decoration:none;\" target=\"_blank\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/header-fcac.jpg\" width=\"675\" height=\"150\" border=\"0\" alt=\"Firestone Complete Auto Care - Together, we&#8217;ll keep your car running newer, longer.\" />\n");
		mailMessage.append("</a>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr valign=\"top\">\n");
		mailMessage.append("<td bgcolor=\"#FFFFFF\" colspan=\"5\" align=\"center\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("This message contains graphics. If you do not see the graphics <a id=\"tire-pricing-quote-email-html-version\" href=\"%%QUOTE_URL%%\" style=\"color:#055BA5;\" target=\"_blank\">view the HTML version</a>.\n");
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
		mailMessage.append("%%MILEAGE%% mileage warranty | %%TIRE_TYPE%% Tires |\n");
		mailMessage.append("%%TIRE_SIZE_INFO%%\n");
		mailMessage.append(" | Article Number: %%ARTICLE_NO%% | Load Index: %%LOAD_INDEX%% (%%LOAD_INDEX_POUNDS%% lbs) | Speed Rating: %%SPEED_RATING%% (%%SPEED_RATING_MPH%% mph) | Sidewall: %%SIDE_WALL%%\n");
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
		mailMessage.append("%%QUOTE_TOTAL_PRICE%% \n");		
		mailMessage.append("</table>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getBingStoreMapHtml() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<img src=\"http://dev.virtualearth.net/REST/v1/Imagery/Map/Road/%%STORE_LAT%%,%%STORE_LNG%%/12?ms=220,110&pp=%%STORE_LAT%%,%%STORE_LNG%%;23&ml=TrafficFlow&fmt=jpeg&mmd=0&key=%%BING_MAPS_KEY%%\" alt=\"Map to %%STORE_NAME%%\" />\n");
		return mailMessage.toString();
	}

	@Override
	public String getCfnaHtml() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<a href=\"https://www.cfna.com/web/personalservices/firestonecompleteautocare\" rel=\"nofollow\" onClick=\"\n");
		mailMessage.append("var s=s_gi(s_account);\n");
		mailMessage.append("s.linkTrackVars='eVar36,events';\n");
		mailMessage.append("s.linkTrackEvents='event32';\n");
		mailMessage.append("s.eVar36='TR_IL_TOP_EC';\n");
		mailMessage.append("s.events='event32';\n");
		mailMessage.append("s.tl(this,'o','CFNA');\" target=\"_blank\"><img src=\"/%%IMAGE_PATH%%/email-quote-cfna.png\" border=\"0\" alt=\"Convenient Credit. Quick Online Application.\" /></a>\n");
		return mailMessage.toString();
	}
	
	@Override
	public String getQuoteFooterHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr bgcolor=\"#E6E6E6\" align=\"center\">\n");
		mailMessage.append("<td colspan=\"5\">\n");
		mailMessage.append("<font color=\"#666666\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("&copy;%%COPY_RIGHT_YEAR%% Firestone Complete Auto Care | <a id=\"tire-pricing-quote-email-privacy-statement\" href=\"%%BASE_URL%%/privacy-statement/\" style=\"color:#055BA5;\" target=\"_blank\">Privacy Statement</a> | <a id=\"tire-pricing-quote-email-legal-notice\" href=\"%%BASE_URL%%/legal-notice/\" style=\"color:#055BA5;\" target=\"_blank\">Legal Notice</a> | <a id=\"tire-pricing-quote-email-unsubscribe\" href=\"%%BASE_URL%%/signup/unsubscribe.jsp\" style=\"color:#055BA5;\" target=\"_blank\">Unsubscribe</a>\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("Written inquiries may be sent to: Bridgestone Retail Operations, LLC 333 E. Lake Street Bloomingdale, Illinois 60108\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<table border=\"0\" align=\"center\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#666666\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<a id=\"tire-pricing-quote-email-fcacmobile\" href=\"http://m.fcacmobile.com\" style=\"color:#055BA5;text-decoration:none;\" target=\"_blank\">\n");
		mailMessage.append("<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/iphone.gif\" width=\"10\" height=\"20\" border=\"0\" alt=\"icon: iphone\" />\n");
		mailMessage.append("</a>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#666666\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<a id=\"tire-pricing-quote-email-fcacmobile-2\" href=\"http://m.fcacmobile.com\" style=\"color:#055BA5;\" target=\"_blank\">\n");
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
		mailMessage.append("This is a Special Internet Price. In-Store Battery Installation Required.<br />\n");
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
		return "7009886";
	}
	
	@Override
	public String getTireQuoteURL() {
		return "%%BASE_URL%%/content/bsro/fcac/en/tires/tire-quote.html?quoteId=%%QUOTE_ID%%&lw_cmp=int-em_fcac_eg-fcac-tires-other_em-link";
	}
	
	@Override
	public String getTirePromoURL() {
		return "%%BASE_URL%%/offers/tire-coupons/?id=%%TIRE_PROMO_ID%%";
	}
	
	@Override
	public String getBatteryQuoteURL() {
		return "%%BASE_URL%%/content/bsro/fcac/en/maintain/batteries/battery-quote.html?quoteId=%%QUOTE_ID%%&lw_cmp=int-em_fcac_eg-fcac-btry-other_em-link";
	}
	
	@Override
	public String getAlignmentQuoteURL() {
		return "%%BASE_URL%%/content/bsro/fcac/en/repair/alignment/alignment-quote.html?quoteId=%%QUOTE_ID%%&lw_cmp=int-em_fcac_eg-fcac-align-other_em-link";
	}
	
	@Override
	public String getStoreDetailURL(States statesMap, Store store) {
		return store.getLocalPageURL();
	}
	
	@Override
	public String getEmailFrom() {
		return EMAIL_FROM;
	}

	@Override
	public String getImagePath() {
		return IMAGE_PATH;
	}
	
	@Override
	public String getBingMapsKey() {
		return BING_MAPS_KEY;
	}
}
