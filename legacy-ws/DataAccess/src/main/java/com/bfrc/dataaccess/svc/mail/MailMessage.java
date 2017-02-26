package com.bfrc.dataaccess.svc.mail;

import map.States;

import com.bfrc.dataaccess.model.store.Store;

/**
 * @author smoorthy
 *
 */
public abstract class MailMessage {
	
	public abstract String getEmailFrom();
	
	public abstract String getImagePath();
	
	public abstract String getBingMapsKey();
	
	public abstract String getTireQuoteURL();
	
	public abstract String getTirePromoURL();
	
	public abstract String getBatteryQuoteURL();
	
	public abstract String getAlignmentQuoteURL();
	
	public abstract String getBannerHTML();
	
	public abstract String getTireDetailsHTML();
	
	public abstract String getQuoteLineItemsHTML();
	
	public abstract String getBingStoreMapHtml();
	
	public abstract String getCfnaHtml();
	
	public abstract String getQuoteFooterHTML();
	
	public abstract String getTireQuoteDisclaimerHTML();
	
	public abstract String getBatteryQuoteHeaderHTML();
	
	public abstract String getAlignmentQuoteHeaderHTML();
	
	public abstract String getAlignmentCheckArticleNo();
	
	public abstract String getStoreDetailURL(States statesMap, Store store);
	
