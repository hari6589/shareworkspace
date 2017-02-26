/**
 * 
 */
package com.bfrc.pojo.pricing;

import java.util.Date;

/**
 * @author smoorthy
 *
 */
public class TpSuppressionDisplayVehType implements java.io.Serializable {
	
	private TpSuppressionDisplayVehId id;
	private Long suppressionId;
	private Date dateCreated;
	
	public TpSuppressionDisplayVehType() {
		
	}
	
	public TpSuppressionDisplayVehType(TpSuppressionDisplayVehId id) {
		this.id = id;
	}
	
	public TpSuppressionDisplayVehType(TpSuppressionDisplayVehId id, Long suppressionId, Date dateCreated) {
		this.id = id;
		this.suppressionId = suppressionId;
		this.dateCreated = dateCreated;
	}
	
	public TpSuppressionDisplayVehId getId() {
		return this.id;
	}

	public void setId(TpSuppressionDisplayVehId id) {
		this.id = id;
	}
	
	public Long getSuppressionId() {
		return this.suppressionId;
	}

	public void setSuppressionId(Long suppressionId) {
		this.suppressionId = suppressionId;
	}
	
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
