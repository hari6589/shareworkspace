package com.bfrc.dataaccess.model.pricing;

import java.io.Serializable;

public class TpTaxStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private TpTaxStatusId id;
	private long  itaxCode;
	private long  geocode;
	private long  ntaxable;
	private String description;
	
	public TpTaxStatusId getId() {
		return this.id;
	}

	public void setId(TpTaxStatusId id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public long getNtaxable() {
		return this.ntaxable;
	}

	public void setNtaxable(long ntaxable) {
		this.ntaxable = ntaxable;
	}
	
	public long getItaxCode() {
		return this.itaxCode;
	}

	public void setItaxCode(long itaxCode) {
		this.itaxCode = itaxCode;
	}
	
	public long getGeocode() {
		return this.geocode;
	}

	public void setGeocode(long geocode) {
		this.geocode = geocode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TpTaxStatus other = (TpTaxStatus) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
