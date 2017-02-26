package com.bfrc.dataaccess.model.maintenance;

public class Intervals implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private IntervalsId id;
	private String footnoteTxt;
	private Long timeInterval;
	private String timeUnits;
	private OperationTypes operationTypes;
	private Components components;

	public Intervals() {}

	public IntervalsId getId() {
		return id;
	}

	public void setId(IntervalsId id) {
		this.id = id;
	}

	public void setFootnoteTxt(String footnoteTxt) {
		this.footnoteTxt = footnoteTxt;
	}
	public void setTimeInterval(Long timeInterval) {
		this.timeInterval = timeInterval;
	}
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}
	public String getFootnoteTxt() {
		return footnoteTxt;
	}
	public Long getTimeInterval() {
		return timeInterval;
	}
	public String getTimeUnits() {
		return timeUnits;
	}
	public OperationTypes getOperationTypes() {
		return operationTypes;
	}

	public void setOperationTypes(OperationTypes operationTypes) {
		this.operationTypes = operationTypes;
	}

	public Components getComponents() {
		return components;
	}

	public void setComponents(Components components) {
		this.components = components;
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
		Intervals other = (Intervals) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	
}
