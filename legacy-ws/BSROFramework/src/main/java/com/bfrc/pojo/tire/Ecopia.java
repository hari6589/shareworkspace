package com.bfrc.pojo.tire;
public class Ecopia implements java.io.Serializable {

	private String country = "";
	private String mileage = "";
	private String price = "";
	private String vehicleType = "";
	private String savings = "";

	public Ecopia(){
	}
	public Ecopia(String country, String mileage, String price, String vehicleType){
		this.country = country;
		this.mileage = mileage;
		this.price = price;
		this.vehicleType = vehicleType;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getSavings() {
		return savings;
	}
	public void setSavings(String savings) {
		this.savings = savings;
	}
}