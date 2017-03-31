package tirequote;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class StoreHourId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long storeNumber;
	private String weekDay;

	@JsonIgnore
	public Long getStoreNumber() {
		return this.storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public String getWeekDay() {
		return this.weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((storeNumber == null) ? 0 : storeNumber.hashCode());
		result = prime * result + ((weekDay == null) ? 0 : weekDay.hashCode());
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
		StoreHourId other = (StoreHourId) obj;
		if (storeNumber == null) {
			if (other.storeNumber != null)
				return false;
		} else if (!storeNumber.equals(other.storeNumber))
			return false;
		if (weekDay == null) {
			if (other.weekDay != null)
				return false;
		} else if (!weekDay.equals(other.weekDay))
			return false;
		return true;
	}

}