	public String getTireQuoteMailMessage() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
		mailMessage.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		mailMessage.append("	<head>\n");
		mailMessage.append("	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
		mailMessage.append("	<base href=\"%%BASE_URL%%\" />\n");
		mailMessage.append("	<title>Tire Quote: %%USER_VEHILE%%</title>\n");
		mailMessage.append("	<style type=\"text/css\">\n");
		mailMessage.append("		img { display: block; border-width: 1px; border-color: Black;}\n");
		mailMessage.append("	</style>\n");
		mailMessage.append("	</head>\n");
		mailMessage.append("	<body style=\"background-color:#4D4D4D;margin:0;\">\n");
		mailMessage.append("		<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("						%%BRAND_BANNER_IMAGE%% \n");
		mailMessage.append("						<tr valign=\"top\">\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"220\" valign=\"top\">\n");
		mailMessage.append("								<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("									%%PREFERED_VEHILE%% \n");
		mailMessage.append("									%%PREFERED_STORE%% \n");
		mailMessage.append("								</font>\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\">\n");
		mailMessage.append("								%%QUOTE_HEADER%% \n");
		mailMessage.append("								%%QUOTE_TIRE_DETAIL_INFO%% \n");
		mailMessage.append("								%%QUOTE_LINE_ITEMS%% \n");		
		mailMessage.append("								<br />\n");
		mailMessage.append("									%%QUOTE_SERVICE_GUARANTY%% \n");
		mailMessage.append("									%%QUOTE_DISCLAIMER%% \n");		
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("						</tr>\n");
		mailMessage.append("						%%QUOTE_FOOTER%% \n");
		mailMessage.append("					</table>\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>	\n");
		mailMessage.append("		</table>\n");
		mailMessage.append("	</body>\n");
		mailMessage.append("</html>\n");
		
		return mailMessage.toString();
	}
	
	public String getBatteryQuoteMailMessage() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
		mailMessage.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		mailMessage.append("	<head>\n");
		mailMessage.append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
		mailMessage.append("		<base href=\"%%BASE_URL%%\" />\n");
		mailMessage.append("		<title>Battery Quote: | %%STORE_NAME%%</title>\n");
		mailMessage.append("		<style type=\"text/css\">\n");
		mailMessage.append("			img { display: block; border-width: 1px; border-color: Black;}\n");
		mailMessage.append("		</style>\n");
		mailMessage.append("	</head>\n");
		mailMessage.append("	<body style=\"background-color:#4D4D4D;margin:0;\">\n");
		mailMessage.append("		<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("						%%BRAND_BANNER_IMAGE%% \n");		
		mailMessage.append("						<tr valign=\"top\">\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"220\" valign=\"top\">\n");
		mailMessage.append("								<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("									%%PREFERED_VEHILE%% \n");
		mailMessage.append("									%%PREFERED_STORE%% \n");
		mailMessage.append("								</font><br />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\">\n");
		mailMessage.append("								%%QUOTE_HEADER_HTML%% \n");		
		mailMessage.append("								%%QUOTE_DETAILS_HTML%% \n");		
		mailMessage.append("								%%QUOTE_PRICE_DISCLAIMER%% \n");
		mailMessage.append("								%%QUOTE_APPOINTMENT_HTML%% \n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("						</tr>\n");
		mailMessage.append("						%%QUOTE_FOOTER%% \n");
		mailMessage.append("					</table>\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>	\n");
		mailMessage.append("		</table>\n");
		mailMessage.append("	</body>\n");
		mailMessage.append("</html>\n");
		
		return mailMessage.toString();
	}
	
	public String getAlignmentQuoteMailMessage() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
		mailMessage.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		mailMessage.append("	<head>\n");
		mailMessage.append("	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
		mailMessage.append("	<base href=\"%%BASE_URL%%/\" />\n");
		mailMessage.append("	<title>Alignment Quote: %%USER_VEHILE%% | %%STORE_NAME%%</title>\n");
		mailMessage.append("	<style type=\"text/css\">\n");
		mailMessage.append("		img { display: block; border-width: 1px; border-color: Black;}\n");
		mailMessage.append("	</style>\n");
		mailMessage.append("	</head>\n");
		mailMessage.append("	<body style=\"background-color:#4D4D4D;margin:0;\">\n");
		mailMessage.append("		<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("						%%BRAND_BANNER_IMAGE%% \n");
		mailMessage.append("						<tr valign=\"top\">\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"220\" valign=\"top\">\n");
		mailMessage.append("								<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("									%%PREFERED_VEHILE%% \n");
		mailMessage.append("									%%PREFERED_STORE%% \n");		
		mailMessage.append("								</font>\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\">\n");
		mailMessage.append("								%%QUOTE_HEADER_HTML%% \n");
		mailMessage.append("								%%QUOTE_APPOINTMENT_HTML%% \n");
		mailMessage.append("								%%QUOTE_DETAILS_HTML%% \n");
		mailMessage.append("								%%QUOTE_PRICE_DISCLAIMER%% \n");
		mailMessage.append("							</td>\n");
		mailMessage.append("							<td bgcolor=\"#FFFFFF\" width=\"15\">\n");
		mailMessage.append("								<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"15\" height=\"1\" border=\"0\" />\n");
		mailMessage.append("							</td>\n");
		mailMessage.append("						</tr>\n");
		mailMessage.append("						%%QUOTE_FOOTER%% \n");
		mailMessage.append("					</table>\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>\n");
		mailMessage.append("			<tr valign=\"top\">\n");
		mailMessage.append("				<td bgcolor=\"#4D4D4D\">\n");
		mailMessage.append("					<img src=\"%%BASE_URL%%/%%IMAGE_PATH%%/e/spacer.gif\" alt=\"\" width=\"1\" height=\"15\" border=\"0\" />\n");
		mailMessage.append("				</td>\n");
		mailMessage.append("			</tr>	\n");
		mailMessage.append("		</table>\n");
		mailMessage.append("	</body>\n");
		mailMessage.append("</html>\n");
		
		return mailMessage.toString();
	}
	
	public String getCustomerInfoHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"3\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%CUSTOMER_NAME%%%%PUNCHMARK%% Tire Quote \n");
		mailMessage.append("<br /> \n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}
	
	public String getVehicleDetailsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"3\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>My Vehicle</strong><br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>%%USER_VEHILE%%</strong><br />\n");
		mailMessage.append("%%TIRE_SPEC_INFO%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<br /><br />\n");
		return mailMessage.toString();
	}
	
	public String getTireSpecDetailsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<br /><font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\"><strong>Tire Sizes</strong><br />%%STD_OPT%%:<br /><strong>Size:</strong> %%TIRE_SIZE%% <br /><strong>Speed Rating:</strong> %%SPEED_RATING%% <br /><strong>Load Range:</strong> %%LOAD_RANGE%% <br /><strong>Load Index:</strong> %%LOAD_INDEX%% <br /></font> \n");
		return mailMessage.toString();
	}
	
	public String getStoreDetailsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"3\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>My Store</strong><br />	\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<a href=\"%%STORE_DETAIL_URL%%\" style=\"color:#055BA5;\" target=\"_blank\">\n");
		mailMessage.append("<strong>%%STORE_NAME%%</strong>\n");
		mailMessage.append("</a><br />\n");
		mailMessage.append("#%%STORE_NUMBER%%<br />\n");
		mailMessage.append("%%STORE_ADDRESS%%<br />\n");
		mailMessage.append("%%STORE_CITY%%, %%STORE_STATE%% %%STORE_ZIP%%<br />\n");
		mailMessage.append("%%STORE_PHONE%% \n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("<strong>Hours</strong><br />\n");
		mailMessage.append("%%STORE_HOUR%% \n");
		mailMessage.append("<span class=\"red\">%%STORE_HOLIDAY%%</span><br />\n");
		mailMessage.append("%%STORE_BING_MAP%% \n");
		mailMessage.append("%%CFNA_HTML%% \n");
		mailMessage.append("</font>\n");		
		return mailMessage.toString();
	}
	
	public String getTirePriceHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%TIRE_QTY%% Tires @ &#36;%%TIRE_UNIT_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%TIRE_TOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getMatchedSetTirePriceHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("%%FRONT_TIRE_PRICE%%\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("%%REAR_TIRE_PRICE%%\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getFrontTirePriceHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%TIRE_QTY%% Front Tire @ &#36;%%TIRE_UNIT_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%TIRE_TOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		return mailMessage.toString();
	}
	
