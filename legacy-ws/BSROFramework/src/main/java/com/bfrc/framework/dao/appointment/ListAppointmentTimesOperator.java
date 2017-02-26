package com.bfrc.framework.dao.appointment;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.bfrc.*;
import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.framework.dao.StoreScheduleDAO;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;

public class ListAppointmentTimesOperator extends BusinessOperatorSupport {
	
	//--- 20100105 change from 30 to 15 minutes ---//
    public static final int HOURS_INCREMENT = 15;
    //cxs
    public static final long DAY = 1000*60*60*24;//cxs
    public static final String OPENINT="OPENINT";
    public static final String FIRSTMONTH="FIRSTMONTH";
    public static final String FIRSTDAY="FIRSTDAY";
    public static final String CHECKHOUR="CHECKHOUR";
    public static final String BUFFEREDHOURS="BUFFEREDHOURS";
    public static final String WEBCLOSEINT="WEBCLOSEINT";
    public static final String STORECLOSEINT="STORECLOSEINT";
    public static final String STORE_NUMBER="storeNumber";
    public static final String MONTH="month";
    public static final String MIN_HOURS_FROM_CLOSE="minFromClose";
    public static final String DATE = "date";
    public static final String USE_CLASSIC_RULES = "useClassicRules";
    
    public static final String MINIMUMHOURS_FROMCLOSE="MINIMUMHOURS_FROMCLOSE";
    
    public static final int DEFAULT_BUFFER_HOUR = 24;
    public static final int DEFAULT_MIN_HOURS_FROM_CLOSE = 3;
    public static final int DEFAULT_MIN_HOURS_FROM_OPEN = 4;
    
	private StoreScheduleDAO holidayChecker;
	
	public StoreScheduleDAO getStoreScheduleDAO() {
		return this.holidayChecker;
	}
	public void setStoreScheduleDAO(StoreScheduleDAO holidayChecker) {
		this.holidayChecker = holidayChecker;
	}
	public List getMonths(Object storeNumber){
		return getMonths(storeNumber,0,0);
	}
	public List getMonths(Object storeNumber, int minFromClose, int bufferedHours){
		java.util.Map monthmap = new java.util.HashMap();
		if(bufferedHours > 0){
			monthmap.put(ListAppointmentTimesOperator.BUFFEREDHOURS, String.valueOf(bufferedHours));
		}
	    
	    monthmap.put("storeNumber", String.valueOf(storeNumber));
	    if(minFromClose > 0){
	    	monthmap.put(MINIMUMHOURS_FROMCLOSE, String.valueOf(minFromClose));
	    }
	    return (List)monthmap.get("result");
	}
	
	public List getDays(Object storeNumber, Object month){
		return getDays(storeNumber,month,0,0);
	}
	public List getDays(Object storeNumber, Object month, int minFromClose, int bufferedHours){
		java.util.Map operationmap = new java.util.HashMap();
		if(bufferedHours > 0){
			operationmap.put(ListAppointmentTimesOperator.BUFFEREDHOURS, String.valueOf(bufferedHours));
		}
	    operationmap.put("storeNumber", String.valueOf(storeNumber));
	    operationmap.put("month", String.valueOf(month));
	    if(minFromClose > 0){
	    	operationmap.put(MINIMUMHOURS_FROMCLOSE, String.valueOf(minFromClose));
	    }
	    return (List)operationmap.get("result");
	}
	
