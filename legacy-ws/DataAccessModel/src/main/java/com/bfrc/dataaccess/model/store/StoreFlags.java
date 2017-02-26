package com.bfrc.dataaccess.model.store;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class StoreFlags implements Serializable{
	private static final BigDecimal DEFAULT = new BigDecimal("0");

	private Long storeNumber;
	
	private BigDecimal activeFlag;
	
	private BigDecimal onlineAppointmentActiveFlag;
	
	private BigDecimal tirePricingActiveFlag;
	
	private BigDecimal eCommActiveFlag;
	
	public StoreFlags() {
	
	}
	
	public StoreFlags(Long storeNumber, BigDecimal activeFlag, BigDecimal onlineAppointmentActiveFlag, BigDecimal tirePricingActiveFlag, BigDecimal eCommActiveFlag) {
		this.storeNumber = storeNumber;
		this.activeFlag = activeFlag;
		this.onlineAppointmentActiveFlag = onlineAppointmentActiveFlag;
		this.tirePricingActiveFlag = tirePricingActiveFlag;
		this.eCommActiveFlag = eCommActiveFlag;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(BigDecimal activeFlag) {
		if(activeFlag == null) activeFlag = DEFAULT;
		this.activeFlag = activeFlag;
	}
	
	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getOnlineAppointmentActiveFlag() {
		return onlineAppointmentActiveFlag;
	}

	public void setOnlineAppointmentActiveFlag(
			BigDecimal onlineAppointmentActiveFlag) {
		if(onlineAppointmentActiveFlag == null) onlineAppointmentActiveFlag = DEFAULT;
		this.onlineAppointmentActiveFlag = onlineAppointmentActiveFlag;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getTirePricingActiveFlag() {
		return tirePricingActiveFlag;
	}

	public void setTirePricingActiveFlag(BigDecimal tirePricingActiveFlag) {
		if(tirePricingActiveFlag == null) tirePricingActiveFlag = DEFAULT;
		this.tirePricingActiveFlag = tirePricingActiveFlag;
	}
	
	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal geteCommActiveFlag() {
		return eCommActiveFlag;
	}

	public void seteCommActiveFlag(BigDecimal eCommActiveFlag) {
		if(eCommActiveFlag == null) eCommActiveFlag = DEFAULT;
		this.eCommActiveFlag = eCommActiveFlag;
	}
	
	@Override
	public String toString() {
		return "StoreFlags [storeNumber=" + storeNumber 
				+ ", activeFlag=" + activeFlag 
				+ ", onlineAppointmentActiveFlag=" + onlineAppointmentActiveFlag 
				+ ", tirePricingActiveFlag=" + tirePricingActiveFlag 
				+ ", eCommActiveFlag="	+ eCommActiveFlag + "]";
	}
	
}
