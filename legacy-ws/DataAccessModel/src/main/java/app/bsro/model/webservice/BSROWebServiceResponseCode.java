package app.bsro.model.webservice;

public enum BSROWebServiceResponseCode {
	
	SUCCESSFUL("SUCCESSFUL"),
	FAILURE("FAILURE"),
	VALIDATION_ERROR("VALIDATION_ERROR"),
	DATABASE_ERROR("DATABASE_ERROR"),
	BUSINESS_SERVICE_ERROR("BUSINESS_SERVICE_ERROR"),
	BUSINESS_SERVICE_INFO("BUSINESS_SERVICE_INFO"),
	UNKNOWN_ERROR("UNKNOWN_ERROR");
	
	String errorCode;
	
	BSROWebServiceResponseCode(String errorCode){
		this.errorCode = errorCode;
	}	

}