	public List getHours(Object storeNumber, Object month, Object date){
		return getHours(storeNumber,month,date,0,0,false);
	}
	public List getHours(Object storeNumber, Object month, Object date, int minFromClose, int bufferedHours,boolean checkHour){
		java.util.Map operationmap = new java.util.HashMap();
		if(bufferedHours > 0){
			operationmap.put(ListAppointmentTimesOperator.BUFFEREDHOURS, String.valueOf(bufferedHours));
		}
	    operationmap.put("storeNumber", String.valueOf(storeNumber));
	    operationmap.put("month", String.valueOf(month));
	    operationmap.put("date", String.valueOf(date));
	    if(minFromClose > 0){
	    	operationmap.put(MINIMUMHOURS_FROMCLOSE, String.valueOf(minFromClose));
	    }
	    if(checkHour){
	    	operationmap.put(CHECKHOUR, "1");
	    }
	    return (List)operationmap.get("result");
	}
	
	
	public Object operate(Object o) throws Exception {

		Util.debug("===TESTING IF CURRENT BUILD====== ");
		Map m = (Map)o;
//		Util.debug("               Parameters: "+m);
        // minimum 3 hours
//      int minimum = 300;		
		//i'm not sure how 3 hours = 300 of anything?????!?!?
		//default
		int minimum = DEFAULT_MIN_HOURS_FROM_CLOSE;
		Object hoursFromCloseObj = m.get(MIN_HOURS_FROM_CLOSE);
		if(hoursFromCloseObj !=null){
			Integer minFromClose = null;
			if(hoursFromCloseObj instanceof String)
				minFromClose = Integer.parseInt(((String)hoursFromCloseObj));
			else if(hoursFromCloseObj instanceof Integer)
				minFromClose =(Integer)hoursFromCloseObj;
			
			if(minFromClose != null){
				minimum = minFromClose.intValue() * 100;
			}
		}
		String result = "dropdown";
		List l = new java.util.ArrayList();
		m.put(RESULT, l);
		String store = (String)m.get("storeNumber");
		if(store == null || "".equals(store) || "null".equals(store))
			return null;
		GregorianCalendar tomorrow = new GregorianCalendar();
		String todayYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String todayMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
		String todayDay = String.valueOf(Calendar.getInstance().get(Calendar.DATE));
		
		/** default is 24 hours **/
        // Add one day because appointments must be scheduled 
        // at least 24 hours in advance
//		check if need cutomized days //cxs
        SimpleDateFormat intHourFormat = new SimpleDateFormat("Hmm");//cxs
        String checkHour = (String)m.get(CHECKHOUR);
        int bufferedHours = 0;
        if(m.get(BUFFEREDHOURS) != null){
            try{
              bufferedHours = Integer.parseInt(m.get(BUFFEREDHOURS).toString());
            }catch(Exception ex){
                 //
            }
        }
        int targetBuffer = DEFAULT_BUFFER_HOUR;
        if(bufferedHours > 0 ){
        	targetBuffer = bufferedHours;
        }

        Boolean b = (Boolean) m.get(USE_CLASSIC_RULES);
        boolean useClassicRules = true;
        if(b != null){
        	useClassicRules = b.booleanValue();
        }
        
        Util.debug("ListAppointmentTimesOperator.useClassicRules: " + useClassicRules);
        boolean dayShifted = false;
        //NEW RULES: allows for shifting to next day if it's sat/sun.
        
        if(!useClassicRules){
			GregorianCalendar gc = new GregorianCalendar();
			int dow = gc.get(Calendar.DAY_OF_WEEK);
			if( dow == Calendar.SATURDAY && (gc.get(Calendar.AM_PM) == Calendar.AM)){
				targetBuffer = 0;
				tomorrow.add(Calendar.DATE, 1);
				dayShifted=true;
				//making sure it's not too much
				if(bufferedHours < 8){
					tomorrow.set(Calendar.HOUR_OF_DAY, bufferedHours);
				}
				
			} else if( dow == Calendar.SUNDAY && (gc.get(Calendar.AM_PM) == Calendar.AM)){
				targetBuffer = 0;
				tomorrow.add(Calendar.DATE, 1);	
				dayShifted=true;
				//making sure it's not too much
				if(bufferedHours < 8){
					tomorrow.set(Calendar.HOUR_OF_DAY, bufferedHours);
				}
				
			}
			Util.debug("ListAppointmentTimesOperator.targetBuffer " + targetBuffer);
        }
        
        //--------20101201 holiday changes -------//
        StoreScheduleDAO checker = holidayChecker.populate();
        if(checker.isHoliday(todayYear, todayMonth, todayDay, store)){
        	//-- if today is holiday then add extra 16 hours --//
        	//-- i.e. from 12:00AM - 4PM( 3 hours before store close --//
        	if(useClassicRules)
        		targetBuffer += 17;
        	else
        	{
        		targetBuffer = 0;
				tomorrow.add(Calendar.DATE, 1);	
				dayShifted=true;
				//making sure it's not too much
				if(bufferedHours < 8){
					tomorrow.set(Calendar.HOUR_OF_DAY, bufferedHours);
				}
        	}
        	//Util.debug("\t\tHoliday-----------------------------add a day");
        }else{
        	//-- if tomorrow is holiday then add extra 24 hours --//
        	if(useClassicRules){
	        	Calendar tom = Calendar.getInstance();
	        	tom.add(Calendar.DATE, 1);
	        	String tomYear = String.valueOf(tom.get(Calendar.YEAR));
	    		String tomMonth = String.valueOf(tom.get(Calendar.MONTH)+1);
	    		String tomDay = String.valueOf(tom.get(Calendar.DATE));
	    		if(checker.isHoliday(tomYear, tomMonth, tomDay, store)){
	            	targetBuffer += 25;
	            	//Util.debug("\t\tTomorrow Holiday-----------------------------add a day");
	    		}
        	}
        }
        tomorrow.add(Calendar.HOUR, targetBuffer); 
        boolean isTodayClosed =false;
        boolean isTodayAndTomorrowClosed =false;
        Schedule scheduleNow=null;
        if(!useClassicRules){
        	//because of previous shift we must get schedule for real time and check if today is closed
            GregorianCalendar nowCal = new GregorianCalendar();
            scheduleNow = getThirtyDaySchedule(store, nowCal, this.holidayChecker, minimum);
            isTodayClosed = !scheduleNow.isOpen(0);
            if(isTodayClosed)
            	isTodayAndTomorrowClosed = !scheduleNow.isOpen(1);
            
        }
        boolean isAddDay = false;
        GregorianCalendar tm = new GregorianCalendar();
        tm.add(Calendar.HOUR, DEFAULT_BUFFER_HOUR);
        Schedule schedule = getThirtyDaySchedule(store, tomorrow, this.holidayChecker, minimum);
        Schedule schedule2 = getThirtyDaySchedule(store, tm, this.holidayChecker, minimum);
        Hours hours2 = schedule2.getHours(1);
        int intHourNow = Integer.parseInt(intHourFormat.format(tomorrow.getTime()));
        if(hours2 != null){
	        int closeInt2 = ( hours2.getStopAsInt() - minimum);
	        if(intHourNow > closeInt2 && "1".equals(checkHour) ){//add one more daye
	//        	 If it is before 3pm, allow as early as 7am the next day so manually add one day
	        	if(intHourNow < 1500) tomorrow.add(Calendar.HOUR, 24);
	            isAddDay = true;
	        }
        }
/* TODO determine if this is needed
        if(!isBlankOrNull(zone)) {
            GregorianCalendar test = new GregorianCalendar(TimeZone.getTimeZone(zone));
            int offset = ((test.get(Calendar.ZONE_OFFSET)
                    + test.get(Calendar.DST_OFFSET)) 
                - (tomorrow.get(Calendar.ZONE_OFFSET)
                    + tomorrow.get(Calendar.DST_OFFSET)));
            tomorrow.add(Calendar.MILLISECOND, offset);
        }
*/        
        GregorianCalendar iterator = (GregorianCalendar)tomorrow.clone();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEE");

        String currentMonth = monthFormat.format(tomorrow.getTime());
        if(!m.containsKey("month")) {
            // If there are no days available in this month or the next one
            // just return the month that is available
            // At the end of January it is possible there will be 3 months
        	if(useClassicRules){
	            if(schedule.isOpen(0))
	                l.add(currentMonth);
	            else currentMonth = null;
	            int max = 30;
	           
	            for(int i = 1; i < max; i++) {
	                iterator.add(Calendar.DATE, 1);
	                if(!schedule.isOpen(i))
	                    continue;
	                String newMonth = monthFormat.format(iterator.getTime());
	                if(!newMonth.equals(currentMonth)) {
	                    currentMonth = newMonth;
	                    l.add(currentMonth);
	                }
	            }
        	}else{
        		Util.debug("using new way to get month");
        		GregorianCalendar nowCal = new GregorianCalendar();int dow = nowCal.get(Calendar.DAY_OF_WEEK);
                GregorianCalendar nowCalIterator = (GregorianCalendar)nowCal.clone();
        		currentMonth =  monthFormat.format(nowCal.getTime());
        		
        		//Create a last time to schedule which is 4 hours before close
                //only add the current day's month if it's before the last time to schedule an appt for the day.
        		//
	            Integer tempStopTime = null;
		            if(scheduleNow!=null){
			            for(int k =0 ; k<30;k++){
			            	if(scheduleNow.getHours(k)!=null){
			            		tempStopTime = (scheduleNow.getHours(k).getStopAsInt()-400);
			            		break;
			            	}
			            }
		            }
                int lastTimeToSchedule = 0;
                if(tempStopTime!=null)
                	lastTimeToSchedule=tempStopTime.intValue();
                 GregorianCalendar tempGreg = new GregorianCalendar();
                int hourOfDay = (tempGreg.get(Calendar.HOUR_OF_DAY)*100) + tempGreg.get(Calendar.MINUTE);
                
                Util.debug( "LAOP: for months, lastTimeToSchedule " + lastTimeToSchedule);
                Util.debug( "LAOP: for months, hourOfDay " + hourOfDay);
        		
        		if(scheduleNow.isOpen(0) && hourOfDay < tempStopTime  && dow != Calendar.SATURDAY&& dow != Calendar.SUNDAY )
	                l.add(currentMonth);
	            else currentMonth = null;
	            int max = 29;
	            for(int i = 1; i < max; i++) {
	            	nowCalIterator.add(Calendar.DATE, 1);
	                if(!scheduleNow.isOpen(i))
	                    continue;
	                String newMonth = monthFormat.format(nowCalIterator.getTime());
	                if(!newMonth.equals(currentMonth)) {
	                    currentMonth = newMonth;
	                    l.add(currentMonth);
	                }
	            }
        	}
        } else if(!m.containsKey("date")) {
            // Only return the days for which a hours info is available
            String month = (String)m.get("month");
            int offset = 0;
            if(!StringUtils.isNullOrEmpty(month) && VALID_MONTHS.contains("|"+month+"|")){
                while(!monthFormat.format(iterator.getTime()).equals(month)) {
                    iterator.add(Calendar.DATE, 1);
                    offset++;
                    if(offset >40) break;//avoid infinit loop
    //Util.debug("phase 1:offset="+offset+", iterator="+monthFormat.format(iterator.getTime()));
                }
            }else{
            	// return an empty results if not a valid month
            	return result;
            }
            while(offset < 30 &&
                    monthFormat.format(iterator.getTime()).equals(month)) {
            	Date iteratorTime = iterator.getTime();
                String currentDay = dayFormat.format(iteratorTime);
                if(schedule.isOpen(offset)) {
                	Config config = getConfig();
                	String suffix = "";
                	if(config != null && (Config.TP.equals(config.getSiteName()) || Config.FCAC.equalsIgnoreCase(config.getSiteName())|| Config.ET.equalsIgnoreCase(config.getSiteName())|| Config.HT.equals(config.getSiteName()))){
	               		Util.debug("dayNameFormat.format(iteratorTime)): " + dayNameFormat.format(iteratorTime));
	               		if( "Sat".equals(dayNameFormat.format(iteratorTime)) || (!useClassicRules && "Sun".equals(dayNameFormat.format(iteratorTime)))  ){
	               			suffix = "S";
	               		}
	               		
	               	}
                	
                    l.add(currentDay + suffix);
                }
                iterator.add(Calendar.DATE, 1);
                offset++;
//Util.debug("phase 2:offset="+offset);
            }
        } else {
            // If the chosen day is the first available day, the start time
            // in the STORE_HOUR table may be overridden by the minimum 
            // 24 hour requirement
            // Otherwise, use OPEN_TIME -> CLOSE_TIME - minFromClose
            SimpleDateFormat hourFormat = new SimpleDateFormat("H");
            SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
            String month = (String)m.get("month");
            String day = (String)m.get("date");
            boolean roundIntSet = false;
            try{
            	int intDay = Integer.parseInt(day);
            	if(intDay > 31)
            		day="31";
            }catch(Exception ex){
            	return result;
            }
//          cxs
            String firstMonth = (String)m.get(FIRSTMONTH);
            String firstDay = (String)m.get(FIRSTDAY);
            
            Date test = tomorrow.getTime();

            boolean firstAvailable = false;
            //Check to see if today is closed and the target is tomorrow, regardless of time...which will trigger a shift of 4 hours
            //no need to check next day after tomorrow because we never shift more than one day and the null check later on todaysHours will provide us with the shift 
            boolean shiftBecauseTargetIsTomorrowAndTodayIsClosed=false;
            if(isTodayClosed){

                GregorianCalendar nowCal = new GregorianCalendar();
                nowCal.add(Calendar.DATE, 1);
	            if(monthFormat.format(nowCal.getTime()).equals(month) && dayFormat.format(nowCal.getTime()).equals(day)){
	            	shiftBecauseTargetIsTomorrowAndTodayIsClosed = true;
	            	firstAvailable=true;
	            }
	            else if(isTodayAndTomorrowClosed){
	                nowCal.add(Calendar.DATE, 1);
		            if(monthFormat.format(nowCal.getTime()).equals(month) && dayFormat.format(nowCal.getTime()).equals(day)){
		            	shiftBecauseTargetIsTomorrowAndTodayIsClosed = true;
		            	firstAvailable=true;
		            }
	            }
            }
            
            Util.debug("ListAppointmentTimes: requested month = *" + month + "*, requested day = *" + day + "*, test month = *" + monthFormat.format(test) + "*, test day = *" + dayFormat.format(test) + "*");
            if(monthFormat.format(test).equals(month)
                    && dayFormat.format(test).equals(day))
                firstAvailable = true;
            //first available for NEW RULES is different than before.  
            //if we are 6/26, first availabe time is for 6/27.  
            //make the operator think that we are getting firstAvailableTimes.
            int offset = 0;   // Get hours from schedule
            if(!firstAvailable)
                for(offset = 1; offset < 30; offset++) { 
                    iterator.add(Calendar.DATE, 1);
                    test = iterator.getTime();
                    if(monthFormat.format(test).equals(month)
                            && dayFormat.format(test).equals(day))
                        break;
                }
            //          cxs if need check available hours aginst the the hour now
            
            try {
            	
	            Hours hours = schedule.getHours(offset);
	          
	            if(hours == null){
                   // temp avoid the null pointer here
	            	//avoid ArrayIndexOutOfBoundsException by checking if offset is zero.
	            	if( offset != 0){
	            		hours = schedule.getHours(offset-1);
	            	}
	            	//-- fix null pointer issue when user select time(a Saturday) after 3 and select a store that has Sunday closed --//
	            	//-- this will fix both Sunday and Saturday closed to --//
	            	if(hours == null){ 
	            		hours = schedule.getHours(offset+1); 
		                if(hours == null) {
		                	hours = schedule.getHours(offset+2);
		                	if(hours == null) 
		                		hours = schedule.getHours(offset+3);
		                }
	            	}
//	            	Util.debug("schedule.getHours(offset) returned null.\noffset="+offset+"\nstore="+store);
	            }
	            
	            
	            // Calculate first start time
	            int openInt = hours.getStartAsInt();
	            Util.debug("openInt = " + openInt);
	            int closeInt = hours.getStopAsInt() - minimum;
                //Create a last time to schedule which is 4 hours before close
                //if currentTime is > lastTimeToSchedule then kick to next day.
	            Integer tempStopTime = null;
		            if(scheduleNow!=null){
			            for(int k =0 ; k<30;k++){
			            	if(scheduleNow.getHours(k)!=null){
			            		tempStopTime = scheduleNow.getHours(k).getStopAsInt()-400;
			            		break;
			            	}
			            }
		            }
                int lastTimeToSchedule = hours.getStopAsInt()-400;
                if(tempStopTime!=null)
                	lastTimeToSchedule=tempStopTime.intValue();
                Util.debug( "LAOP: lastTimeToSchedule " + lastTimeToSchedule);
                
	            if(!useClassicRules){
	            	GregorianCalendar tempGreg = new GregorianCalendar();
	                int hourOfDay = (tempGreg.get(Calendar.HOUR_OF_DAY)*100) + tempGreg.get(Calendar.MINUTE);
	            	Util.debug("hourOfDay: " + hourOfDay);
            		GregorianCalendar temp = (GregorianCalendar)tomorrow.clone();
            		Util.debug("LAOP: temp.date"+temp.get(Calendar.DATE) +"  temp.get(Calendar.HOUR_OF_DAY) " + temp.get(Calendar.HOUR_OF_DAY) + " bufferedHours " + bufferedHours);
	            	if(hourOfDay >= lastTimeToSchedule) {

		            	if(temp.get(Calendar.HOUR_OF_DAY) >= bufferedHours)
	        				temp.add(Calendar.DATE, 1);
		            	Util.debug("LAOP: temp.date"+temp.get(Calendar.DATE) +"  temp.get(Calendar.HOUR_OF_DAY) " + temp.get(Calendar.HOUR_OF_DAY) + " bufferedHours " + bufferedHours);
		            	
	            		//this means we already shifted the day, so we dont need to shift again.

	            		Date testTemp = temp.getTime();
	            		Util.debug("ListAppointmentTimes: requested month = *" + month + "*, requested day = *" + day + "*, test month = *" + monthFormat.format(testTemp) + "*, test day = *" + dayFormat.format(testTemp) + "*");
	            		if(monthFormat.format(testTemp).equals(month) && dayFormat.format(testTemp).equals(day))
	                        firstAvailable = true;
            		
	            		  //if the previous day is closed then automatically shift 4 hours for the day after tomorrow.
                		Util.debug(" Checking for closed days previous to target day, offset: "+ offset);
	    	        	if(offset == 2){
	                		Hours temporaryHours = schedule.getHours(offset-1);
	                		if(temporaryHours == null){ 
	                			  firstAvailable = true;
	  	                		  Util.debug(" Checking back 1 day, firstAvailable =true");
	                		}
	                	}else if(offset == 3){
		            		  //if the previous 2 days are closed then automatically shift 4 for three days from now.
	                		Hours temporaryHours = schedule.getHours(offset-1);
	                		if(temporaryHours == null){ 
	                			temporaryHours = schedule.getHours(offset-2);
	                			if(temporaryHours == null){ 
	                			  firstAvailable = true;
		  	                	  Util.debug(" Checking back 2 days, firstAvailable =true");
	                			}
	                		}
	                	}else if(offset == 4){
		            		  //if the previous 3 days are closed then automatically shift 4 for four days from now.
	                		Hours temporaryHours = schedule.getHours(offset-1);
	                		Util.debug(" Checking back one day, requested day: "+ offset);
	                		if(temporaryHours == null){ 
	                			temporaryHours = schedule.getHours(offset-2);
	                			if(temporaryHours == null){ 
		                			temporaryHours = schedule.getHours(offset-3);
		                			if(temporaryHours == null){ 
		                			  firstAvailable = true;
		  	                		  Util.debug(" Checking back 3 days, firstAvailable =true");
		                			}
	                			}
	                		}
	                	}
	            		
	
	            	}else{
	            		//TODO
	            		int dow = tomorrow.get(Calendar.DAY_OF_WEEK);
	                	//different scenarios, they all shift to next day, open time
	                	//if it's sat or sun,
	                	//if the hourOfDay is within [ (storeclose - 4hours), storeClose ]	                	
	                	//if early morning, like 1AM, prior to store open
	                	if((dow == Calendar.SATURDAY || dow == Calendar.SUNDAY)&& dayFormat.format(temp.getTime()).equals(day)){
	                		//tomorrow.add(Calendar.DATE, 1);
	                		Util.debug("ListAppointmentTimes: requested month = *" + month + "*, requested day = *" + day + "*, tomorrow month = *" + monthFormat.format(tomorrow.getTime()) + "*, tomorrow day = *" + dayFormat.format(tomorrow.getTime()) + "*");
		            		
	                        firstAvailable = true;
	                	}
	            	}
	            }
	            Util.debug("LAOP: firstAvailable " + firstAvailable);
	            
	            
	         
	            boolean isFirst = false;
	            if(month.equals(firstMonth) && day.equals(firstDay)){
	                isFirst = true;
	            }
	            boolean checkHours = false;
	            if(isFirst && "1".equals(checkHour)) checkHours = true;
	            
	            if(isAddDay) checkHours = false;

	            //cxs put the store open and close hours to the result
	            m.put(OPENINT, openInt+"");//cxs
                m.put(WEBCLOSEINT, closeInt+"");//
                m.put(STORECLOSEINT, hours.getStopAsInt()+"");//               
                
	            GregorianCalendar round = (GregorianCalendar)tomorrow.clone();
	            
	            //NEW RULE - if after 3pm, shift to 4 hours after store open
//	            if(!useClassicRules){
//		            if(new GregorianCalendar().get(Calendar.HOUR_OF_DAY) >= 15) {
//	                    round.set(Calendar.HOUR, openInt / 100);
//	                    round.set(Calendar.AM_PM, Calendar.AM);
//	                    round.set(Calendar.MINUTE, 0);
//	                    
//	                    
//	                    //add 4 hours to allow MAI scheduling
//	                    round.add(Calendar.HOUR, DEFAULT_MIN_HOURS_FROM_OPEN);
////	                    openInt = openInt + (DEFAULT_MIN_HOURS_FROM_OPEN * 100);
//	                }
//	            }
	            if(firstAvailable) {
	                // Round forward current time tomorrow to the nearest HOURS_INCREMENT
	                // Unless it is before 3pm: then allow as early as 7am the next day
	                SimpleDateFormat milHourFormat = new SimpleDateFormat("H");
	                int currHours = Integer.valueOf(milHourFormat.format(round.getTime())).intValue();

	                if(useClassicRules){
		                //this is saying if it's earlier than 3, shift to 7 AM for next available	                
		                if(currHours < 15) {
		                    round.set(Calendar.HOUR, 7);
		                    round.set(Calendar.AM_PM, Calendar.AM);
		                    round.set(Calendar.MINUTE, 0);
		                }else {
	
			                //if its later than hour 15, it was never setting to a nice interval
			                //the round calendar should already have the correct day, just need
			                //to shift the times to the interval
		                	
		                	int currentMinute = round.get(Calendar.MINUTE);
	//	                	Util.debug("currentMinute " + currentMinute);
		                	double incAsDouble = HOURS_INCREMENT;
	//	                	Util.debug("incAsDouble " + incAsDouble);
		                	double nextMinuteDouble = Math.ceil((currentMinute / incAsDouble)) * HOURS_INCREMENT;
	//	                	Util.debug("nextMinuteDouble " + nextMinuteDouble);
		                	int targetMinute = (int) nextMinuteDouble;
	//	                	Util.debug("targetMinute " + targetMinute);
		                	round.set(Calendar.MINUTE, targetMinute);
		                }
	                }else{
	                	//NEW RULES: if current time is after 3pm, shift to 7 AM next day.
	                	//this just shifts to the next day, the hourWorker section adjusts for store open.
	                	GregorianCalendar gc = new GregorianCalendar();
	        			int dow = gc.get(Calendar.DAY_OF_WEEK);
	                	int hourOfDay = (gc.get(Calendar.HOUR_OF_DAY)*100 + gc.get(Calendar.MINUTE));
	                	//different scenarios, they all shift to next day, open time
	                	//if it's sat or sun,
	                	//if the hourOfDay is within [ (storeclose - 4hours), storeClose ]	                	
	                	//if early morning, like 1AM, prior to store open
	                	if(dow == Calendar.SATURDAY || dow == Calendar.SUNDAY || shiftBecauseTargetIsTomorrowAndTodayIsClosed||
                			hourOfDay >= lastTimeToSchedule || hourOfDay < openInt) {
		                    round.set(Calendar.HOUR, openInt / 100);
		                    round.set(Calendar.AM_PM, Calendar.AM);
		                    round.set(Calendar.MINUTE, 0);
		                    roundIntSet=true;
		                    
		                    //if 9pm - 4 hours = 5pm.  But current time is 4pm, we need to allow for store open, so
		                    //only add 4 hours if the hourOfDay is less than store close - 4 hours.  
		                    Hours todayHours = schedule.getHours(0);
		                    if(todayHours!=null)
		                    	Util.debug(" todayHours: " + todayHours.getStartAsInt());
		                    if((shiftBecauseTargetIsTomorrowAndTodayIsClosed)||todayHours==null||hourOfDay >= ( todayHours.getStopAsInt() - 400) || (!dayShifted && hourOfDay < todayHours.getStartAsInt())){
				                 	 //add 4 hours to allow MAI scheduling
			                    round.add(Calendar.HOUR, DEFAULT_MIN_HOURS_FROM_OPEN);
			                    Util.debug(" added for MAI: " + DEFAULT_MIN_HOURS_FROM_OPEN);
		                    }
		                   
		                    
		                    //also need to add a half hour in case store opens at #:30
		                    String openIntAsString = String.valueOf(openInt);
		                    int length = openIntAsString.length();
		                    //take the last 2 characters, those are minutes.
		                    String minutes = openIntAsString.substring(length-2, length);
		                    if(!"00".equalsIgnoreCase(minutes)){
		                    	Util.debug("ListAppointmentTimesOperator: adding minutes to next open time " +minutes );
		                    	round.add(Calendar.MINUTE, Integer.parseInt(minutes));
		                    }
		                }else{
			                //if its later than hour 15, it was never setting to a nice interval
			                //the round calendar should already have the correct day, just need
			                //to shift the times to the interval
		                	
		                	int currentMinute = round.get(Calendar.MINUTE);
	//	                	Util.debug("currentMinute " + currentMinute);
		                	double incAsDouble = HOURS_INCREMENT;
	//	                	Util.debug("incAsDouble " + incAsDouble);
		                	double nextMinuteDouble = Math.ceil((currentMinute / incAsDouble)) * HOURS_INCREMENT;
	//	                	Util.debug("nextMinuteDouble " + nextMinuteDouble);
		                	int targetMinute = (int) nextMinuteDouble;
	//	                	Util.debug("targetMinute " + targetMinute);
		                	round.set(Calendar.MINUTE, targetMinute);
		                }
	                	
	                }
	                
	                
	                String tempMinute = minuteFormat.format(round.getTime());
	                int minuteInHour = Integer.valueOf(tempMinute).intValue();
	                switch(HOURS_INCREMENT) {
	                    case 30:
	                        if(minuteInHour != 30 && minuteInHour != 0) {
	                            int increment = 30;
	                            if(minuteInHour > 30)
	                                increment = 60;
	                            round.add(Calendar.MINUTE, increment - minuteInHour);
	                        }
	                    break;
	                    case 60:
	                        if(minuteInHour != 0) {
	                            round.add(Calendar.MINUTE, 60 - minuteInHour);
	                        }
	                    default:
	                    break;
	                }
	                // Compare the value of round to the start time
	                test = round.getTime();
	                String tempRound = hourFormat.format(test) 
	                    + minuteFormat.format(test);
	                int roundInt = Integer.valueOf(tempRound).intValue();
	                //NEW RULES
                	//change openInt (which is first available appt time) to be 4 hours from now.
                	//still not sure why everything is in the hundreds
	                if(!useClassicRules){
	                	int hourOfDay = new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
	                	Util.debug("ListAppointmentTimesOperator: roundInt = " + roundInt + " bufferedHours = " + bufferedHours + ", hourOfDay = " + hourOfDay );
	                	//if we are later than 3pm, we should put the first available appointment time to be 4 hours from store open.
//	                	if(hourOfDay >= 15) {
//	                		roundInt = openInt + (DEFAULT_MIN_HOURS_FROM_OPEN * 100);	 
//	                	}else {
	                	if(!roundIntSet&&monthFormat.format(test).equals(month)
	                            && dayFormat.format(test).equals(day)){
	                		if((hourOfDay * 100) > roundInt){
		                		roundInt = (hourOfDay * 100) + (bufferedHours * 100);	                		
		                	}
	                	}
//	                	}
	                }
	                // If round > (stop time - minFromClose * 100), 
	                // return empty results
	                Util.debug("ListAppointmentTimesOperator: roundInt = " + roundInt + " openInt = " + openInt +", closeInt = " + closeInt + ", currHours = " + currHours);
	                if(roundInt > closeInt)
	                    return result;
	                // If round > start time, round must be used as the first time
	                if(roundInt > openInt)
	                    openInt = roundInt;
	                
	            }
	            
	            // Open and close times for this day are now determined
	            // Fill in the results
	            int hourWorker = openInt;
	            Util.debug("HOUR WORKER: " + hourWorker + " closeInt " + closeInt);
	            while(hourWorker <= closeInt) {
                   // cxs  if need check available hours
                    if(checkHours && inRange(intHourNow, hourWorker, openInt, closeInt)){
	                    l.add(formatIntAsHours(hourWorker));
//                         Util.debug(hourWorker+"                            Hour: "+formatIntAsHours(hourWorker));
                    }else if(!checkHours){
                    	l.add(formatIntAsHours(hourWorker));
                    }
	                hourWorker += HOURS_INCREMENT;
	                String minutes = getMinutes(String.valueOf(hourWorker));
	                if(Integer.valueOf(minutes).intValue() > 59)
	                    hourWorker += 40;
	            }
	        } catch(ArrayIndexOutOfBoundsException ex) {
	        	ex.printStackTrace(System.err);
	        	System.err.println("ArrayIndexOutOfBounds listing appointment times, requested offset: " + offset);
	        }
        }
        return result;
	}
	
