package com.bsro.service.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.DSTDAO;
import com.bfrc.framework.dao.ZipCodeDataDAO;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.zipcode.ZipCodeData;


@Service("timeZoneDstService")
public class TimeZoneDstServiceImpl implements TimeZoneDstService {

	private Log logger;

	private Date startDate;
	private Date endDate;
	private Date nextYearStartDate;
	private Date nextYearEndDate;

	public static Map<String,String> timeZoneMap = new HashMap<String,String>();
	@PostConstruct
	public void init(){
		
		Date now = new Date();
		SimpleDateFormat dtf = new SimpleDateFormat("yyyy");
		String year = dtf.format(now);
		Map<String, Date> startEndDates= dstDAO.getStartAndEndDateForYear(year);
		if(startEndDates!=null){
			startDate = startEndDates.get("start");
			endDate = startEndDates.get("end");
		}
		GregorianCalendar calNow = new GregorianCalendar();
		calNow.setTime(now);
		calNow.add(GregorianCalendar.YEAR, 1);
		year = dtf.format(calNow.getTime());
		Map<String, Date> nextYearStartEndDates= dstDAO.getStartAndEndDateForYear(year);
		if(nextYearStartEndDates!=null){
			nextYearStartDate = nextYearStartEndDates.get("start");
			nextYearEndDate = nextYearStartEndDates.get("end");
		}
		timeZoneMap.put("None", "CST");
		timeZoneMap.put("Alaska", "US/Alaska");
		timeZoneMap.put("UTC+11", "Etc/GMT+11");
		timeZoneMap.put("Atlantic", "AST");
		timeZoneMap.put("UTC+12", "Etc/GMT+12");
		timeZoneMap.put("Pacific", "PST");
		timeZoneMap.put("Hawaii", "US/Hawaii");
		timeZoneMap.put("Mountain", "MST");
		timeZoneMap.put("Central", "CST");
		timeZoneMap.put("UTC+10", "Etc/GMT+10");
		timeZoneMap.put("Eastern", "EST");
		timeZoneMap.put("UTC+9", "Etc/GMT+9");
		timeZoneMap.put("Samoa", "US/Samoa");
	}
	@Autowired
	private DSTDAO dstDAO;
	@Autowired
	private ZipCodeDataDAO zipCodeDataDAO;

	public TimeZoneDstServiceImpl() {
		this.logger = LogFactory.getLog(TimeZoneDstServiceImpl.class);
	}

	@Override
	public boolean isInDayLightSavings(Date date) {
		
		//Util.debug("checking for dst on ===" + date + " nextYearStartDate = " + nextYearStartDate+ " nextYearEndDate = " + nextYearEndDate);
		if(startDate!=null&&endDate!=null){			
			if(date.after(startDate)&& date.before(endDate))
				return true;
		}
		if(nextYearStartDate!=null&&nextYearEndDate!=null){
			if(date.after(nextYearStartDate)&& date.before(nextYearEndDate))
				return true;
		}
		
		//System.err.println("Start or end date not set in DSTServiceImpl");
		return false;
	}

	@Override
	public ZipCodeData getZipCodeDataByZip(String zip) {
		if(zip!=null&&zip.contains("-"))
		{
			zip = zip.split("-")[0];
		}
		return zipCodeDataDAO.getZipCodeDataByZip(zip);
	}

	@Override
	public String getStateByZip(String zip) {
		if(zip!=null&&zip.contains("-"))
		{
			zip = zip.split("-")[0];
		}
		return zipCodeDataDAO.getStateByZip(zip);
	}


}
