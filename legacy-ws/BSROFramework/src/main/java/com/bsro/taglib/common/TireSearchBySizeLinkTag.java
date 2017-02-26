package com.bsro.taglib.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tag that generates the Tire Search by Size Link
 *
 */
public class TireSearchBySizeLinkTag extends TagSupport {
	
	private static final long serialVersionUID = 1L;
	private final static Log	log	= LogFactory.getLog(StoreDetailLinkTag.class);


	/*
	 * Generates the link itself based on the following format:
	 * /tire_pricing/search.htm?method=size&cross=[cross]&aspect=[aspect]&rim=[rim]
	 * 
	 * The full link is URL encoded, lower case, and any spaces are replaced with a dash.
	 */
	public static String generateTireSearchBySizeLink (String size) {

		StringBuilder urlString = generateTireSearchBySizeString(size);
		urlString.insert(0, "/");

		return urlString.toString();
	}
	
	/*
	 * Generates the link itself based on the following format:
	 * /tire_pricing/search.htm?method=size&cross=[cross]&aspect=[aspect]&rim=[rim]
	 * 
	 * The full link is URL encoded, lower case, and any spaces are replaced with a dash.
	 */
	public static String generateTireSearchBySizeLink (String appRoot, String size) {

		StringBuilder urlString = generateTireSearchBySizeString(size);
		urlString.insert(0, appRoot);
		
		return urlString.toString();
	}
	
	private static StringBuilder generateTireSearchBySizeString(String size) {
		StringBuilder urlString = new StringBuilder(80);
		urlString.append("tire_pricing/search.htm?method=size");
		
		try {
			if (size != null) {
				int aspectStartindex = size.indexOf("/");
				if (aspectStartindex != -1) {
					urlString.append("&cross=");
					urlString.append(URLEncoder.encode(size.substring(0, aspectStartindex), "UTF-8"));
				}
				
				int rimStartindex = size.indexOf("-");
				if (rimStartindex != -1) {
					urlString.append("&aspect=");
					urlString.append(URLEncoder.encode(size.substring(aspectStartindex+1, rimStartindex), "UTF-8"));
					
					urlString.append("&rim=");
					urlString.append(URLEncoder.encode(size.substring(rimStartindex+1), "UTF-8"));
				}
			} else {
				urlString.append(URLEncoder.encode("cross=&aspect=&rim=", "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			log.warn("Unable to encode URL parameters for Tire Search by Size Link, therefore it was generated without encoding.", e);
		}
		
		return urlString;
	}

}
