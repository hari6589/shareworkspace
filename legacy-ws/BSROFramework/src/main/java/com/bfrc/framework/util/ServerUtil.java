package com.bfrc.framework.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.web.context.WebApplicationContext;

import com.bfrc.Config;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.email.EmailSignup;

public class ServerUtil {
	private static String _ip = null;
	private static String _hostname = null;
	public static boolean DEBUG = false;
	private static final String WS_TOKENS = "d3Mtd0YyZlZXTU1xbw==|d3MtNkkxVm1nZDE4Mg==|d3MteHBOdk9oMWV6bA==|d3MtRUY4bGhzNWhwZg==|d3MtOTFMOGtjYVF0Yg==|d3MtTW5MZWFORDFkdg==|d3MtZkx4SXE2Y1BObg==|d3Mtd2Uyd2YwTDZ0bQ==|d3Mtbm1OMjRWZm12bQ==|d3MtSDA1cHIwY09CeA==|d3MtR1o4NjR3bUdUOA==|d3MtV2MzaDE4QXY4aA==|d3MtTE11Q3RESFB3dA==|d3MtRE8yMVcxZTlvRg==|d3MtaGFyTTZtYlFSSg==|d3MtU0JZNGx3NmlMRQ==|d3MtVXQ3UW5telJzVg==|d3MtZUhQR2dkcXcxZA==|d3MtY1I2NHkyUzRTRw==|d3MtbkVZYzl5WmwzWg==";
	public static boolean TIRE_SUPPRESSION = true;
	public static final String PROXY_HOST_NAME = "prxy-ch-bfr.bfr.bfrco.com";
	public static final String PROXY_PORT = "8080";

	public static void debug(Object msg) {
		if (DEBUG)
			System.out.println(msg);
	}

	public static String getHostname() {
		if (_hostname == null)
			loadAddress();
		return _hostname;
	}

	public static String getIP() {
		if (_ip == null)
			loadAddress();
		return _ip;
	}

	private static void loadAddress() {
		if (_ip != null && _hostname != null)
			return;
		String hostName = null;
		int hostNameIndex = 0;
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			_ip = thisIp.getHostAddress();
			hostName = thisIp.getHostName();
			if (hostName == null)
				return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		hostNameIndex = hostName.indexOf(".");
		if (hostNameIndex > 0)
			hostName = hostName.substring(0, hostNameIndex);
		hostName = hostName.toLowerCase();
		_hostname = hostName;
	}
	
	public static boolean isLocalhost(){
		if (getHostname() != null && getHostname().startsWith("localhost"))
			return true;
		return false;
	}
	
