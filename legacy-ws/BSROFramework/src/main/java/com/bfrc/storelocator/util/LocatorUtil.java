package com.bfrc.storelocator.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bfrc.Config;
import com.bfrc.LocatorConfig;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.pojo.store.StoreHour;

/**
 * @author Ed Knutson
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LocatorUtil {
	
	public static String padStoreNumber(String storeNumber){
		StringBuffer paddedNumber = new StringBuffer();
		for(int j=0; j<6-storeNumber.length(); j++)
			paddedNumber.append("0");
		paddedNumber.append(storeNumber);
		
		return paddedNumber.toString();
	}
	
	public static String padStoreNumber(long storeNumber){
		return padStoreNumber(String.valueOf(storeNumber));
	}
	
	public static String getNameForNumber(int n) {
		switch(n) {
			case 1:
			return "one";
			case 2:
			return "two";
			case 3:
			return "three";
			case 4:
			return "four";
			case 5:
			return "five";
			case 6:
			return "six";
			case 7:
			return "seven";
			case 8:
			return "eight";
		}
	return "";
	}

	public static String getLogNameForApp(String app) {
		String out = app;
		if(Config.PPS.equalsIgnoreCase(app) || (app != null && app.indexOf("partner") > -1))
			out = "PPS";
		// Firestone Complete Auto Care
		else if(Config.FCAC.equalsIgnoreCase(app))
			out = "FCAC";
		// Tires Plus
		else if(Config.TP.equalsIgnoreCase(app))
			out = "TP";
		// ExpertTire
		else if(Config.ET.equalsIgnoreCase(app))
			out = "ExpertTire";
		// Company Car Connection
		else if(Config.FCFC.equalsIgnoreCase(app))
			out = "FleetCare";
	    // Hibdon Tire
		else if(Config.HT.equalsIgnoreCase(app))
			out = "HibdonTire";
		else if("fan".equalsIgnoreCase(app))
			out = "Faneuil";
		return out + " ";
	}

	public static String getTypesForApp(String app, Config config, boolean licensee, StoreDAO dao, String licenseeType) {
		String out = "AND ";
		Map storeMap = dao.getPrimaryStoreMap(config, licenseeType);
		String where = "";
		if(app.startsWith("5starPrimary")){
			String primarySiteNames="|FCAC|TP|ET|HTP|WW|";
			Iterator i = storeMap.keySet().iterator();
			boolean flag = false;
			while(i.hasNext()) {
				String type = (String)i.next();
				if(primarySiteNames.contains("|"+type+"|")){
					if(!flag){
						where = "(store_type='"+type+"'";
						flag = true;
					}else{
						where += " or store_type='"+type+"'";
					}
				}
			}
		}else if(app.startsWith("fleetCare")){
			String primarySiteNames="|FCAC|TP|HTP|WW|";
			Iterator i = storeMap.keySet().iterator();
			boolean flag = false;
			while(i.hasNext()) {
				String type = (String)i.next();
				if(primarySiteNames.contains("|"+type+"|")){
					if(!flag){
						where = "(store_type='"+type+"'";
						flag = true;
					}else{
						where += " or store_type='"+type+"'";
					}
				}
			}
		}
		else{
			// build criteria from primary type(s)
			Iterator i = storeMap.keySet().iterator();
			String type = (String)i.next();
			where = "(store_type='"+type+"'";
			while(i.hasNext()) {
				type = (String)i.next();
				where += " or store_type='"+type+"'";
			}
			// add secondary types if "licensee" flag is set
			if(licensee) {
				Map secondaryMap = dao.getSecondaryStoreMap(config);
				i = secondaryMap.keySet().iterator();
				while(i.hasNext()) {
					type = (String)i.next();
					where += " or store_type='"+type+"'";
				}
			}
		}
		where += ")";		
		return out + where + " ";
	}

	public static StoreHour getStoreHour(List l, String day) {	
		StoreHour hour = null;		
		for (int i=0; i<l.size(); i++) {
			StoreHour temp = (StoreHour) l.get(i);
			if (temp != null && temp.getId().getWeekDay().equals(day)) {
				hour = temp;
				break;
			}				
		}
		return hour;
	}
	
	public static String getStoreHourHTML(List storeHours) {
		return getStoreHourHTML(storeHours, false);
	}
	public static String getStoreHourHTML(List storeHours, boolean isFormatted) {
		String out = "";
		if (storeHours == null || storeHours.isEmpty())
			return out;
		for (int i=0; i<7; i++) {
			StoreHour curr = getStoreHour(storeHours, DAY_ABBREV[i]);
			if (curr == null)
				continue;
			//if (curr != null)
				//System.err.println(i+":"+DAY_ABBREV[i]+":"+curr.getId().getWeekDay()+":"+curr.getOpenTime());
			for (int j=i+1; j<=7; j++) {
				StoreHour test = null;
				if (j<7)
					test = getStoreHour(storeHours, DAY_ABBREV[j]);
				if (test != null && test.equals(curr))
                    continue;
				if (isFormatted)
					out = out + getHourFormattedHTML(i, j - 1, curr);
				else
					out = out + getHourHTML(i, j - 1, curr);
				i = j - 1;
				break;
			}		
		}
		return out;		
	}
	
	public static String getHourHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = DAY_ABBREV[begin];
        if(end > begin)
        {
            out = out + "&#45;" + DAY_ABBREV[end];            
        }
        return out + " " + format(hour.getOpenTime()) + "&#45;" + format(hour.getCloseTime()) + "<br />";
	}
	
	public static String getHourFormattedHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = "<b>" + DAY_ABBREV[begin];
        if(end > begin)
        {
            out = out + "&#45;" + DAY_ABBREV[end];            
        }
        return out + ":</b> " + format(hour.getOpenTime()) + "&#45;" + format(hour.getCloseTime()) + "<br />";
	}
	
	public static String format(String time)
    {
        String temp = pad(time);
        int hour = Integer.valueOf(temp.substring(0, 2)).intValue();
        String m = "am";
        if(hour > 11)
        {
            m = "pm";
            if(hour > 12)
                hour -= 12;
        }
        String out = Integer.toString(hour) + ":" + temp.substring(3, 5);
        return out + m;
    }
	
	public static String pad(String param)
    {
        if(param == null)
            return null;
        String temp = param.trim();
        if(temp.length() < 5)
            return "0" + temp;
        return temp;
    }
	
	public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY = 4;
    public static final int SATURDAY = 5;
    public static final int SUNDAY = 6;
    public static final String DAY_LABEL[] = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    public static final String DAY_ABBREV[] = {
        "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
    };
    public static final String DAY_LABEL_ABBREV[] = {
        "M", "TU", "W", "TH", "F", "Sa", "Su"
    };
    
    public static int getDefaultStoresPerPage(String app) {
    	return new LocatorConfig().getStoresPerPage(app).intValue();
    }
}
