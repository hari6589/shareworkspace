package app.bsro.model.scheduled.maintenance.ncd.rtq;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EDWRTQ")
public class RtqRequest {

	private static String DFLT_APP_ID = "MOBILE";
	
	public static String getRtq2010(String area, String exchange, String line) {
		StringBuilder builder = new StringBuilder("&lt;EDWRTQ AppId='").append(DFLT_APP_ID).append("' ");
		builder.append("ReqId='RTQ2010' RequestVersion='1' ResponseVersion='1' ");
		builder.append("AreaCode='").append(area).append("' " );
		builder.append("Exchange='").append(exchange).append("' " );
		builder.append("Line='").append(line).append("'" );
		builder.append("&gt;&lt;/EDWRTQ&gt;");
		return builder.toString();
	}
	
	public static String getRtq2020(String storeNbr, String invoiceNbr) {
		StringBuilder builder = new StringBuilder("&lt;EDWRTQ AppId='").append(DFLT_APP_ID).append("' ");
		builder.append("ReqId='RTQ2020' RequestVersion='1' ResponseVersion='1' ");
		builder.append("StoreNbr='").append(storeNbr).append("' " );
		builder.append("InvoiceNbr='").append(invoiceNbr).append("' " );
		builder.append("&gt;&lt;/EDWRTQ&gt;");
		return builder.toString();
	}
	
	public static String getRtq2030(String vehicleId, String fromDate) {
		StringBuilder builder = new StringBuilder("&lt;EDWRTQ AppId='").append(DFLT_APP_ID).append("' ");
		builder.append("ReqId='RTQ2030' RequestVersion='1' ResponseVersion='1' ");
		builder.append("NCD_VehicleID='").append(vehicleId).append("' " );
		builder.append("FromDate='").append(fromDate).append("' " );
		builder.append("&gt;&lt;/EDWRTQ&gt;");
		return builder.toString();
	}
	
	private enum Rtq {
		RTQ2010("RTQ2010"), 
		RTQ2020("RTQ2020"), 
		RTQ2030("RTQ2030");
		private String param;

		private Rtq(String value) {
			this.param = value;
		}
		public String getValue() {
			return this.param;
		}
	};
	private String AppId;
	private String ReqId;
	private String RequestVersion;
	private String ResponseVersion;
	
	//For phone number search
	private Integer AreaCode;
	private Integer Exchange;
	private Integer Line;
	
	//For secondary store/invoice search
	private String StoreNbr;
	private Integer InvoiceNbr;
	
	//To pull Job records
	private Integer vehicleId;
	private String fromDate;
	
	public RtqRequest(){}
	
	@XmlAttribute(name="AppId")
	public String getAppId() {
		return AppId;
	}
	public void setAppId(String appId) {
		AppId = appId;
	}
	
	@XmlAttribute(name="ReqId")
	public String getReqId() {
		return ReqId;
	}
	public void setReqId(String reqId) {
		ReqId = reqId;
	}
	
	@XmlAttribute(name="RequestVersion")
	public String getRequestVersion() {
		return RequestVersion;
	}
	public void setRequestVersion(String requestVersion) {
		RequestVersion = requestVersion;
	}
	
	@XmlAttribute(name="ResponseVersion")
	public String getResponseVersion() {
		return ResponseVersion;
	}
	public void setResponseVersion(String responseVersion) {
		ResponseVersion = responseVersion;
	}

	@XmlAttribute(name="AreaCode")
	public Integer getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(Integer areaCode) {
		AreaCode = areaCode;
	}
	
	@XmlAttribute(name="Exchange")
	public Integer getExchange() {
		return Exchange;
	}
	public void setExchange(Integer exchange) {
		Exchange = exchange;
	}

	@XmlAttribute(name="Line")
	public Integer getLine() {
		return Line;
	}
	public void setLine(Integer line) {
		Line = line;
	}

	@XmlAttribute(name="StoreNbr")
	public String getStoreNbr() {
		return StoreNbr;
	}
	public void setStoreNbr(String storeNbr) {
		StoreNbr = storeNbr;
	}

	@XmlAttribute(name="InvoiceNbr")
	public Integer getInvoiceNbr() {
		return InvoiceNbr;
	}
	public void setInvoiceNbr(Integer invoiceNbr) {
		InvoiceNbr = invoiceNbr;
	}
	
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	@XmlAttribute(name="NCD_VehicleID")
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	@XmlAttribute(name="FromDate")
	public String getFromDate() {
		return fromDate;
	}
}
