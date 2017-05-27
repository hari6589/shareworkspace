package com.bridgestone.bsro.aem.voice.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.adobe.cq.sightly.WCMUse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bridgestone.bsro.aem.voice.services.IVoiceService;


public class VoiceSightlyController extends WCMUse {

	private static final Logger log = LoggerFactory
			.getLogger(VoiceSightlyController.class);

	Map<String, Object> appointmentDetails = null;
	
	public Map<String, Object> confirmAppointment (HttpServletRequest request){
		return appointmentDetails;
	}
	Map<String, Object> storeServicesDetails = null;
	public Map<String, Object> storeServices (){
		return storeServicesDetails;
	}

	/**
	 * Override activate method
	 */
	@Override
	public void activate() throws Exception {
		try {
			IVoiceService iappointmentService =
					getSlingScriptHelper().getService(IVoiceService.class);

			storeServicesDetails = iappointmentService.getAllServices(getRequest());
		} catch (Exception e) {
			log.error("Exception in activate() method of AppointmentSightlyController::", e);
		}
	}

}
