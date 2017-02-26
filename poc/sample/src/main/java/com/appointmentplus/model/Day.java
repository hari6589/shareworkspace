package com.appointmentplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "c_id", "employee_id" })
public class Day {

//	"date": "20170207",
//    "c_id": 1581,
//    "employee_id": 12709,
//    "month": 2,
//    "day": 7,
//    "year": 2017,
//    "day_name": "Tuesday"
    	
//    	 "availableDate": "20170207",
//         "availableDay": "7",
//         "availableMonth": "2",
//         "availableYear": "2017",
//         "dayName": "Tuesday"
	private String date;
	private String c_id;
	private String employee_id;
	private String month;
	private String day;
	private String year;
	private String day_name;
	
	@JsonIgnore
	public String getDate() {
		return date;
	}
	@JsonIgnore
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String c_id) {
		this.c_id = c_id;
	}
	
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getDay_name() {
		return day_name;
	}
	public void setDay_name(String day_name) {
		this.day_name = day_name;
	}
	
}
