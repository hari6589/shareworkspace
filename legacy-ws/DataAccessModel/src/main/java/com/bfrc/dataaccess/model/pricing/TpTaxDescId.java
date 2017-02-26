package com.bfrc.dataaccess.model.pricing;

import java.io.Serializable;


public class TpTaxDescId implements Serializable {

	private static final long serialVersionUID = 1L;
	private long storeNumber;
	private long counter;


	public TpTaxDescId() {
	}

	public TpTaxDescId(long storeNumber, long counter) {
		this.storeNumber = storeNumber;
		this.counter = counter;
	}


	public long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public long getCounter() {
		return this.counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (counter ^ (counter >>> 32));
		result = prime * result + (int) (storeNumber ^ (storeNumber >>> 32));
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
		TpTaxDescId other = (TpTaxDescId) obj;
		if (counter != other.counter)
			return false;
		if (storeNumber != other.storeNumber)
			return false;
		return true;
	}

}
