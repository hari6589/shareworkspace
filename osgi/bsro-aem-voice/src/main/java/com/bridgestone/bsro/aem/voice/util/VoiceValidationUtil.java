package com.bridgestone.bsro.aem.voice.util;

import com.bridgestone.bsro.aem.core.constants.CoreCodes;
import com.bridgestone.bsro.aem.core.exception.BSROBusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains functions for validating input parameters
 * of the Appointment OSGI Services
 *
 * Created by abatta on 4/4/16.
 */
public class VoiceValidationUtil {

    private static final Logger log = LoggerFactory.getLogger(VoiceValidationUtil.class);

    public static boolean validateLocationID(String locationID)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(locationID)) {
                int locationIDInt = Integer.parseInt(locationID);
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "locationID is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateEmployeeID(String employeeID)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(employeeID)) {
                int employeeIDInt = Integer.parseInt(employeeID);
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "employeeID is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateStartDate(String startDate)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(startDate)) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat
                                (VoiceConstants.START_DATE_FORMAT);
                Date date = sdf.parse(startDate);
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "startDate is null/empty.");
            }
        } catch(ParseException e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateSelectedDate(String selectedDate)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(selectedDate)) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat
                                (VoiceConstants.SELECTED_DATE_FORMAT);
                Date date = sdf.parse(selectedDate);
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "selectedDate is null/empty.");
            }
        } catch(ParseException e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateNumDays(String numDays)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(numDays)) {
                int numDaysInt = Integer.parseInt(numDays);
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "numDays is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateServiceIDs(String serviceIDs)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(serviceIDs)) {
                String [] serviceIDArray =
                        serviceIDs.split
                                (VoiceConstants.SEPARATOR_COMMA);
                if((serviceIDArray != null)
                        && (serviceIDArray.length > 0)) {
                    valid = true;
                } else {
                    valid = false;
                    throw new BSROBusinessException(CoreCodes.CODE_053,
                            "serviceIDs is null/empty.");
                }
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "serviceIDs is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateServiceNames(String serviceNames)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(serviceNames)) {
                String [] serviceIDArray =
                        serviceNames.split
                                (VoiceConstants.SEPARATOR_COMMA);
                if((serviceIDArray != null)
                        && (serviceIDArray.length > 0)) {
                    valid = true;
                } else {
                    valid = false;
                }
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "serviceNames is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateAppointmentDate(String apptDate)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(apptDate)) {
                SimpleDateFormat sdf =
                        new SimpleDateFormat
                                (VoiceConstants.APPOINTMENT_DATE_FORMAT);
                Date date = sdf.parse(apptDate);
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Appointment Date is null/empty.");
            }
        } catch(ParseException e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateDropOffOrPickUpTime
            (String pickUpOrDropOffTime) throws BSROBusinessException {
        boolean valid = false;
        try {
            if (StringUtils.isBlank(pickUpOrDropOffTime)) {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "pickUpOrDropOffTime is null/empty.");
            }

            String lowerCaseTime = pickUpOrDropOffTime.toLowerCase();
            if ((!lowerCaseTime.endsWith(VoiceConstants.TIME_AM))
                    && (!lowerCaseTime.endsWith(VoiceConstants.TIME_PM))) {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Invalid pickUpOrDropOffTime. " +
                                "It needs to be in the " +
                                "format <1-12>:<00-60><AM/PM>.");
            }
            int hrInt = -1;
            int minInt = 0;
            lowerCaseTime = lowerCaseTime
                    .replaceAll(VoiceConstants.TIME_AM, "")
                    .replaceAll(VoiceConstants.TIME_PM, "");
            String[] hrMin =
                    lowerCaseTime.split(VoiceConstants.SEPARATOR_COLON);
            if ((hrMin != null) && (hrMin.length == 2)) {
                String hr = hrMin[0];
                hrInt = Integer.parseInt(hr);
                if ((hrInt <= 0) || (hrInt > 12)) {
                    throw new BSROBusinessException(CoreCodes.CODE_053,
                            "Invalid pickUpTime or dropOffTime. " +
                                    "It needs to be in the " +
                                    "format <1-12>:<00-60><AM/PM>.");
                }

                String min = hrMin[1];
                minInt = Integer.parseInt(min);
                if ((minInt < 0) || (minInt > 60)) {
                    throw new BSROBusinessException(CoreCodes.CODE_053,
                            "Invalid pickUpTime or dropOffTime. " +
                                    "It needs to be in the " +
                                    "format <1-12>:<00-60><AM/PM>.");
                }
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Invalid pickUpTime or dropOffTime. " +
                                "It needs to be in the " +
                                "format <1-12>:<00-60><AM/PM>.");
            }
            valid = true;
        } catch (NumberFormatException e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        } catch (BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch (Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }
    
    public static boolean validateCustomerName(String name)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(name)) {
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Customer Name is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateCustomerEmail(String emailId)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (!StringUtils.isBlank(emailId)) {
            	
	            if(! emailId.trim().matches("^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$")) {
	            	throw new BSROBusinessException(CoreCodes.CODE_053,
	                        "Invalid EmailId ");
	            }
	            else {
	            	valid = true;
	            }
	            	
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "EmailId is null/empty.");
            }
        } catch(BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch(Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    public static boolean validateCustomerPhone(String phone)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            //Phone number cannot be null/empty.
            //It must contain at least one digit and
            //the phone number length must be less
            //than equal to 24.
            //It may contain numbers, spaces and alphabets
            //and the following special characters-
            //!@#$%^&*()_+-=[]{};’:”\|,./?~`
            //The special characters < and > are NOT allowed
            if (!StringUtils.isBlank(phone)) {
                if ((phone.trim().length() >= 1) && (phone.trim().length() <= 24) && (phone.trim().
                        matches("[\\w!@#$%^&*()_+\\-=\\[\\]\\{\\};’:”\\|,./?~` ]*[\\d][\\w!@#$%^&*()_+\\-=\\[\\]\\{\\};’:”\\|,./?~` ]*"))) {
                    valid = true;
                } else {
                    throw new BSROBusinessException(CoreCodes.CODE_053,
                            "Invalid Phone No.");
                }
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Phone No is null/empty.");
            }
        } catch (BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch (Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

    /*public static boolean validateMileage(String mileage)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            //Mileage is NOT mandatory and can be null/empty.
            //If mileage is not null/empty, its length must be
            //less than equal to 24.
            //It may contain numbers, alphabets, spaces and the
            //following special characters-
            //!@#$%^&*()_+-=[]{};’:”\|,./?~`
            //The special characters < and > are NOT allowed
            if (StringUtils.isBlank(mileage)) {
                valid = true;
            } else if ((mileage.trim().length() <= 24) && (mileage.trim().
                    matches("[\\w!@#$%^&*()_+\\-=\\[\\]\\{\\};’:”\\|,./?~` ]*"))) {
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Invalid Mileage.");
            }
        } catch (BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch (Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }*/

    public static boolean validateMileage(String mileage)
            throws BSROBusinessException {
        boolean valid = false;
        try {
            if (StringUtils.isBlank(mileage)) {
                valid = true;
            } else if (mileage.matches("[0-9]+")) {
                valid = true;
            } else {
                throw new BSROBusinessException(CoreCodes.CODE_053,
                        "Invalid Mileage.");
            }
        } catch (BSROBusinessException e) {
            throw new BSROBusinessException(e.getCode(),
                    e.getMessage(), e);
        } catch (Exception e) {
            throw new BSROBusinessException(CoreCodes.CODE_053,
                    e.getMessage(), e);
        }
        return valid;
    }

}
