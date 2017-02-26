package com.bfrc.dataaccess.model.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotionViewObject;

import org.apache.commons.lang.StringUtils;

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Store implements Serializable{

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
	
	private List<String> holidayHourMessages;
	
	private Set<StoreHour> hours;
	
	private List<StoreHoliday> holidays;
	
	private List<StoreHolidayHour> holidayHours;
	
	private List taxRates = null;
	
	private List taxStatus = null;
		
	private HrDistricts district = null;
	
	private List<StoreAdminAnnouncement> announcements;
	
	private	List<StoreAdminPromotionViewObject> localPromos;
	
	public Store() {
	}

	public Store(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public Store(Store s) {
		this.storeHour = s.getStoreHour();
		this.hours = s.getHours();
		this.district = s.getDistrict();
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

	@JsonSerialize(using=ToStringSerializer.class)
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
		String formatted = this.getPhone();
		formatted = StringUtils.replace(formatted, "(", "");
		formatted = StringUtils.replace(formatted, ")", "-");
		formatted = formatted.replaceAll("\\s", "");
		
		return formatted;
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

	@JsonSerialize(using=ToStringSerializer.class)
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

	@JsonSerialize(using=ToStringSerializer.class)
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

	@JsonSerialize(using=ToStringSerializer.class)
	public Float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	
	@JsonIgnore
	public void setDistrict(HrDistricts district) {
		this.district = district;
	}
	
	@JsonIgnore
	public HrDistricts getDistrict() {
		return district;
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

	public String getStoreHour() {
		return this.storeHour;
	}

	public void setStoreHour(String storeHour) {
		this.storeHour = storeHour;
	}
	
	public List<StoreHoliday> getHolidays() {
		return this.holidays;
	}
	
	public void setHolidays(List<StoreHoliday> holidays) {
		this.holidays = holidays;
	}
	
	public List<StoreHolidayHour> getHolidayHours() {
		return this.holidayHours;
	}
	
	public void setHolidayHours(List<StoreHolidayHour> holidayHours) {
		this.holidayHours = holidayHours;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getTirePricingActiveFlag() {
		return this.tirePricingActiveFlag;
	}

	public void setTirePricingActiveFlag(BigDecimal tirePricingActiveFlag) {
		this.tirePricingActiveFlag = tirePricingActiveFlag;
	}
	@JsonIgnore
	public List getTaxRates() {
		return this.taxRates;
	}
	@JsonIgnore
	public void setTaxRates(List taxRates) {
		this.taxRates = taxRates;
	}
	@JsonIgnore
	public List getTaxStatus() {
		return this.taxStatus;
	}
	@JsonIgnore
	public void setTaxStatus(List taxStatus) {
		this.taxStatus = taxStatus;
	}
	
	public boolean getIsMilitaryStore() {
		return isMilitaryStore;
	}

	public void setMilitaryStore(boolean isMilitaryStore) {
		this.isMilitaryStore = isMilitaryStore;
	}
	
	@JsonIgnore
	public List<String> getHolidayHourMessages() {
		return holidayHourMessages;
	}
	
	@JsonIgnore
	public void setHolidayHourMessages(List<String> holidayHourMessages) {
		this.holidayHourMessages = holidayHourMessages;
	}
	
	public List<StoreAdminAnnouncement> getAnnouncements() {
		return announcements;
	}
	
	public void setAnnouncements(List<StoreAdminAnnouncement> announcements) {
		this.announcements = announcements;
	}
	
	public List<StoreAdminPromotionViewObject> getLocalPromos() {
		return localPromos;
	}
	
	public void setLocalPromos(List<StoreAdminPromotionViewObject> localPromos) {
		this.localPromos = localPromos;
	}
	
	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal geteCommActiveFlag() {
		return eCommActiveFlag;
	}

	public void seteCommActiveFlag(BigDecimal eCommActiveFlag) {
		this.eCommActiveFlag = eCommActiveFlag;
	}

	@Override
	public String toString() {
		return "Store [storeNumber=" + storeNumber + ", districtId="
				+ districtId + ", address=" + address + ", city=" + city
				+ ", state=" + state + ", zip=" + zip + ", phone=" + phone
				+ ", managerName=" + managerName + ", managerEmailAddress="
				+ managerEmailAddress + ", storeType=" + storeType
				+ ", activeFlag=" + activeFlag + ", geoMatch=" + geoMatch
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", postalStatusCode=" + postalStatusCode + ", storeName="
				+ storeName + ", onlineAppointmentActiveFlag="
				+ onlineAppointmentActiveFlag + ", tirePricingActiveFlag="
				+ tirePricingActiveFlag + ", fax=" + fax + ", eCommActiveFlag=" 
				+ eCommActiveFlag + "]";
	}
	
	
}