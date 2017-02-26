/**
 * 
 */
package com.bfrc.dataaccess.svc.myprofile;

import java.util.Date;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

/**
 * @author schowdhury
 *
 */
public interface BFSUserProfileService {
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse getUserProfile(String email, String userType);
	
	/**
	 * 
	 * @param userJSON
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse registerUserProfile(String userJSON, String userType);
	
	/**
	 * 
	 * @param userJSON
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse updateUserProfile(String userJSON, String userType);
	
	/**
	 * 
	 * @param userJSON
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse registerNewDriver(String userJSON, String userType);
	
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse removeUser(String email , String userType);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse getAllVehicles(String email, String userType);
	
	/**
	 * 
	 * @param email
	 * @param baseVehicleId
	 * @param submodel
	 * @return
	 */
	public BSROWebServiceResponse getVehicle( String email, String userType, Long acesVehicleId, String vinNumber);
	
	/**
	 * 
	 * @param userType
	 * @param email
	 * @param year
	 * @param make
	 * @param model
	 * @param submodel
	 * @return
	 */
	public BSROWebServiceResponse registerNewVehicle(String userType, String email, 
			Integer year, String make, String model, String submodel, String vehicleJSON);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param vehicleJSON
	 * @return
	 */
	public BSROWebServiceResponse updateVehicle( String email, String userType, String vehicleJSON);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param vehicleJSON
	 * @return
	 */
	public BSROWebServiceResponse removeVehicle( String email, String userType, Long acesVehicleId, String vinNumber);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @return
	 */
	public BSROWebServiceResponse getVehicleConfigurationOptions( String email, String userType, Long acesVehicleId);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @return
	 */
	public BSROWebServiceResponse getVehicleGasFillup(String email, String userType, Long acesVehicleId, String vinNumber);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @param jsonData
	 * @return
	 */
	public BSROWebServiceResponse addVehicleGasFillup(String email, String userType, Long acesVehicleId, String vinNumber, String jsonData);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @param jsonData
	 * @return
	 */
	public BSROWebServiceResponse updateVehicleGasFillup(String email, String userType, Long acesVehicleId, String vinNumber, String jsonData);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @return
	 */
	public BSROWebServiceResponse getServicePerformed(String email, String userType, Long acesVehicleId, String vinNumber);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @param jsonData
	 * @return
	 */
	public BSROWebServiceResponse addServicePerformed(String email, String userType, Long acesVehicleId, String vinNumber, String jsonData);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @return
	 */
	public BSROWebServiceResponse updateServicePerformed(String email, String userType, Long acesVehicleId, String vinNumber, String jsonData);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param acesVehicleId
	 * @param vinNumber
	 * @return
	 */
	public BSROWebServiceResponse removeServicePerformed(String email, String userType, Long servicePerformedId);
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param baseVehicleId
	 * @param submodel
	 * @param appointmentId
	 * @return
	 */
	public BSROWebServiceResponse addAppointmentForVehicle(String email, String userType, Long acesVehicleId, String vinNumber, Long appointmentId);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param email
	 * @return
	 */
	public BSROWebServiceResponse getFavouriteStores(String email, String userType);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param email
	 * @param storeNumber
	 * @return
	 */
	public BSROWebServiceResponse saveFavouriteStore(String email, String userType, String storeNumber);
	
	/** 
	 * @param email
	 * @param userType
	 * @param email
	 * @param storeNumber
	 * @return
	 */
	public BSROWebServiceResponse updateDistanceToStore(String email, String userType, String storeNumber);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param email
	 * @param storeNumber
	 * @return
	 */
	public BSROWebServiceResponse removeFavouriteStore(String email, String userType, String storeNumber);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse getFavouritePromotions(String email, String userType);
	
	/**
	 * 
	 * @param promotionId
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse saveFavouritePromotion(Long promotionId, String email, String userType);
	
	/**
	 * 
	 * @param promotionId
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse removeFavouritePromotion(Long promotionId, String email, String userType);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse getUserSavedQuotes(String email, String userType);
	
	/**
	 * 
	 * @param quoteId
	 * @param type
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse saveProductQuote(Long quoteId, String type,String email, String userType);
	
	/**
	 * 
	 * @param quoteId
	 * @param type
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse removeProductQuote(Long quoteId, String type, String email, String userType);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param baseVehicleId
	 * @param submodel
	 * @return
	 */
	public BSROWebServiceResponse getVehicleMaintenanceHistory(String email, String userType, Long acesVehicleId, String vinNumber);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param jsonData
	 * @param downloadDate
	 * @return
	 */
	public BSROWebServiceResponse saveVehicleMaintenanceHistory(String email, String userType, 
			Long acesVehicleId, String vinNumber, String jsonData, Date downloadDate);
	/**
	 * 
	 * @param imageType
	 * @param imageId
	 * @return
	 */
	public byte[] getImage(String imageType, Long myPictureId);
	/**
	 * 
	 * @param imageType
	 * @param image
	 * @return
	 */
	public BSROWebServiceResponse saveImage(String imageType, Long imageId, byte[] image);
	
	/**
	 * 
	 * @param imageType
	 * @param imageId
	 * @return
	 */
	public BSROWebServiceResponse deleteImage(String imageType, Long imageId);
	
	/**
	 * 
	 * @param resultset
	 * @return
	 */
	public BSROWebServiceResponse getJsonResponse(Object resultset);
	
	/**
	 * 
	 * @param message
	 * @param messageType
	 * @return
	 */
	public BSROWebServiceResponse getJsonMessage(String message, BSROWebServiceResponseCode messageType);
	
	/**
	 * 
	 * @param error
	 * @param errorType
	 * @return
	 */
	public BSROWebServiceResponse getJsonError(String error, BSROWebServiceResponseCode errorType);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse getRegisteredDevices(String email, String userType);
	
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param deviceJson
	 * @return
	 */
	public BSROWebServiceResponse registerNewDevice(String email, String userType, String deviceJson);
	/**
	 * 
	 * @param email
	 * @param userType
	 * @param deviceJson
	 * @return
	 */
	public BSROWebServiceResponse updateDeviceInfo(String email, String userType, String deviceJson);
	/**
	 * 
	 * @param email
	 * @param userType
	 * @return
	 */
	public BSROWebServiceResponse removeDevice(String email, String userType,Long deviceId);
}
