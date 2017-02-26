package com.bfrc.dataaccess.model.maintenance;

import java.util.Date;

public class MaintSignup implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private long maintSignupId;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String emailAddress;
	private String password;
	private String passwordHint;
	private long acesVehicleId;
	private String submodel;
	private long initMileage;
	private Long annualMileage;
	private Character dutySchedule;
	private Character originalTires;
	private Byte tiresReplaced;
	private char emailReminder;
	private Date createdDate;
	private String source;

	public MaintSignup() {}

	public MaintSignup(long maintSignupId, String firstName, String lastName,
			String address1, String city, String state, String zip,
			String emailAddress, long acesVehicleId, long initMileage,
			char emailReminder, Date createdDate) {
		this.maintSignupId = maintSignupId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address1 = address1;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.emailAddress = emailAddress;
		this.acesVehicleId = acesVehicleId;
		this.initMileage = initMileage;
		this.emailReminder = emailReminder;
		this.createdDate = createdDate;
	}

	public MaintSignup(long maintSignupId, String firstName,
			String middleInitial, String lastName, String address1,
			String address2, String city, String state, String zip,
			String emailAddress, String password, String passwordHint,
			long acesVehicleId, String submodel, long initMileage,
			Long annualMileage, Character dutySchedule,
			Character originalTires, Byte tiresReplaced, char emailReminder,
			Date createdDate, String source) {
		this.maintSignupId = maintSignupId;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.emailAddress = emailAddress;
		this.password = password;
		this.passwordHint = passwordHint;
		this.acesVehicleId = acesVehicleId;
		this.submodel = submodel;
		this.initMileage = initMileage;
		this.annualMileage = annualMileage;
		this.dutySchedule = dutySchedule;
		this.originalTires = originalTires;
		this.tiresReplaced = tiresReplaced;
		this.emailReminder = emailReminder;
		this.createdDate = createdDate;
		this.source = source;
	}
	
	public long getMaintSignupId() {
		return this.maintSignupId;
	}

	public void setMaintSignupId(long maintSignupId) {
		this.maintSignupId = maintSignupId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitial() {
		return this.middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordHint() {
		return this.passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}

	public long getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}

	public String getSubmodel() {
		return this.submodel;
	}

	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}

	public long getInitMileage() {
		return this.initMileage;
	}

	public void setInitMileage(long initMileage) {
		this.initMileage = initMileage;
	}

	public Long getAnnualMileage() {
		return this.annualMileage;
	}

	public void setAnnualMileage(Long annualMileage) {
		this.annualMileage = annualMileage;
	}

	public Character getDutySchedule() {
		return this.dutySchedule;
	}

	public void setDutySchedule(Character dutySchedule) {
		this.dutySchedule = dutySchedule;
	}

	public Character getOriginalTires() {
		return this.originalTires;
	}

	public void setOriginalTires(Character originalTires) {
		this.originalTires = originalTires;
	}

	public Byte getTiresReplaced() {
		return this.tiresReplaced;
	}

	public void setTiresReplaced(Byte tiresReplaced) {
		this.tiresReplaced = tiresReplaced;
	}

	public char getEmailReminder() {
		return this.emailReminder;
	}

	public void setEmailReminder(char emailReminder) {
		this.emailReminder = emailReminder;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
