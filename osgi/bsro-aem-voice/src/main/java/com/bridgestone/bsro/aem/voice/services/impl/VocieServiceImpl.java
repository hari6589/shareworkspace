package com.bridgestone.bsro.aem.voice.services.impl;

import com.bridgestone.bsro.aem.core.IProfileService;
import com.bridgestone.bsro.aem.core.client.IServiceCaller;
import com.bridgestone.bsro.aem.core.constants.CoreCodes;
import com.bridgestone.bsro.aem.core.constants.CoreConstants;
import com.bridgestone.bsro.aem.core.exception.BSROBusinessException;
import com.bridgestone.bsro.aem.core.pojo.profile.IProfile;
import com.bridgestone.bsro.aem.core.security.ISecurityService;
import com.bridgestone.bsro.aem.core.site.ISiteService;
import com.bridgestone.bsro.aem.core.utils.BSROStringUtil;
import com.bridgestone.bsro.aem.core.utils.DateUtil;
import com.bridgestone.bsro.aem.core.utils.MicroServicesUtil;
import com.bridgestone.bsro.aem.core.utils.ProfileValidationUtil;
import com.bridgestone.bsro.aem.storelocator.services.IStoreServices;
import com.bridgestone.bsro.aem.voice.services.IVoiceService;
import com.bridgestone.bsro.aem.voice.util.VoiceConstants;
import com.bridgestone.bsro.aem.voice.util.VoiceRequest;
import com.bridgestone.bsro.aem.voice.util.VoiceValidationUtil;
import com.bridgestone.bsro.aem.voice.util.Choice;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

/*
 *
 * Created by  rreddykandari on : Jan 28, 2016 for the project : bsro-aem-appointment-service
 */
@Component(metatype = true, immediate = true, label = "BSRO Appointment Service")
@Service(IVoiceService.class)
@SuppressWarnings("rawtypes")
public class VocieServiceImpl implements IVoiceService {

	/** The Constant LOGGER. */
	private static final Logger log = LoggerFactory.getLogger(VocieServiceImpl.class);


	@Reference
	private IProfileService profileService;

	@Reference
	private IServiceCaller serviceCaller;

	@Reference
	private ConfigurationAdmin configAdmin;

	@Reference
	private IStoreServices istoreService;

	@Reference
	private ISecurityService securityService;

	@Reference
	ISiteService siteService;


	@Override
	public Map<String, Object> getAllServices(HttpServletRequest request)
			throws BSROBusinessException {
		Map<String, Object> storeServicesDetails = null;
		try {
			if(request == null) {
				throw new BSROBusinessException(CoreCodes.CODE_053,
						"HttpServletRequest is null.");
			}
			IProfile profile = profileService.getProfile(request);
			if(profile == null) {
				profile = profileService.createProfile(request);
				if(profile == null) {
					throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
							"Unable to create shell profile.");
				}
			}

			Object storeNumberObject = profile.get("location.storeNumber");
			String storeNumber = null;
			if (storeNumberObject != null) {
				storeNumber = (String) storeNumberObject;
			}

			boolean storeNumberValid = false;
			try {
				storeNumberValid =
						ProfileValidationUtil.validateStoreNumber(storeNumber);
			} catch (BSROBusinessException e) {
				//Do Nothing
			}

			if(storeNumberValid) {
				storeServicesDetails = istoreService.getAllServices(request, storeNumber);
			} else {
				log.error("getAllServices: Not calling Store Locator getAllServices service " +
						"as the Store Number in Profile is invalid.\n storeNumber-{}",
						storeNumber);
				//Populate Error Response in storeServicesDetails Map
				storeServicesDetails = new HashMap<String, Object>();
				storeServicesDetails.put(CoreConstants.KEY_SUCCESS, CoreConstants.SUCCESS_FALSE);
				storeServicesDetails.put(CoreConstants.KEY_STATUS, CoreCodes.CODE_COMPONENT_ERROR);
			}
		} catch (BSROBusinessException e) {
			log.error("BSROBusinessException occurred in " +
					"getAllServices Appointment Service.", e);
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			log.error("Exception occurred in " +
					"getAllServices Appointment Service.", e);
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return storeServicesDetails;
	}

