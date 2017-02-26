/**
 * 
 */
package com.bfrc.dataaccess.svc.myprofile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.dao.generic.AppointmentDAO;
import com.bfrc.dataaccess.dao.generic.PromotionDAO;
import com.bfrc.dataaccess.dao.generic.StoreDAO;
import com.bfrc.dataaccess.dao.myprofile.BFSUserDataDAO;
import com.bfrc.dataaccess.dao.myprofile.BFSUserProfileDAO;
import com.bfrc.dataaccess.model.aces.BaseVehicle;
import com.bfrc.dataaccess.model.aces.Vehicle;
import com.bfrc.dataaccess.model.aces.VehicleConfiguration;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.myprofile.BFSUser;
import com.bfrc.dataaccess.model.myprofile.MyAppointment;
import com.bfrc.dataaccess.model.myprofile.MyDevice;
import com.bfrc.dataaccess.model.myprofile.MyDriver;
import com.bfrc.dataaccess.model.myprofile.MyMaintenanceServicePerformed;
import com.bfrc.dataaccess.model.myprofile.MyPicture;
import com.bfrc.dataaccess.model.myprofile.MyProductQuote;
import com.bfrc.dataaccess.model.myprofile.MyPromotion;
import com.bfrc.dataaccess.model.myprofile.MyServiceHistoryInvoice;
import com.bfrc.dataaccess.model.myprofile.MyServiceHistoryJob;
import com.bfrc.dataaccess.model.myprofile.MyServiceHistoryJobDetail;
import com.bfrc.dataaccess.model.myprofile.MyServiceHistoryVehicle;
import com.bfrc.dataaccess.model.myprofile.MyStore;
import com.bfrc.dataaccess.model.myprofile.MyVehicle;
import com.bfrc.dataaccess.model.myprofile.MyVehicleGas;
import com.bfrc.dataaccess.model.myprofile.NCDVehicleHistory;
import com.bfrc.dataaccess.model.myprofile.PictureType;
import com.bfrc.dataaccess.model.myprofile.ProductType;
import com.bfrc.dataaccess.model.myprofile.Promotions;
import com.bfrc.dataaccess.model.myprofile.SourceType;
import com.bfrc.dataaccess.model.promotion.Promotion;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.vehicle.Fitment;
import com.bfrc.dataaccess.svc.bing.BingMapsAPI;
import com.bfrc.dataaccess.util.JsonObjectMapper;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.util.ValidatorUtils;



/**
 * @author schowdhu
 *
 */