	public static boolean isProxyServer(){
		boolean status = false;
		try {
			InetAddress addr=InetAddress.getByName(PROXY_HOST_NAME);	
			status=addr.isReachable(1000); //1 sec response time
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 								

		return status;
	}


	public static boolean isProduction() {
		if (getHostname() != null
				&& (getHostname().startsWith("crmapp") || getHostname()
						.startsWith("crmstatic")))
			return true;
		return false;
	}

	public static boolean isBSRODev() {
		if (getHostname() != null && getHostname().startsWith("crmdev"))
			return true;
		return false;
	}

	public static boolean isBSROQa() {
		if (getHostname() != null && getHostname().startsWith("crmcert"))
			return true;
		return false;
	}
	
	public static boolean isBSROLatysis() {
		if (getHostname() != null && getHostname().startsWith("crml"))
			return true;
		return false;
	}

	public static boolean isBSRO() {
		if (isProduction() || isBSROLatysis() || isBSRODev() || isBSROQa() || isLocalhost()) {
			return true;
		} else {
			return false;
		}
	}
	

	public static boolean isLCDev(HttpServletRequest request) {
		if (request.getServerName() != null && request.getServerName().startsWith("localhost"))
			return true;
		return false;
	}
	public static boolean isLCStaging(HttpServletRequest request) {
		if (request.getServerName() != null && request.getServerName().startsWith("stage"))
			return true;
		return false;
	}

	public static boolean forceHttps(HttpServletRequest request, HttpServletResponse response) {
		// EXAMPLE: http://hostname.com:80/mywebapp/servlet/MyServlet/a/b;c=123?d=789
	    String scheme = request.getScheme();             // http
	    
		if (!"https".equals(scheme)) {		
			// http://hostname.com:80/mywebapp/servlet/MyServlet/a/b;c=123?d=789
		    String serverName = request.getServerName();     // hostname.com
		    int serverPort = request.getServerPort();        // 80
		    String contextPath = request.getContextPath();   // /mywebapp
		    String servletPath = request.getServletPath();   // /servlet/MyServlet
		    String pathInfo = request.getPathInfo();         // /a/b;c=123
		    String queryString = request.getQueryString();          // d=789

		    String url = "";

			url = "https://"+serverName;
			url = url + contextPath + servletPath;
			
		    if (pathInfo != null) {
		        url = url + pathInfo;
		    }
			if (queryString != null) {
				url = url + "?" + queryString;
			}
			
			try {
				response.sendRedirect(url);
				return true;
			} catch (Exception ex) {
				// do nothing
			}
		}
		return false;
	}

	public static boolean isNullOrEmpty(String str) {

		if (str == null || str.trim().equals("") || str.trim().equals("null")) {
			return true;
		}
		return false;
	}

	public static boolean isNullOrEmpty(Object obj) {

		if (obj == null)
			return true;
		return isNullOrEmpty(obj.toString());
	}

	public static String replace(String string, String from, String to) {
		if (string == null || from == null || from.equals("") || to == null)
			return string;
		StringBuffer buf = new StringBuffer(2 * string.length());
		int previndex = 0;
		int index = 0;
		int flen = from.length();
		while (true) {
			index = string.indexOf(from, previndex);
			if (index == -1) {
				buf.append(string.substring(previndex));
				break;
			}
			buf.append(string.substring(previndex, index) + to);
			previndex = index + flen;
		}
		return buf.toString();
	}

	public static String formatDecimal(double d, String pattern) {
		java.text.DecimalFormat nf = new java.text.DecimalFormat(pattern);
		return nf.format(new Double(d));
	}

	public static String[] tokenToArray(String s, String delim) {
		if (s == null)
			return null;
		java.util.StringTokenizer st = new java.util.StringTokenizer(s, delim);
		Vector vector = new Vector();
		String t = null;
		while (st.hasMoreTokens()) {
			t = st.nextToken();
			vector.addElement(t);
			// LogUtil.log("elemt="+t);
		}
		String[] rt = new String[vector.size()];
		vector.copyInto(rt);
		return rt;
	}

	public static String dateFormat(java.util.Date date, String pattern) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
			return simpledateformat.format(date);
		}
	}

	public static Date parseDate(String s, String fomat) {
		if (s == null) {
			return null;
		} else {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(fomat);
			Date dt = null;
			try {
				dt = simpledateformat.parse(s);
			} catch (Exception ex) {
			}
			return dt;
		}
	}

	public static String arrayToString(String[] a, String separator) {
		if (a == null || separator == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		if (a.length > 0) {
			result.append(a[0]);
			for (int i = 1; i < a.length; i++) {
				result.append(separator);
				result.append(a[i]);
			}
		}
		return result.toString();
	}

	public static String sanitize(String inStr) {
		if (inStr != null) {
			inStr = replace(inStr, "&", "&amp;");
			inStr = replace(inStr, "<", "&lt;");
			inStr = replace(inStr, ">", "&gt;");
			inStr = replace(inStr, "\"", "&quot;");
			inStr = replace(inStr, "#", "&#35;");
			// inStr = replace(inStr, "(", "&#40;");
			// inStr = replace(inStr, ")", "&#41;");
			inStr = replace(inStr, "\r", ""); // remove carriage return
			inStr = replace(inStr, "\n", ""); // remove line feed
			return inStr;
		}
		return "";
	}

	// check if a page request from a crawler
	public static boolean isWebCrawler(HttpServletRequest request) {
		boolean flag = false;
		if (request == null)
			return false;
		if ("1".equals(request.getParameter("sw6pH292phETUYaPuPhEPres")))
			return true;// dbug purpose
		String s = request.getHeader("User-Agent");
		if (s == null)
			s = "";
		String ls = s.toLowerCase();
		// System.out.println("								"+s);
		// --- googlebot, yahoobot, etc --//
		if (ls.indexOf("bot") >= 0 || ls.indexOf("slurp") >= 0
				|| ls.indexOf("crawler") >= 0)
			flag = true;
		request.setAttribute("isWebCrawler", new Boolean(flag));// used for JSTL
		return flag;
	}

	// --- generate a 13 digits ID ---//
	public static String generateUID() {
		return generateUID(13);
	}

	// --- generate digits ID ---//
	public static String generateUID(int numberOfDigits) {
		if (numberOfDigits <= 0)
			numberOfDigits = 13;
		try {
			long d = (long) Math.pow(10, numberOfDigits);
			String s = String
					.valueOf((long) (System.currentTimeMillis() + (Math
							.random() * d)));
			if (s.length() > numberOfDigits)
				s = s.substring(0, numberOfDigits);
			return s;
		} catch (Exception nsae) {

		}
		return null;
	}

	static String[] monthName = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	public static String getMonthName(int m) {
		if (m > 12 || m < 1)
			return null;
		return monthName[m - 1];
	}

	// check if an unsafe parameter
	public static boolean hasUnsafeCode(String data) {
		if (data != null && data.toLowerCase().indexOf(": ") >= 0)
			return true;
		return false;
	}

	// check if an unsafe parameters
	public static boolean hasUnsafeCode(String[] params) {
		if (params == null)
			return false;
		for (int i = 0; i < params.length; i++) {
			if (hasUnsafeCode(params[i])) {
				return true;
			}
		}
		return false;
	}

	public static String getParameter(HttpServletRequest req, String name) {
		try {
			return com.bfrc.security.Encode.html(req.getParameter(name));
		} catch (Exception ex) {
		}
		return null;
	}

	public static String[] getParameterValues(HttpServletRequest req,
			String name) {
		try {
			return com.bfrc.security.Encode.html(req.getParameterValues(name));
		} catch (Exception ex) {
		}
		return null;
	}

	static Map<String, String> bsroSites;

	public static Map<String, String> getBSROSites() {
		if (bsroSites == null) {
			bsroSites = new HashMap<String, String>();
			bsroSites.put("TP", "www.tiresplus.com");
			bsroSites.put("FCAC", "www.firestonecompleteautocare.com");
			bsroSites.put("FFC", "www.firestonefleetcare.com");
			bsroSites.put("BFM", "www.bfmastercare.com");
			bsroSites.put("ET", "www.experttire.com");
			bsroSites.put("LT", "www.lemanstire.com");
			bsroSites.put("BFRC", "www.bfrc.com");
			bsroSites.put("ETIRE", "www.etire.com");
			bsroSites.put("PPS", "www.partnersplussavings.com");
			bsroSites.put("PTT", "www.pennytiretest.com");
			bsroSites.put("HT", "www.hibdontire.com");
		}
		return bsroSites;
	}

	public static String populateEmailMessage(String file,
			Map<String, String> data) {
		java.io.FileInputStream fis = null;
		java.io.BufferedReader br = null;
		String line = null;
		StringBuffer sb = new StringBuffer();

		try {
//			File file1 = new File(file);
//			Util.debug(file1.getAbsolutePath());
			fis = new java.io.FileInputStream(file);
			br = new java.io.BufferedReader(new java.io.InputStreamReader(fis));
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			String s = sb.toString();
			for (String key : data.keySet()) {
				String value = data.get(key);
				if (s.contains("%%" + key + "%%") && value != null) {
					try {
						s = s.replace("%%" + key + "%%", value);
					} catch (Exception ex) {
						//
					}
				}
			}
			return s;
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (br != null)
					br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	public static String populateEmailMessageContent(String content,
			Map<String, String> data) {

		try {
			for (String key : data.keySet()) {
				String value = data.get(key);
				if (content.contains("%%" + key + "%%") && value != null) {
					try {
						content = content.replace("%%" + key + "%%", value);
					} catch (Exception ex) {
						//
					}
				}
			}
			return content;
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		return "";
	}
	public static String grabPage(String url, String method, String data) {
		if (url == null)
			return "";
		java.net.URL url1 = null;
		java.net.URLConnection conn = null;
		java.io.OutputStreamWriter osw = null;
		BufferedReader dis = null;
		try {
			url1 = new java.net.URL(url);
			conn = url1.openConnection();
			if ("post".equalsIgnoreCase(method) && data != null) {
				conn.setUseCaches(false);
				conn.setDoOutput(true);
				osw = new java.io.OutputStreamWriter(conn.getOutputStream());
				osw.write(data);
				osw.flush();
			}
			StringBuffer buf = new StringBuffer();
			String inputLine;
			dis = new java.io.BufferedReader(new java.io.InputStreamReader(
					conn.getInputStream()));
			while ((inputLine = dis.readLine()) != null) {
				buf.append(inputLine + "\n");
				// System.out.println(inputLine);
			}
			if (osw != null) {
				osw.close();
			}
			dis.close();
			return buf.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (osw != null)
					osw.close();
				if (dis != null)
					dis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String grabPage(String uri) {
		return grabPage(uri, null, null);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Wrapper routines to handle malformed user input. //
	// 1. strip away non-printing characters //
	// 2. replaced with a default value, if malformed (missing, null, or invalid
	// type). //
	// Tip: you can use default value as a sentinel value for checks, all
	// without the //
	// need for explicit exception handling. - ecrisostomo //
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param param
	 *            The user-input-string to be "normalized".
	 * @param defval
	 *            The default value to return if param is malformed.
	 * @return String
	 */
	public static String getParam(String param, String defval) {
		String str = StringUtils.trimToNull(param);
		return (str != null) ? str : defval;
	}

	/**
	 * @param param
	 *            The user-input-string to be "normalized", and converted to an
	 *            int.
	 * @param defval
	 *            The default value to return if param is malformed or a
	 *            negative integer.
	 * @return int
	 * 
	 *         Note: you can use GenericValidator.isInt(str) if you wish to
	 *         support negative values.
	 */
	public static int getParam(String param, int defval) {
		String str = StringUtils.trimToNull(param);
		// return (StringUtils.isNumeric(str)) ? Integer.parseInt(str) : defval;
		return NumberUtils.toInt(str, defval);
	}

	public static long getParam(String param, long defval) {
		String str = StringUtils.trimToNull(param);
		// return (StringUtils.isNumeric(str)) ? Integer.parseInt(str) : defval;
		return NumberUtils.toLong(str, defval);
	}

	/**
	 * param must be within the specified positive integer range. Otherwise,
	 * return defval (which, for instance, can be -1 if using it as a sentinel
	 * value).
	 * <p>
	 * 
	 * @param param
	 *            The user-input-string to be "normalized", and converted to an
	 *            int.
	 * @param defval
	 *            The default value to return if param is malformed or not
	 *            within range.
	 * @param min
	 *            defines the minimum value (edge of the range, inclusive).
	 * @param max
	 *            defines the maximum value (edge of the range, inclusive).
	 * @return int
	 */
	public static int getParam(String param, int defval, int min, int max) {
		String str = StringUtils.trimToNull(param);
		if (str == null) {
			return defval;
		}
		try {
			IntRange range = new IntRange(min, max);
			int x = Integer.parseInt(str);
			if (range.containsInteger(x)) {
				return x;
			} else {
				return defval;
			}
		} catch (NumberFormatException nfe) {
			return defval;
		}
	}

	/**
	 * @param param
	 *            The user-input-string to be "normalized", and converted to a
	 *            double.
	 * @param defval
	 *            The default value to return if param is malformed.
	 * @return double
	 */
	public static double getParam(String param, double defval) {
		String str = StringUtils.trimToNull(param);
		// return (GenericValidator.isDouble(str)) ? Double.parseDouble(str) :
		// defval;
		return NumberUtils.toDouble(str, defval);
	}

	/**
	 * @param param
	 *            The user-input-string to be "normalized", and converted to an
	 *            int.
	 * @param defval
	 *            The default value to return if param is malformed or not
	 *            within range.
	 * @param min
	 *            defines the minimum value (edge of the range, inclusive).
	 * @param max
	 *            defines the maximum value (edge of the range, inclusive).
	 * @return int
	 */
	public static double getParam(String param, double defval, double min,
			double max) {
		String str = StringUtils.trimToNull(param);
		if (str == null) {
			return defval;
		}
		try {
			DoubleRange range = new DoubleRange(min, max);
			double x = Double.parseDouble(str);
			if (range.containsDouble(x)) {
				return x;
			} else {
				return defval;
			}
		} catch (NumberFormatException nfe) {
			return defval;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// Wrapper routines: request-parameter versions of the above //
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @param request
	 *            The HTTP request we are processing
	 * 
	 *            Example usage in a jsp file: int rowsPerPage =
	 *            getParam(request, "rowsPerPage", 20); double discount =
	 *            getParam(request, "discount", 0D, .1D, .5D);
	 */
	public static String getParam(ServletRequest request, String param,
			String defval) {
		return getParam(request.getParameter(param), defval);
	}

	public static int getParam(ServletRequest request, String param, int defval) {
		return getParam(request.getParameter(param), defval);
	}

	public static int getParam(ServletRequest request, String param,
			int defval, int min, int max) {
		return getParam(request.getParameter(param), defval, min, max);
	}

	public static double getParam(ServletRequest request, String param,
			double defval) {
		return getParam(request.getParameter(param), defval);
	}

	public static double getParam(ServletRequest request, String param,
			double defval, double min, double max) {
		return getParam(request.getParameter(param), defval, min, max);
	}

	public static boolean isValidWebServiceToken(String token) {
		if (com.bfrc.framework.util.StringUtils.isNullOrEmpty(token))
			return false;
		return WS_TOKENS.contains(token);
	}

	public static boolean sendConfirmation(ServletContext application,
			EmailSignup e) throws Exception {
		try {
			Config thisConfig = (Config) Config.locate(application, "config");
			Mail mail = new Mail();
			mail.setHtml(true);
			ContactDAO contactDAO = (ContactDAO) Config.locate(application,
					"contactDAO");
			mail.setFrom(contactDAO.getFrom());
			String subject = contactDAO.getSubject(new Integer(16));
			mail.setSubject(subject);
			mail.setTo(new String[] { e.getEmailAddress() });
			String realPath = application.getRealPath("");
			String file = realPath + "/e/email_signup_confirm.html";
			Map data = new HashMap();
			data.put("PAGE_TITLE", subject);
			data.put("FIRST_NAME", e.getFirstName());
			data.put("LAST_NAME", e.getLastName());
			data.put("OPTIN_CODE", e.getOptinCode());
			if (!com.bfrc.framework.util.StringUtils.isNullOrEmpty(e
					.getEmailAddress())) {
				data.put("EMAIL_ADDRESS", e.getEmailAddress());
			} else {
				data.put("EMAIL_ADDRESS", "");
			}
			String body = ServerUtil.populateEmailMessage(file, data);
			mail.setBody(body);
			MailManager mailManager = (MailManager) Config.locate(application,
					"mailManager");
			mailManager.sendMail(mail);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static byte[] grabWebContent(String url, String method, String data) {
		if (url == null)
			return null;
		java.net.URL url1 = null;
		java.net.URLConnection conn = null;
		java.io.OutputStreamWriter osw = null;
		DataInputStream in = null;
		try {
			url1 = new java.net.URL(url);
			conn = url1.openConnection();
			if ("post".equalsIgnoreCase(method) && data != null) {
				conn.setUseCaches(false);
				conn.setDoOutput(true);
				osw = new java.io.OutputStreamWriter(conn.getOutputStream());
				osw.write(data);
				osw.flush();
			}
			in = new DataInputStream(conn.getInputStream());
			byte[] buffer = new byte[conn.getContentLength()];
			in.readFully(buffer);
			if (osw != null) {
				osw.close();
			}
			in.close();
			return buffer;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (osw != null)
					osw.close();
				if (in != null)
					in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] grabWebContent(String uri) {
		return grabWebContent(uri, null, null);
	}

	public static boolean isValidImageType(String contentType) {
		if (contentType == null)
			return true;
		if (contentType.contains("gif"))
			return true;
		else if (contentType.contains("jpeg"))
			return true;
		else if (contentType.contains("png"))
			return true;
		else if (contentType.contains("octet-stream"))
			return true;
		return false;
	}

	public static String getParameter(ServletRequest req, String name) {
		try {
			return com.bfrc.security.Encode.html(req.getParameter(name));
		} catch (Exception ex) {
		}
		return null;
	}

	public static String[] getParameterValues(ServletRequest req, String name) {
		try {
			return com.bfrc.security.Encode.html(req.getParameterValues(name));
		} catch (Exception ex) {
		}
		return null;
	}

	public static String getQueryString(ServletRequest request) {
		String qs = "";
		try {
			java.util.Enumeration e = request.getParameterNames();
			if (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String value = request.getParameter(name);
				qs += "?" + name + "="
						+ java.net.URLEncoder.encode(value, "utf-8");
			}
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String value = request.getParameter(name);
				qs += "&" + name + "="
						+ java.net.URLEncoder.encode(value, "utf-8");
			}

			if (qs == null || "".equals(qs.trim()))
				qs = "";
		} catch (Exception ex) {
		}
		return qs;
	}

	public static void sendErrorEmail(Mail mail, String invalidEmails,
			MailManager mailManager, WebApplicationContext applicationCtx) {
		try {
			if (invalidEmails != null && invalidEmails.length() > 0) {
				Mail mail2 = new Mail();
				mail2.setAttachments(mail.getAttachments());
				mail2.setFrom(mail.getFrom());
				mail2.setHtml(mail.isHtml());
				mail2.setReplyTo(mail.getReplyTo());
				mail2.setSubject(mail.getSubject());
				int feedbackID = 0;
		  	      String strFeedbackID = applicationCtx.getServletContext().getInitParameter("feedbackID");
		  		  try{
		  			  feedbackID = Integer.parseInt(strFeedbackID);
		  		  }catch(Throwable ex){
		  			  feedbackID=85;//default to TP
		  		  }
		  		ContactDAO contactDAO=(ContactDAO)applicationCtx.getBean("contactDAO"); 
		  		 List to = contactDAO.getToAddresses(new Integer(feedbackID));
	  			  if( to != null && to.size() > 0 ){
	  			    mail2.setTo(listToArray(to));
	  			  }
	  			  else{ //always set the To
	  			mail2.setTo(new String[] { "marketingitsupport@bfrc.com" });
	  			    
	  			  }
	  			 List cc = contactDAO.getCCAddresses(new Integer(feedbackID));
	  			  if( cc != null && cc.size() > 0 )
	  			    mail2.setCc(listToArray(cc));
	  			  List bcc = contactDAO.getCCAddresses(new Integer(feedbackID));
	  			  if( bcc != null && bcc.size() > 0 )
	  			    mail2.setBcc(listToArray(bcc));
				mail2.setBody(mail.getBody() + "\n\n Emails not valid: \n"+ invalidEmails);		
				
				mailManager.sendMail(mail2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String[] sanitizeInvalidEmails(String[] emails, StringBuffer invalid, String contactType) {
		List<String> validEmails = new ArrayList<String>();
		
		if(emails.length > 0) {
			for(int i=0; i<emails.length; i++) {
				String email = emails[i];
				try {
					
					if(!com.bfrc.framework.util.StringUtils.validEmail(email, true)) {
						invalid.append("Email " + contactType + " not valid, offending string: " + email + "\n");		
					}
					else {
						validEmails.add(email);
					}
				} catch (Exception e) {
					Util.debug("Error validating " + email + " in ServerUtil.java \n"+ e.getLocalizedMessage());
				}
			}
		}
		
		return listToArray(validEmails);
	}
	
	 private static String[] listToArray(List l){
	    	//fix the class cast exception
	    	if(l != null && l.size()> 0){
	    		String[] ss = new String[l.size()];
	    		int i=0;
	    		for(Iterator it = l.iterator(); it.hasNext();i++){
	    			ss[i] = (String) it.next();
	    		}
	    		return ss;
	    	}
	    	return null;
	    }
	 
		public static String getMostRelevantUri(HttpServletRequest request) {
			String uri = "unknown";
		    
			try {
				if (request != null) {
				    // using getAttribute allows us to get the orginal url out of the page when a forward has taken place.
				    String queryString = (String)request.getAttribute("javax.servlet.forward.query_string");
				    String requestURI = (String)request.getAttribute("javax.servlet.forward.request_uri");
				    
				    if(requestURI == null) {
				    	// using getAttribute allows us to get the orginal url out of the page when a include has taken place.
				    	queryString = (String)request.getAttribute("javax.servlet.include.query_string");
				    	requestURI = (String)request.getAttribute("javax.servlet.include.request_uri");
				    }
				    
				    if(requestURI == null) {
				    	queryString = (String)request.getQueryString();
				    	requestURI = request.getRequestURI();
				    }
	
					uri = requestURI+(queryString == null ? "" : "?"+queryString);
				}
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
			
			return uri;
		}	
		
		public static boolean isValidYear(String year) {
			   if(year == null) return false;
		   	   String yearPattern = "\\d{4}";
		       return year.matches(yearPattern);
		   }
		public static boolean isNumber(String num) {
			   if(num == null) return false;
		   	   String pattern = "\\d{1,}";
		       return num.matches(pattern);
		   }
}
