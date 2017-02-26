package com.bfrc.dataaccess.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateUtils {
	
	public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat YYYYMMDDHH24MISS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Converts a java.util.Date to a java.sql.Date
	 * @param date
	 * @return
	 */
	public static Date convertSqlToUtil(java.util.Date date) {
		if(date == null) return null;
		return new Date(date.getTime());
	}
	
	public static String format(java.util.Date date, SimpleDateFormat sdf) {
		String result = sdf.format(date);
		return result;
	}

	/**
	 * Converts a java.util.Data value from the current time zone to GMT
	 * @param dateToBeConvertedToGMT
	 * @return GMT Date
	 */
	public static java.util.Date convertToGmt(java.util.Date dateToBeConvertedToGMT) {
		DateTime dt = new DateTime(dateToBeConvertedToGMT.getTime());
	    // translate to GMT local time
	    DateTime dtGmt = dt.withZone(DateTimeZone.forID("Etc/GMT"));
	    
	    return new java.util.Date(dtGmt.getMillis());
	}
}
