package com.bfrc.dataaccess.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Authenticate {

	private String userId;
	private String tokenId;
	
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTokenId() {
		return tokenId;
	}
	public String getUserId() {
		return userId;
	}
	
}
