/*
 * Schedule.java
 *
 * Created on August 2, 2002, 2:18 PM
 */

package com.bfrc.framework.dao.appointment;

import java.text.*;
import java.util.*;

import com.bfrc.framework.dao.StoreScheduleDAO;

/**
 *
 * @author  eak
 * @version 
 */
public class Schedule {

    protected static SimpleDateFormat monthFormatter = new SimpleDateFormat("M");
    protected static SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    protected static SimpleDateFormat dayFormatter = new SimpleDateFormat("d");
    protected GregorianCalendar start = null;
    protected int minimum;
    protected Hours[] hours = new Hours[30];
    
    private StoreScheduleDAO holidayChecker;
    private String storeNumber;
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<30;i++) {
    		sb.append("Offset " + i + ": ");
    		if(hours[i] == null)
    			sb.append("closed");
    		else sb.append(hours[i].getStart() + "-" + hours[i].getStop());
    		sb.append("\n");
    	}
    	return sb.toString();
    }
    
    /** Creates new Schedule */
    public Schedule(GregorianCalendar date, int minimum) {
    	this.minimum = minimum;
    	this.start = date;
    }

    public boolean isOpen(int offset) {
        if(this.hours[offset] == null)
            return false;
        return true;
    }
    
    public String getTimeZone() {
        Hours first = null;
        for(int i = 0; i < 7; i++)
            if((first = this.hours[i]) != null)
                break;
        if(first == null)
            return null;
        return first.getTimeZone();
    }
    
    public Hours getHours(int offset) {
        return this.hours[offset];
    }
    
    public void setHours(Hours[] weekly, Date tomorrow) {
        // Get the first day of the schedule
        int day = this.start.get(Calendar.DAY_OF_WEEK);
        // Convert it to an index for weekly
        int startPos = Hours.convert(day);
        // Iterate through weekly and populate hours
        for(int offset = 0; offset < 36; offset += 7) {
            for(int j = startPos; j < 7; j++) {
                int index = j + offset - startPos;
                if(index >= 30)
                    break;
                if(index == 0 && tooLateForNextDay(tomorrow, weekly[j]))
                	continue;
                else if(!isHoliday(index)) {
                	this.hours[index] = weekly[j];
                }
            }
            for(int j = 0; j < startPos; j++) {
                int index = j + offset - startPos + 7;
                if(index >= 30)
                    break;
                if(index == 0 && tooLateForNextDay(tomorrow, weekly[j]))
                	continue;
                else if(!isHoliday(index)) {
                	this.hours[index] = weekly[j];
                }
            }
        }
    }
    
    public boolean isHoliday(int offset) {
        GregorianCalendar worker = (GregorianCalendar)this.start.clone();
        worker.add(Calendar.DAY_OF_MONTH, offset);
        java.util.Date date = worker.getTime();
        String day = dayFormatter.format(date);
        String month = monthFormatter.format(date);
        String year = yearFormatter.format(date);
        return holidayChecker.isHoliday(year, month, day, getStoreNumber());
    }

	public com.bfrc.framework.dao.StoreScheduleDAO getHolidayChecker() {
		return this.holidayChecker;
	}

	public void setHolidayChecker(
			com.bfrc.framework.dao.StoreScheduleDAO holidayChecker) {
		this.holidayChecker = holidayChecker;
	}
	
	private boolean tooLateForNextDay(Date test, Hours hours) {
        // If the chosen day is the first available day, the start time
        // in the STORE_HOUR table may be overridden by the minimum 
        // 24 hour requirement
        // Otherwise, use OPEN_TIME -> CLOSE_TIME - minFromClose
        // Calculate first start time
        if(hours == null)
        	return true;
        SimpleDateFormat milHourFormat = new SimpleDateFormat("H");
        GregorianCalendar round = new GregorianCalendar();
        round.setTime(test);
        Date test_copy = (Date)round.getTime().clone();
        int currHours = Integer.valueOf(milHourFormat.format(round.getTime())).intValue();
        // If it is before 3pm, allow as early as 7am the next day
        if(currHours < 15)
        	return false;
        SimpleDateFormat hourFormat = new SimpleDateFormat("H");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        int closeInt = hours.getStopAsInt() - minimum;
        // Round forward current time tomorrow to the nearest HOURS_INCREMENT
        String tempMinute = minuteFormat.format(round.getTime());
        int minuteInHour = Integer.valueOf(tempMinute).intValue();
        switch(ListAppointmentTimesOperator.HOURS_INCREMENT) {
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
//      Compare the value of round to the start time
        test = round.getTime();
        String tempRound = hourFormat.format(test) 
            + minuteFormat.format(test);
        String tempRoundCopy = hourFormat.format(test_copy) 
        + minuteFormat.format(test_copy);
        //System.out.println("==================================tempRound : "+tempRound+" tempRoundCopy================>"+tempRoundCopy);
        int roundIntCopy = Integer.valueOf(tempRound).intValue();
        int roundInt = Integer.valueOf(tempRoundCopy).intValue();
        //--- if the increment over next day say 00:00 01:00AM, 2:00AM,etc then it should return true --//cxs
        //--- Make it flexible if we change the increment bigger than one hour, say two hours --//cxs
        if(roundInt <= 300 && roundIntCopy >= 2100) return true;
        // If round > (stop time - minFromClose * 100), 
        // return empty results
//System.out.println("TooLateForNextDay: roundInt = " + roundInt + ", closeInt = " + closeInt);
        if(roundInt > closeInt)
            return true;
		return false;
	}
	
	public String getStoreNumber(){
		return this.storeNumber;
	}
	
	public void setStoreNumber(String storeNumber){
		this.storeNumber = storeNumber;
	}
}
