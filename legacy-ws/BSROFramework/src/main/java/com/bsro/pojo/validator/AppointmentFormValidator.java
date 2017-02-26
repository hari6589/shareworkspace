package com.bsro.pojo.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletContext;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bfrc.framework.util.ServerUtil;
import com.bsro.pojo.form.AppointmentForm;
import com.bsro.service.store.StoreService;

@Component
public class AppointmentFormValidator implements Validator{
	
	private SimpleDateFormat completeDateFormat = new SimpleDateFormat("MM/d/yyyy");
	

	@Override
	public boolean supports(Class clazz) {
		return AppointmentForm.class.equals(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub		
	}

	@Autowired
	private ServletContext appContext;
	
	@Autowired
	private StoreService storeService;


	public void validateSelectStore(AppointmentForm appointmentForm, ValidationContext context) {
		MessageContext messages = context.getMessageContext();

		String currentEvent = context.getUserEvent();
		if(currentEvent.equalsIgnoreCase("search")){
			if(!StringUtils.hasText(appointmentForm.getEnteredZip())){
				if(!messages.hasErrorMessages()){
					messages.addMessage(new MessageBuilder().error()
							.source("enteredZip").code("msg.req.enterValue").resolvableArg("Zip or Address").build());
				}
			} 
		}else if(currentEvent.equals("continue") || currentEvent.equals("submit")){		
			if (appointmentForm.getStoreNumber() == null || appointmentForm.getStoreNumber() == 0) {
				if(!messages.hasErrorMessages()){
					messages.addMessage(new MessageBuilder().error()
							.source("storeNumber").code("msg.req.selectValue").resolvableArg("store").build());
				}
			}
		}
	}

	public void validateEnterVehicleInfo(AppointmentForm appointmentForm, ValidationContext context) {
		MessageContext messages = context.getMessageContext();
		if(!messages.hasErrorMessages()){
			if (appointmentForm.getVehicleYear() == null || appointmentForm.getVehicleYear() == 0 || "Year".equalsIgnoreCase(appointmentForm.getVehicleYear().toString())) {
				messages.addMessage(new MessageBuilder().error().source("vehicleYear")
						.code("msg.req.selectValue").resolvableArg("vehicle year").build());
				return;
			}
			if (!StringUtils.hasText(appointmentForm.getVehicleMakeName()) || "Select Make".equalsIgnoreCase(appointmentForm.getVehicleMakeName())) {
				messages.addMessage(new MessageBuilder().error().source("vehicleMakeName")
						.code("msg.req.selectValue").resolvableArg("vehicle make").build());
				return;
			}
			if (!StringUtils.hasText(appointmentForm.getVehicleModelName()) || "Select Model".equalsIgnoreCase(appointmentForm.getVehicleModelName())) {
				messages.addMessage(new MessageBuilder().error().source("vehicleModelName")
						.code("msg.req.selectValue").resolvableArg("vehicle model").build());
				return;
			}
			if (!StringUtils.hasText(appointmentForm.getVehicleSubmodel()) || "Select Submodel".equalsIgnoreCase(appointmentForm.getVehicleSubmodel())) {
				messages.addMessage(new MessageBuilder().error().source("vehicleSubmodel")
						.code("msg.req.selectValue").resolvableArg("vehicle submodel").build());
				return;
			}
			/*if(appointmentForm.getMaintenanceChoices() == null || appointmentForm.getMaintenanceChoices().length == 0){
				messages.addMessage(new MessageBuilder().error().source("maintenanceChoices").code("msg.appt.maintenanceItem").build());
				return;
			}
			//do we need to strip out commas here if jquery is turned off?
			if(appointmentForm.getMileage() == null){
				messages.addMessage(new MessageBuilder().error().source("mileage").code("msg.req.enterValue").resolvableArg("mileage").build());
				return;
			}else if(!NumberUtils.isDigits(appointmentForm.getMileage().toString())){
				messages.addMessage(new MessageBuilder().error().source("mileage").code("msg.appt.enterMileageDigits").build());
				return;
			}else if(appointmentForm.getMileage() <= 0 || appointmentForm.getMileage() > 999999){
				String[] args = {"mileage", "1", "999,999"};
				messages.addMessage(new MessageBuilder().error().source("mileage").code("msg.req.fieldOutOfRange").resolvableArgs(args).build());
				return;
			}


			if(appointmentForm.getMaintenanceChoices() != null){
				//make sure we only have one option selected and that option is "OTHER" == 19
				if(appointmentForm.getMaintenanceChoices().length == 1 && appointmentForm.getMaintenanceChoices()[0].equalsIgnoreCase("19")){				
					if(!StringUtils.hasText(appointmentForm.getComments())){
						messages.addMessage(new MessageBuilder().error().source("comments").code("msg.appt.enterServiceComment").build());
						return;
					}				
				}
			}*/

			//		if(appointmentForm.getTpms() != null && (!appointmentForm.getTpms().equalsIgnoreCase("1") || !appointmentForm.getTpms().equalsIgnoreCase("0")) ){
			//				messages.addMessage(new MessageBuilder().error().source("tpms").defaultText("Please select a TPMS value").build());				
			//		}

			//Need to check for illegal characters here.
			//		if(!StringUtils.hasText(appointmentForm.getComments())){
			//			messages.addMessage(new MessageBuilder().error().source("comments").defaultText("Please enter a mileage between 1 and 999,999").build());
			//		}
		}
		
	}

	public void validateSelectDateTime(AppointmentForm appointmentForm, ValidationContext context) {
		int numEligibleDays = appointmentForm.getNumEligibleDays();
		MessageContext messages = context.getMessageContext();
		if(!messages.hasErrorMessages()){
			GregorianCalendar today = new GregorianCalendar();
			GregorianCalendar daysOut = new GregorianCalendar();
			try {
				// the validation uses calendar.before, which does not right inclusive,
				//so we need to add one day to the max to allow for the final day to be scheduled.
				daysOut.setTime(completeDateFormat.parse(appointmentForm.getAppointmentSchedule().getMaxDate()));
				daysOut.add(Calendar.DATE, 1);
			} catch (ParseException e1) {
				//unable to parse the exception
				e1.printStackTrace();
				//continue on with daysOut30 in current time + 31 days.
				// since set hours to beginning of day, cannot be more than 31 days past this time
				daysOut.set(Calendar.HOUR_OF_DAY, 0);
				daysOut.set(Calendar.MINUTE, 0);
				daysOut.set(Calendar.SECOND, 0);
				daysOut.set(Calendar.MILLISECOND, 0);
				daysOut.add(Calendar.DATE, numEligibleDays+1);
			}



			// actually, cannot be before tomorrow
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			today.add(Calendar.DATE, 1);

			GregorianCalendar cal1 = new GregorianCalendar(), cal2= new GregorianCalendar();
			boolean choice1Valid = false, choice2Valid = false;
			boolean choice1Populated = false, choice2Populated = false;
			String choice1Time="";
			int validDates = 0;

			if (!ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Date())){
				if(appointmentForm.getChoice1Radio().equalsIgnoreCase("drop")){
					
					if(!ServerUtil.isNullOrEmpty(appointmentForm.getDropOffTime())){
						/*choice1Populated = true;
						try{
							choice1Time = appointmentForm.getDropOffTime();
							cal1.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(choice1Time+"m").toUpperCase()));
							choice1Valid = true;
							validDates++;
						}catch (Exception e) {
						}*/
					}else{
						/*messages.addMessage(new MessageBuilder().error()
								.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("drop off time").build());
						return;*/
					}
					
					if(ServerUtil.isNullOrEmpty(appointmentForm.getPickUpTime())){
						/*messages.addMessage(new MessageBuilder().error()
								.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("pick up time").build());
						return;*/
					}

					/*try {
						Calendar dCal = Calendar.getInstance();
						dCal.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(appointmentForm.getDropOffTime()+"m").toUpperCase()));
						Calendar pCal =  Calendar.getInstance();
						pCal.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(appointmentForm.getPickUpTime()+"m").toUpperCase()));
						
						if(dCal.after(pCal) || dCal.equals(pCal)){
							messages.addMessage(new MessageBuilder().error()
									.source("choice1Date").code("msg.appt.pickUpBeforeDropOffTime").build());
							return;
						}
						
//						int minTimeBetweenDropOffAndPickUp = appointmentForm.getMinTimeBetweenPickUpAndDropOff();
//						if((pCal.get(pCal.HOUR_OF_DAY) - dCal.get(dCal.HOUR_OF_DAY)) < minTimeBetweenDropOffAndPickUp){
//							messages.addMessage(new MessageBuilder().error()
//								.source("choice1Date").code("msg.appt.timeDiffPickUpAndDropOff")
//								.resolvableArg(String.valueOf(minTimeBetweenDropOffAndPickUp)).build());
//							return;
//						}
						
					} catch (ParseException e) {
						//e.printStackTrace();
					}*/
					
					if(ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Time())){
						messages.addMessage(new MessageBuilder().error()
								.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("appointment time").build());
						return;
					}
					
				}
				else if(appointmentForm.getChoice1Radio().equalsIgnoreCase("wait")){
					if(ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Time())){
						messages.addMessage(new MessageBuilder().error()
								.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("appointment time").build());
						return;
					}
				}
				else if(!ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Time())){
					choice1Populated = true;
					try{
						choice1Time = appointmentForm.getChoice1Time();
						cal1.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(choice1Time+"m").toUpperCase()));
						choice1Valid = true;
						validDates++;
					}catch (Exception e) {
					}
				}
			}else{
				messages.addMessage(new MessageBuilder().error()
						.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("appointment date").build());
				return;				
			}
		}
	}

	public void validateEnterYourInfo(AppointmentForm appointmentForm, ValidationContext context) {
		//THESE CONSTANTS NEED TO BE EXTRACTED TO A CENTRAL LOCATION
		MessageContext messages = context.getMessageContext();
		if(!messages.hasErrorMessages()){
			appointmentForm.setFirstName((appointmentForm.getFirstName() == null) ? "" : appointmentForm.getFirstName().replace("'", ""));
			appointmentForm.setLastName((appointmentForm.getLastName() == null) ? "" : appointmentForm.getLastName().replace("'", ""));
			appointmentForm.setComments((appointmentForm.getComments() == null) ? "" : appointmentForm.getComments().replace("'", ""));
			appointmentForm.setCity((appointmentForm.getCity() == null) ? "" : appointmentForm.getCity().replace("'", ""));				

			//		validationFields.put("firstName", "{name: 'First Name', validations: 'required', minLength:'1', maxLength:'50'}");
			if(!StringUtils.hasText(appointmentForm.getFirstName()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("firstName").code("msg.req.enterFieldName").resolvableArg("first name").build());
				return;
			}else if(appointmentForm.getFirstName().length() < 1 || appointmentForm.getFirstName().length() > 50){
				String[] args = {"first name", "1", "50"};
				messages.addMessage(new MessageBuilder().error()
						.source("firstName").code("msg.req.fieldCharRange").resolvableArg(args).build());
				return;
			}else if(appointmentForm.getFirstName().replace("'", "").length() < 1){
				messages.addMessage(new MessageBuilder().error()
						.source("firstName").code("msg.req.enterValidValue").resolvableArg("first name").build());
				return;
			}

			//	    validationFields.put("lastName", "{name: 'Last Name', validations: 'required', minLength:'1', maxLength:'80'}");
			if(!StringUtils.hasText(appointmentForm.getLastName()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("lastName").code("msg.req.enterFieldName").resolvableArg("last name").build());
				return;
			}else if(appointmentForm.getLastName().length() < 1 || appointmentForm.getLastName().length() > 80){
				String[] args = {"last name", "1", "50"};
				messages.addMessage(new MessageBuilder().error()
						.source("lastName").code("msg.req.fieldCharRange").resolvableArg(args).build());
				return;
			}else if(appointmentForm.getLastName().replace("'", "").length() < 1){
				messages.addMessage(new MessageBuilder().error()
						.source("lastName").code("msg.req.enterValidValue").resolvableArg("last name").build());
				return;
			}

			//	    validationFields.put("address", "{name: 'Address', validations: 'required', minLength:'1', maxLength:'50'}");
			if(!StringUtils.hasText(appointmentForm.getAddress()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("address").code("msg.req.enterFieldName").resolvableArg("address").build());
				return;
			}else if(appointmentForm.getAddress().length() < 1 || appointmentForm.getAddress().length() > 50){
				messages.addMessage(new MessageBuilder().error()
						.source("address").code("msg.req.fieldCharRange").resolvableArg("address").build());
				return;
			}

			//	    validationFields.put("city", "{name: 'City', validations: 'required', minLength:'1', maxLength:'50'}");		
			if(!StringUtils.hasText(appointmentForm.getCity()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("city").code("msg.req.enterFieldName").resolvableArg("city").build());
				return;
			}else if(appointmentForm.getCity().length() < 1 || appointmentForm.getCity().length() > 50){
				String[] args = {"city", "1", "50"};
				messages.addMessage(new MessageBuilder().error()
						.source("city").code("msg.req.fieldCharRange").resolvableArg(args).build());
				return;
			}else if(appointmentForm.getCity().replace("'", "").length() < 1){
				messages.addMessage(new MessageBuilder().error()
						.source("city").code("msg.req.enterValidValue").resolvableArg("city").build());
				return;
			}

			//	    validationFields.put("state", "{name: 'State', validations: 'required'}");
			if(!StringUtils.hasText(appointmentForm.getState()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("state").code("msg.req.selectValue").resolvableArg("state").build());
				return;
			}

			//	    validationFields.put("zip", "{name: 'Zip', validations: 'required', minLength:'1', maxLength:'10'}");		
			if(!StringUtils.hasText(appointmentForm.getZip()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("zip").code("msg.req.enterFieldName").resolvableArg("zip code").build());
				return;
			}else if(appointmentForm.getZip().length() < 1 || appointmentForm.getZip().length() > 10){
				String[] args = {"zip", "1", "10"};
				messages.addMessage(new MessageBuilder().error()
						.source("zip").code("msg.req.fieldNumRange").resolvableArg(args).build());
				return;
			}else if(!appointmentForm.getZip().matches("\\d{5}([- ]?\\d{4})?")){			
				messages.addMessage(new MessageBuilder().error()
						.source("zip").code("msg.req.enterValidValue").resolvableArg("zip code").build());
				return;
			}else if (!com.bfrc.framework.util.StoreSearchUtils.checkZipCode(appContext, appointmentForm.getZip()) ) {
				messages.addMessage(new MessageBuilder().error()
						.source("zip").code("msg.req.enterValidValue").resolvableArg("zip code").build());
				return;
			}	

			//	    validationFields.put("daytimePhone", "{name: 'Daytime Phone', validations: 'required', minLength:'1', maxLength:'14'}");
			if(!StringUtils.hasText(appointmentForm.getDaytimePhone()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("daytimePhone").code("msg.req.enterFieldName").resolvableArg("phone number").build());
				return;
			}else if(appointmentForm.getDaytimePhone().length() < 1 || appointmentForm.getDaytimePhone().length() > 14){
				messages.addMessage(new MessageBuilder().error()
						.source("daytimePhone").code("msg.req.enterValidValue").resolvableArg("phone number 555-555-5555").build());
				return;
			}else if(!com.bfrc.framework.util.StringUtils.isValidPhone(appointmentForm.getDaytimePhone())){			
				messages.addMessage(new MessageBuilder().error()
						.source("daytimePhone").code("msg.appt.enterValidPhoneNum").build());
				return;	
			}

			//phone extension - the field is optional, but should validate it if anything entered.
			if(StringUtils.hasText(appointmentForm.getDaytimePhoneExt())){
				if(appointmentForm.getDaytimePhoneExt().length() > 7){
					String[] args={"Extension", "7"};
					messages.addMessage(new MessageBuilder().error()
							.source("daytimePhoneExt").code("msg.req.fieldMaxDigits").resolvableArgs(args).build());
					return;
				}else if(!NumberUtils.isDigits(appointmentForm.getDaytimePhoneExt())){
					messages.addMessage(new MessageBuilder().error()
							.source("daytimePhoneExt").code("msg.appt.fieldDigitsOnly").resolvableArg("extension").build());
					return;
				}
			}

			//	    validationFields.put("emailAddress", "{name: 'Email Address', validations: 'required',  minLength:'1', maxLength:'255'}");
			if(!StringUtils.hasText(appointmentForm.getEmailAddress()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("emailAddress").code("msg.req.enterFieldName").resolvableArg("email address").build());
				return;
			}else if(appointmentForm.getEmailAddress().length() < 1 || appointmentForm.getEmailAddress().length() > 255){
				String[] args = {"email address", "1", "255"};
				messages.addMessage(new MessageBuilder().error()
						.source("emailAddress").code("msg.req.fieldCharRange").resolvableArg(args).build());
				return;
			}else if(!com.bfrc.framework.util.StringUtils.isValidEmail(appointmentForm.getEmailAddress())){
				messages.addMessage(new MessageBuilder().error()
						.source("emailAddress").code("msg.req.enterValidValue").resolvableArg("email address").build());
				return;
			}
		}
	}

	public void validateFindaStore(AppointmentForm appointmentForm, ValidationContext context) {
		MessageContext messages = context.getMessageContext();

		String currentEvent = context.getUserEvent();
		if(currentEvent.equalsIgnoreCase("search")){
			if(!StringUtils.hasText(appointmentForm.getEnteredZip())){
				if(!messages.hasErrorMessages()){
					messages.addMessage(new MessageBuilder().error()
							.source("enteredZip").code("msg.req.enterValue").resolvableArg("Zip or Address").build());
				}
			} 
		}else if(currentEvent.equals("continue")){		
			if (appointmentForm.getStoreNumber() == null || appointmentForm.getStoreNumber() == 0) {
				if(!messages.hasErrorMessages()){
					messages.addMessage(new MessageBuilder().error()
							.source("storeNumber").code("msg.req.selectValue").resolvableArg("store").build());
				}
			}
		}
	}

	public void validateContactAndServiceDetails(AppointmentForm appointmentForm, ValidationContext context) {
		//THESE CONSTANTS NEED TO BE EXTRACTED TO A CENTRAL LOCATION
		MessageContext messages = context.getMessageContext();
		if(!messages.hasErrorMessages()){
			appointmentForm.setFirstName((appointmentForm.getFirstName() == null) ? "" : appointmentForm.getFirstName().replace("'", ""));
			appointmentForm.setLastName((appointmentForm.getLastName() == null) ? "" : appointmentForm.getLastName().replace("'", ""));
			appointmentForm.setComments((appointmentForm.getComments() == null) ? "" : appointmentForm.getComments().replace("'", ""));

			//		validationFields.put("firstName", "{name: 'First Name', validations: 'required', minLength:'1', maxLength:'50'}");
			if(!StringUtils.hasText(appointmentForm.getFirstName()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("firstName").code("msg.req.enterFieldName").resolvableArg("first name").build());
			}else if(appointmentForm.getFirstName().length() < 1 || appointmentForm.getFirstName().length() > 50){
				String[] args = {"first name", "1", "50"};
				messages.addMessage(new MessageBuilder().error()
						.source("firstName").code("msg.req.fieldCharRange").resolvableArg(args).build());
			}else if(appointmentForm.getFirstName().replace("'", "").length() < 1){
				messages.addMessage(new MessageBuilder().error()
						.source("firstName").code("msg.req.enterValidValue").resolvableArg("first name").build());
			}

			//	    validationFields.put("lastName", "{name: 'Last Name', validations: 'required', minLength:'1', maxLength:'80'}");
			if(!StringUtils.hasText(appointmentForm.getLastName()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("lastName").code("msg.req.enterFieldName").resolvableArg("last name").build());
			}else if(appointmentForm.getLastName().length() < 1 || appointmentForm.getLastName().length() > 80){
				String[] args = {"last name", "1", "50"};
				messages.addMessage(new MessageBuilder().error()
						.source("lastName").code("msg.req.fieldCharRange").resolvableArg(args).build());
			}else if(appointmentForm.getLastName().replace("'", "").length() < 1){
				messages.addMessage(new MessageBuilder().error()
						.source("lastName").code("msg.req.enterValidValue").resolvableArg("last name").build());
			}

			//	    validationFields.put("daytimePhone", "{name: 'Daytime Phone', validations: 'required', minLength:'1', maxLength:'14'}");
			if(!StringUtils.hasText(appointmentForm.getDaytimePhone()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("daytimePhone").code("msg.req.enterFieldName").resolvableArg("phone number").build());
			}else if(appointmentForm.getDaytimePhone().length() < 1 || appointmentForm.getDaytimePhone().length() > 14){
				messages.addMessage(new MessageBuilder().error()
						.source("daytimePhone").code("msg.req.enterValidValue").resolvableArg("phone number 5555555555").build());
			}else if(!com.bfrc.framework.util.StringUtils.isValidPhone(appointmentForm.getDaytimePhone())){			
				messages.addMessage(new MessageBuilder().error()
						.source("daytimePhone").code("msg.appt.enterValidPhoneNum").build());
			}

			//	    validationFields.put("emailAddress", "{name: 'Email Address', validations: 'required',  minLength:'1', maxLength:'255'}");
			if(!StringUtils.hasText(appointmentForm.getEmailAddress()) ){
				messages.addMessage(new MessageBuilder().error()
						.source("emailAddress").code("msg.req.enterFieldName").resolvableArg("email address").build());
			}else if(appointmentForm.getEmailAddress().length() < 1 || appointmentForm.getEmailAddress().length() > 255){
				String[] args = {"email address", "1", "255"};
				messages.addMessage(new MessageBuilder().error()
						.source("emailAddress").code("msg.req.fieldCharRange").resolvableArg(args).build());
			}else if(!com.bfrc.framework.util.StringUtils.isValidEmail(appointmentForm.getEmailAddress())){
				messages.addMessage(new MessageBuilder().error()
						.source("emailAddress").code("msg.req.enterValidValue").resolvableArg("email address").build());
			}
			
			//vehicles
			if (appointmentForm.getVehicleYear() == null || appointmentForm.getVehicleYear() == 0) {
				messages.addMessage(new MessageBuilder().error().source("vehicleYear")
						.code("msg.req.selectValue").resolvableArg("vehicle year").build());
			}
			if (!StringUtils.hasText(appointmentForm.getVehicleMakeName()) || "Make".equalsIgnoreCase(appointmentForm.getVehicleMakeName())) {
				messages.addMessage(new MessageBuilder().error().source("vehicleMakeName")
						.code("msg.req.selectValue").resolvableArg("vehicle make").build());
			}
			if (!StringUtils.hasText(appointmentForm.getVehicleModelName()) || "Model".equalsIgnoreCase(appointmentForm.getVehicleModelName())) {
				messages.addMessage(new MessageBuilder().error().source("vehicleModelName")
						.code("msg.req.selectValue").resolvableArg("vehicle model").build());
			}
//			if (!StringUtils.hasText(appointmentForm.getVehicleSubmodel()) || "Submodel".equalsIgnoreCase(appointmentForm.getVehicleSubmodel())) {
//				messages.addMessage(new MessageBuilder().error().source("vehicleSubmodel")
//						.code("msg.req.selectValue").resolvableArg("vehicle submodel").build());
//				return;
//			}
			
			//do we need to strip out commas here if jquery is turned off?
			if(appointmentForm.getMileage()!= null){
				if(!NumberUtils.isDigits(appointmentForm.getMileage().toString())){
					messages.addMessage(new MessageBuilder().error().source("mileage").code("msg.appt.enterMileageDigits").build());
				}else if((appointmentForm.getMileage() <= 0 || appointmentForm.getMileage() > 999999)){
					String[] args = {"mileage", "1", "999,999"};
					messages.addMessage(new MessageBuilder().error().source("mileage").code("msg.req.fieldOutOfRange").resolvableArgs(args).build());
				}
			}

			// 	services
			if(appointmentForm.getMaintenanceChoices() == null || appointmentForm.getMaintenanceChoices().length == 0){			
				if(appointmentForm.getCustomerComments() == null || !StringUtils.hasText(appointmentForm.getCustomerComments().trim())){
					messages.addMessage(new MessageBuilder().error().source("maintenanceChoices").code("msg.appt.maintenanceItem").build());
				}									
			}			
		}
	}
	
	public void validateAppointmentDateTime(AppointmentForm appointmentForm, ValidationContext context) {
		int numEligibleDays = appointmentForm.getNumEligibleDays();
		MessageContext messages = context.getMessageContext();
		if(!messages.hasErrorMessages()){
			GregorianCalendar today = new GregorianCalendar();
			GregorianCalendar daysOut = new GregorianCalendar();
			try {
				// the validation uses calendar.before, which does not right inclusive,
				//so we need to add one day to the max to allow for the final day to be scheduled.
				daysOut.setTime(completeDateFormat.parse(appointmentForm.getAppointmentSchedule().getMaxDate()));
				daysOut.add(Calendar.DATE, 1);
			} catch (ParseException e1) {
				//unable to parse the exception
				e1.printStackTrace();
				//continue on with daysOut30 in current time + 31 days.
				// since set hours to beginning of day, cannot be more than 31 days past this time
				daysOut.set(Calendar.HOUR_OF_DAY, 0);
				daysOut.set(Calendar.MINUTE, 0);
				daysOut.set(Calendar.SECOND, 0);
				daysOut.set(Calendar.MILLISECOND, 0);
				daysOut.add(Calendar.DATE, numEligibleDays+1);
			}



			// actually, cannot be before tomorrow
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			today.add(Calendar.DATE, 1);

			GregorianCalendar cal1 = new GregorianCalendar(), cal2= new GregorianCalendar();
			boolean choice1Valid = false, choice2Valid = false;
			boolean choice1Populated = false, choice2Populated = false;
			String choice1Time="";
			int validDates = 0;

			if (!ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Date())){
//				if(storeService.isApptPilotStore(appointmentForm.getStoreNumber()) && appointmentForm.getChoice1Radio().equalsIgnoreCase("drop")){
//					if(!ServerUtil.isNullOrEmpty(appointmentForm.getDropOffTime())){
//						choice1Populated = true;
//						try{
//							choice1Time = appointmentForm.getDropOffTime();
//							cal1.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(choice1Time+"m").toUpperCase()));
//							choice1Valid = true;
//							validDates++;
//						}catch (Exception e) {
//						}
//					}else{
//						messages.addMessage(new MessageBuilder().error()
//								.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("drop off time").build());
//						return;
//					}
//					
//					if(ServerUtil.isNullOrEmpty(appointmentForm.getPickUpTime())){
//						messages.addMessage(new MessageBuilder().error()
//								.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("pick up time").build());
//						return;
//					}

//					try {
//						Calendar dCal = Calendar.getInstance();
//						dCal.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(appointmentForm.getDropOffTime()+"m").toUpperCase()));
//						Calendar pCal =  Calendar.getInstance();
//						pCal.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(appointmentForm.getPickUpTime()+"m").toUpperCase()));
//						
//						if(dCal.after(pCal) || dCal.equals(pCal)){
//							messages.addMessage(new MessageBuilder().error()
//									.source("choice1Date").code("msg.appt.pickUpBeforeDropOffTime").build());
//							return;
//						}
						
//						int minTimeBetweenDropOffAndPickUp = appointmentForm.getMinTimeBetweenPickUpAndDropOff();
//						if((pCal.get(pCal.HOUR_OF_DAY) - dCal.get(dCal.HOUR_OF_DAY)) < minTimeBetweenDropOffAndPickUp){
//							messages.addMessage(new MessageBuilder().error()
//								.source("choice1Date").code("msg.appt.timeDiffPickUpAndDropOff")
//								.resolvableArg(String.valueOf(minTimeBetweenDropOffAndPickUp)).build());
//							return;
//						}
						
//					} catch (ParseException e) {
//						//e.printStackTrace();
//					}
					
//				}
				if(ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Time())){
					messages.addMessage(new MessageBuilder().error()
							.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("appointment time").build());
					return;
				}
				
				else if(!ServerUtil.isNullOrEmpty(appointmentForm.getChoice1Time())){
					choice1Populated = true;
					try{
						choice1Time = appointmentForm.getChoice1Time();
						cal1.setTime(appointmentForm.getDatetimeFormat().parse(appointmentForm.getChoice1Date()+" "+(choice1Time+"m").toUpperCase()));
						choice1Valid = true;
						validDates++;
					}catch (Exception e) {
					}
				}
			}else{
				messages.addMessage(new MessageBuilder().error()
						.source("choice1Date").code("msg.req.enterValidValue").resolvableArg("appointment date").build());
				return;				
			}
		}
	}
	
	public void validateAppointmentDateTimePilot(AppointmentForm appointmentForm, ValidationContext context) {
		validateAppointmentDateTime(appointmentForm, context);
	}
	
	private boolean withinHours(GregorianCalendar aCal, GregorianCalendar bCal, int minTime){
		if (aCal.get(Calendar.MONTH) != bCal.get(Calendar.MONTH))
			return false;
		if (aCal.get(Calendar.DAY_OF_MONTH) != bCal.get(Calendar.DAY_OF_MONTH))
			return false;
		long hour1 = aCal.getTimeInMillis();
		long hour2 = bCal.getTimeInMillis();
		long interval = 1000 * 60 * 60 * minTime; // milliseconds in a second times seconds in a minute times minutes in an hour times 3 hours
		if (Math.abs(hour1 - hour2) < interval){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param aCal  - first appointment date time
	 * @param bCal - second appointment date time
	 * @param calenderDay day of the week Ex- CALENDAR.SATURDAY
	 * @return true if both appointments are before noon on the same day, else false
	 */
	private boolean sameDayMorning(GregorianCalendar aCal, GregorianCalendar bCal, int calenderDay){
		if (aCal.get(Calendar.MONTH) != bCal.get(Calendar.MONTH))
			return false;
		if (aCal.get(Calendar.DAY_OF_MONTH) != bCal.get(Calendar.DAY_OF_MONTH))
			return false;
		if (aCal.get(Calendar.DAY_OF_WEEK) != calenderDay
			|| bCal.get(Calendar.DAY_OF_WEEK) != calenderDay)
			return false;
		GregorianCalendar noon = new GregorianCalendar();
		noon.setTime(aCal.getTime());
		noon.set(Calendar.HOUR_OF_DAY, 12);
		noon.set(Calendar.MINUTE, 0);
		noon.set(Calendar.SECOND, 0);
		noon.set(Calendar.MILLISECOND, 0);
		if (aCal.before(noon) && bCal.before(noon)){
			return true;
		}
		return false;
	}

	private String getWeekDayLiteral(int calendarDay){
		if(calendarDay == Calendar.MONDAY){
			return "Monday";
		}else if(calendarDay == Calendar.TUESDAY){
			return "Tuesday";
		}else if(calendarDay == Calendar.WEDNESDAY){
			return "Wednesday";
		}else if(calendarDay == Calendar.THURSDAY){
			return "Thursday";
		}else if(calendarDay == Calendar.FRIDAY){
			return "Friday";
		}else if(calendarDay == Calendar.SATURDAY){
			return "Saturday";
		}else if(calendarDay == Calendar.SUNDAY){
			return "Sunday";
		}
		return "";
	}
}
