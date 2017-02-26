/**
 * 
 */
package com.bfrc.pojo.mediacampaign;

/**
 * @author smoorthy
 *
 */

public class MediaCampaignParameters {
	
	private Long mediaCampaignId;
	private String keys;
	private String description;
	
	public MediaCampaignParameters() {		
	}
	
	public MediaCampaignParameters(Long mediaCampaignId, String keys, String description) {
		this.mediaCampaignId = mediaCampaignId;
		this.keys = keys;
		this.description = description;
	}
	
	public Long getMediaCampaignId() {
		return mediaCampaignId;
	}
	
	public void setMediaCampaignId(Long mediaCampaignId) {
		this.mediaCampaignId = mediaCampaignId;
	}
	
	public String getKeys() {
		return keys;
	}
	
	public void setKeys(String keys) {
		this.keys = keys;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

}
