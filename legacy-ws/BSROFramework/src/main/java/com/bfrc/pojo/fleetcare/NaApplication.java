package com.bfrc.pojo.fleetcare;

import java.util.Date;


public class NaApplication implements java.io.Serializable {

	// Fields

	private Long id;
	private String companyName;
	private String parentCompanyName;
	private String companyAddress1;
	private String companyAddress2;
	private String companyCity;
	private String companyState;
	private String companyZipCode;
	private String companyPhoneNumber;
	private String companyFaxNumber;
	private String natureOfBusiness;
	private String applcntFirstName;
	private String applcntLastName;
	private String applcntMiddleInitial;
	private String applcntPhoneNumber;
	private String applcntFaxNumber;
	private String applcntEmail;
	private Long numVehiclesPassenger;
	private Long numVehiclesLighttruck;
	private Long numVehiclesOther;
	private String vehiclesOtherDesc;
	private Double estAnnualPurchasesTires;
	private Double estAnnualPurchasesServices;
	private String purchasingMngmntType;
	private String purchasingContactIsApplcnt;
	private String purchasingContactName;
	private String purchasingContactPhone;
	private String otherNationalAccntDesc;
	private Date createdDate;
	private boolean otherAccounts;

	// Constructors

	/** default constructor */
	public NaApplication() {
	}

	/** minimal constructor */
	public NaApplication(Long id, String companyName,
			String companyAddress1, String companyCity, String companyState,
			String companyZipCode, String companyPhoneNumber,
			String natureOfBusiness, String applcntFirstName,
			String applcntLastName, String applcntPhoneNumber,
			String applcntEmail, Long numVehiclesPassenger,
			Long numVehiclesLighttruck, Long numVehiclesOther,
			Double estAnnualPurchasesTires, Double estAnnualPurchasesServices,
			String purchasingMngmntType, String purchasingContactIsApplcnt,
			Date createdDate) {
		this.id = id;
		this.companyName = companyName;
		this.companyAddress1 = companyAddress1;
		this.companyCity = companyCity;
		this.companyState = companyState;
		this.companyZipCode = companyZipCode;
		this.companyPhoneNumber = companyPhoneNumber;
		this.natureOfBusiness = natureOfBusiness;
		this.applcntFirstName = applcntFirstName;
		this.applcntLastName = applcntLastName;
		this.applcntPhoneNumber = applcntPhoneNumber;
		this.applcntEmail = applcntEmail;
		this.numVehiclesPassenger = numVehiclesPassenger;
		this.numVehiclesLighttruck = numVehiclesLighttruck;
		this.numVehiclesOther = numVehiclesOther;
		this.estAnnualPurchasesTires = estAnnualPurchasesTires;
		this.estAnnualPurchasesServices = estAnnualPurchasesServices;
		this.purchasingMngmntType = purchasingMngmntType;
		this.purchasingContactIsApplcnt = purchasingContactIsApplcnt;
		this.createdDate = createdDate;
	}

	/** full constructor */
	public NaApplication(Long id, String companyName,
			String parentCompanyName, String companyAddress1,
			String companyAddress2, String companyCity, String companyState,
			String companyZipCode, String companyPhoneNumber,
			String companyFaxNumber, String natureOfBusiness,
			String applcntFirstName, String applcntLastName,
			String applcntMiddleInitial, String applcntPhoneNumber,
			String applcntFaxNumber, String applcntEmail,
			Long numVehiclesPassenger, Long numVehiclesLighttruck,
			Long numVehiclesOther, String vehiclesOtherDesc,
			Double estAnnualPurchasesTires, Double estAnnualPurchasesServices,
			String purchasingMngmntType, String purchasingContactIsApplcnt,
			String purchasingContactName, String purchasingContactPhone,
			String otherNationalAccntDesc, Date createdDate) {
		this.id = id;
		this.companyName = companyName;
		this.parentCompanyName = parentCompanyName;
		this.companyAddress1 = companyAddress1;
		this.companyAddress2 = companyAddress2;
		this.companyCity = companyCity;
		this.companyState = companyState;
		this.companyZipCode = companyZipCode;
		this.companyPhoneNumber = companyPhoneNumber;
		this.companyFaxNumber = companyFaxNumber;
		this.natureOfBusiness = natureOfBusiness;
		this.applcntFirstName = applcntFirstName;
		this.applcntLastName = applcntLastName;
		this.applcntMiddleInitial = applcntMiddleInitial;
		this.applcntPhoneNumber = applcntPhoneNumber;
		this.applcntFaxNumber = applcntFaxNumber;
		this.applcntEmail = applcntEmail;
		this.numVehiclesPassenger = numVehiclesPassenger;
		this.numVehiclesLighttruck = numVehiclesLighttruck;
		this.numVehiclesOther = numVehiclesOther;
		this.vehiclesOtherDesc = vehiclesOtherDesc;
		this.estAnnualPurchasesTires = estAnnualPurchasesTires;
		this.estAnnualPurchasesServices = estAnnualPurchasesServices;
		this.purchasingMngmntType = purchasingMngmntType;
		this.purchasingContactIsApplcnt = purchasingContactIsApplcnt;
		this.purchasingContactName = purchasingContactName;
		this.purchasingContactPhone = purchasingContactPhone;
		this.otherNationalAccntDesc = otherNationalAccntDesc;
		this.createdDate = createdDate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getParentCompanyName() {
		return this.parentCompanyName;
	}

	public void setParentCompanyName(String parentCompanyName) {
		this.parentCompanyName = parentCompanyName;
	}

	public String getCompanyAddress1() {
		return this.companyAddress1;
	}

	public void setCompanyAddress1(String companyAddress1) {
		this.companyAddress1 = companyAddress1;
	}

	public String getCompanyAddress2() {
		return this.companyAddress2;
	}

	public void setCompanyAddress2(String companyAddress2) {
		this.companyAddress2 = companyAddress2;
	}

	public String getCompanyCity() {
		return this.companyCity;
	}

	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}

