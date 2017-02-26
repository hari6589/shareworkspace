package com.bfrc.dataaccess.util;

public class ValidationConstants {
	
	public static final String ADDRESS_VALIDATION = "Please enter a valid address.";
	public static final String CITY_VALIDATION = "Please enter a valid city.";
	public static final String EMAIL_VALIDATION="The email entered is not valid.";
	public static final String FIRST_NAME_VALIDATION="Please enter a valid first name.";
	public static final String LAST_NAME_VALIDATION="Please enter a valid last name.";
	public static final String PASSWORD_MATCH_VALIDATION="The passwords entered do not match.";
	public static final String PASSWORD_VALIDATION = "Your password is not strong enough or it needs to be 8-10 characters long. It also must contain three of the following: a number, capital letter, lowercase letter, or !@#$%^*()_+-={}[]|/ ";
	public static final String PHONE_VALIDATION = "Enter a valid phone number 555-555-5555.";
	public static final String STATE_VALIDATION = "Please enter a valid state, two letters.";
	public static final String VALIDATION_ERROR="There was an error validating input.";
	public static final String ZIP_CODE_VALIDATION = "Please use a zip code in the correct format (00000 or 00000-0000).";
	public static final String INVALID_REQUEST = "Not enough request parameters were provided.";
	public static final String PASSWORDS_DID_NOT_MATCH = "The passwords provided did not match.";
	public static final String EMAIL_ALREADY_EXISTS = "An account with that email already exists.";
	public static final String NAME_VALIDATION="Please enter a valid name.";
	public static final String NO_DATA_FOUND ="NoDataFound";
	public static final String INVALID_PARAMETERS_PASSED ="Invalid Parameters Passed";
	public static final String INSUFFICIENT_PARAMS_PASSED ="Insufficient Parameters Passed";
	
	public static final String UPDATE_VALIDATION="Cannot update ? because it does not exist.";
	public static final String REQUIRED_VALIDATION="Please enter a valid ?.";
	public static final String SELECT_VALIDATION="Please select a ?.";
	
	public static final String BOOK_APPOINTMENT_ERR_MESSAGE = "Error Booking appointment through Scheduler Service, please call store to book appointment";
	public static final String AVAILABILITY_ERR_MESSAGE = "Currently the store appointment availability application is not available,  please call store to schedule appointment.";
	
	public static final String GEOCODE_RESPONSE_ENTITY_ABSENT = "Response Entity is not present";
	public static final String IPINFO_OBJECT__MISSING = "No IP Info Object in respose";
	public static final String COUNTRY_CITY_STATE_GEODATA_MISSING = "Country, State or City Geocode data missing";
	