@Service("bfsUserProfileService")
public class BFSUserProfileServiceImpl implements BFSUserProfileService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	BFSUserProfileDAO bfsUserProfileDAO;
	
	@Autowired
	BFSUserDataDAO bfsUserDataDAO;
	
	@Autowired
	private PromotionDAO promotionDAO;
	
	@Autowired
	private JsonObjectMapper jsonObjectMapper;
	
	@Autowired
	private AppointmentDAO appointmentDAO;
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	private BingMapsAPI bingMapsAPI;
	

	/* (non-Javadoc)
	 * @see com.bfrc.dataaccess.svc.myprofile.BFSUserProfileService#getUserProfileData(java.lang.String, java.lang.String, java.lang.String)
	 */
	public BSROWebServiceResponse getUserProfile(String email,	String userType) {
		logger.info("Inside getUserProfile");
		WebSite webSite = bfsUserDataDAO.getSite(userType);
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		}
		BSROWebServiceResponse userJson = getJsonResponse(user);
		logger.info("json="+userJson.getPayload());
		return userJson;
	}

	public BSROWebServiceResponse registerUserProfile(String userJSON, String userType) {
		logger.info("Inside registerUserProfile");
		logger.info("userJson="+userJSON);
		BFSUser user = null;
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			user = mapper.readValue(userJSON, BFSUser.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		WebSite webSite = bfsUserDataDAO.getSite(userType);
		BFSUser userPresent = bfsUserProfileDAO.doesUserExist(user.getEmail(), userType);
		
		if(userPresent != null && userPresent.getUserId() != null){
			return getJsonError(ValidationConstants.USER_ALREADY_EXISTS, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}

		String encryptPwd = ValidatorUtils.encryptPassword(user.getPassword());
		user.setPreviousEmail(user.getEmail());
		user.setPassword(encryptPwd);
		user.setUserType(webSite);

		user.setUnsuccessfulAttempts(BigDecimal.ZERO.intValue());
		
		
		MyDriver driver = user.getDriver();
		if(driver == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		driver.setUser(user);
		logger.info("user="+user);
		//user.setDriver(null);
		Long userId = bfsUserDataDAO.saveBfsUser(user);
		System.out.println("userId="+userId);
		//driver.setUserId(userId);
		//bfsUserProfileDAO.saveDriver(driver);
		
		return getJsonMessage(ValidationConstants.USER_SUCCESSFULLY_REGISTERED, BSROWebServiceResponseCode.SUCCESSFUL);
		
	}

	public BSROWebServiceResponse saveFavouriteStore(String email, String userType, String storeNumber) {
		MyDriver driver = bfsUserProfileDAO.doesDriverExist(userType, email);
		if(driver == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		if(bfsUserProfileDAO.doesFavouriteStoreExist(driver.getUserId(), storeNumber) != null){
			return getJsonError(ValidationConstants.STORE_AREADY_EXIST,BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		Store store = storeDAO.get(new Long(storeNumber));
		BigDecimal distance = null;
		
		if(driver.getAddress() != null && !driver.equals(""))
		{
		if(store.getLatitude() != null && store.getLongitude() != null)
		{
			distance = bingMapsAPI.getDistanceFromStore(driver.getAddress(), driver.getCity(), 
					driver.getState(), driver.getZip(),
					String.valueOf(store.getLatitude()), String.valueOf(store.getLongitude()));
		}
		else{
			if((driver.getCity()== null || driver.getCity().isEmpty())
					&& (driver.getState() == null || driver.getState().isEmpty()) 
					&& (driver.getZip()==null || driver.getZip().isEmpty()))
				return getJsonError(ValidationConstants.USER_ADDRESS_INVALID, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
				
			distance = bingMapsAPI.getDistanceBetweenAddress(driver.getAddress(), driver.getCity(), 
					driver.getState(), driver.getZip(),store.getAddress(), store.getCity(), 
					store.getState(), store.getZip());
		}
		}
		else
		{
			MyStore myStore = bfsUserProfileDAO.getMaxDistanceStore(driver.getUserId(), userType);
			if(myStore != null)
				distance = myStore.getDistance().add(new BigDecimal(1));
			else
				distance = new BigDecimal(1);
		}
		logger.info("distance======>"+distance);
		bfsUserProfileDAO.saveDriverStore(driver, storeNumber, distance);
		return getJsonMessage(ValidationConstants.STORE_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}
	
	public BSROWebServiceResponse updateDistanceToStore(String email, String userType, String storeNumber){
		MyDriver driver = bfsUserProfileDAO.doesDriverExist(userType, email);
		if(driver == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		if(bfsUserProfileDAO.doesFavouriteStoreExist(driver.getUserId(), storeNumber) == null){
			return getJsonError(ValidationConstants.STORE_DOES_NOT_EXIST,BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		MyStore myStore = bfsUserProfileDAO.doesFavouriteStoreExist(driver.getUserId(), storeNumber);
		BigDecimal distance = null;
		if(myStore.getStore().getLatitude() != null && myStore.getStore().getLongitude() != null)
			distance = bingMapsAPI.getDistanceFromStore(driver.getAddress(), driver.getCity(), 
					driver.getState(), driver.getZip(),
					String.valueOf(myStore.getStore().getLatitude()), String.valueOf(myStore.getStore().getLongitude()));
		else{
			if((driver.getCity()== null || driver.getCity().isEmpty())
					&& (driver.getState() == null || driver.getState().isEmpty()) 
					&& (driver.getZip()==null || driver.getZip().isEmpty()))
				return getJsonError(ValidationConstants.USER_ADDRESS_INVALID, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
				
			distance = bingMapsAPI.getDistanceBetweenAddress(driver.getAddress(), driver.getCity(), 
					driver.getState(), driver.getZip(),myStore.getStore().getAddress(), myStore.getStore().getCity(), 
					myStore.getStore().getState(), myStore.getStore().getZip());
		}
		if(distance == null)
			return getJsonError(ValidationConstants.USER_ADDRESS_INVALID, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		
		myStore.setDistance(distance);
		bfsUserProfileDAO.updateDriverStore(myStore);
		return getJsonMessage(ValidationConstants.STORE_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse removeFavouriteStore(String email,String userType,String storeNumber) {
		MyDriver driver = bfsUserProfileDAO.doesDriverExist(userType,email);
		if (driver != null){
			MyStore myStore = bfsUserProfileDAO.doesFavouriteStoreExist(driver.getUserId(), storeNumber);
			if(myStore != null){
				bfsUserProfileDAO.removeDriverStore(myStore);
				return getJsonMessage(ValidationConstants.USER_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
			}else{
				return getJsonMessage(ValidationConstants.STORE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
			}
		}
		return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
	}

	public BSROWebServiceResponse saveFavouritePromotion(Long promotionId, String email, String userType) {
		BFSUser user = bfsUserDataDAO.doesUserExist(email, bfsUserDataDAO.getSite(userType));
		if(user != null){
			MyPromotion myPromotion = bfsUserProfileDAO.doesFavouritePromotionExist(email, promotionId);
			if(myPromotion != null){
				return getJsonMessage(ValidationConstants.PROMOTION_AREADY_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
			}
			Promotion promotion = promotionDAO.get(promotionId);
			if(promotion != null){
				bfsUserProfileDAO.saveUserPromotion(user, promotion);
			}
			return getJsonMessage(ValidationConstants.PROMOTION_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
	}

	public BSROWebServiceResponse removeFavouritePromotion(Long promotionId, String email,
			String userType) {
		logger.info("Inside removeFavouritePromotion");
		MyPromotion myPromotion = bfsUserProfileDAO.doesFavouritePromotionExist(email, promotionId);
		if(myPromotion != null){
			bfsUserProfileDAO.removeUserPromotion(myPromotion);
			return getJsonMessage(ValidationConstants.PROMOTION_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
		}else
			return getJsonMessage(ValidationConstants.PROMOTION_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
	}
	
	public BSROWebServiceResponse getVehicle(String email, String userType, Long acesVehicleId, String vinNumber){
		logger.info("Inside getVehicle email="+email+" userType="+userType+" acesId="+acesVehicleId+" vin="+vinNumber);
		List<MyVehicle> vehicles = bfsUserProfileDAO.getVehicles(email,userType,acesVehicleId);
		MyVehicle myVehicle = null;
		if(vehicles == null || vehicles.isEmpty())
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		if(vehicles.size() > 1 && (vinNumber == null || vinNumber.isEmpty()))
			return getJsonError(ValidationConstants.MORE_THAN_ONE_VEHICLE_PRESENT, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		
		if(vehicles.size() == 1)
			myVehicle = vehicles.get(0);
		else if(vehicles.size() > 1){
			for(MyVehicle vehicle : vehicles){
				if(vinNumber.equalsIgnoreCase(vehicle.getVinNumber())){
					myVehicle = vehicle;
					break;
				}
			}
			if(myVehicle == null)
				return getJsonError(ValidationConstants.VIN_NUMBER_DONOT_MATCH, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO); 
		}
		//Vehicle vehicle = myVehicle.getVehicle();
		
		return getJsonResponse(myVehicle);
	}


	public BSROWebServiceResponse getAllVehicles(String email, String userType) {
		logger.info("Inside getVehicles");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		Set<MyVehicle> vehicles = user.getDriver().getVehicles();
		
		return getJsonResponse(vehicles);
	}
	
	public BSROWebServiceResponse getVehicleConfigurationOptions( String email, String userType, Long acesVehicleId){
		logger.info("Inside getVehicleConfigOptions");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		List<VehicleConfiguration> configList = bfsUserProfileDAO.getVehicleConfig(acesVehicleId);
		return getJsonResponse(configList);
	}
	
	public BSROWebServiceResponse removeVehicle(String email, String userType, Long acesVehicleId, String vinNumber){
		logger.info("Inside removeVehicle");
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle vehicle = (MyVehicle)getVehicleResp.getPayload();
		if(bfsUserProfileDAO.doesVehicleExist(vehicle.getMyVehicleId()) == null){
			return getJsonMessage(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		}
		bfsUserProfileDAO.removeVehicle(vehicle);
		return getJsonMessage(ValidationConstants.VEHICLE_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL); 
	}

	public BSROWebServiceResponse registerNewVehicle(String userType, String email, Integer year, String make, 
			String model, String submodel, String vehicleJson) {
		logger.info("Inside registerNewVehicle");
		
		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		MyVehicle myVehicle = new MyVehicle();
		try {
			myVehicle = jsonObjectMapper.readValue(vehicleJson, MyVehicle.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.debug("myVehicle= "+myVehicle);
		MyDriver driver = bfsUserProfileDAO.doesDriverExist(userType, email);
		if(driver == null)
			return getJsonError(ValidationConstants.DRIVER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		Fitment fitment = bfsUserProfileDAO.getVehicle(year, make, model, submodel);
		if(fitment == null)
			return getJsonError(ValidationConstants.VEHICLE_DATA_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		myVehicle.setFitment(fitment);
		myVehicle.setDriver(driver);
		List<VehicleConfiguration> vehicleConfigList = bfsUserProfileDAO.getVehicleConfig(fitment.getAcesVehicleId());
		if(vehicleConfigList !=null && vehicleConfigList.size() == 1)
			myVehicle.setVehicleConfiguration(vehicleConfigList.get(0));
		
		// This below code is used to update the primary vehicle
		if(myVehicle.getPrimaryVehicle().equals(1))
		{
			if(driver.getVehicles() != null && !driver.getVehicles().isEmpty())
			{
				for(MyVehicle vehicle : driver.getVehicles())
				{
					if(vehicle.getPrimaryVehicle().equals(1))
					{
					vehicle.setPrimaryVehicle(0);
					bfsUserProfileDAO.updateVehicle(vehicle);
					}
				}
			}
		}
		
		Long vehicleId=bfsUserProfileDAO.saveVehicle(myVehicle);
		
		return getJsonMessage(vehicleId.toString(), BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse updateVehicle(String email, String userType, String vehicleJSON) {
		logger.info("Inside updateUserVehicle");
		MyVehicle myVehicle = null;
		
		MyDriver driver = bfsUserProfileDAO.doesDriverExist(userType, email);
		
		if(driver == null){
			return getJsonError(ValidationConstants.DRIVER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		
		String year="";
		String make="";
		String model="";
		String submodel="";
		try {
			JSONObject jObj = new JSONObject(vehicleJSON);
			year = jObj.getString("year");
			make = jObj.getString("make");
			model = jObj.getString("model");
			submodel = jObj.getString("submodel");
		} catch (JSONException e) {
			System.err.println("JSON exception while trying to parse vehicle data");
		}
		
		if(year == null || year.isEmpty() || make == null || make.isEmpty() ||
				model == null || model.isEmpty() || submodel == null || submodel.isEmpty()){
			return getJsonError(ValidationConstants.INVALID_DATA, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			myVehicle = mapper.readValue(vehicleJSON, MyVehicle.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("user details =======>"+myVehicle.getMyVehicleId());
		MyVehicle existingVehicle =  bfsUserProfileDAO.doesVehicleExist(myVehicle.getMyVehicleId());
		
		Fitment fitment = bfsUserProfileDAO.getVehicle(Integer.parseInt(year), make, model, submodel);
		if(fitment == null)
			return getJsonError(ValidationConstants.VEHICLE_DATA_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		existingVehicle.setFitment(fitment);
		existingVehicle.setDriver(driver);
		existingVehicle.setName(myVehicle.getName());
		existingVehicle.setMileageDefault(myVehicle.getMileageDefault());
		
		existingVehicle.setVinNumber(myVehicle.getVinNumber());
		//myVehicle.setFitment(fitment);
		//myVehicle.setDriver(driver);
		
		// This code is used to update primary vehicle
		if(myVehicle.getPrimaryVehicle().equals(1))
		{
			if(driver.getVehicles() != null && !driver.getVehicles().isEmpty())
			{
				for(MyVehicle vehicle : driver.getVehicles())
				{
					if(vehicle.getPrimaryVehicle().equals(1))
					{
					vehicle.setPrimaryVehicle(0);
					bfsUserProfileDAO.updateVehicle(vehicle);
					}
				}
			}
		}
		existingVehicle.setPrimaryVehicle(myVehicle.getPrimaryVehicle());
		bfsUserProfileDAO.updateVehicle(existingVehicle);
		return getJsonMessage(ValidationConstants.VEHICLE_UPDATE_SUCCESSFUL, BSROWebServiceResponseCode.SUCCESSFUL);
	}
	

	public BSROWebServiceResponse addAppointmentForVehicle(String email, String userType,
			Long acesVehicleId, String vinNumber, Long appointmentId) {
		
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		Appointment appt = appointmentDAO.get(appointmentId);
		if(appt == null)
			return getJsonError(ValidationConstants.APPOINTMENT_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);

		MyAppointment myAppt = new MyAppointment(myVehicle,appt, user);
		bfsUserProfileDAO.saveVehicleAppointment(myAppt);
		
		return getJsonMessage(ValidationConstants.APPOINTMENT_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}



	public BSROWebServiceResponse updateUserProfile(String userJSON, String userType) {
		logger.info("Inside registerUserProfile");
		BFSUser user = null;
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			user = mapper.readValue(userJSON, BFSUser.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WebSite webSite = bfsUserDataDAO.getSite(userType);
		BFSUser userPresent = bfsUserProfileDAO.doesUserExistBasedonId(user.getUserId(), userType);
		user.setUserType(webSite);
		
		if(userPresent == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}else{
			MyDriver driverPresent = null;
			if(user.getDriver() != null){
				MyDriver currDriver = user.getDriver();
				driverPresent = bfsUserProfileDAO.getDriver(currDriver.getUserId());
				if(driverPresent != null){
					
					driverPresent.setFirstName(currDriver.getFirstName());
					driverPresent.setLastName(currDriver.getLastName());
					driverPresent.setEmailAddress(currDriver.getEmailAddress());
					driverPresent.setCellPhone(currDriver.getCellPhone());
					driverPresent.setLastModifiedDate(currDriver.getLastModifiedDate());
					
					userPresent.setDriver(driverPresent);
					userPresent.setEmail(user.getEmail());
					userPresent.setLastModifiedDate(user.getLastModifiedDate());
					
					//driverPresent.copyFrom(currDriver);
					//userPresent.copyFrom(user);
				}else{
					return getJsonError(ValidationConstants.DRIVER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
				}
			}
		}
		
		bfsUserProfileDAO.updateUser(userPresent);
		return getJsonMessage(ValidationConstants.USER_UPDATE_SUCCESSFUL, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse removeUser(String email , String userType) {
		logger.info("Inside removeUser");
		BFSUser userPresent = bfsUserProfileDAO.doesUserExist(email, userType);
		if(userPresent != null){
			bfsUserProfileDAO.deleteUser(userPresent);
			return getJsonMessage(ValidationConstants.USER_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
		}else{
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		}
	}

	public BSROWebServiceResponse registerNewDriver(String userJSON, String userType) {
		return null;
	}

	public BSROWebServiceResponse saveProductQuote(Long quoteId, String type, String email, String userType) {
		BFSUser user = bfsUserDataDAO.doesUserExist(email, bfsUserDataDAO.getSite(userType));
		
		if(user != null){
			MyProductQuote quoteExists = bfsUserProfileDAO.doesQuoteExist(quoteId, getProductTypeEnum(type), email);
			if(quoteExists != null){
				return getJsonMessage(ValidationConstants.PRODUCT_QUOTE_AREADY_EXIST, BSROWebServiceResponseCode.SUCCESSFUL);
			}else{
				
				MyProductQuote myQuote = new MyProductQuote(user,getProductTypeEnum(type),quoteId);
				try {
					myQuote.setLastModifiedDate(new Date());
					bfsUserProfileDAO.saveProductQuote(myQuote);
				} catch (Exception e) {
					return getJsonError(ValidationConstants.PRODUCT_QUOTE_DOES_NOT_EXIST, BSROWebServiceResponseCode.DATABASE_ERROR);
				}
				return getJsonMessage(ValidationConstants.PRODUCT_QUOTE_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
				}
			//}			
		}
		return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
	}

	public BSROWebServiceResponse removeProductQuote(Long quoteId, String type, String email, String userType) {
		MyProductQuote myQuote = bfsUserProfileDAO.doesQuoteExist(quoteId, getProductTypeEnum(type), email);

		if(myQuote != null){
			try {
				bfsUserProfileDAO.removeProductQuote(myQuote);
			} catch (Exception e) {
				return getJsonError(ValidationConstants.PRODUCT_QUOTE_NOT_REMOVED, BSROWebServiceResponseCode.DATABASE_ERROR);
			}
			return getJsonMessage(ValidationConstants.PRODUCT_QUOTE_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
		}
		return getJsonError(ValidationConstants.PRODUCT_QUOTE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
	}

	private ProductType getProductTypeEnum(String type){
		if(type.equalsIgnoreCase("battery")){
			return ProductType.BATTERY;
		}else if(type.equalsIgnoreCase("alignment")){
			return ProductType.ALIGNMENT;
		}else if(type.equalsIgnoreCase("tire")){
			return ProductType.TIRE;
		}else if(type.equalsIgnoreCase("oilChange")){
			return ProductType.OIL_CHANGE;
		}else
			return ProductType.OTHERS;
	}

	public BSROWebServiceResponse getFavouriteStores(String email, String userType) {
		Set<MyStore> myStores = new HashSet<MyStore>();
		logger.info("email="+email+" email ="+email);
		if(email == null || email.isEmpty()){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		else{
			MyDriver myDriver = bfsUserProfileDAO.doesDriverExist(userType,email);
			if(myDriver != null){
				return getJsonResponse(myDriver.getStores());
			}else{
				return getJsonError(ValidationConstants.DRIVER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
			}
		}
		
		
	}

	public BSROWebServiceResponse getFavouritePromotions(String email, String userType) {
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user != null){
			Set<MyPromotion> promotions = user.getPromotions();		
			return getJsonResponse(new Promotions(promotions));
		}
		return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
				
	}

	public BSROWebServiceResponse getUserSavedQuotes(String email, String userType) {
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user != null){
			List<MyProductQuote> productQuotes = user.getProductQuotes();
			return getJsonResponse(productQuotes);
		}
		return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
	}
	
	public BSROWebServiceResponse getVehicleMaintenanceHistory(String email, String userType, 
			Long acesVehicleId, String vinNumber){
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null){
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		
		NCDVehicleHistory vehicleHistory = new NCDVehicleHistory(acesVehicleId, myVehicle.getVehicleHistory());
		
		/*if(myVehicle.getVehicle()!= null && myVehicle.getVehicle().getBaseVehicle()!=null 
				&& myVehicle.getVehicle().getBaseVehicle() != null){
			Vehicle vehicle = myVehicle.getVehicle();
			BaseVehicle baseVehicle = vehicle.getBaseVehicle();
			
			vehicleHistory.getVehicles().get(0).setYear(String.valueOf(baseVehicle.getYear()));
			
			if(baseVehicle.getMake() != null)
				vehicleHistory.getVehicles().get(0).setMake(baseVehicle.getMake().getMakeName());
	
			if(baseVehicle.getModel() != null)
				vehicleHistory.getVehicles().get(0).setModel(baseVehicle.getModel().getModelName());
			
			if(vehicle.getSubmodel() != null)
				vehicleHistory.getVehicles().get(0).setSubmodel(vehicle.getSubmodel().getSubmodelName());
		}*/
		
		if(myVehicle.getFitment()!= null){
			Fitment fitment = myVehicle.getFitment();
			//BaseVehicle baseVehicle = vehicle.getBaseVehicle();
			vehicleHistory.getVehicles().get(0).setYear(String.valueOf(fitment.getModelYear()));
				vehicleHistory.getVehicles().get(0).setMake(fitment.getMakeName());
				vehicleHistory.getVehicles().get(0).setModel(fitment.getModelName());
				vehicleHistory.getVehicles().get(0).setSubmodel(fitment.getSubmodel());
		}
		
		return getJsonResponse(vehicleHistory);
	}

	public BSROWebServiceResponse saveVehicleMaintenanceHistory(String email, String userType, 
			Long acesVehicleId, String vinNumber, String jsonData, Date downloadDate) {
		logger.info("inside saveVehicleMaintenanceHistory");
		NCDVehicleHistory vehicleHistory = new NCDVehicleHistory();

		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonObjectMapper.configure(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS, true);
		try {
			vehicleHistory = jsonObjectMapper.readValue(jsonData, NCDVehicleHistory.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("history= "+vehicleHistory);
		
		if(vehicleHistory != null && vehicleHistory.getVehicles() != null){
			BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
			if(getVehicleResp!= null && 
					!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
				return getVehicleResp;
			}
			MyVehicle vehicleExist = (MyVehicle)getVehicleResp.getPayload();
			if(vehicleExist == null){
				return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
			}		

			MyServiceHistoryVehicle processedData = processNCDVehicleData(vehicleExist, vehicleHistory, downloadDate);
			if(processedData == null)
				return getJsonError(ValidationConstants.ACES_ID_HISTORY_DATA_DONOT_MATCH, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
			
			bfsUserProfileDAO.updateVehicleHistory(vehicleExist, processedData);
			return getJsonMessage(ValidationConstants.VEHICLE_HISTORY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
			
		}
		return getJsonError(ValidationConstants.INVALID_VEHICLE_DATA, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
	}
	
	private MyServiceHistoryVehicle processNCDVehicleData(MyVehicle myVehicle, NCDVehicleHistory vehicleHistory, Date downloadDate){
		logger.info("Inside processNCDVehicleData");
		for(MyServiceHistoryVehicle vehicle : vehicleHistory.getVehicles()){
			vehicle.setLastDownloadDate(downloadDate);
			vehicle.setMyVehicle(myVehicle);
			if(vehicle.getYear().equals(String.valueOf(myVehicle.getFitment().getModelYear()))
					&& vehicle.getMake().equalsIgnoreCase(myVehicle.getFitment().getMakeName())
					&& vehicle.getModel().equalsIgnoreCase(myVehicle.getFitment().getModelName())){
				for(MyServiceHistoryInvoice invoice : vehicle.getInvoices()){
					invoice.setSource(SourceType.NCD.getValue());
					invoice.setVehicleHistory(vehicle);
					if(invoice.getStoreNbr() != null && !invoice.getStoreNbr().isEmpty()){
						logger.info("storeNum="+invoice.getStoreNbr());
						Store store = storeDAO.get(new Long(invoice.getStoreNbr()));
						invoice.setStore(store);
					}
					for(MyServiceHistoryJob job : invoice.getJobs()){
						job.setServiceHistoryInvoice(invoice);
						if(job.getAuthorized()!=null && !job.getAuthorized().isEmpty()){
							if(job.getAuthorized().equalsIgnoreCase("true")){
								job.setAuthorized("Y");
							}else{
								job.setAuthorized("N");
							}
						}
						for(MyServiceHistoryJobDetail detail : job.getDetails()){
							detail.setServiceHistoryJob(job);
						}
					}
				}
				return vehicle;
			}
		}
		return null;
	}
	
	public BSROWebServiceResponse getVehicleGasFillup(String email, String userType, 
			Long acesVehicleId, String vinNumber){
		logger.info("Inside getVehicleGasFillup");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null)
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		List<MyVehicleGas> myVehicleGasList = bfsUserProfileDAO.getMyVehicleGasFillups(user.getUserId(), acesVehicleId, vinNumber);
		if(myVehicleGasList == null || myVehicleGasList.isEmpty())
			return getJsonMessage(ValidationConstants.VEHICLE_GAS_FILLUP_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		return getJsonResponse(myVehicleGasList);

	}
	
	public BSROWebServiceResponse addVehicleGasFillup(String email, String userType, Long acesVehicleId, 
			String vinNumber, String jsonData){
		logger.info("Inside addVehicleGasFillup");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null)
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		MyVehicleGas myVehicleGas = new MyVehicleGas();
		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			myVehicleGas = jsonObjectMapper.readValue(jsonData, MyVehicleGas.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		myVehicleGas.setMyVehicle(myVehicle);
		bfsUserProfileDAO.saveMyVehicleGasFillup(myVehicleGas);
		return getJsonMessage(ValidationConstants.VEHICLE_GAS_FILLUP_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}
	

	public BSROWebServiceResponse updateVehicleGasFillup(String email, String userType, Long acesVehicleId, 
			String vinNumber, String jsonData){
		logger.info("Inside updateVehicleGasFillup");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null)
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		MyVehicleGas myVehicleGas = new MyVehicleGas();
		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			myVehicleGas = jsonObjectMapper.readValue(jsonData, MyVehicleGas.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bfsUserProfileDAO.doesVehicleGasFillupExist(myVehicleGas.getMyVehicleGasId())==null)
			return getJsonError(ValidationConstants.VEHICLE_GAS_FILLUP_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		myVehicleGas.setMyVehicle(myVehicle);
		bfsUserProfileDAO.updateMyVehicleGasFillup(myVehicleGas);
		return getJsonMessage(ValidationConstants.VEHICLE_GAS_FILLUP_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}
	
	public BSROWebServiceResponse getServicePerformed(String email,
			String userType, Long acesVehicleId, String vinNumber) {
		logger.info("Inside getServicePerformed");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null)
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		List<MyMaintenanceServicePerformed> servicePerformedList = bfsUserProfileDAO.getMaintenanceServiceData(user.getUserId(), acesVehicleId, vinNumber);
		if(servicePerformedList == null || servicePerformedList.isEmpty())
			return getJsonMessage(ValidationConstants.SERVICE_PERFORMED_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		return getJsonResponse(servicePerformedList);
	}

	public BSROWebServiceResponse addServicePerformed(String email,
			String userType, Long acesVehicleId, String vinNumber,
			String jsonData) {
		logger.info("Inside addServicePerformed");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null)
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		MyMaintenanceServicePerformed servicePerformed = new MyMaintenanceServicePerformed();
		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			servicePerformed = jsonObjectMapper.readValue(jsonData, MyMaintenanceServicePerformed.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		servicePerformed.setMyVehicle(myVehicle);
		bfsUserProfileDAO.saveMaintenanceServiceData(servicePerformed);
		return getJsonMessage(ValidationConstants.SERVICE_PERFORMED_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse updateServicePerformed(String email,
			String userType, Long acesVehicleId, String vinNumber, String jsonData) {
		logger.info("Inside updateServicePerformed");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null)
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		BSROWebServiceResponse getVehicleResp = this.getVehicle(email, userType, acesVehicleId, vinNumber);
		if(getVehicleResp!= null && 
				!BSROWebServiceResponseCode.SUCCESSFUL.name().equalsIgnoreCase(getVehicleResp.getStatusCode())){
			return getVehicleResp;
		}
		MyVehicle myVehicle = (MyVehicle)getVehicleResp.getPayload();
		if(myVehicle == null)
			return getJsonError(ValidationConstants.VEHICLE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		MyMaintenanceServicePerformed servicePerformed = new MyMaintenanceServicePerformed();
		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			servicePerformed = jsonObjectMapper.readValue(jsonData, MyMaintenanceServicePerformed.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("servicePerformed===> "+servicePerformed);
		if(bfsUserProfileDAO.doesMaintenanceServiceDataExist(servicePerformed.getMyMaintServicePerformedId()) == null)
			return getJsonError(ValidationConstants.SERVICE_PERFORMED_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		
		servicePerformed.setMyVehicle(myVehicle);
		bfsUserProfileDAO.updateMaintenanceServiceData(servicePerformed);
		return getJsonMessage(ValidationConstants.SERVICE_PERFORMED_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse removeServicePerformed(String email, String userType, Long servicePerformedId) {
		logger.info("Inside removeServicePerformed");
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		MyMaintenanceServicePerformed servicePerformed = bfsUserProfileDAO.doesMaintenanceServiceDataExist(servicePerformedId);
		if(servicePerformed == null){
			return getJsonError(ValidationConstants.SERVICE_PERFORMED_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		bfsUserProfileDAO.removeMaintenanceServiceData(servicePerformed);		
		return getJsonMessage(ValidationConstants.SERVICE_PERFORMED_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}
	
	
	public byte[] getImage(String imageType,Long myPictureId) {	
		MyPicture picture= bfsUserProfileDAO.getImages(myPictureId);
		
		if( picture != null && picture.getImage().length > 0){
			return picture.getImage();
		}
		return null;
	}

	public BSROWebServiceResponse saveImage(String imageType, Long productImageId, byte[] image) {
		logger.error("Inside saveImage");
		
		MyPicture pic = new MyPicture(getPictureTypeEnum(imageType), image);
		pic.setPictureObjectId(productImageId);
		pic.setLastModifiedDate(new Date());
		bfsUserProfileDAO.saveImage(pic);
		return getJsonMessage(ValidationConstants.IMAGE_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse deleteImage(String imageType, Long imageId) {
		logger.error("Inside deleteImage");
		MyPicture picture = bfsUserProfileDAO.getImages(imageId);
		if(picture == null || picture.getImage().length == 0){
			return getJsonMessage(ValidationConstants.IMAGE_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		}
		bfsUserProfileDAO.deleteImage(picture);
		return getJsonMessage(ValidationConstants.IMAGE_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}
	
	public BSROWebServiceResponse getRegisteredDevices(String email, String userType) {
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user != null){
			Set<MyDevice> devices = user.getDevices();	
			if(devices!= null && devices.isEmpty()){
				return getJsonMessage(ValidationConstants.NO_DEVICE_IS_REGISTERED_TO_USER, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
			}
			return getJsonResponse(devices);
		}
		return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
				
	} 
	
	public BSROWebServiceResponse registerNewDevice(String email, String userType, String deviceJson) {
		logger.info("inside saveVehicleMaintenanceHistory and json ="+ deviceJson);
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		MyDevice myDevice = new MyDevice();

		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			myDevice = jsonObjectMapper.readValue(deviceJson, MyDevice.class);
		} catch (JsonParseException e) {
			return getJsonError(ValidationConstants.DEVICE_DATA_INSUFFICIENT, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		} catch (JsonMappingException e) {
			return getJsonError(ValidationConstants.DEVICE_DATA_INSUFFICIENT, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		} catch (IOException e) {
			return getJsonError(ValidationConstants.DEVICE_DATA_INSUFFICIENT, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		logger.info("device = "+myDevice);
		myDevice.setUser(user);
		if("".equalsIgnoreCase(myDevice.getSyncFlag()))
			myDevice.setSyncFlag("Y");
		MyDevice deviceExists = bfsUserProfileDAO.doesDeviceExist(user.getUserId(),myDevice.getDeviceType(), myDevice.getMfgName(), myDevice.getDeviceModel());
		if(deviceExists.equals(myDevice)){
			return getJsonMessage(ValidationConstants.DEVICE_AREADY_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO);
		}
		
		bfsUserProfileDAO.saveDevice(myDevice);
		return getJsonMessage(ValidationConstants.DEVICE_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse updateDeviceInfo(String email, String userType, String deviceJson) {
		logger.info("inside updateDeviceInfo");
		
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		MyDevice myDevice = new MyDevice();

		jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
		jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			myDevice = jsonObjectMapper.readValue(deviceJson, MyDevice.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("device = "+myDevice);
		myDevice.setUser(user);
		MyDevice deviceExists = bfsUserProfileDAO.doesDeviceExist(user.getUserId(),myDevice.getDeviceType(), 
				myDevice.getMfgName(), myDevice.getDeviceModel());
		if(deviceExists == null){
			return getJsonError(ValidationConstants.DEVICE_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		bfsUserProfileDAO.updateDevice(myDevice);
		return getJsonMessage(ValidationConstants.DEVICE_SUCCESSFULLY_SAVED, BSROWebServiceResponseCode.SUCCESSFUL);
	}

	public BSROWebServiceResponse removeDevice(String email, String userType, Long deviceId) {
		logger.info("inside removeDevice");
		
		BFSUser user = bfsUserProfileDAO.doesUserExist(email, userType);
		if(user == null){
			return getJsonError(ValidationConstants.USER_DOES_NOT_EXIST, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		MyDevice device = bfsUserProfileDAO.getDevice(deviceId);
		if(device == null){
			return getJsonError(ValidationConstants.DEVICE_NOT_FOUND, BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR);
		}
		bfsUserProfileDAO.deleteDevice(device);		
		return getJsonMessage(ValidationConstants.DEVICE_SUCCESSFULLY_REMOVED, BSROWebServiceResponseCode.SUCCESSFUL);
		
	}


	private PictureType getPictureTypeEnum(String type){
		if(type.equalsIgnoreCase("vehicle")){
			return PictureType.VEHICLE;
		}else if(type.equalsIgnoreCase("device")){
			return PictureType.DEVICE;
		}else if(type.equalsIgnoreCase("driver")){
			return PictureType.DRIVER;
		}else
			return PictureType.PRODUCT;
	}
	
	public BSROWebServiceResponse getJsonResponse(Object resultset){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(resultset);
		return response;
	}
	
	public BSROWebServiceResponse getJsonMessage(String message, BSROWebServiceResponseCode messageType){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		response.setStatusCode(messageType.name());
		response.setMessage(message);
		response.setPayload(null);
		return response;
	}
	
	public BSROWebServiceResponse getJsonError(String error, BSROWebServiceResponseCode errorType){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(error);
		response.setErrors(errors);
		response.setStatusCode(errorType.name());
		response.setPayload(null);
		return response;
	}

}
