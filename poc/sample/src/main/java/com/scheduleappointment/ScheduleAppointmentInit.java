package com.scheduleappointment;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import samples.App;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduleappointment.model.AppointmentData;

public class ScheduleAppointmentInit {

	public static void main(String[] args) {
		
		//JsonToObject(); // Json Structure String to Object
		
		objectSender(); // Hashmap of String to Object
		
	}
	
	public static void objectSender() {
		//Object input = "{body={storeNumber=23817, locationId=1581, employeeId=12713, appointmentStatusId=4088, appointmentStatusDesc=Scheduled, vehicleYear=2010, vehicleMake=Chevrolet, vehicleModel=Colorado, vehicleSubmodel=WT, quoteId=123456, mileage=7500, customerFirstName=Stallin, customerLastName=Moorthy, customerDayTimePhone=227-876-5678, customerEmailAddress=test@bfrc.com, websiteName=FCAC, appointmentType=New, choice={choice=1, datetime=1455811200000, dropWaitOption=drop}, selectedServices=2751,2767}}";
		Object input = "{storeNumber=23817, locationId=1581, employeeId=12713, appointmentStatusId=4088, appointmentStatusDesc=Scheduled, vehicleYear=2010, vehicleMake=Chevrolet, vehicleModel=Colorado, vehicleSubmodel=WT, quoteId=123456, mileage=7500, customerFirstName=Stallin, customerLastName=Moorthy, customerDayTimePhone=227-876-5678, customerEmailAddress=test@bfrc.com, websiteName=FCAC, appointmentType=New, choice={choice=1, datetime=1455811200000, dropWaitOption=drop}, selectedServices=2751,2767}";
		//String input = "{body={storeNumber=23817, locationId=1581, employeeId=12713, appointmentStatusId=4088, appointmentStatusDesc=Scheduled, vehicleYear=2010, vehicleMake=Chevrolet, vehicleModel=Colorado, vehicleSubmodel=WT, quoteId=123456, mileage=7500, customerFirstName=Stallin, customerLastName=Moorthy, customerDayTimePhone=227-876-5678, customerEmailAddress=test@bfrc.com, websiteName=FCAC, appointmentType=New, choice={choice=1, datetime=1455811200000, dropWaitOption=drop}, selectedServices=2751,2767}}";
		//String input = "{storeNumber=23817, locationId=1581, employeeId=12713, appointmentStatusId=4088, appointmentStatusDesc=Scheduled, vehicleYear=2010, vehicleMake=Chevrolet, vehicleModel=Colorado, vehicleSubmodel=WT, quoteId=123456, mileage=7500, customerFirstName=Stallin, customerLastName=Moorthy, customerDayTimePhone=227-876-5678, customerEmailAddress=test@bfrc.com, websiteName=FCAC, appointmentType=New, choice={choice=1, datetime=1455811200000, dropWaitOption=drop}, selectedServices=2751,2767}";
		objectReceiver(input);
	}
	
	public static void objectReceiver(Object input) {
		ObjectMapper objectMapper = new ObjectMapper();
		AppointmentData result = null;
		JSONObject json = new JSONObject(input);
		try {
			result = objectMapper.readValue(json.toString(), AppointmentData.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//result = objectMapper.convertValue(input, AppointmentData.class);
		
		System.out.println(result.getEmployeeId());
		
		//AppointmentData ad = (AppointmentData) input;
		//System.out.println(ad.toString());
	}
	
	public static void JsonToObject() {
		String jsonData = getJsonData();
		
		ObjectMapper mapper = new ObjectMapper();
		App result;
		try {
			result = mapper.readValue(jsonData, App.class);
			System.out.println("Property : " + result.toString());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getJsonData() {
		String jsonData = ""
				+ "{"
				+ "\"storeNumber\":\"23817\", \"locationId\":\"1581\", \"employeeId\":\"12713\","
				+ "\"subApp\":{\"choice\":\"1\"}"
				+ "}";
		return jsonData;
	}

}
