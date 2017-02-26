/*
 * HolidayChecker.java
 *
 * Created on July 1, 2003, 10:51 AM
 */

package com.bfrc.framework.dao.hibernate3;

import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;

import com.bfrc.framework.dao.StoreScheduleDAO;
import com.bfrc.framework.dao.appointment.Hours;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.StoreSearchUtils;
import com.bfrc.pojo.store.Store;

/**
 *
 * @author  eak
 */
public class StoreScheduleDAOImpl extends HibernateDAOImpl implements StoreScheduleDAO, java.io.Serializable {

    protected Vector year = null;
    protected Vector month = null;
    protected Vector day = null;
    
    /** Creates a new instance of HolidayChecker */
    public StoreScheduleDAOImpl() {
        this.year = new Vector();
        this.month = new Vector();
        this.day = new Vector();
    }

    public void addYear(String year) {
    	this.year.add(year);
    }
    
    public void addMonth(String month) {
    	this.month.add(month);
    }
    
    public void addDay(String day) {
    	this.day.add(day);
    }
    
    public StoreScheduleDAO populate() throws DataAccessException {
    	StoreScheduleDAO out = new StoreScheduleDAOImpl();
        String nativeSql = "SELECT YEAR, MONTH, DAY FROM STORE_HOLIDAY";
    	Session s = getSession();
    	List l = null;
    	try {
			SQLQuery q = s.createSQLQuery(nativeSql);
			q.addScalar("YEAR", Hibernate.INTEGER);
			q.addScalar("MONTH", Hibernate.INTEGER);
			q.addScalar("DAY", Hibernate.INTEGER);
			l = q.list();
    	} finally {
    		//s.close();
			this.releaseSession(s);
    	}
        for(int i=0;i<l.size();i++) {
        	Object[] currRow = (Object[])l.get(i);
            Integer currYear = (Integer)currRow[0];
            if(currYear == null)
            	out.addYear("");
            else out.addYear(currYear.toString());
            Integer currMonth = (Integer)currRow[1];
            out.addMonth(currMonth.toString());
            Integer currDay = (Integer)currRow[2];
            out.addDay(currDay.toString());
        }
        return out;
    }
    
    public boolean isHoliday(String testYear, String testMonth, String testDay) {
        return isHoliday(testYear,testMonth,testDay,null);
    }

    public boolean isHoliday(String testYear, String testMonth, String testDay, String storeNumber) {
    	//intercept the exception first to see if we need make exceptions for the a list of stores that can be open during holiday--//
    	if(storeNumber!= null){
	        try{
		    	Calendar testDate = Calendar.getInstance();
		    	testDate.set(Calendar.MONTH, Integer.parseInt(testMonth)-1);
		    	testDate.set(Calendar.DATE, Integer.parseInt(testDay));
		    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		        String strTestDate = sdf.format(testDate.getTime());
		        if(strTestDate.equals(StoreSearchUtils.holidayDateToCheck)){
		        	long intStoreNumber = Long.parseLong(storeNumber);
		        	if(StoreSearchUtils.holidayHoursExceptionStoreNumberList.contains(String.valueOf(intStoreNumber))){
		        		return false;
		        	}
		        }
	        }catch(Exception ex){
	          //ex.printStackTrace();
	         //ignore the bad data
	    	}
    	}
    	
        for(int i=0; i<this.year.size(); i++) {
            String currYear = (String)this.year.elementAt(i);
            if("".equals(currYear) || testYear.equals(currYear)) {
                if(testMonth.equals(this.month.elementAt(i)) && testDay.equals(this.day.elementAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }
    
	public Hours[] getHours(String storeNumber) {
		Hours[] out = new Hours[7];
		String nativeSql = "SELECT WEEK_DAY,OPEN_TIME,CLOSE_TIME,TIME_ZONE FROM STORE_HOUR WHERE STORE_NUMBER=:storeNumber";
		Session s = getSession();
		List l = null;
		try {
			SQLQuery q = s.createSQLQuery(nativeSql);
			q.addScalar("WEEK_DAY", Hibernate.STRING);
			q.addScalar("OPEN_TIME", Hibernate.STRING);
			q.addScalar("CLOSE_TIME", Hibernate.STRING);
			q.addScalar("TIME_ZONE", Hibernate.STRING);
			Store store = new Store();
			store.setStoreNumber(Long.valueOf(storeNumber));
			q.setProperties(store);
			l = q.list();
		} finally {
			//s.close();
			this.releaseSession(s);
		}
        for(int i=0;i<l.size();i++) {
        	Object[] currRow = (Object[])l.get(i);
            String dayOfWeek = (String)currRow[0];
            int index = Hours.translateAbbrev(dayOfWeek);
            String start = (String)currRow[1];
            if(start == null)
                continue;
    		Hours temp = new Hours();
            temp.setStart(start);
            String stop = (String)currRow[2];
            if(stop == null)
                continue;
            temp.setStop(stop);
            String zone = (String)currRow[3];
            temp.setTimeZone(zone);
            out[index] = temp;
        }
        return out;
	}

}
