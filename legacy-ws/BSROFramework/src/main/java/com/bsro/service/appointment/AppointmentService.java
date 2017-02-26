package com.bsro.service.appointment;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.mvc.servlet.MvcExternalContext;

import com.bfrc.pojo.appointment.Appointment;
import com.bfrc.pojo.appointment.AppointmentServiceAndCat;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreSearch;
import com.bsro.pojo.form.AppointmentForm;

public interface AppointmentService {

	public AppointmentForm initializeForm(HttpServletRequest request,String storeNumber, Store preferredStore, 
			String zip, String comments, String maintenanceChoices, String firstName, String lastName, 
			String emailAddress, String noValidation, String rules);
	
	public List<Store> initializeClosestStores(HttpServletRequest request, String city, String state, AppointmentForm appointmentForm, Store store);
	
	public Store syncStore(AppointmentForm appointmentForm, Store store);
	
	public List<AppointmentServiceAndCat> getAppointmentServices();
	
	public List<AppointmentServiceAndCat> getAppointmentServices(AppointmentForm appointmentForm);
	
	public List<AppointmentServiceAndCat> getAppointmentServices(Long storeNumber, AppointmentForm appointmentForm);
	
	public List<AppointmentServiceAndCat> getServicesFromAppointmentPlus(AppointmentForm appointmentForm);
	
	public void printAppointmentInfo(AppointmentForm appointmentForm);
	
	public map.States getStatesMap();
	
	public Appointment scheduleAppointment(AppointmentForm appointmentForm) throws Exception;
	
	public Map getServiceMap();
	
	public void populateVehicleNames(AppointmentForm appointmentForm);
	
	public void checkIfMilitaryStore(AppointmentForm appointmentForm);
	public void savePreferredStoreToSession(MvcExternalContext context, Store store, AppointmentForm appointmentForm);
	public List<Store> searchForClosestStores(HttpServletRequest request, AppointmentForm appointmentForm);
	public List<Store> searchForClosestAppointmentStores(String ipAddress, StoreSearch storeSearch);
	
	public void setCachedZip(HttpServletRequest request, String enteredZip);
	public void saveVehicleDataToCache(HttpServletRequest request, AppointmentForm appointmentForm);
	
	public boolean doesStoreAllowScheduleAppointment(Store store);
	public boolean isMobileTireInstallStoreWithoutQuote(Store store, AppointmentForm appointmentForm);

	public void saveCustInfo(AppointmentForm appointmentForm);
	
	public void setAppointmentMetadata(AppointmentForm appointmentForm);
	
	public void stripInvalidVehicleData(AppointmentForm appointmentForm);

}
