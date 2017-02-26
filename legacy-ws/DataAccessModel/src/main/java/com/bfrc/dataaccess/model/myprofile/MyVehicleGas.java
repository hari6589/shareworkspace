/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"myVehicle","lastModifiedDate", "fillupDate"})
@JsonPropertyOrder({"id", "fillUpDate","price","gallons","odometer","fuelType","notes"})
public class MyVehicleGas  {

	private Long myVehicleGasId;
	
	private MyVehicle myVehicle;

	@Temporal(TemporalType.TIME)
	private Calendar fillupDate;

	private BigDecimal price;

	private BigDecimal gallons;

	private Integer odometer;

	private FuelType fuelType;

	private String notes;
	
	private Date lastModifiedDate;
	
	private SimpleDateFormat dformat = new SimpleDateFormat("MMddyyyy-HH:mm");

	public MyVehicleGas() {
	}
	/**
	 * @param myVehicle
	 * @param fillUpDate
	 * @param price
	 * @param gallons
	 */
	public MyVehicleGas(MyVehicle myVehicle, Calendar fillUpDate, BigDecimal price,
			BigDecimal gallons) {
		this.myVehicle = myVehicle;
		this.fillupDate = fillUpDate;
		this.price = price;
		this.gallons = gallons;
		this.lastModifiedDate = new Date();
	}
	/**
	 * @param myVehicle
	 * @param fillUpDate
	 * @param price
	 * @param gallons
	 * @param odometer
	 * @param fuelType
	 */
	public MyVehicleGas(MyVehicle myVehicle, Calendar fillUpDate, BigDecimal price,
			BigDecimal gallons, Integer odometer, FuelType fuelType) {
		this.myVehicle = myVehicle;
		this.fillupDate = fillUpDate;
		this.price = price;
		this.gallons = gallons;
		this.odometer = odometer;
		this.fuelType = fuelType;
		this.lastModifiedDate = new Date();
	}
	/**
	 * @return the myVehicleGasId
	 */
	@JsonProperty("id")
	public Long getMyVehicleGasId() {
		return myVehicleGasId;
	}
	/**
	 * @param myVehicleGasId the myVehicleGasId to set
	 */
	@JsonProperty("id")
	public void setMyVehicleGasId(Long myVehicleGasId) {
		this.myVehicleGasId = myVehicleGasId;
	}
	/**
	 * @return the myVehicle
	 */
	public MyVehicle getMyVehicle() {
		return myVehicle;
	}
	/**
	 * @param myVehicle the myVehicle to set
	 */
	public void setMyVehicle(MyVehicle myVehicle) {
		this.myVehicle = myVehicle;
	}
	
	/**
	 * @return the fillupDate
	 */
	public Calendar getFillupDate() {
		return fillupDate;
	}
	/**
	 * @param fillupDate the fillupDate to set
	 */
	public void setFillupDate(Calendar fillupDate) {
		this.fillupDate = fillupDate;
	}
	/**
	 * @return the fillupDateStr
	 */
	@JsonProperty("fillupDateTime")
	public String getFillupDateStr() {	
		String fillupDateStr = dformat.format(fillupDate.getTime());
		return fillupDateStr;
	}
	/**
	 * @param fillupDateStr the fillupDateStr to set
	 */
	@JsonProperty("fillupDateTime")
	public void setFillupDateStr(String fillupDateStr) {
		Date date=null;
		try {
			date = dformat.parse(fillupDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.fillupDate = cal;
	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	/**
	 * @return the gallons
	 */
	public BigDecimal getGallons() {
		return gallons;
	}
	/**
	 * @param gallons the gallons to set
	 */
	public void setGallons(BigDecimal gallons) {
		this.gallons = gallons;
	}
	/**
	 * @return the odometer
	 */
	public Integer getOdometer() {
		return odometer;
	}
	/**
	 * @param odometer the odometer to set
	 */
	public void setOdometer(Integer odometer) {
		this.odometer = odometer;
	}
	/**
	 * @return the fuelType
	 */
	public FuelType getFuelType() {
		return fuelType;
	}
	/**
	 * @param fuelType the fuelType to set
	 */
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the lastModifiedDate
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MyVehicleGas [myVehicleGasId=" + myVehicleGasId
				+ ", fillUpDate=" + fillupDate
				+ ", price=" + price + ", gallons=" + gallons + ", odometer="
				+ odometer + ", fuelType=" + fuelType + ", notes=" + notes
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fillupDate == null) ? 0 : fillupDate.hashCode());
		result = prime * result
				+ ((fuelType == null) ? 0 : fuelType.hashCode());
		result = prime * result + ((gallons == null) ? 0 : gallons.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
				+ ((odometer == null) ? 0 : odometer.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		MyVehicleGas other = (MyVehicleGas) obj;
		if (fillupDate == null) {
			if (other.fillupDate != null)
				return false;
		} else if (!fillupDate.equals(other.fillupDate))
			return false;
		if (fuelType != other.fuelType)
			return false;
		if (gallons == null) {
			if (other.gallons != null)
				return false;
		} else if (!gallons.equals(other.gallons))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (odometer == null) {
			if (other.odometer != null)
				return false;
		} else if (!odometer.equals(other.odometer))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}
	

}
