package com.bfrc.pojo.pricing;

// Generated Sep 23, 2008 3:31:04 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * TpRequestLog generated by hbm2java
 */
public class TpRequestLog implements java.io.Serializable {

	private long tpRequestId;
	private long tpUserId;
	private Date requestDate;
	private long storeNumber;
	private String modelYear;
	private String makeName;
	private String modelName;
	private String submodel;

	public TpRequestLog() {
	}

	public TpRequestLog(long tpRequestId, long tpUserId, Date requestDate,
			long storeNumber) {
		this.tpRequestId = tpRequestId;
		this.tpUserId = tpUserId;
		this.requestDate = requestDate;
		this.storeNumber = storeNumber;
	}

	public TpRequestLog(long tpRequestId, long tpUserId, Date requestDate,
			long storeNumber, String modelYear, String makeName,
			String modelName, String submodel) {
		this.tpRequestId = tpRequestId;
		this.tpUserId = tpUserId;
		this.requestDate = requestDate;
		this.storeNumber = storeNumber;
		this.modelYear = modelYear;
		this.makeName = makeName;
		this.modelName = modelName;
		this.submodel = submodel;
	}

	public long getTpRequestId() {
		return this.tpRequestId;
	}

	public void setTpRequestId(long tpRequestId) {
		this.tpRequestId = tpRequestId;
	}

	public long getTpUserId() {
		return this.tpUserId;
	}

	public void setTpUserId(long tpUserId) {
		this.tpUserId = tpUserId;
	}

	public Date getRequestDate() {
		return this.requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getModelYear() {
		return this.modelYear;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getMakeName() {
		return this.makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

	public String getModelName() {
		return this.modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getSubmodel() {
		return this.submodel;
	}

	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}

}
