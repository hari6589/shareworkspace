package app.bsro.model.gas.stations;

import java.io.Serializable;
import java.util.Date;

public class StationPrice implements Serializable {

	private static final long serialVersionUID = 3096101841510497034L;
	private String stationName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String brandName;
	
	private double unleadedPrice;
	private Date unleadedDate;
	
	private double midGradePrice;
	private Date midGradeDate;
	
	private double dieselPrice;
	private Date dieselDate;
	
	private double premiumPrice;
	private Date premiumDate;
	
	private double latitude;
	private double longitude;
	private String phone;
	private double distance;
	
	private String stationIdentifier;
	
	public StationPrice(){}

	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public double getUnleadedPrice() {
		return unleadedPrice;
	}
	public void setUnleadedPrice(double price) {
		this.unleadedPrice = price;
	}

	public Date getUnleadedDate() {
		return unleadedDate;
	}
	public void setUnleadedDate(Date date) {
		this.unleadedDate = date;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getDistance() {
		return distance;
	}
	
	public void setDieselDate(Date dieselDate) {
		this.dieselDate = dieselDate;
	}
	public void setDieselPrice(double dieselPrice) {
		this.dieselPrice = dieselPrice;
	}
	public void setMidGradeDate(Date midGradeDate) {
		this.midGradeDate = midGradeDate;
	}
	public void setMidGradePrice(double midGradePrice) {
		this.midGradePrice = midGradePrice;
	}
	public void setPremiumDate(Date premiumDate) {
		this.premiumDate = premiumDate;
	}
	public void setPremiumPrice(double premiumPrice) {
		this.premiumPrice = premiumPrice;
	}
	public Date getDieselDate() {
		return dieselDate;
	}
	public double getDieselPrice() {
		return dieselPrice;
	}
	public Date getMidGradeDate() {
		return midGradeDate;
	}
	public double getMidGradePrice() {
		return midGradePrice;
	}
	public Date getPremiumDate() {
		return premiumDate;
	}
	public double getPremiumPrice() {
		return premiumPrice;
	}

	public String getStationIdentifier() {
		return stationIdentifier;
	}
	public void setStationIdentifier(String stationIdentifier) {
		this.stationIdentifier = stationIdentifier;
	}

}
