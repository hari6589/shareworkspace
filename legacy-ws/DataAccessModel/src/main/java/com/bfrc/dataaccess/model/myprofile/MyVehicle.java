/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.bfrc.dataaccess.model.aces.Vehicle;
import com.bfrc.dataaccess.model.aces.VehicleConfiguration;
import com.bfrc.dataaccess.model.appointment.Appointment;
import com.bfrc.dataaccess.model.vehicle.Fitment;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"driver", "gasFillUps", 
	"appointments", "maintenanceServicePerformed", "vehicleHistory", "vehicleConfiguration"})
@JsonPropertyOrder({"id","activeFlag", "name","vinNumber","drivingCondition", "lastModifiedDate", 
	"odometer", "mpgCity", "mpgHighway", "mpgTotal", "mileageDefault","purchaseDate", "primaryVehicle",
	"notes", "vehicle"})
public class MyVehicle {

	private Long myVehicleId;

	private Fitment fitment;
	
	private VehicleConfiguration vehicleConfiguration;
	
	private MyDriver driver;
	
	private String name;
	
	private String drivingCondition;
	
	private Integer odometer;
	
	private Integer mpgCity;
	
	private Integer mpgHighway;
	
	private Integer mpgTotal;
	
	private Integer mileageDefault;
	
	private Date purchaseDate;
	
	private String vinNumber;

	private String notes;
	
	private Date lastModifiedDate;
	
	private String activeFlag;
	
	private Integer primaryVehicle;
	
	private Set<Appointment> appointments = new HashSet<Appointment>();

	private List<MyMaintenanceServicePerformed> maintenanceServicePerformed = new ArrayList<MyMaintenanceServicePerformed>();

	private List<MyVehicleGas> gasFillUps = new ArrayList<MyVehicleGas>();

	private List<MyServiceHistoryVehicle> vehicleHistory = new ArrayList<MyServiceHistoryVehicle>();
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy-HH:mm");
	
	public MyVehicle(){
	}
	

	/**
	 * 
	 * @return
	 */
	public MyDriver getDriver() {
		return driver;
	}


	public void setDriver(MyDriver driver) {
		this.driver = driver;
	}


	/**
	 * @return the myVehicleId
	 */
	@JsonProperty("id")
	public Long getMyVehicleId() {
		return myVehicleId;
	}

	/**
	 * @param myVehicleId the myVehicleId to set
	 */
	@JsonProperty("id")
	public void setMyVehicleId(Long myVehicleId) {
		this.myVehicleId = myVehicleId;
	}
	
	

	public Fitment getFitment() {
		return fitment;
	}


	public void setFitment(Fitment fitment) {
		this.fitment = fitment;
	}


	public VehicleConfiguration getVehicleConfiguration() {
		return vehicleConfiguration;
	}


