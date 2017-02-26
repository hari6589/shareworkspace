package com.bfrc.dataaccess.model.store;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class HrDistricts implements Serializable {

	private static final long serialVersionUID = 1L;
	private String districtId;
	private String districtZone;
	private String districtName;
	private Set<Store> stores = new HashSet<Store>();

	public HrDistricts() {
	}

	public HrDistricts(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictZone() {
		return this.districtZone;
	}

	public void setDistrictZone(String districtZone) {
		this.districtZone = districtZone;
	}

	public String getDistrictName() {
		return this.districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Set<Store> getStores() {
		return stores;
	}

	public void setStores(Set<Store> stores) {
		this.stores = stores;
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
		HrDistricts other = (HrDistricts) obj;
		if (districtId == null) {
			if (other.districtId != null)
				return false;
		} else if (!districtId.equals(other.districtId))
			return false;
		return true;
	}

}