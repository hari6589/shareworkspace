package app.bsro.model.scheduled.maintenance.ncd.rtq;

import javax.xml.bind.annotation.XmlElement;

public class VehicleSearchResult {

	//Returns for vehicle search (RTQ2010 and RTQ2020)
	private Integer customerId;
	private Integer vehicleId;
	private String year;
	private String make;
	private String model;
	private String subModel;
	
	
	@XmlElement(name="XNCD_PARTYID")
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	@XmlElement(name="XNCD_VEHICLEID")
	public Integer getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	@XmlElement(name="XYEAR")
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@XmlElement(name="XMAKE")
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	
	@XmlElement(name="XMODEL")
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	@XmlElement(name="XSUBMODEL")
	public String getSubModel() {
		return subModel;
	}
	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}
	
	@Override
	public String toString() {
		return "VehicleSearchResult [customerId=" + customerId + ", vehicleId="
				+ vehicleId + ", year=" + year + ", make=" + make + ", model="
				+ model + ", subModel=" + subModel + "]";
	}
	
}
