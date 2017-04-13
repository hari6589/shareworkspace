package com.bridgestone.bsro.aws.email.model.tire;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonAutoDetect(fieldVisibility=Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Store {
	private static final BigDecimal ECOMM_ACTIVE_FLAG_DEFAULT = new BigDecimal("0");
	
	private Long storeNumber;
	private String districtId;
	private String districtName;
	private String areaId;
	private String areaName;
	private String regionId;
	private String regionName;
	private String divisionId;
	private String divisionName;
	private String storeName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String trackingPhone;
	private String managerName;
	private String managerEmailAddress;
	private String storeType;
	private BigDecimal activeFlag;
	private String geoMatch;
	private Float latitude;
	private Float longitude;
	private String postalStatusCode;
	private BigDecimal onlineAppointmentActiveFlag;
	private BigDecimal tirePricingActiveFlag;
	private String fax;
	private boolean isMilitaryStore = false;
	private String reasonDesc;
	private String storeHour;
	private String localPageURL;
	private BigDecimal eCommActiveFlag;
	private Set<StoreHour> hours;
	private List<StoreHoliday> holidays;
	
	public Store() {
	}

	public Store(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public Store(Store s) {
		this.storeHour = s.getStoreHour();
		this.hours = s.getHours();
		this.storeNumber = s.getStoreNumber();
		this.districtId = s.getDistrictId();
		this.address = s.getAddress();
		this.city = s.getCity();
		this.state = s.getState();
		this.zip = s.getZip();
		this.phone = s.getPhone();
		this.managerName = s.getManagerName();
		this.managerEmailAddress = s.getManagerEmailAddress();
		this.storeType = s.getStoreType();
		this.activeFlag = s.getActiveFlag();
		this.geoMatch = s.getGeoMatch();
		this.latitude = s.getLatitude();
		this.longitude = s.getLongitude();
		this.postalStatusCode = s.getPostalStatusCode();
		this.storeName = s.getStoreName();
		this.onlineAppointmentActiveFlag = s.getOnlineAppointmentActiveFlag();
		this.tirePricingActiveFlag = s.getTirePricingActiveFlag();
		this.fax = s.getFax();
		this.localPageURL = s.getLocalPageURL();
	}

	public Long getStoreNumber() {
		return this.storeNumber;
	}

	@JsonIgnore
	public long getNumber() {
		return this.storeNumber.longValue();
	}
	
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	@JsonIgnore
	public void setNumber(long storeNumber) {
		setStoreNumber(new Long(storeNumber));
	}
	
	@JsonIgnore
	public String getDistrictId() {
		return this.districtId;
	}
	
	@JsonIgnore
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	
	@JsonIgnore
	public String getDistrictName() {
		return this.districtName;
	}
	
	@JsonIgnore
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	public String getAreaId() {
		return this.areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String getAreaName() {
		return this.areaName;
	}
	
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	public String getTrackingPhone() {
		return trackingPhone;
	}

	public void setTrackingPhone(String trackingPhone) {
		this.trackingPhone = trackingPhone;
	}

	public String getPhone() {
		if(this.trackingPhone == null 
				|| this.trackingPhone.length() == 0 
				|| this.trackingPhone.equals("")){
			return this.phone;
		}else{
			return this.trackingPhone;
		}
	}
	
	@JsonIgnore
	public String getFormattedPhone() {
		return this.getPhone();
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getManagerName() {
		return this.managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerEmailAddress() {
		return this.managerEmailAddress;
	}

	@JsonIgnore
	public String getManagerEmail() {
		return getManagerEmailAddress();
	}

	public void setManagerEmailAddress(String managerEmailAddress) {
		this.managerEmailAddress = managerEmailAddress;
	}

	@JsonIgnore
	public void setManagerEmail(String managerEmailAddress) {
		setManagerEmailAddress(managerEmailAddress);
	}

	public String getStoreType() {
		return this.storeType;
	}

	@JsonIgnore
	public String getType() {
		return getStoreType();
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	@JsonIgnore
	public void setType(String storeType) {
		setStoreType(storeType);
	}

	public BigDecimal getActiveFlag() {
		return this.activeFlag;
	}

	@JsonIgnore
	public int getActive() {
		return getActiveFlag().intValue();
	}

	public void setActiveFlag(BigDecimal activeFlag) {
		this.activeFlag = activeFlag;
	}

	@JsonIgnore
	public void setActive(int active) {
		setActiveFlag(new BigDecimal(active));
	}

	@JsonIgnore
	public String getGeoMatch() {
		return this.geoMatch;
	}

	@JsonIgnore
	public void setGeoMatch(String geoMatch) {
		this.geoMatch = geoMatch;
	}

	@JsonIgnore
	public String getPostalStatusCode() {
		return this.postalStatusCode;
	}

	@JsonIgnore
	public void setPostalStatusCode(String postalStatusCode) {
		this.postalStatusCode = postalStatusCode;
	}

	public String getStoreName() {
		return this.storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public BigDecimal getOnlineAppointmentActiveFlag() {
		return this.onlineAppointmentActiveFlag;
	}

	public void setOnlineAppointmentActiveFlag(
			BigDecimal onlineAppointmentActiveFlag) {
		this.onlineAppointmentActiveFlag = onlineAppointmentActiveFlag;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public Float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
	public Float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	
	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	
	public String getLocalPageURL() {
		return this.localPageURL;
	}
	
	public void setLocalPageURL(String localPageURL) {
		this.localPageURL = localPageURL;
	}
	
	public void setHours(Set<StoreHour> hours) {
		this.hours = hours;
	}
	
	public Set<StoreHour> getHours() {
		return hours;
	}
	
	public List<StoreHoliday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<StoreHoliday> holidays) {
		this.holidays = holidays;
	}

	public String getRegionId() {
		return regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public String getStoreHour() {
		return this.storeHour;
	}

	public void setStoreHour(String storeHour) {
		this.storeHour = storeHour;
	}
	
	public BigDecimal getTirePricingActiveFlag() {
		return this.tirePricingActiveFlag;
	}

	public void setTirePricingActiveFlag(BigDecimal tirePricingActiveFlag) {
		this.tirePricingActiveFlag = tirePricingActiveFlag;
	}
	
	public boolean getIsMilitaryStore() {
		return isMilitaryStore;
	}

	public void setMilitaryStore(boolean isMilitaryStore) {
		this.isMilitaryStore = isMilitaryStore;
	}
	
	public BigDecimal geteCommActiveFlag() {
		return eCommActiveFlag;
	}

	public void seteCommActiveFlag(BigDecimal eCommActiveFlag) {
		if(eCommActiveFlag == null) eCommActiveFlag = ECOMM_ACTIVE_FLAG_DEFAULT;
		this.eCommActiveFlag = eCommActiveFlag;
	}

	@Override
	public String toString() {
		return "Store [storeNumber=" + storeNumber + ", districtId="
				+ districtId + ", districtName=" + districtName + ", areaId="
				+ areaId + ", areaName=" + areaName + ", regionId=" + regionId
				+ ", regionName=" + regionName + ", divisionId=" + divisionId
				+ ", divisionName=" + divisionName + ", storeName=" + storeName
				+ ", address=" + address + ", city=" + city + ", state="
				+ state + ", zip=" + zip + ", phone=" + phone
				+ ", trackingPhone=" + trackingPhone + ", managerName="
				+ managerName + ", managerEmailAddress=" + managerEmailAddress
				+ ", storeType=" + storeType + ", activeFlag=" + activeFlag
				+ ", geoMatch=" + geoMatch + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", postalStatusCode="
				+ postalStatusCode + ", onlineAppointmentActiveFlag="
				+ onlineAppointmentActiveFlag + ", tirePricingActiveFlag="
				+ tirePricingActiveFlag + ", fax=" + fax + ", isMilitaryStore="
				+ isMilitaryStore + ", reasonDesc=" + reasonDesc
				+ ", storeHour=" + storeHour + ", localPageURL=" + localPageURL
				+ ", eCommActiveFlag=" + eCommActiveFlag + "]";
	}

}