	public void setVehicleConfiguration(VehicleConfiguration vehicleConfiguration) {
		this.vehicleConfiguration = vehicleConfiguration;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the drivingCondition
	 */
	public String getDrivingCondition() {
		return drivingCondition;
	}

	/**
	 * @param drivingCondition the drivingCondition to set
	 */
	public void setDrivingCondition(String drivingCondition) {
		this.drivingCondition = drivingCondition;
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
	 * @return the mpgCity
	 */
	public Integer getMpgCity() {
		return mpgCity;
	}

	/**
	 * @param mpgCity the mpgCity to set
	 */
	public void setMpgCity(Integer mpgCity) {
		this.mpgCity = mpgCity;
	}

	/**
	 * @return the mpgHighway
	 */
	public Integer getMpgHighway() {
		return mpgHighway;
	}

	/**
	 * @param mpgHighway the mpgHighway to set
	 */
	public void setMpgHighway(Integer mpgHighway) {
		this.mpgHighway = mpgHighway;
	}

	/**
	 * @return the mpgTotal
	 */
	public Integer getMpgTotal() {
		return mpgTotal;
	}

	/**
	 * @param mpgTotal the mpgTotal to set
	 */
	public void setMpgTotal(Integer mpgTotal) {
		this.mpgTotal = mpgTotal;
	}

	/**
	 * @return the mileageDefault
	 */
	public Integer getMileageDefault() {
		return mileageDefault;
	}

	/**
	 * @param mileageDefault the mileageDefault to set
	 */
	public void setMileageDefault(Integer mileageDefault) {
		this.mileageDefault = mileageDefault;
	}

	/**
	 * @return the purchaseDate
	 */
	@JsonIgnore
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate the purchaseDate to set
	 */
	@JsonIgnore
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	@JsonProperty("purchaseDate")
	public String getPurchaseDateStr(){
		if(purchaseDate !=null)
		{
		return dateFormat.format(purchaseDate);
		}
		else
			return null;
	}
	
	@JsonProperty("purchaseDate")
	public void setPurchaseDateStr(String purchaseDate){
		Date formattedPurchaseDate = null;
		try {
			if(purchaseDate !=null)
			{
			formattedPurchaseDate = dateFormat.parse(purchaseDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.purchaseDate = formattedPurchaseDate;
	}

	/**
	 * @return the vinNumber
	 */
	public String getVinNumber() {
		return vinNumber;
	}

	/**
	 * @param vinNumber the vinNumber to set
	 */
	public void setVinNumber(String vinNumber) {
		this.vinNumber = vinNumber;
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
	

	/**
	 * @return the activeFlag
	 */
	public String getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	
	public Integer getPrimaryVehicle() {
		return primaryVehicle;
	}


	public void setPrimaryVehicle(Integer primaryVehicle) {
		this.primaryVehicle = primaryVehicle;
	}


	/**
	 * @return the appointments
	 */
	public Set<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * @param appointments the appointments to set
	 */
	public void setAppointments(Set<Appointment> appointments) {
		this.appointments = appointments;
	}


	/**
	 * @return the maintenanceServicePerformed
	 */
	public List<MyMaintenanceServicePerformed> getMaintenanceServicePerformed() {
		return maintenanceServicePerformed;
	}

	/**
	 * @param maintenanceServicePerformed the maintenanceServicePerformed to set
	 */
	public void setMaintenanceServicePerformed(
			List<MyMaintenanceServicePerformed> maintenanceServicePerformed) {
		this.maintenanceServicePerformed = maintenanceServicePerformed;
	}

	/**
	 * @return the gasFillUps
	 */
	public List<MyVehicleGas> getGasFillUps() {
		return gasFillUps;
	}

	/**
	 * @param gasFillUps the gasFillUps to set
	 */
	public void setGasFillUps(List<MyVehicleGas> gasFillUps) {
		this.gasFillUps = gasFillUps;
	}

	/**
	 * @return the vehicleHistory
	 */
	public List<MyServiceHistoryVehicle> getVehicleHistory() {
		return vehicleHistory;
	}

	/**
	 * @param vehicleHistory the vehicleHistory to set
	 */
	public void setVehicleHistory(List<MyServiceHistoryVehicle> vehicleHistory) {
		this.vehicleHistory = vehicleHistory;
	}


	@Override
	public String toString() {
		return "MyVehicle [myVehicleId=" + myVehicleId + ", name=" + name
				+ ", drivingCondition=" + drivingCondition + ", odometer="
				+ odometer + ", mpgCity=" + mpgCity + ", mpgHighway="
				+ mpgHighway + ", mpgTotal=" + mpgTotal + ", mileageDefault="
				+ mileageDefault + ", purchaseDate=" + purchaseDate
				+ ", vinNumber=" + vinNumber + ", notes=" + notes
				+ ", lastModifiedDate=" + lastModifiedDate + ", activeFlag="
				+ activeFlag + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activeFlag == null) ? 0 : activeFlag.hashCode());
		result = prime
				* result
				+ ((drivingCondition == null) ? 0 : drivingCondition.hashCode());
		result = prime * result
				+ ((mileageDefault == null) ? 0 : mileageDefault.hashCode());
		result = prime * result + ((mpgCity == null) ? 0 : mpgCity.hashCode());
		result = prime * result
				+ ((mpgHighway == null) ? 0 : mpgHighway.hashCode());
		result = prime * result
				+ ((mpgTotal == null) ? 0 : mpgTotal.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
				+ ((odometer == null) ? 0 : odometer.hashCode());
		result = prime * result
				+ ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
		result = prime * result
				+ ((vinNumber == null) ? 0 : vinNumber.hashCode());
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
		MyVehicle other = (MyVehicle) obj;
		if (activeFlag == null) {
			if (other.activeFlag != null)
				return false;
		} else if (!activeFlag.equals(other.activeFlag))
			return false;
		if (drivingCondition == null) {
			if (other.drivingCondition != null)
				return false;
		} else if (!drivingCondition.equals(other.drivingCondition))
			return false;
		if (mileageDefault == null) {
			if (other.mileageDefault != null)
				return false;
		} else if (!mileageDefault.equals(other.mileageDefault))
			return false;
		if (mpgCity == null) {
			if (other.mpgCity != null)
				return false;
		} else if (!mpgCity.equals(other.mpgCity))
			return false;
		if (mpgHighway == null) {
			if (other.mpgHighway != null)
				return false;
		} else if (!mpgHighway.equals(other.mpgHighway))
			return false;
		if (mpgTotal == null) {
			if (other.mpgTotal != null)
				return false;
		} else if (!mpgTotal.equals(other.mpgTotal))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		if (purchaseDate == null) {
			if (other.purchaseDate != null)
				return false;
		} else if (!purchaseDate.equals(other.purchaseDate))
			return false;
		if (vinNumber == null) {
			if (other.vinNumber != null)
				return false;
		} else if (!vinNumber.equals(other.vinNumber))
			return false;
		return true;
	}


	
}
