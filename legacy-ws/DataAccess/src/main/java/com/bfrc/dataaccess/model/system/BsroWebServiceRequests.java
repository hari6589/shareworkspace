package com.bfrc.dataaccess.model.system;

import javax.xml.bind.annotation.XmlRootElement;

import com.bfrc.dataaccess.svc.webdb.system.WebServiceRequestServiceImpl.WebServiceId;

@XmlRootElement
public class BsroWebServiceRequests {

	private Integer webServiceId;
	private String webServiceCode;
	private String webServiceMessage;
	
	public Integer getWebServiceId() {
		return webServiceId;
	}
	
	public void setWebServiceType(WebServiceId id) {
		
		this.webServiceId = id.getId();
	}
	public void setWebServiceId(Integer webServiceId) {
		this.webServiceId = webServiceId;
	}
	public String getWebServiceCode() {
		return webServiceCode;
	}
	public void setWebServiceCode(String webServiceCode) {
		this.webServiceCode = webServiceCode;
	}
	public String getWebServiceMessage() {
		return webServiceMessage;
	}
	public void setWebServiceMessage(String webServiceMessage) {
		this.webServiceMessage = webServiceMessage;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((webServiceId == null) ? 0 : webServiceId.hashCode());
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
		BsroWebServiceRequests other = (BsroWebServiceRequests) obj;
		if (webServiceId == null) {
			if (other.webServiceId != null)
				return false;
		} else if (!webServiceId.equals(other.webServiceId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BsroWebServiceRequests [webServiceId=" + webServiceId
				+ ", webServiceCode=" + webServiceCode + ", webServiceMessage="
				+ webServiceMessage + "]";
	}
	
	
}