	@Override
	public Map appointmentConfirm(HttpServletRequest request,
								  String vehicleType)
			throws BSROBusinessException {
		Map<String, Object> response = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("appointmentConfirm: Invoking " +
						"Confirm Appointment Micro Service");
			}
			String jsonRequest = constructConfirmAppointmentJSON(request,
					vehicleType);
			Map headers = getHeaderValuesForAppointmentMicroService
					(request, vehicleType, true);
			/*response = serviceCaller.invokePostRequest
					(jsonRequest, getAppointmentConfirmPath(),
							headers);*/
			response = serviceCaller.invokePostRequestUsingHttpClient
					(jsonRequest, getAppointmentConfirmPath(), headers);
			if (null == response) {
				log.error("appointmentConfirm: Response " +
						"from Confirm Appointment Micro Service " +
						"is null.");
			}
		} catch (BSROBusinessException e) {
			log.error("BSROBusinessException occurred in " +
					"appointmentConfirm OSGI service.", e);
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			log.error("Exception occurred in appointmentConfirm " +
					"OSGI service.", e);
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		if (log.isDebugEnabled()) {
			log.debug("appointmentConfirm: response-{}", response);
		}
		return response;
	}

	private String constructConfirmAppointmentJSON(HttpServletRequest request,
												   String vehicleType)
			throws BSROBusinessException {
		String json = null;
		try {
			if (request != null) {
				Choice choice = getChoiceFromHttpRequest(request);

				String selectedServicesParam =
						request.getParameter(
								VoiceConstants.REQUEST_PARAMETER_NAME_SELECTED_SERVICES);
				if (log.isDebugEnabled()) {
					log.debug("constructConfirmAppointmentJSON: selectedServicesParam-{}",
							selectedServicesParam);
				}
				//Validate selectedServicesParam
				/*boolean selectedServicesParamValid = AppointmentValidationUtil
						.validateServiceIDs(selectedServicesParam);*/
				
				//we need to pass selectedServices as comma seperated string 
				/*ArrayList selectedServices = null;
				if (!StringUtils.isBlank(selectedServicesParam)) {
					selectedServices = new ArrayList();
					selectedServices.add(selectedServicesParam);
				}*/

				VoiceRequest appointmentRequestObj =
						new VoiceRequest(request, choice, selectedServicesParam);
				
				if(validateAppointmentRequest(appointmentRequestObj,
						vehicleType)) {
				Gson gson = new Gson();
				json = gson.toJson(appointmentRequestObj);
				} else {
					throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
							"Unable to create Appointment due to the validation error.");
				}
			}
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		if (log.isDebugEnabled()) {
			log.debug("constructConfirmAppointmentJSON: " +
					"postRequestObj converted to JSON-{}", json);
		}
		return json;
	}
	
	// validations for appointment confirm request values 
	private boolean validateAppointmentRequest(VoiceRequest appointment,
											   String vehicleType)
			throws BSROBusinessException {
		boolean validateFlag = false;
		try {
			validateFlag = VoiceValidationUtil.validateEmployeeID(appointment.getEmployeeId());
			validateFlag = VoiceValidationUtil.validateLocationID(appointment.getLocationId());
			validateFlag = VoiceValidationUtil.validateServiceIDs(appointment.getSelectedServices());
			validateFlag = ProfileValidationUtil.validateYear(appointment.getVehicleYear());
			validateFlag = ProfileValidationUtil.validateMake(appointment.getVehicleMake());
			validateFlag = ProfileValidationUtil.validateModel(appointment.getVehicleModel());
			validateFlag = ProfileValidationUtil.validateTrim(appointment.getVehicleSubmodel());
			validateFlag = ProfileValidationUtil.validateStoreNumber(appointment.getStoreNumber());
			validateFlag = ProfileValidationUtil.validateSite(appointment.getWebsiteName());
			validateFlag = VoiceValidationUtil.validateCustomerName(appointment.getCustomerFirstName());
			validateFlag = VoiceValidationUtil.validateCustomerName(appointment.getCustomerLastName());
			validateFlag = VoiceValidationUtil.validateCustomerEmail(appointment.getCustomerEmailAddress());
			validateFlag = VoiceValidationUtil.validateCustomerPhone(appointment.getCustomerDayTimePhone());
			validateFlag = VoiceValidationUtil.validateMileage(appointment.getMileage());
			validateFlag = ProfileValidationUtil.validateVehicleTypeForTrimTPMS(vehicleType);
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		if(log.isDebugEnabled()) {
			log.debug("validateAppointmentRequest: validateFlag-{}",
					new Boolean(validateFlag).toString());
		}
		return validateFlag;
	}

	private Choice getChoiceFromHttpRequest(HttpServletRequest request)
	throws BSROBusinessException {
		Choice choice = null;
		try {
			choice = new Choice();
			String appointmentChoiceId = request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_APPOINTMENT_CHOICE_ID);
			choice.setAppointmentChoiceId(appointmentChoiceId);

			String choiceParam = request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_CHOICE);
			choice.setChoice(choiceParam);

			String strApptDate = request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_DATE);
			String dropOffTime = request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_DROP_OFF_TIME);
			String pickUpTime =request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_PICK_UP_TIME);
			if (log.isDebugEnabled()) {
				log.debug("getChoiceFromHttpRequest: strApptDate-{}, dropOffTime-{}, " +
						"pickUpTime-{}", strApptDate, dropOffTime, pickUpTime);
			}

			//Drop Off Time is a mandatory input for the
			//Micro Service Call
			if(StringUtils.isBlank(dropOffTime)) {
				throw new BSROBusinessException(CoreCodes.CODE_053,
						"dropOffTime is null/empty.");
			}
			//Construct 'datetime' Choice attribute based on strApptDate
			// and dropOffTime
			String datetime = getPickUpDropOffDateTime(strApptDate,
					dropOffTime);
			if (log.isDebugEnabled()) {
				log.debug("getChoiceFromHttpRequest: datetime-{}", datetime);
			}
			//Set datetime and dropOffTime in Choice
			choice.setDatetime(datetime);
			choice.setDropOffTime(datetime);

			//Pick Up Time is a NON-MANDATORY input for the
			//Micro Service Call
			String pickUpDateTime = null;
			if(!StringUtils.isBlank(pickUpTime)) {
				//Construct 'datetime' Choice attribute based on strApptDate
				// and pickUpTime
				pickUpDateTime = getPickUpDropOffDateTime(strApptDate,
						pickUpTime);
			}
			if (log.isDebugEnabled()) {
				log.debug("getChoiceFromHttpRequest: pickUpDateTime-{}",
						pickUpDateTime);
			}
			//Set pickUpTime in Choice
			choice.setPickUpTime(pickUpDateTime);

			String dropWaitOption = request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_DROP_WAIT_OPTION);
			choice.setDropWaitOption(dropWaitOption);
			String appointmentId =request.getParameter
					(VoiceConstants.REQUEST_PARAMETER_NAME_APPOINTMENT_ID);
			choice.setAppointmentId(appointmentId);
		} catch (BSROBusinessException e) {
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return choice;
	}

	private String getPickUpDropOffDateTime(String strApptDate,
											String pickUpDropOffTime)
			throws BSROBusinessException {
		String pickUpDropOffDateTime = null;
		try {
			//Validate Appt date
			boolean apptStrDateValid =
					VoiceValidationUtil
							.validateAppointmentDate(strApptDate);
			//Validate pickUpDropOffTime
			boolean pickUpDropOffTimeValid =
					VoiceValidationUtil
							.validateDropOffOrPickUpTime(pickUpDropOffTime);
			if ((apptStrDateValid) && (pickUpDropOffTimeValid)) {
				String strApptDateTime = new StringBuffer(strApptDate).append(" ")
						.append(pickUpDropOffTime).toString();
				if(log.isDebugEnabled()) {
					log.debug("getPickUpDropOffDateTime: strApptDateTime-{}",
							strApptDateTime);
				}

				//Assuming Central Timezone as agreed upon with the Client
				Date apptDateTime = DateUtil.getDate(strApptDateTime,
						VoiceConstants.APPOINTMENT_DATE_TIME_INPUT_FORMAT,
						CoreConstants.US_CENTRAL_TIME_ZONE_ID);

				pickUpDropOffDateTime = DateUtil.getStringDate(apptDateTime,
						VoiceConstants.APPOINTMENT_DATE_TIME_OUTPUT_FORMAT,
						CoreConstants.US_CENTRAL_TIME_ZONE_ID);
			}
		} catch (BSROBusinessException e) {
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return pickUpDropOffDateTime;
	}

	public String getAppointmentConfirmPath() throws BSROBusinessException {
		String getAppointmentConfirmPath = "";
		try {
			Configuration conf = getConfiguration();
			Dictionary<String, Object> props = conf.getProperties();
			if (log.isDebugEnabled()) {
				log.debug("getAppointmentConfirmPath: properties-{}", props);
			}
			if (props != null) {
				getAppointmentConfirmPath = PropertiesUtil.toString
						(props.get("appointment.confirm.path"), "");
			}
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return getAppointmentConfirmPath;
	}

	public String getNumDays() throws BSROBusinessException {
		String getNumDays = "";
		try {
			Configuration conf = getConfiguration();
			Dictionary<String, Object> props = conf.getProperties();
			if (log.isDebugEnabled()) {
				log.debug("getNumDays: properties-{}", props);
			}
			if (props != null) {
				getNumDays = PropertiesUtil.toString(props.get("get.Num.days"),
						"");
			}
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return getNumDays;
	}

	@Override
	public Map getAvailableDays(HttpServletRequest request, String locationId,
								String employeeId, String startDate, String numDays)
			throws BSROBusinessException {
		Map<String, Object> response = null;
		try {
			if((request == null)
					|| (!VoiceValidationUtil.validateLocationID(locationId))
					|| (!VoiceValidationUtil.validateEmployeeID(employeeId))
					|| (!VoiceValidationUtil.validateStartDate(startDate))) {
				throw new BSROBusinessException(CoreCodes.CODE_053,
						"Invalid Input Parameters. Need " +
								"HttpServletRequest, locationId, " +
								"employeeId and startDate.");
			}
			StringBuffer buffer = new StringBuffer(getAvailableDaysPath());
			buffer.append(locationId);
			buffer.append("/");
			buffer.append(employeeId);
			buffer.append("/");
			buffer.append(startDate);
			buffer.append("/");
			//Validate the numDays input parameter.
			//If valid, find the minimum of it and the
			//AppointmentConstants.MAX_NUM_APPOINTMENT_AVAILABLE_DAYS
			//and append that value plus 1, to the Micro Service request
			//else
			//append the AppointmentConstants.MAX_NUM_APPOINTMENT_AVAILABLE_DAYS
			//plus 1, to the Micro Service request
			boolean numDaysValid = false;
			try {
				numDaysValid =
						VoiceValidationUtil.validateNumDays(numDays);
			} catch (BSROBusinessException e) {
				//Do Nothing
			}
			int finalNumDays = 0;
			if(numDaysValid) {
				int numDaysInt = Integer.parseInt(numDays);
				finalNumDays = (numDaysInt <= VoiceConstants.MAX_NUM_APPOINTMENT_AVAILABLE_DAYS) ?
						numDaysInt : VoiceConstants.MAX_NUM_APPOINTMENT_AVAILABLE_DAYS;
			} else {
				finalNumDays = VoiceConstants.MAX_NUM_APPOINTMENT_AVAILABLE_DAYS;
			}
			//Adding 1 to the finalNumDays as the CoreSite service sends
			//one record less than the requested numbers of days value
			buffer.append(finalNumDays + 1);
			response = invokeAppointmentMicroService(request, buffer.toString(),
					null, false);
			if (log.isDebugEnabled()) {
				log.debug("getAvailableDays: Response is: {}", response);
			}
			if (null == response) {
				log.error("getAvailableDays: Micro Service response is null.");
			}
		} catch (BSROBusinessException e) {
			log.error("BSROBusinessException occurred in " +
					"getAvailableDays Appointment Service.", e);
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			log.error("Exception occurred in " +
					"getAvailableDays Appointment Service.", e);
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return response;
	}

	public String getAvailableDaysPath() throws BSROBusinessException {
		String getAvailableDaysPath = "";
		try {
			Configuration conf = getConfiguration();
			Dictionary<String, Object> props = conf.getProperties();
			if (log.isDebugEnabled()) {
				log.debug("getAvailableDaysPath: properties-{}", props);
			}
			if (props != null) {
				getAvailableDaysPath =
						PropertiesUtil.toString(props.get("get.available.days.path"),
						"");
			}
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return getAvailableDaysPath;
	}

	@Override
	public Map getAvailableTimes(HttpServletRequest request, String locationId,
								 String employeeId, String selectedDate, String serviceIds)
			throws BSROBusinessException {
		Map<String, Object> response = null;
		try {
			if((request == null)
					|| (!VoiceValidationUtil.validateLocationID(locationId))
					|| (!VoiceValidationUtil.validateEmployeeID(employeeId))
					|| (!VoiceValidationUtil.validateSelectedDate(selectedDate))
					|| (!VoiceValidationUtil.validateServiceIDs(serviceIds))) {
				throw new BSROBusinessException(CoreCodes.CODE_053,
						"Invalid Input Parameters. Need " +
								"HttpServletRequest, locationId, " +
								"employeeId, selectedDate and " +
								"serviceIds.");
			}
			StringBuffer buffer = new StringBuffer(getAvailableTimesPath());
			buffer.append(locationId);
			buffer.append("/");
			buffer.append(employeeId);
			buffer.append("/");
			buffer.append(selectedDate);
			buffer.append("/");
			buffer.append(BSROStringUtil.urlEncode(serviceIds));
			response = invokeAppointmentMicroService(request, buffer.toString(),
					null, false);
			if (log.isDebugEnabled()) {
				log.debug("getAvailableTimes: Response is: {}", response);
			}
			if (null == response) {
				log.error("getAvailableTimes: Response from Micro Service is null.");
			}
		} catch (BSROBusinessException e) {
			log.error("BSROBusinessException occurred in getAvailableTimes " +
					"Appointment Service.", e);
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			log.error("Exception occurred in getAvailableTimes " +
					"Appointment Service.", e);
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return response;
	}

	public String getAvailableTimesPath() throws BSROBusinessException {
		String getAvailableTimesPath = "";
		try {
			Configuration conf = getConfiguration();
			Dictionary<String, Object> props = conf.getProperties();
			if (log.isDebugEnabled()) {
				log.debug("getAvailableTimesPath: properties-{}",
						props);
			}
			if (props != null) {
				getAvailableTimesPath =
						PropertiesUtil.toString(props.get("get.available.times.path"),
						"");
			}
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return getAvailableTimesPath;
	}

	public Configuration getConfiguration() throws BSROBusinessException {
		Configuration conf = null;
		try {
			conf = configAdmin.getConfiguration("BSRO Appointment Service Configuration");
		} catch (IOException e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		} catch(Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return conf;
	}

	@Override
	public Map getServiceAssignments(HttpServletRequest request,
									 String storeNumber,
									 String serviceNamesList)
			throws BSROBusinessException {
		Map<String, Object> response = null;
		try {
			if((request == null)
					|| (!ProfileValidationUtil.validateStoreNumber(storeNumber))
					|| (!VoiceValidationUtil.validateServiceNames(serviceNamesList))) {
				throw new BSROBusinessException(CoreCodes.CODE_053,
						"Invalid Input paramters. Need HttpServletRequest, Store " +
								"Number and Service Names.");
			}
			StringBuffer buffer = new StringBuffer(getServiceAssignmentsPath());
			buffer.append(storeNumber);
			buffer.append("/");
			buffer.append(BSROStringUtil.urlEncode(serviceNamesList));
			response = invokeAppointmentMicroService(request, buffer.toString(),
					null, false);
			if (log.isDebugEnabled()) {
				log.debug("getServiceAssignments: Response is:  {}", response);
			}
			if (null == response) {
				log.error("getServiceAssignments: Micro Service response is null. \n" +
						"storeNumber-{}, serviceNamesList-{}",
						storeNumber, serviceNamesList);
			}
		} catch (BSROBusinessException e) {
			log.error("BSROBusinessException occurred in " +
					"getServiceAssignments Appointment " +
					"Service.", e);
			throw new BSROBusinessException(e.getCode(), e.getMessage(), e);
		} catch (Exception e) {
			log.error("Exception occurred in getServiceAssignments " +
					"Appointment Service.", e);
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return response;
	}

	public String getServiceAssignmentsPath() throws BSROBusinessException {
		String getServiceAssignmentsPath = "";
		try {
			Configuration conf = getConfiguration();
			Dictionary<String, Object> props = conf.getProperties();
			if (log.isDebugEnabled()) {
				log.debug("getServiceAssignmentsPath: properties-{}",
						props);
			}
			if (props != null) {
				getServiceAssignmentsPath =
						PropertiesUtil.toString(props.get("get.service.assignments.path"),
						"");
			}
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return getServiceAssignmentsPath;
	}

	private Map invokeAppointmentMicroService(HttpServletRequest request, String serviceURL,
											  String vehicleType,
											  boolean dataSourceHeader)
			throws BSROBusinessException {
		Map microServicesResponse = null;
		try {
			if (request == null) {
				throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
						"HttpServletRequest is null");
			} else if (StringUtils.isBlank(serviceURL)) {
				throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
						"serviceURL is null/empty.");
			} else if (dataSourceHeader && (!validateVehicleType(vehicleType))) {
				throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
						"vehicle type is invalid.");
			} else {
				HttpGet getRequest = new HttpGet(serviceURL);

				//Set Header Values in the getRequest
				Map headerValues =
						getHeaderValuesForAppointmentMicroService(request,
								vehicleType, dataSourceHeader);
				if (log.isDebugEnabled()) {
					log.debug("invokeAppointmentMicroService: " +
							"Header Values map-{}", headerValues);
				}
				getRequest = MicroServicesUtil.
						populateHeaderValuesInHttpGetRequest(getRequest, headerValues);

				microServicesResponse = serviceCaller.invokeGetRequest(getRequest);
			}
		} catch (BSROBusinessException e) {
			throw new BSROBusinessException(e.getCode(),
					e.getMessage(), e);
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e);
		}
		return microServicesResponse;
	}

	private boolean validateVehicleType(String vehicleType)
			throws BSROBusinessException {
		boolean vehicleTypeForTrimTpmsValid =
				ProfileValidationUtil.validateVehicleTypeForTrimTPMS
						(vehicleType);
		return vehicleTypeForTrimTpmsValid;
	}

	private Map getHeaderValuesForAppointmentMicroService(HttpServletRequest request,
														  String vehicleType,
														  boolean dataSourceHeader)
			throws BSROBusinessException {
		Map headerValuesMap = null;
		try {
			if (dataSourceHeader && (!validateVehicleType(vehicleType))) {
				throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
						"vehicle type is invalid.");
			}

			headerValuesMap = new HashMap();

			//Get datasource value based on the input vehicleType
			//and then populate the value in the Header Values Map
			if(dataSourceHeader) {
				String datasource = MicroServicesUtil.
						getDatasourceName(vehicleType);
				if (!StringUtils.isBlank(datasource)) {
					headerValuesMap.put(CoreConstants.HEADER_NAME_DATASOURCE,
							datasource);
				}
			}

			// Get Security Info for getting tokenId and site values
			// and then populate those value in the Header Values Map
			//Get Site from HttpServletRequest
			String siteFromHttpRequest = siteService.getSite(request);
			Map securityInfoMap = securityService.getSecurityInfo
					(siteFromHttpRequest);
			if (securityInfoMap != null) {
				Object objTokenID = securityInfoMap
						.get(CoreConstants.HEADER_NAME_TOKENID);
				String tokenID = null;
				if (objTokenID != null) {
					tokenID = (String) objTokenID;
				}

				Object objSite = securityInfoMap
						.get(CoreConstants.HEADER_NAME_SITE);
				String site = null;
				if (objSite != null) {
					site = (String) objSite;
				}

				boolean tokenIDValid = MicroServicesUtil
						.validateTokenID(tokenID);
				boolean siteValid = ProfileValidationUtil.validateSite(site);
				if (tokenIDValid && siteValid) {
					// Populate tokenID header
					headerValuesMap.put(CoreConstants.HEADER_NAME_TOKENID,
							tokenID);

					// Populate site header
					headerValuesMap.put(CoreConstants.HEADER_NAME_SITE, site);
				}
			} else {
				throw new BSROBusinessException(
						CoreCodes.CODE_COMPONENT_ERROR,
						"Security Info Map returned from Security Service is null."
								+ "Cannot populate tokenId and site header values for"
								+ "Micro Services Call");
			}

		} catch (BSROBusinessException e) {
			throw new BSROBusinessException(e.getCode(),
					e.getMessage(), e);
		} catch (Exception e) {
			throw new BSROBusinessException(CoreCodes.CODE_COMPONENT_ERROR,
					e);
		}
		return headerValuesMap;
	}

}
