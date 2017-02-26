package com.bfrc.pojo.pricing;

import java.math.BigDecimal;

/**
 * @author smoorthy
 *
 */
public class MobileTireInstallFee implements java.io.Serializable {
	
	private Long storeNumber;
	private BigDecimal tireInstallFee;
	private String feeDisclaimer;
	
	/** default constructor */
	public MobileTireInstallFee() {
		
	}
	
	public MobileTireInstallFee(Long storeNumber, BigDecimal tireInstallFee) {
		this.storeNumber = storeNumber;
		this.tireInstallFee = tireInstallFee;
	}
	
	/** constructor with id */
	public Long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public BigDecimal getTireInstallFee() {
		return this.tireInstallFee;
	}

	public void setTireInstallFee(BigDecimal tireInstallFee) {
		this.tireInstallFee = tireInstallFee;
	}
	
	public String getFeeDisclaimer() {
		return this.feeDisclaimer;
	}
	
	public void setFeeDisclaimer(String feeDisclaimer) {
		this.feeDisclaimer = feeDisclaimer;
	}

}
