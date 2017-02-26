package com.bfrc.dataaccess.model.vehicle;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

public class UserVehicle implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long acesVehicleId;
	private String year;
	private String make;
	private String model;
	private String submodel;
	private String originalTires;
	private Integer annualMileage;
	private Integer currentMileage;
	private Date createdDate;
	private Date mileageDate;
	private boolean active;
//	private User user;
	private String tiresReplaced;
	
	private String name;
    private Boolean defaultFlag;
	
    private byte[] vehicleImage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
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

	public String getSubmodel() {
		return submodel;
	}

	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}

	public String getOriginalTires() {
		return originalTires;
	}

	public void setOriginalTires(String originalTires) {
		this.originalTires = originalTires;
	}

	public Integer getAnnualMileage() {
		return annualMileage;
	}

	public void setAnnualMileage(Integer annualMileage) {
		this.annualMileage = annualMileage;
	}

	public Integer getCurrentMileage() {
		return currentMileage;
	}

	public void setCurrentMileage(Integer currentMileage) {
		this.currentMileage = currentMileage;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getMileageDate() {
		return mileageDate;
	}

	public void setMileageDate(Date mileageDate) {
		this.mileageDate = mileageDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTiresReplaced() {
		return tiresReplaced;
	}

	public void setTiresReplaced(String tiresReplaced) {
		this.tiresReplaced = tiresReplaced;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(Boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public byte[] getVehicleImage() {
		return vehicleImage;
	}

	public void setVehicleImage(byte[] vehicleImage) {
		this.vehicleImage = vehicleImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((annualMileage == null) ? 0 : annualMileage.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((currentMileage == null) ? 0 : currentMileage.hashCode());
		result = prime * result
				+ ((defaultFlag == null) ? 0 : defaultFlag.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((make == null) ? 0 : make.hashCode());
		result = prime * result
				+ ((mileageDate == null) ? 0 : mileageDate.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((originalTires == null) ? 0 : originalTires.hashCode());
		result = prime * result
				+ ((submodel == null) ? 0 : submodel.hashCode());
		result = prime * result
				+ ((tiresReplaced == null) ? 0 : tiresReplaced.hashCode());
		result = prime * result + Arrays.hashCode(vehicleImage);
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		UserVehicle other = (UserVehicle) obj;
		if (active != other.active)
			return false;
		if (annualMileage == null) {
			if (other.annualMileage != null)
				return false;
		} else if (!annualMileage.equals(other.annualMileage))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (currentMileage == null) {
			if (other.currentMileage != null)
				return false;
		} else if (!currentMileage.equals(other.currentMileage))
			return false;
		if (defaultFlag == null) {
			if (other.defaultFlag != null)
				return false;
		} else if (!defaultFlag.equals(other.defaultFlag))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (make == null) {
			if (other.make != null)
				return false;
		} else if (!make.equals(other.make))
			return false;
		if (mileageDate == null) {
			if (other.mileageDate != null)
				return false;
		} else if (!mileageDate.equals(other.mileageDate))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (originalTires == null) {
			if (other.originalTires != null)
				return false;
		} else if (!originalTires.equals(other.originalTires))
			return false;
		if (submodel == null) {
			if (other.submodel != null)
				return false;
		} else if (!submodel.equals(other.submodel))
			return false;
		if (tiresReplaced == null) {
			if (other.tiresReplaced != null)
				return false;
		} else if (!tiresReplaced.equals(other.tiresReplaced))
			return false;
		if (!Arrays.equals(vehicleImage, other.vehicleImage))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}


 
}
