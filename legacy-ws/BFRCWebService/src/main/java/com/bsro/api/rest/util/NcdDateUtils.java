package com.bsro.api.rest.util;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

/**
 * Utility class to help with dates 
 * @author Brad Balmer
 *
 */
public class NcdDateUtils {

	private static SimpleDateFormat ncdFromDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat ncdInvoiceDate = new SimpleDateFormat("M/d/yyyy HH:mm:ss a");
	private static Logger log = Logger.getLogger(NcdDateUtils.class.getName());
	
	public static String getNcdFromDt(Long val) {
		if(val == null || val == 0) 
			return null;

		return ncdFromDate.format(new java.util.Date(val));
	}
	
	public static java.util.Date getInvoiceDate(String invcDt) {
		try {
		return ncdInvoiceDate.parse(invcDt);
		}catch(Exception E) {
			return null;
		}
	}
}
