/**
 * 
 */
package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.mediacampaign.MediaCampaignParameters;

/**
 * @author smoorthy
 *
 */
public interface MediaCampaignDAO {
	List<MediaCampaignParameters> getAllCampaignParameters();
	List<String> getAllCampaignParameterKeys();
}
