package com.bfrc.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bfrc.Bean;
import com.bfrc.security.Encode;

public class UserVehicle implements Bean {
	private static SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");

	private long id;
	private long acesVehicleId;
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
	private User user;
	private String tiresReplaced;
	
	public String toString() {
		 StringBuffer sb = new StringBuffer("ID: " + id + "\nAces Vehicle ID: " + acesVehicleId + "\nActive: " + active + "\n");
		 if(year != null)
			 sb.append("Year: " + year + "\n");
		 if(make != null)
			 sb.append("Make: " + make + "\n");
		 if(model != null)
			 sb.append("Model: " + model + "\n");
		 if(submodel != null)
			 sb.append("Submodel: " + submodel + "\n");
		 if(annualMileage != null)
			 sb.append("Annual Mileage: " + annualMileage + "\n");
		 if(currentMileage != null)
			 sb.append("Current Mileage: " + currentMileage + "\n");
		 if(createdDate != null)
			 sb.append("Created Date: " + sdf.format(createdDate) + "\n");
		 if(mileageDate != null)
			 sb.append("Mileage Date: " + sdf.format(mileageDate) + "\n");
		 if(tiresReplaced != null)
			 sb.append("Tires Replaced: " + tiresReplaced + "\n");
		 if(user != null)
			 sb.append("User ID: " + user.getId() + "\n");
		return sb.toString();
	}
	
	public String getTiresReplaced() {
		return tiresReplaced;
	}

	public void setTiresReplaced(String tiresReplaced) {
		this.tiresReplaced = tiresReplaced;
	}

	public String getDescription() {
		return this.year + " " + this.make + " " + this.model + " " + this.submodel;
	}
	
	public boolean isActive() {
		return this.active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Integer getAnnualMileage() {
		return this.annualMileage;
	}
	public void setAnnualMileage(Integer annualMileage) {
		this.annualMileage = annualMileage;
	}

	public long getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCurrentMileage() {
		return this.currentMileage;
	}
	public void setCurrentMileage(Integer currentMileage) {
		this.currentMileage = currentMileage;
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMake() {
		return this.make;
	}
	public void setMake(String make) {
		this.make = Encode.encodeForJavaScript(make);
	}
	public Date getMileageDate() {
		return this.mileageDate;
	}
	public void setMileageDate(Date mileageDate) {
		this.mileageDate = mileageDate;
	}
	public String getModel() {
		return this.model;
	}
	public void setModel(String model) {
		this.model = Encode.encodeForJavaScript(model);
	}
	public String getOriginalTires() {
		return this.originalTires;
	}
	public void setOriginalTires(String originalTires) {
		this.originalTires = originalTires;
	}
	public String getSubmodel() {
		return this.submodel;
	}
	public void setSubmodel(String submodel) {
		this.submodel = Encode.encodeForJavaScript(submodel);
	}
	public User getUser() {
		return this.user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getYear() {
		return this.year;
	}
	public void setYear(String year) {
		this.year = Encode.encodeForJavaScript(year);
	}
}