    public static String getMinutes(String in) {
        int len = in.length();
        return in.substring((len == 4) ? 2 : 1);
    }
    
    public static String formatIntAsHours(int in) {
        String out = null;
        String temp = String.valueOf(in);
        int len = temp.length();
        String minutes = getMinutes(temp);
        String amOrPm = "a";
        String hours = temp.substring(0, (len == 4) ? 2 : 1);
        if(in > 1159) {
            amOrPm = "p";
            if(in > 1259)
                hours = String.valueOf(Integer.valueOf(hours).intValue() - 12);
        }
        out = hours + ":" + minutes + amOrPm;
        return out;
    }
    
    public static Schedule getThirtyDaySchedule(String storeNum, GregorianCalendar start, StoreScheduleDAO holidayChecker, int minimum) {
    	Schedule schedule = new Schedule(start, minimum);
		schedule.setStoreNumber(storeNum);
    	schedule.setHolidayChecker(holidayChecker.populate());
    	schedule.setHours(holidayChecker.getHours(storeNum), start.getTime());
    	return schedule;
   }
    
    public static boolean inRange(int now, int hourWorker, int start, int end){
        //Util.debug(now+"    :    "+hourWorker+"    :    "+start+"    :    "+end);
        if(now <= start){
            return true;
        }else if(now > end) {
            return true;
        }else if( now == hourWorker )  {
            return true;
        }else if( now >= (hourWorker - HOURS_INCREMENT)  && now < hourWorker)  {
            return true;
        }else if( now < hourWorker )  {
            return true;
        }
        return false;
    }
    private static final String VALID_MONTHS="|January|February|March|April|May|June|July|August|September|October|November|December|";

}
