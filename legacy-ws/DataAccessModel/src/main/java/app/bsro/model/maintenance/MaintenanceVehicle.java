package app.bsro.model.maintenance;

import java.util.List;

import app.bsro.model.scheduled.maintenance.MaintenanceInvoice;

public class MaintenanceVehicle implements Comparable<MaintenanceVehicle>{

	private Integer customerId;
	private Integer vehicleId;
	private String year;
	private String make;
	private String model;
	private String subModel;
	
	private boolean yearMatch;
	private boolean modelMatch;
	
	private List<MaintenanceInvoice> invoices;
	
	public MaintenanceVehicle(){ super(); }

	public MaintenanceVehicle(Integer customerId, Integer vehicleId,
			String year, String make, String model, String subModel) {
		super();
		this.customerId = customerId;
		this.vehicleId = vehicleId;
		this.year = year;
		this.make = make;
		this.model = model;
		this.subModel = subModel;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSubModel() {
		return subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}
	
	public void setYearMatch(boolean yearMatch) {
		this.yearMatch = yearMatch;
	}
	
	public boolean isYearMatch() {
		return yearMatch;
	}
	
	public void setModelMatch(boolean modelMatch) {
		this.modelMatch = modelMatch;
	}
	public boolean isModelMatch() {
		return modelMatch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((make == null) ? 0 : make.hashCode());
		return result;
	}
	
	public void setInvoices(List<MaintenanceInvoice> invoices) {
		this.invoices = invoices;
	}
	public List<MaintenanceInvoice> getInvoices() {
		return invoices;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaintenanceVehicle other = (MaintenanceVehicle) obj;
		if (make == null) {
			if (other.make != null)
				return false;
		} else if (!make.equalsIgnoreCase(other.make))
			return false;
		return true;
	}
	
	public int compareTo(MaintenanceVehicle mv) {
		Boolean thisMatch = new Boolean(this.modelMatch);
		Boolean thatMatch = new Boolean(mv.modelMatch);
		
		return thisMatch.compareTo(thatMatch);
	}

	@Override
	public String toString() {
		return "MaintenanceVehicle [customerId=" + customerId + ", vehicleId="
				+ vehicleId + ", year=" + year + ", make=" + make + ", model="
				+ model + ", subModel=" + subModel + ", yearMatch=" + yearMatch
				+ ", modelMatch=" + modelMatch + "]";
	}
	
}