	public String getCompanyState() {
		return this.companyState;
	}

	public void setCompanyState(String companyState) {
		this.companyState = companyState;
	}

	public String getCompanyZipCode() {
		return this.companyZipCode;
	}

	public void setCompanyZipCode(String companyZipCode) {
		this.companyZipCode = companyZipCode;
	}

	public String getCompanyPhoneNumber() {
		return this.companyPhoneNumber;
	}

	public void setCompanyPhoneNumber(String companyPhoneNumber) {
		this.companyPhoneNumber = companyPhoneNumber;
	}

	public String getCompanyFaxNumber() {
		return this.companyFaxNumber;
	}

	public void setCompanyFaxNumber(String companyFaxNumber) {
		this.companyFaxNumber = companyFaxNumber;
	}

	public String getNatureOfBusiness() {
		return this.natureOfBusiness;
	}

	public void setNatureOfBusiness(String natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	public String getApplcntFirstName() {
		return this.applcntFirstName;
	}

	public void setApplcntFirstName(String applcntFirstName) {
		this.applcntFirstName = applcntFirstName;
	}

	public String getApplcntLastName() {
		return this.applcntLastName;
	}

	public void setApplcntLastName(String applcntLastName) {
		this.applcntLastName = applcntLastName;
	}

	public String getApplcntMiddleInitial() {
		return this.applcntMiddleInitial;
	}

	public void setApplcntMiddleInitial(String applcntMiddleInitial) {
		this.applcntMiddleInitial = applcntMiddleInitial;
	}

	public String getApplcntPhoneNumber() {
		return this.applcntPhoneNumber;
	}

	public void setApplcntPhoneNumber(String applcntPhoneNumber) {
		this.applcntPhoneNumber = applcntPhoneNumber;
	}

	public String getApplcntFaxNumber() {
		return this.applcntFaxNumber;
	}

	public void setApplcntFaxNumber(String applcntFaxNumber) {
		this.applcntFaxNumber = applcntFaxNumber;
	}

	public String getApplcntEmail() {
		return this.applcntEmail;
	}

	public void setApplcntEmail(String applcntEmail) {
		this.applcntEmail = applcntEmail;
	}

	public Long getNumVehiclesPassenger() {
		return this.numVehiclesPassenger;
	}

	public void setNumVehiclesPassenger(Long numVehiclesPassenger) {
		this.numVehiclesPassenger = numVehiclesPassenger;
	}

	public Long getNumVehiclesLighttruck() {
		return this.numVehiclesLighttruck;
	}

	public void setNumVehiclesLighttruck(Long numVehiclesLighttruck) {
		this.numVehiclesLighttruck = numVehiclesLighttruck;
	}

	public Long getNumVehiclesOther() {
		return this.numVehiclesOther;
	}

	public void setNumVehiclesOther(Long numVehiclesOther) {
		this.numVehiclesOther = numVehiclesOther;
	}

	public String getVehiclesOtherDesc() {
		return this.vehiclesOtherDesc;
	}

	public void setVehiclesOtherDesc(String vehiclesOtherDesc) {
		this.vehiclesOtherDesc = vehiclesOtherDesc;
	}

	public Double getEstAnnualPurchasesTires() {
		return this.estAnnualPurchasesTires;
	}

	public void setEstAnnualPurchasesTires(Double estAnnualPurchasesTires) {
		this.estAnnualPurchasesTires = estAnnualPurchasesTires;
	}

	public Double getEstAnnualPurchasesServices() {
		return this.estAnnualPurchasesServices;
	}

	public void setEstAnnualPurchasesServices(Double estAnnualPurchasesServices) {
		this.estAnnualPurchasesServices = estAnnualPurchasesServices;
	}

	public String getPurchasingMngmntType() {
		return this.purchasingMngmntType;
	}

	public void setPurchasingMngmntType(String purchasingMngmntType) {
		this.purchasingMngmntType = purchasingMngmntType;
	}

	public String getPurchasingContactIsApplcnt() {
		return this.purchasingContactIsApplcnt;
	}

	public void setPurchasingContactIsApplcnt(String purchasingContactIsApplcnt) {
		this.purchasingContactIsApplcnt = purchasingContactIsApplcnt;
	}

	public String getPurchasingContactName() {
		return this.purchasingContactName;
	}

	public void setPurchasingContactName(String purchasingContactName) {
		this.purchasingContactName = purchasingContactName;
	}

	public String getPurchasingContactPhone() {
		return this.purchasingContactPhone;
	}

	public void setPurchasingContactPhone(String purchasingContactPhone) {
		this.purchasingContactPhone = purchasingContactPhone;
	}

	public String getOtherNationalAccntDesc() {
		return this.otherNationalAccntDesc;
	}

	public void setOtherNationalAccntDesc(String otherNationalAccntDesc) {
		this.otherNationalAccntDesc = otherNationalAccntDesc;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isOtherAccounts() {
		return otherAccounts;
	}

	public void setOtherAccounts(boolean otherAccounts) {
		this.otherAccounts = otherAccounts;
	}

}