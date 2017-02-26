package com.bfrc.pojo.store;

import esri.aws.v2006_1.dto.mapimage.*;

import com.bfrc.*;
import com.bfrc.security.*;
import com.bfrc.framework.dao.store.*;

/**
 * @author edc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Map extends Base {

boolean wheelWorks = false;
protected String mapURL;
protected int displayCount;
protected int storeCount;
protected int offset;
protected int mapWidth;
protected int mapHeight;
protected String[] storeHTML;
protected Store[] stores;
protected long[] distance;
protected Float[] location;
protected Integer radius;
protected Integer zoomLevel;
protected String description;
protected String[] directions;
protected String street;
protected String city;
protected String state;
protected String zip;
protected String storeType;
protected boolean complete = false;
protected boolean dirty = true;
private MapImageInfo info, originalInfo;

public Map(String app, Config config) {
	this.displayCount = config.getLocator().getStoresPerPage(app).intValue();
}

/**
 * Returns the mapURL.
 * @return String
 */
public String getMapURL() {
	return this.mapURL;
}

/**
 * Returns the store.
 * @return String[]
 */
public String[] getStoreHTML() {
	return this.storeHTML;
}

public String[] getStoreHTML(LocatorOperator locator, String app) throws java.sql.SQLException {
	return locator.getStoreHTML(stores, app);
}

/**
 * Sets the mapURL.
 * @param mapURL The mapURL to set
 */
public void setMapURL(String mapURL) {
	this.mapURL = mapURL;
}

/**
 * Sets the store.
 * @param store The store to set
 */
public void setStoreHTML(String[] store) {
	this.storeHTML = store;
}

/**
 * Returns the mapHeight.
 * @return int
 */
public int getMapHeight() {
	return this.mapHeight;
}

/**
 * Returns the mapWidth.
 * @return int
 */
public int getMapWidth() {
	return this.mapWidth;
}

/**
 * Sets the mapHeight.
 * @param mapHeight The mapHeight to set
 */
public void setMapHeight(int mapHeight) {
	this.mapHeight = mapHeight;
}

/**
 * Sets the mapWidth.
 * @param mapWidth The mapWidth to set
 */
public void setMapWidth(int mapWidth) {
	this.mapWidth = mapWidth;
}

/**
 * Returns the offset.
 * @return int
 */
public int getOffset() {
	return this.offset;
}

/**
 * Sets the offset.
 * @param offset The offset to set
 */
public void setOffset(int offset) {
	this.offset = offset;
}

/**
 * Returns the storeCount.
 * @return int
 */
public int getStoreCount() {
	return this.storeCount;
}

/**
 * Sets the storeCount.
 * @param storeCount The storeCount to set
 */
public void setStoreCount(int storeCount) {
	this.storeCount = storeCount;
}

/**
 * Returns the location.
 * @return Float[]
 */
public Float[] getLocation() {
	return this.location;
}

/**
 * Returns the stores.
 * @return Store[]
 */
public Store[] getStores() {
	return this.stores;
}

/**
 * Sets the location.
 * @param location The location to set
 */
public void setLocation(Float[] location) {
	this.location = location;
}

/**
 * Sets the stores.
 * @param stores The stores to set
 */
public void setStores(Store[] stores) {
	if(stores != null)
		for(int i=0;i<stores.length;i++)
			if(stores[i].getStoreName().startsWith("Wheel Works"))
				this.wheelWorks = true;
	this.stores = stores;
}

/**
 * Returns the description.
 * @return String
 */
public String getDescription() {
	return this.description;
}

/**
 * Returns the directions.
 * @return String[]
 */
public String[] getDirections() {
	return this.directions;
}

/**
 * Sets the directions.
 * @param directions The directions to set
 */
public void setDirections(String[] directions) {
	this.directions = directions;
}

/*
public String getLocationName() {
	return locationName;
}

public void setLocationName(String locationName) {
	this.locationName = locationName;
}
*/

