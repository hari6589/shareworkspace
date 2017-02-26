package com.bfrc.dataaccess.model.appointment;

import java.io.Serializable;

public class AppointmentService implements Serializable {

	private static final long serialVersionUID = -447238644517773259L;

    private Long appointmentServiceId;
    private Integer serviceId;
    private String serviceNameTx;
    private Long appointmentId;
    
    public AppointmentService(){}
    
    
	public AppointmentService(Long appointmentServiceId, Integer serviceId, String serviceNameTx,
			Long appointmentId) {
		super();
		this.appointmentServiceId = appointmentServiceId;
		this.serviceId = serviceId;
		this.serviceNameTx = serviceNameTx;
		this.appointmentId = appointmentId;
	}


	public void setServiceNameTx(String serviceNameTx) {
		this.serviceNameTx = serviceNameTx;
	}
	public String getServiceNameTx() {
		return serviceNameTx;
	}
	
	public Long getAppointmentServiceId() {
		return appointmentServiceId;
	}
	public void setAppointmentServiceId(Long appointmentServiceId) {
		this.appointmentServiceId = appointmentServiceId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((appointmentServiceId == null) ? 0 : appointmentServiceId
						.hashCode());
		result = prime * result
				+ ((serviceId == null) ? 0 : serviceId.hashCode());
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
		AppointmentService other = (AppointmentService) obj;
		if (appointmentServiceId == null) {
			if (other.appointmentServiceId != null)
				return false;
		} else if (!appointmentServiceId.equals(other.appointmentServiceId))
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}
	
}