	public String getRearTirePriceHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%REAR_TIRE_QTY%% Rear Tire @ &#36;%%REAR_TIRE_UNIT_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%REAR_TIRE_TOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		return mailMessage.toString();
	}
	
	public String getContactStorePricingHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%FRB_TIRE_QTY%% %%FRB_KEY%% Tire\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("***Contact Store\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		return mailMessage.toString();
	}
	
	public String getTirePromotionHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%PROMO_DISPLAY_NAME%% <a href=\"%%PROMO_URL%%\" style=\"color:#055BA5;\" target=\"_blank\">(Details)\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("- &#36;%%TIRE_PROMO_DISCOUNT_AMOUNT%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTireSubTotalHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr bgcolor=\"#E9E9E9\" align=\"right\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>SUBTOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"4\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%SUBTOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTireWheelBalanceHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Computerized Wheel Balance\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%WHEEL_BALANCE_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTireMountingHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Tire Mounting\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("FREE\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getWheelAlignmentHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Tire/Wheel Alignment Check (Art. #7009886)\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("FREE\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTPMSValveServiceKitHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("TPMS Valve Service Kit\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%TPMS_KIT_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getValveStemPriceHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Valve Stem\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%VALVE_STEM_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTPMSValveServiceKitLaborHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("TPMS Valve Service Kit Labor\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%TPMS_KIT_LABOR_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getStateEnvironmentFeeHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("State Environmental Fee\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%STATE_ENVIRONMENT_FEE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getScrapTireRecyclingChargeHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Scrap Tire Recycling Charge\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%SCRAP_TIRE_RECYCLE_FEE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getShopSuppliesHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Shop Supplies\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%SHOP_SUPPLY_FEE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTaxHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Tax\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#DF291B\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("&#36;%%TAX%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTireQuoteTotalHTML() {
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
		mailMessage.append("&#36;%%TOTAL%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		return mailMessage.toString();
	}
	
	public String getTireQuoteHeaderHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%CURRENT_DATE%%<br /> \n");
		mailMessage.append("</font>\n");
		mailMessage.append("%%CUSTOMER_INFO%%\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%USER_VEHILE%%\n");
		mailMessage.append("<br /> \n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("This is a Special Internet Price. Please be sure to <strong>print this quote</strong> and bring it to the store to receive this price. Here is the Tire Pricing Quote you requested for your vehicle.\n");
		mailMessage.append("<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<hr />\n");
		mailMessage.append("<font color=\"#000000\" size=\"3\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Schedule an Appointment<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Set up a time to get new tires put on your vehicle. We&#8217;ve got a store near you with convenient hours.\n");
		mailMessage.append("<strong>%%STORE_PHONE%%</strong> or <a id=\"tire-pricing-quote-email-schedule-appointment\" href=\"%%APPOINTMENT_URL%%\" style=\"color:#055BA5;\" target=\"_blank\">schedule an appointment online</a>.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}
	
	public String getBatteryQuoteDetailsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<table width=\"100%\" border=\"1\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("<td align=\"center\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Battery&nbsp;Description</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"center\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Warranty</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"center\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Article&nbsp;Number</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>Price</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("<td align=\"center\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%BATTERY_DESCRIPTION%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"center\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%BATTERY_WARRANTY_YEARS%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"center\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("%%BATTERY_ARTICLE_NO%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("$%%BATTERY_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("<td colspan=\"3\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Battery Installation (Art. #7012440)\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("$%%BATTERY_INSTALLATION_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr bgcolor=\"#E9E9E9\" align=\"right\">\n");
		mailMessage.append("<td align=\"right\" colspan=\"3\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>SUBTOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"4\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("$%%QUOTE_SUBTOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr bgcolor=\"#E9E9E9\" align=\"right\">\n");
		mailMessage.append("<td colspan=\"3\" align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>TOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("$%%QUOTE_TOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		return mailMessage.toString();
	}
	
	public String getBatteryQuoteScheduleAppointmentHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"3\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Schedule an Appointment<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Set up a time to get a new battery installed in your vehicle. We&#8217;ve got a store near you with convenient hours.\n");
		mailMessage.append("<strong>%%STORE_PHONE%%</strong> or \n");
		mailMessage.append("<a id=\"repair-battery-quote-email-schedule-appointment-online\" href=\"%%APPOINTMENT_URL%%\" style=\"color:#055BA5;\" target=\"_blank\">schedule an appointment online</a>.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<br/>\n");
		return mailMessage.toString();
	}
	
	public String getBatteryQuoteDisclaimerHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Price valid for 30 days and does not include shop supplies, tax or State disposal fee (where applicable).<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<hr />\n");
		return mailMessage.toString();
	}
	
	public String getAlignmentQuoteScheduleAppointmentHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"3\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Schedule an Appointment<br />\n");
		mailMessage.append("</font>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Set up a time for a wheel alignment. We&#8217;ve got a store near you with convenient hours.\n");
		mailMessage.append("<strong>%%STORE_PHONE%%</strong> or <a id=\"repair-alignment-pricing-quote-email-schedule-appointment\" href=\"%%APPOINTMENT_URL%%\" style=\"color:#055BA5;\" target=\"_blank\">schedule an appointment online</a>.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}
	
	public String getAlignmentQuoteDetailsHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<table width=\"100%\" border=\"1\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("Alignment Check (Art.#%%ALIGNMENT_CHECK_ARTICLE%%)\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("FREE\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr bgcolor=\"#E9E9E9\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>%%ALIGNMENT_TYPE%% Alignment</strong> (Art. #%%ALIGNMENT_TYPE_ARTICLE%%)\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>SUBTOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"4\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("$%%QUOTE_SUBTOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("<tr bgcolor=\"#E9E9E9\" align=\"right\">\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\">\n");
		mailMessage.append("<tr valign=\"middle\">\n");
		mailMessage.append("<td>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td>\n");
		mailMessage.append("<font color=\"#000000\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<strong>TOTAL</strong>\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("<td align=\"right\">\n");
		mailMessage.append("<font color=\"#000000\" size=\"5\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("$%%QUOTE_TOTAL_PRICE%%\n");
		mailMessage.append("</font>\n");
		mailMessage.append("</td>\n");
		mailMessage.append("</tr>\n");
		mailMessage.append("</table>\n");
		return mailMessage.toString();
	}
	
	public String getAlignmentQuoteDisclaimerHTML() {
		StringBuilder mailMessage = new StringBuilder();
		mailMessage.append("<font color=\"#000000\" size=\"1\" face=\"Arial, Helvetica, sans-serif\">\n");
		mailMessage.append("<br />\n");
		mailMessage.append("<strong>CODE #%%QUOTE_ID%%</strong>. Price valid for 30 days and does not include shop supplies or sales tax.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("&dagger; Most Vehicles. Subject to in-store equipment availability and employee qualifications to align vehicle. Stores may not be able to perform alignments on vehicles with steering/suspensions altered from manufacturers&rsquo; specifications. Please call store to confirm if you have any questions.\n");
		mailMessage.append("<br /><br />\n");
		mailMessage.append("</font>\n");
		return mailMessage.toString();
	}
	
	public String getQuoteServiceGuaranty() {
		return "";
	}
	
	public String getTireQuoteSubTotalHTML() {
		return "";
	}
}
