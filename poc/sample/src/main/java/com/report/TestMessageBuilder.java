package com.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestMessageBuilder {

	public static SimpleDateFormat displayDateHourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
	public static SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static void main(String[] args) {
		System.out.println(messageHeaderContent());
	}

	public static String messageHeaderContent() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		//cal.setTime(sdf.parse("2017-03-06 02:45:00"));
		
		String startTime = "";
		String endTime = displayDateHourFormat.format(cal.getTime());
		
		cal.add(Calendar.HOUR, -1);
		startTime = displayDateHourFormat.format(cal.getTime());
		
		StringBuilder messageContent = new StringBuilder();
		messageContent.append("Appointments Variance Report\n\n");
		messageContent.append("Date\t\t: " + displayDateFormat.format(cal.getTime()) + "\n");
		messageContent.append("Time Between\t: " + startTime + ":00:00");
		
		messageContent.append(" ~ " + endTime + ":00:00\n\n");
		messageContent.append("\t\t4Wk. Avg.\tCurrent\t\tPercentage\n");
		messageContent.append("Store Type\tAppointments\tAppointments\tVariance\n");
		messageContent.append("----------\t------------\t------------\t------------\n");
		messageContent.append("FCAC\t\t75.50\t\t88.50\t\t13.00");
		return messageContent.toString();
	}
}
