package com.bfrc.pojo.appointment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.bsro.pojo.appointment.DateStringComparator;
import com.bsro.pojo.appointment.MonthNameComparator;

/**
 */
public class AppointmentSchedule implements Serializable
{
    private Map<String,Map<String,AppointmentStoreHour>> scheduleMap;
    private Map<String,List<String>> yearMap;
    private Map<String,Date> dateMap;

    private String storeNumber;
    
    private String minDate;
    private String maxDate;
    private String[] services;
    
    public AppointmentSchedule(){
    	
    }


/**
 * map<monthString,map<dateString,list<timesString>>
 */
	public Map<String,Map<String,AppointmentStoreHour>> getScheduleMap() {
		return scheduleMap;
	}

/**
 */
	public void setScheduleMap(Map<String,Map<String,AppointmentStoreHour>> scheduleMap) {
		this.scheduleMap = scheduleMap;
	}

   public List<String> getMonths(){
	   return new ArrayList<String>(scheduleMap.keySet());
   }

   public List<String> getDates(String month){
	   return new ArrayList<String>(scheduleMap.get(month).keySet());
   }

	public String getStoreNumber() {
		return storeNumber;
	}
	
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String printReport() {
		StringBuffer sb = new StringBuffer();
		sb.append("\nEligible Months: " + getMonths());
		sb.append("\n");

		Set<String> yearsList = getYearMap().keySet();
		SortedSet<String> sortedYears = new TreeSet<String>(yearsList);
		
		for(String year : sortedYears){
				SortedSet<String> sortedMonths = new TreeSet<String>(new MonthNameComparator());
				List<String>  monthsList= getYearMap().get(year);
				sortedMonths.addAll(monthsList);
				//go through set and find it's days.  
				for(String sortedMonth : sortedMonths){			
					if(!getScheduleMap().get(sortedMonth).isEmpty()){
						sb.append("Month: " + sortedMonth);
						sb.append("\n");
						//get date key set for first month, order that.
						Set<String> dates = getScheduleMap().get(sortedMonth).keySet();
						
						SortedSet<String> sortedDates = new TreeSet<String>(new DateStringComparator());
						sortedDates.addAll(dates);
						//get first day in that ordered date keyset, and adjust time.
						for(String sortedDate : sortedDates){
							sb.append("\t Date: " + sortedDate + " hours " + getScheduleMap().get(sortedMonth).get(sortedDate));
							sb.append("\n");
						}
						
						sb.append("\n");
					}					
				}
		}
		return sb.toString();
	}


	public Map<String,Date> getDateMap() {
		return dateMap;
	}


	public void setDateMap(Map<String,Date> dateMap) {
		this.dateMap = dateMap;
	}


	public String getMaxDate() {
		return maxDate;
	}


	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}


	public String getMinDate() {
		return minDate;
	}


	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}


	public Map<String,List<String>> getYearMap() {
		return yearMap;
	}


	public void setYearMap(Map<String,List<String>> yearMap) {
		this.yearMap = yearMap;
	}


	/**
	 * @return the services
	 */
	public String[] getServices() {
		return services;
	}


	/**
	 * @param services the services to set
	 */
	public void setServices(String[] services) {
		this.services = services;
	}
	
	
}
