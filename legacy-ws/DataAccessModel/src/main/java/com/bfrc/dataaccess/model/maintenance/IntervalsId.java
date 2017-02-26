package com.bfrc.dataaccess.model.maintenance;

public class IntervalsId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long acesVehicleId;
	private String serviceType;
	private Long mileageInterval;	
	private long operationTypeId;
	private long componentId;

	public IntervalsId() {}

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

	public long getOperationTypeId() {
		return operationTypeId;
	}

	public void setOperationTypeId(long operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	public long getComponentId() {
		return componentId;
	}

	public void setComponentId(long componentId) {
		this.componentId = componentId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acesVehicleId == null) ? 0 : acesVehicleId.hashCode());
		result = prime * result + (int) (componentId ^ (componentId >>> 32));
		result = prime * result
				+ (int) (operationTypeId ^ (operationTypeId >>> 32));
		result = prime * result
				+ ((serviceType == null) ? 0 : serviceType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntervalsId other = (IntervalsId) obj;
		if (acesVehicleId == null) {
			if (other.acesVehicleId != null)
				return false;
		} else if (!acesVehicleId.equals(other.acesVehicleId))
			return false;
		if (componentId != other.componentId)
			return false;
		if (operationTypeId != other.operationTypeId)
			return false;
		if (serviceType == null) {
			if (other.serviceType != null)
				return false;
		} else if (!serviceType.equals(other.serviceType))
			return false;
		return true;
	}

	
	
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((acesVehicleId == null) ? 0 : acesVehicleId.hashCode());
//		result = prime * result + (int) (componentId ^ (componentId >>> 32));
//		result = prime * result
//				+ ((mileageInterval == null) ? 0 : mileageInterval.hashCode());
//		result = prime * result
//				+ (int) (operationTypeId ^ (operationTypeId >>> 32));
//		result = prime * result
//				+ ((serviceType == null) ? 0 : serviceType.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		IntervalsId other = (IntervalsId) obj;
//		if (acesVehicleId == null) {
//			if (other.acesVehicleId != null)
//				return false;
//		} else if (!acesVehicleId.equals(other.acesVehicleId))
//			return false;
//		if (componentId != other.componentId)
//			return false;
//		if (mileageInterval == null) {
//			if (other.mileageInterval != null)
//				return false;
//		} else if (!mileageInterval.equals(other.mileageInterval))
//			return false;
//		if (operationTypeId != other.operationTypeId)
//			return false;
//		if (serviceType == null) {
//			if (other.serviceType != null)
//				return false;
//		} else if (!serviceType.equals(other.serviceType))
//			return false;
//		return true;
//	}
}
