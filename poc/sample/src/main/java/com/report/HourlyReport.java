package com.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.scheduleappointment.model.Appointment;

public class HourlyReport {

	public static SimpleDateFormat displayDateHourFormat = new SimpleDateFormat("yyyy-MM-dd HH");
	public static SimpleDateFormat displayDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public static void main(String[] args) {
		
		String[] webSites = {"FCAC", "TP", "WWT", "HT"}; // Sites to be checked
		int numOfSites = webSites.length; // Number of Sites to be compared
		int numOfWeeks = 5; // Number of Weeks to be compared
		int[][] previousWeeks = new int[webSites.length][numOfWeeks];
		
		DecimalFormat decimalFormat = new DecimalFormat("#.###");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, -1);
		
		/*
		try {
			cal.setTime(sdf.parse("2017-03-06 02:45:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
		/*for(int i=0; i<webSites.length; i++) {
			System.out.println(webSites[i] + ": " + sdf.format(cal.getTime()));
			for(int j=0; j < numOfWeeks; j++, cal.add(Calendar.DATE, -7)) {
				previousWeeks[i][j] = getAppointmentCount(webSites[i] ,dateFormat.format(cal.getTime()));
				//System.out.println("Date: " + sdf.format(cal.getTime()) + ", Count: " + previousWeeks[i][j]);
			}
			cal.setTime(currentDate);
		}*/
		
		previousWeeks[0][0] = 500;
	    previousWeeks[0][1] = 100;
	    previousWeeks[0][2] = 100;
	    previousWeeks[0][3] = 100;
	    previousWeeks[0][4] = 100;
	    
	    previousWeeks[1][0] = 49;
	    previousWeeks[1][1] = 100;
	    previousWeeks[1][2] = 100;
	    previousWeeks[1][3] = 100;
	    previousWeeks[1][4] = 100;
	    
	    previousWeeks[2][0] = 20;
	    previousWeeks[2][1] = 100;
	    previousWeeks[2][2] = 100;
	    previousWeeks[2][3] = 100;
	    previousWeeks[2][4] = 99;
	    
	    previousWeeks[3][0] = 1;
	    previousWeeks[3][1] = 25;
	    previousWeeks[3][2] = 25;
	    previousWeeks[3][3] = 25;
	    previousWeeks[3][4] = 25;
		
		// Temp display section, could be removed
		for(int i=0; i<numOfSites; i++) {
			System.out.print(webSites[i]+": ");
			for(int j=0; j < numOfWeeks; j++, cal.add(Calendar.DATE, -7)) {
				System.out.print("\t" + previousWeeks[i][j]);
			}
			System.out.println();
		}

		int[] currentSiteValue = new int[numOfSites];
		int[] totalSiteValue = new int[numOfSites];
		double[] averageSiteValue = new double[numOfSites];
		double[] dropSitePercent = new double[numOfSites];
		boolean drops = false;
		StringBuilder messageContent = new StringBuilder();
		
		for(int i=0; i<numOfSites; i++) {
			for(int j=0; j < numOfWeeks; j++, cal.add(Calendar.DATE, -7)) {
				if(j == 0) {
					currentSiteValue[i] = previousWeeks[i][j];
				} else {
					totalSiteValue[i] += previousWeeks[i][j];
				}
			}
			averageSiteValue[i] = totalSiteValue[i]/Double.valueOf(numOfWeeks-1);
			dropSitePercent[i] = Double.valueOf(Double.valueOf((currentSiteValue[i] - averageSiteValue[i]) / Double.valueOf(averageSiteValue[i])) * 100);
			
			System.out.println(webSites[i] + "\t\t" + totalSiteValue[i]/Double.valueOf(numOfWeeks-1) + "\t\t" + currentSiteValue[i] + "\t\t" + decimalFormat.format(dropSitePercent[i]) + "\n");
			
			if(dropSitePercent[i] <= -50 && averageSiteValue[i] > 25) {
				drops = true;
				if(messageContent.length() < 1) {
					messageContent.append(messageHeaderContent());
				}
				messageContent.append(webSites[i] + "\t\t" + totalSiteValue[i] + "\t\t" + currentSiteValue[i] + "\t\t" + decimalFormat.format(dropSitePercent[i]) + "\n");
			}
		}
		
		System.out.println(messageContent.toString());
		
		if(drops) {
			MailReport mailReport = new MailReport();
			mailReport.mailReportData(messageContent.toString());
		} else {
			System.out.println("No drops in Appointment count!");
		}
	}
	
	public static int getAppointmentCount(String siteName, String dateHour) {
		
		List<Appointment> appointment = new ArrayList<Appointment>();
		AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(awsCredentials);
    	DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
    	
    	String startDateStr = dateHour+":00:00";
    	String endDateStr = dateHour+":59:59";
    	
    	System.out.println("Dates are " + startDateStr + " & " + endDateStr + " , Site is " + siteName);
    	
	    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
	    eav.put(":startDateTime", new AttributeValue().withS(startDateStr));
	    eav.put(":endDateTime", new AttributeValue().withS(endDateStr));
	    eav.put(":webSite", new AttributeValue().withS(siteName));

    	DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
    		.withFilterExpression("webSite = :webSite and createdDate between :startDateTime and :endDateTime")
	    	.withExpressionAttributeValues(eav);
    	
    	appointment = new ArrayList<Appointment>(dynamoDBMapper.scan(Appointment.class, dynamoDBScanExpression));
    	
		return appointment.size();
	}
	
	public static String messageHeaderContent() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
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
		return messageContent.toString();
	}
}