protected void updateDescription() {
	boolean hasStreet = false;
	boolean hasCity = false;
	boolean hasState = false;
	boolean hasZip = false;
	this.description = "";
	if(this.street != null && !this.street.equals("")) {
		hasStreet = true;
		this.description += this.street + ", ";
	}
	if(this.city != null && !this.city.equals("")) {
		hasCity = true;
		this.description += this.city + ", ";
	}
	if(this.state != null && !this.state.equals("")) {
		hasState = true;
		this.description += this.state + " ";
	}
	if(this.zip != null && !this.zip.equals("")) {
		hasZip = true;
		this.description += this.zip;
	}
	if(hasStreet && ((hasCity && hasState) || hasZip))
		this.complete = true;
	else this.complete = false;
	this.dirty = true;
	this.description = Encode.html(this.description);
}

/**
 * Returns the city.
 * @return String
 */
public String getCity() {
	if(this.city == null || this.city.equals("null"))
		this.city = "";
	return this.city;
}

/**
 * Returns the state.
 * @return String
 */
public String getState() {
	if(this.state == null || this.state.equals("null"))
		this.state = "";
	return this.state;
}

/**
 * Returns the street.
 * @return String
 */
public String getStreet() {
	if(this.street == null || this.street.equals("null"))
		this.street = "";
	return this.street;
}

/**
 * Returns the zip.
 * @return String
 */
public String getZip() {
	if(this.zip == null || this.zip.equals("null"))
		this.zip = "";
	return this.zip;
}

/**
 * Sets the city.
 * @param city The city to set
 */
public void setCity(String city) {
	this.city = city;
	updateDescription();
}

/**
 * Sets the state.
 * @param state The state to set
 */
public void setState(String state) {
	this.state = state;
	updateDescription();
}

/**
 * Sets the street.
 * @param street The street to set
 */
public void setStreet(String street) {
	this.street = street;
	updateDescription();
}

/**
 * Sets the zip.
 * @param zip The zip to set
 */
public void setZip(String zip) {
	this.zip = zip;
	updateDescription();
}

/**
 * Returns the storeType.
 * @return String
 */
public String getStoreType() {
	return this.storeType;
}

/**
 * Sets the storeType.
 * @param storeType The storeType to set
 */
public void setStoreType(String storeType) {
	this.storeType = storeType;
}

public boolean isComplete() {
	return this.complete;
}

/**
 * Returns the dirty.
 * @return boolean
 */
public boolean isDirty() {
	return this.dirty;
}

/**
 * Sets the dirty.
 * @param dirty The dirty to set
 */
public void setDirty(boolean dirty) {
	this.dirty = dirty;
}

/**
 * Returns the displayCount.
 * @return int
 */
public int getDisplayCount() {
	return this.displayCount;
}

/**
 * Sets the displayCount.
 * @param displayCount The displayCount to set
 */
public void setDisplayCount(int displayCount) {
	this.displayCount = displayCount;
}

/**
 * Returns the distance.
 * @return int[]
 */
public long[] getDistance() {
	return this.distance;
}

/**
 * Sets the distance.
 * @param distance The distance to set
 */
public void setDistance(long[] distance) {
	this.distance = distance;
}

public boolean hasWheelWorks() {
	return this.wheelWorks;
}

public void setWheelWorks(boolean wheelWorks) {
	this.wheelWorks = wheelWorks;
}

public Integer getRadius() {
	return this.radius;
}

public void setRadius(Integer radius) {
	this.radius = radius;
}

public Integer getZoomLevel() {
	return this.zoomLevel;
}

public void setZoomLevel(Integer zoomLevel) {
	this.zoomLevel = zoomLevel;
}

public MapImageInfo getInfo() {
	return this.info;
}

public void setInfo(MapImageInfo info) {
	this.info = info;
}

public MapImageInfo getOriginalInfo() {
	return this.originalInfo;
}

public void setOriginalInfo(MapImageInfo originalInfo) {
	this.originalInfo = originalInfo;
}

}
