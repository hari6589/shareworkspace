package com.ses.sample.email;

public class MailMessageTemplate {
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
}
