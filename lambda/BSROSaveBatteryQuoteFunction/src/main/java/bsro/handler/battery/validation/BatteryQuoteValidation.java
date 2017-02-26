package bsro.handler.battery.validation;

public class BatteryQuoteValidation {
	
	public static String getErrorMessage(String storeNumber, String productCode, String year, String make, String model, String engine, String zip) {
		StringBuffer buf = new StringBuffer();
		String errmsg = null;
		int errFieldsCount = 0;
		if (isNullOrEmpty(storeNumber)) {
				buf.append("store number");
				++errFieldsCount;
		}
		if (isNullOrEmpty(productCode)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("productCode");
			++errFieldsCount;
		}
		if (isNullOrEmpty(year)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle year");
			++errFieldsCount;
		}
		
		if (isNullOrEmpty(make)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle make");
			++errFieldsCount;
		}
		if (isNullOrEmpty(model)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle model");
			++errFieldsCount;
		}
		if (isNullOrEmpty(engine)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("vehicle engine");
			++errFieldsCount;
		}
		if (isNullOrEmpty(zip)) {
			if (errFieldsCount > 0) {
				buf.append(",");
			}
			buf.append("Zip code");
			++errFieldsCount;
		}
		
		errmsg = (errFieldsCount == 1) ? "Missing parameter value for field " : "Missing parameter value for fields ";
		errmsg += buf.toString();
		return errmsg;
	}
	
	public static boolean isNullOrEmpty(String str) {
        
        if (str == null || str.trim().equals("") || str.trim().equals("null")) {
            return true;
        } 
        return false;
    }
	
}
