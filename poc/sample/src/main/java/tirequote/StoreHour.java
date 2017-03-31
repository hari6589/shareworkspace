package tirequote;

import java.io.Serializable;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonUnwrapped;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class StoreHour implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonUnwrapped
	private StoreHourId id;
	private String openTime;
	private String closeTime;
	private String timeZone;

	public StoreHour() {
	}

	public StoreHour(StoreHourId id) {
		this.id = id;
	}

	public StoreHourId getId() {
		return this.id;
	}

	public void setId(StoreHourId id) {
		this.id = id;
	}

	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return this.closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getTimeZone() {
		return this.timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
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
		StoreHour other = (StoreHour) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}