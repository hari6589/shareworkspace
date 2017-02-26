package app.bsro.model.maintenance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ServiceMaintenance")
public class ServiceMaintenanceTO implements Serializable {
	private static final long serialVersionUID = 1825720469260016181L;
	private Long acesVehicleId;
	private String serviceType;
	private Long mileageInterval;	
	private String operationTypeTxt;
	private String componentTxt;
	private String footnoteTxt;
	private Long timeInterval;
	private String timeUnits;
	
	
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}
	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Long getMileageInterval() {
		return mileageInterval;
	}
	public void setMileageInterval(Long mileageInterval) {
		this.mileageInterval = mileageInterval;
	}
	public String getOperationTypeTxt() {
		return operationTypeTxt;
	}
	public void setOperationTypeTxt(String operationTypeTxt) {
		this.operationTypeTxt = operationTypeTxt;
	}
	public String getComponentTxt() {
		return componentTxt;
	}
	public void setComponentTxt(String componentTxt) {
		this.componentTxt = componentTxt;
	}
	public String getFootnoteTxt() {
		return footnoteTxt;
	}
	public void setFootnoteTxt(String footnoteTxt) {
		this.footnoteTxt = footnoteTxt;
	}
	public Long getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(Long timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getTimeUnits() {
		return timeUnits;
	}
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	public String toString() {
		return "ServiceMaintenanceTO [acesVehicleId=" + acesVehicleId
				+ ", serviceType=" + serviceType + ", mileageInterval="
				+ mileageInterval + ", operationTypeTxt=" + operationTypeTxt
				+ ", componentTxt=" + componentTxt + "]";
	}
	
	
}
