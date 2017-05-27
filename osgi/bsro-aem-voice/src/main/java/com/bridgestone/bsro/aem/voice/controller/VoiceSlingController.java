package com.bridgestone.bsro.aem.voice.controller;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.Map;

import com.bridgestone.bsro.aem.core.constants.CoreConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bridgestone.bsro.aem.core.constants.CoreCodes;
import com.bridgestone.bsro.aem.core.exception.BSROBusinessException;
import com.bridgestone.bsro.aem.core.utils.DatamapUtil;
import com.bridgestone.bsro.aem.core.site.ISiteService;
import com.bridgestone.bsro.aem.tire.funnel.services.ITireFunnelService;
import com.bridgestone.bsro.aem.voice.services.IVoiceService;


// TODO: Auto-generated Javadoc
/**
 * The Class GetAppointmentDetailsServlet.
 */
@Service({ SlingAllMethodsServlet.class })
@SlingServlet(paths = { 
		"/bsro/services/myfirestone/appointment"
		}, methods = "GET")
public class VoiceSlingController extends SlingAllMethodsServlet{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(VoiceSlingController.class);

	
	static final String MYFIRESTONE_APPOINTMENT = "appointment";
	
	@Reference
	private IVoiceService appointmentService;

	@Reference
	private ISiteService siteService;

	@Reference
	private ITireFunnelService tireFunnelService;


	public void doGet(SlingHttpServletRequest request,
					  SlingHttpServletResponse response) throws ServletException,
			IOException {
		try {
			
			log.info("::Voice:: - Info");
			log.debug("::Voice:: - Debug");
			log.error("::Voice:: - Error");
			
			//Validate and Cleanup Profile Cookies
			try {
				siteService.validateAndCleanupProfileCookies(request, response);
			} catch (BSROBusinessException e) {
				log.error("AppointmentSlingController.doGet: BSROBusinessException " +
						"occurred while Validating and Cleaning Up Profile " +
						"Cookies", e);
			}

			String pathInfo = request.getPathInfo().trim();
			Map<String, Object> responseDetailsList = null;
			if (log.isDebugEnabled()) {
				log.debug("Request path is-{}", pathInfo);
			}
			String serviceFunction = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);
			if (log.isDebugEnabled()) {
				log.debug("Serving the business function-{}", serviceFunction);
			}
			switch (serviceFunction) {
				
				case MYFIRESTONE_APPOINTMENT:
					try {
						String vehicleType = getVehicleType(request);
						responseDetailsList = appointmentService.appointmentConfirm
								(request, vehicleType);
						if (log.isDebugEnabled()) {
							log.debug("responseDetailsList-{}", responseDetailsList);
						}
						if (responseDetailsList != null && !responseDetailsList.isEmpty()) {
							processErrorResponse(responseDetailsList, response);
							response.getWriter().write(
									DatamapUtil.convertMapToJSONResponse(responseDetailsList));
						}else{
							processErrorResponse(responseDetailsList, response);
							response.getWriter().write(DatamapUtil.errorResponse
									(CoreCodes.CODE_COMPONENT_ERROR, "No Data Found"));
						}
						
					} catch (BSROBusinessException e) {
						log.error("BSROBusinessException occurred in APPOINTMENT_CONFIRM_PATH " +
								"in AppointmentSlingController-", e);
						processErrorResponse(null, response);
						response.getWriter().write(DatamapUtil.errorResponse
								(e.getCode(), "BSROBusinessException occurred - " +
												e.getMessage()));
					} catch (Exception e) {
						log.error("Exception occurred in APPOINTMENT_CONFIRM_PATH " +
								"in AppointmentSlingController-", e);
						processErrorResponse(null, response);
						response.getWriter().write(DatamapUtil.errorResponse
								(CoreCodes.CODE_COMPONENT_ERROR,
										"Exception occurred in APPOINTMENT_CONFIRM_PATH " +
												"in AppointmentSlingController - " +
												e.getMessage()));
					}
					break;
				default:
					log.error(" The service path " + pathInfo
							+ " does not have any business service");
					throw new ServletException("NO BUSINESS SERVICE/METHOD " +
							"EXISTS TO HANDLE THE REQUEST " + pathInfo);
			}

		} catch (Exception e) {
			log.error("Exception occurred in AppointmentSlingController doGet Method", e);
		}
	}

	private void processErrorResponse(Map responseMap,
									  SlingHttpServletResponse response) {
		try {
			boolean isErrorResponse =
					DatamapUtil.isErrorResponse(responseMap);
			if(log.isDebugEnabled()) {
				log.debug("AppointmentSlingController." +
								"processErrorResponse: isErrorResponse-{}",
						(new Boolean(isErrorResponse)).toString());
			}
			if(isErrorResponse) {
				//Set HTTP Error Status in the response
				response.setStatus
						(CoreCodes.CODE_OSGI_HTTP_ERROR_STATUS);

				//Set the Response Header
				if(log.isDebugEnabled()) {
					log.debug("AppointmentSlingController." +
							"processErrorResponse: " +
							"Setting Response Header");
				}

				//Get the value for the 'status' key from
				//the responseMap
				String status = null;
				if(responseMap != null) {
					Object statusObj =
							responseMap.get(CoreConstants.KEY_STATUS);
					if (statusObj != null) {
						status = (String) statusObj;
					}
				}
				if(StringUtils.isEmpty(status)) {
					status = CoreCodes.CODE_COMPONENT_ERROR;
				}
				response.setHeader
						(CoreConstants.RESPONSE_HEADER_X_REASON_BSRO,
								status);
			}
		} catch (Exception e) {
			log.error("Exception occurred in " +
					"AppointmentSlingController." +
					"processErrorResponse.", e);
		}
	}

	private String getVehicleType(SlingHttpServletRequest request)
			throws BSROBusinessException {
		String vehicleType = null;
		try {
			RequestParameterMap requestParameterMap =
					request.getRequestParameterMap();
			//Get the vehicleType input from the
			//HTTP Request. If it is null/empty,
			//get the vehicleType based on the
			//'ecommerce' header in the HTTP
			//Request
			vehicleType = getRequestParameterValue(requestParameterMap,
					CoreConstants.REQUEST_PARAMETER_NAME_VEHICLE_TYPE);
			if(StringUtils.isBlank(vehicleType)) {
				vehicleType = tireFunnelService.
						getVehicleTypeByECommerceEnabled(request);
			}
		} catch (BSROBusinessException e) {
			throw new BSROBusinessException(e.getCode(),
					e.getMessage(), e);
		} catch (Exception e) {
			throw new BSROBusinessException(
					CoreCodes.CODE_COMPONENT_ERROR,
					e.getMessage(), e);
		}
		return vehicleType;
	}

	private String getRequestParameterValue(RequestParameterMap requestParameterMap,
											String name) {
		String value = null;
		if((requestParameterMap != null) && (requestParameterMap.size() != 0)
				&& (!StringUtils.isBlank(name))) {
			RequestParameter requestParameter = requestParameterMap.getValue(name);
			if (requestParameter != null) {
				value = requestParameter.getString();
			}
		}
		return value;
	}

}
		
		
		
		
		
		
		
		
		


