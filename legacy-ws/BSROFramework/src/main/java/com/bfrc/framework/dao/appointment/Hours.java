/*
 * Hours.java
 *
 * Created on June 18, 2002, 1:23 PM
 */

package com.bfrc.framework.dao.appointment;

/**
 *
 * @author  eak
 * @version 
 */
public class Hours implements com.bfrc.Bean {

    public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY = 4;
    public static final int SATURDAY = 5;
    public static final int SUNDAY = 6;
    
    // This method converts the java.util.Calendar 
    // constants to array positions
    public static int convert(int calendarConstant) {
        int temp = calendarConstant - 2;
        if(temp < 0)
            temp = 6;
        return temp;
    }

    public static final String[] DAY_LABEL = {
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    };
    
    public static final String[] DAY_ABBREV = {
        "MON",
        "TUE",
        "WED",
        "THU",
        "FRI",
        "SAT",
        "SUN"
    };
    
    public static final String[] DAY_LABEL_ABBREV = {
        "M",
        "T",
        "W",
        "R",
        "F",
        "Sa",
        "Su"
    };
    
    protected String start = null;
    protected String stop = null;
    protected String zone = null;
    
    /** Creates new Hours */
    public Hours() {
    }

    public String getStart() {
        return this.start;
    }
    
    public String getStop() {
        return this.stop;
    }
    
    public void setStart(String in) {
    	this.start = in;
    }
    
    public void setStop(String in) {
    	this.stop = in;
    }
    
    public int getStartAsInt() {
        return toInt(this.start);
    }
    
    public int getStopAsInt() {
        return toInt(this.stop);
    }

    public String getTimeZone() {
        return this.zone;
    }
    
    public void setTimeZone(String in) {
    	this.zone = in;
    }
    
    public static int toInt(String in) {
        String temp = pad(in);
        temp = temp.substring(0,2) + temp.substring(3,5);
        return Integer.valueOf(temp).intValue();
    }
    
    public String getHoursAsHTML(int beginDay, int endDay) {
    	return getHoursAsHTML(beginDay, endDay, false);
    }
        
    public String getHoursAsHTML(int beginDay, int endDay, boolean abbreviate) {
        if(this.start == null || this.stop == null)
            return "";
        String out = DAY_LABEL[beginDay];
        if(abbreviate)
        	out = DAY_LABEL_ABBREV[beginDay];
        if(endDay > beginDay) {
            out += "-";
	        if(abbreviate)
    	    	out += DAY_LABEL_ABBREV[endDay];
        	else out += DAY_LABEL[endDay];
        }
        return out + ": " + format(this.start) + "-" + format(this.stop) + "<br />";
    }
    
    public String toString(int i) {
        if(this.start == null || this.stop == null)
            return "";
        String out = DAY_LABEL[i];
        return out + ": " + format(this.start) + "-" + format(this.stop);
    }
    
    public static String format(String time) {
	    String temp = pad(time);
        int hour = Integer.valueOf(temp.substring(0,2)).intValue();
        String m = "am";
        if(hour > 11) {
            m = "pm";
            if(hour > 12)
                hour -= 12;
        }
        
        String out = Integer.toString(hour) + ":" + temp.substring(3,5);
        return out + m;
    }
    
    public static int translateAbbrev(String in) {
        for(int i = 0;i < 7; i++)
            if(DAY_ABBREV[i].equals(in))
                return i;
        return -1;
    }
    
    public boolean equals(Hours test) {
        if(!this.start.equals(test.getStart()))
            return false;
        if(!this.stop.equals(test.getStop()))
            return false;
        return true;
    }

/**
 * Insert the method's description here.
 * Creation date: (8/27/2002 9:56:16 AM)
 * @return java.lang.String
 * @param param java.lang.String
 */
public static String pad(String param) {
	if(param == null)
		return null;
	String temp = param.trim();
	if(temp.length() < 5)
		return "0" + temp;
	return temp;
}
}
