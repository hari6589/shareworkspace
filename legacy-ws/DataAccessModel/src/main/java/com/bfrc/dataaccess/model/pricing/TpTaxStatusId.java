package com.bfrc.dataaccess.model.pricing;

import java.io.Serializable;

public class TpTaxStatusId implements Serializable {

	private static final long serialVersionUID = 1L;
	private long storeNumber;
	private long itaxCode;

	public TpTaxStatusId() {
	}

	public TpTaxStatusId(long storeNumber, long itaxCode) {
		this.storeNumber = storeNumber;
		this.itaxCode = itaxCode;
	}

	public long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public long getItaxCode() {
		return this.itaxCode;
	}

	public void setItaxCode(long itaxCode) {
		this.itaxCode = itaxCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TpTaxStatusId other = (TpTaxStatusId) obj;
		if (itaxCode != other.itaxCode)
			return false;
		if (storeNumber != other.storeNumber)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (itaxCode ^ (itaxCode >>> 32));
		result = prime * result + (int) (storeNumber ^ (storeNumber >>> 32));
		return result;
	}

}