	public static final String USER_EMAIL_INVALID = "UserEmailInvalid";
	public static final String USER_PASSWORD_INVALID = "UserPasswordInvalid";
	public static final String APP_NAME_NOT_VALID = "noAppName";
	public static final String INVALID_JSON_STRING = "InvalidJsonString";
	public static final String USER_ALREADY_EXISTS = "UserExist";
	public static final String USER_SUCCESSFULLY_REGISTERED = "UserRegistrationSuccess";
	public static final String USER_HAS_NOT_REGISTERED = "UserHasNotRegistered";	
	public static final String USER_AUTHENTICATION_SUCCESSFUL = "UserAuthSuccess";
	public static final String USER_AUTHENTICATION_FAILED = "UserAuthFailed";
	public static final String USER_EMAIL_UPDATE_SUCCESSFUL = "UserEmailUpdateSuccess";
	public static final String USER_PASSWORD_UPDATE_SUCCESSFUL = "UserPasswordUpdateSuccess";
	public static final String USER_UPDATE_SUCCESSFUL = "UserUpdateSuccess";
	public static final String USER_UPDATE_FAILED = "UserNotUpdate";
	public static final String USER_DOES_NOT_EXIST = "UserNotExist";
	public static final String USER_PASSWORD_RESET_SUCCESSFUL = "ResetPwdSuccess";
	public static final String USER_PASSWORD_RESET_FAILED = "ResetPwdServerError";
	public static final String USER_DATA_BACKUP_FAILED = "BackupFailed";
	public static final String USER_DATA_BACKUP_SUCCESSFUL = "BackupSuccess";
	public static final String USER_HAS_NO_BACKUP_DATA = "NoBackupFound";
	public static final String USER_ALREADY_HAS_BACKUP_DATA = "YesBackupFound";
	public static final String USER_DATA_RESTORE_ERROR = "RestoreError";
	public static final String USER_ADDRESS_INVALID = "UserAddressInvalid";
	public static final String USER_SUCCESSFULLY_REMOVED= "UserRemoveSuccess";
	public static final String REQUEST_FORBIDDEN = "UnsecureRequest";
	public static final String USER_ACCOUNT_LOCKED = "AccountLocked";
	public static final String DRIVER_DOES_NOT_EXIST = "DriverDoesNotExist";
	public static final String DRIVER_AREADY_EXIST = "DriverAlreadyExist";
	public static final String STORE_DOES_NOT_EXIST = "UserDidNotSaveStore";
	public static final String STORE_SUCCESSFULLY_REMOVED= "StoreRemoveSuccess";
	public static final String STORE_SUCCESSFULLY_SAVED= "StoreSaveSuccess";
	public static final String STORE_AREADY_EXIST = "StoreAlreadySaved";
	public static final String PRODUCT_QUOTE_DOES_NOT_EXIST = "QuoteDoesNotExist";
	public static final String PRODUCT_QUOTE_AREADY_EXIST = "QuoteAlreadySaved";
	public static final String PRODUCT_QUOTE_NOT_SAVED = "QuoteNotSaved";
	public static final String PRODUCT_QUOTE_SUCCESSFULLY_SAVED = "QuoteSaveSuccess";
	public static final String PRODUCT_QUOTE_NOT_REMOVED = "QuoteRemoveError";
	public static final String PRODUCT_QUOTE_SUCCESSFULLY_REMOVED = "QuoteRemoveSuccess";
	public static final String PRODUCT_TYPE_NOT_DEFINED = "ProductTypeNotDefined";
	public static final String INVALID_QUOTE_ID = "InvalidQuoteId";
	public static final String PROMOTION_NOT_SAVED = "PromotionNotSaved";
	public static final String PROMOTION_AREADY_EXIST = "PromotionAlreadySaved";
	public static final String PROMOTION_SUCCESSFULLY_SAVED = "PromotionSaveSuccess";
	public static final String PROMOTION_DOES_NOT_EXIST = "PromotionDoesNotExist";
	public static final String PROMOTION_SUCCESSFULLY_REMOVED= "PromotionRemoveSuccess";
	public static final String DEVICE_AREADY_EXIST = "DeviceAlreadySaved";
	public static final String DEVICE_SUCCESSFULLY_SAVED = "DeviceSaveSuccess";
	public static final String DEVICE_SUCCESSFULLY_REMOVED = "DeviceRemoveSuccess";
	public static final String DEVICE_NOT_FOUND = "DeviceNotFound";
	public static final String NO_DEVICE_IS_REGISTERED_TO_USER = "NoDeviceIsRegisteredToUser";
	public static final String DEVICE_DATA_INSUFFICIENT = "DeviceDataInsufficient";
	public static final String NO_VEHICLE_FOUND = "NoVehicleForDriver";
	public static final String APPOINTMENT_DOES_NOT_EXIST = "AppointmentDoesNotExist";
	public static final String APPOINTMENT_AREADY_EXIST = "AppointmentAlreadySaved";
	public static final String APPOINTMENT_SUCCESSFULLY_SAVED = "AppointmentSaveSuccess";
	public static final String INVALID_DATA = "InvalidData";
	public static final String INVALID_PRODUCT_TYPE = "InvalidProductType";
	public static final String DRIVER_SUCCESSFULLY_REGISTERED = "DriverRegistrationSuccess";
	public static final String DRIVER_SUCCESSFULLY_REMOVED = "DriverRemoveSuccess";
	public static final String DRIVER_INFO_NOT_FOUND = "NoDriverFoundForUser";
	public static final String DRIVER_EMAIL_INVALID = "DriverEmailInvalid";
	public static final String VEHICLE_SUCCESSFULLY_SAVED = "VehicleSaveSuccess";
	public static final String VEHICLE_SUCCESSFULLY_REMOVED= "VehicleRemoveSuccess";
	public static final String VEHICLE_DOES_NOT_EXIST = "VehicleDoesNotExist";
	public static final String VEHICLE_DATA_NOT_FOUND = "VehicleFitmentDataNotFound";
	public static final String VEHICLE_UPDATE_SUCCESSFUL = "VehicleUpdateSuccessful";
	public static final String VEHICLE_GAS_FILLUP_SUCCESSFULLY_SAVED = "VehicleGasFillupSaveSuccess";
	public static final String VEHICLE_GAS_FILLUP_SUCCESSFULLY_REMOVED= "VehicleGasFillupRemoveSuccess";
	public static final String VEHICLE_GAS_FILLUP_DOES_NOT_EXIST = "VehicleGasFillupDoesNotExist";
	public static final String VEHICLE_GAS_FILLUP_NOT_FOUND = "VehicleGasFillupNotFound";
	public static final String VEHICLE_GAS_FILLUP_UPDATE_SUCCESSFUL = "VehicleGasFillupUpdateSuccessful";	
	public static final String SERVICE_PERFORMED_SUCCESSFULLY_SAVED = "ServicePerformedSaveSuccess";
	public static final String SERVICE_PERFORMED_SUCCESSFULLY_REMOVED= "ServicePerformedRemoveSuccess";
	public static final String SERVICE_PERFORMED_DOES_NOT_EXIST = "ServicePerformedDoesNotExist";
	public static final String SERVICE_PERFORMED_NOT_FOUND = "ServicePerformedNotFound";
	public static final String SERVICE_PERFORMED_UPDATE_SUCCESSFUL = "ServicePerformedUpdateSuccessful";
	public static final String ACES_ID_HISTORY_DATA_DONOT_MATCH = "AcesIdAndHistoryDataDonotMatch";
	public static final String VEHICLE_NOT_REGISTERED_TO_DRIVER = "VehicleNotRegisteredToDriver";
	public static final String MORE_THAN_ONE_VEHICLE_PRESENT = "MoreThanOneVehicleVinNumberReqired";
	public static final String VIN_NUMBER_DONOT_MATCH = "VehicleVinNumberDoNotMatch";
	public static final String INVALID_VEHICLE_DATA = "invalidVehicleData";
	public static final String INVALID_DATE_FORMAT = "Dates must be in mm-dd-yyyy";
	public static final String VEHICLE_HISTORY_SAVED = "vehicleHistorySaved";
	public static final String VEHICLE_HISTORY_DATA_NOT_PARSED = "vehicleHistoryNotParsed";
	public static final String IMAGE_SUCCESSFULLY_SAVED = "ImageSaveSuccess";
	public static final String IMAGE_SUCCESSFULLY_REMOVED = "ImageRemoveSuccess";
	public static final String IMAGE_DOES_NOT_EXIST = "ImageDoesNotExist";
	public static final String INVALID_IMAGE_TYPE = "InvalidImageType";
	public static final String IMAGE_SIZE_TOO_LONG = "ImageSizeTooLong";
	public static final String NO_ACTIVE_NOTIFICATION_FOUND = "NoActiveNotificationFound";
	public static final String UPDATE_SUCCESS = "UpdateSuccess";
	public static final String UPDATE_FAILED = "UpdateFailed";
	public static final String DELETE_SUCCESS = "DeleteSuccess";
	public static final String DELETE_FAILED= "DeleteFailed";
	
	public static final Integer MAX_PASSWORD_FAILURE_ATTEMPTS = 5;
	
	/**
	 * Returns the the error string, replacing each ? in the error string in order with the given parameters<br/><br/>
	 */
	public static String replaceParams(String error, String... parameters){
		for(String param : parameters)
			error = error.replaceFirst("\\?", param);
		return error;
	}
	
}
