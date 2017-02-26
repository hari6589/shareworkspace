/**
 * 
 */
package com.bfrc.dataaccess.model.store;

import java.io.Serializable;

/**
 * @author smoorthy
 *
 */
public class StoreHierarchy implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String districtId;
	private String districtName;
	private String regionId;
	private String regionName;
	private String divisionId;
	private String divisionName;
	
	public StoreHierarchy() {		
	}
	
	public String getDistrictId() {
		return districtId;
	}
	
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	
	public String getDistrictName() {
		return districtName;
	}
	
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	public String getRegionId() {
		return regionId;
	}
	
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
	public String getRegionName() {
		return regionName;
	}
	
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public String getDivisionId() {
		return divisionId;
	}
	
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public String getDivisionName() {
		return divisionName;
	}
	
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((districtId == null) ? 0 : districtId.hashCode());
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
		StoreHierarchy other = (StoreHierarchy) obj;
		if (districtId == null) {
			if (other.districtId != null)
				return false;
		} else if (!districtId.equals(other.districtId))
			return false;
		return true;
	}

}
