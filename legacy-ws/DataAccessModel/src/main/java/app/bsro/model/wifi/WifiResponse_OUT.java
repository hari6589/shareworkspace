package app.bsro.model.wifi;

/**
 * @author smoorthy
 *
 */
public class WifiResponse_OUT {
	
	private String responseCode;
	private String responseMessage;
	
	public WifiResponse_OUT()
	{	
	}
	
	public WifiResponse_OUT(String responseCode,String responseMessage)
	{
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
	}
	
	public String getResponseCode() 
	{
		return responseCode;
	}
	
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}
