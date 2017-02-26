package com.bfrc.dataaccess.model.reward;

import java.util.Date;

public class RewardsLog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tendigitcode;
	private Date loggedinDate;
	private String source;
	
	public RewardsLog(String tendigitcode, Date loggedinDate, String source){
		this.tendigitcode = tendigitcode;
		this.loggedinDate = loggedinDate;
		this.source = source;
	}
	
	public String getTendigitcode() {
		return tendigitcode;
	}

	public void setTendigitcode(String tendigitcode) {
		this.tendigitcode = tendigitcode;
	}

	public Date getLoggedinDate() {
		return loggedinDate;
	}

	public void setLoggedinDate(Date loggedinDate) {
		this.loggedinDate = loggedinDate;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
}
