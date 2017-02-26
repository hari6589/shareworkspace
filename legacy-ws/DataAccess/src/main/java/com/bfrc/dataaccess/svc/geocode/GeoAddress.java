package com.bfrc.dataaccess.svc.geocode;

import java.net.URLEncoder;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.bfrc.dataaccess.util.AddressUtils;

public class GeoAddress {


	private Logger logger = Logger.getLogger(getClass().getName());
	private String address;
	private String city;
	private String state;
	private String zip;
	
	public GeoAddress() {}
	public GeoAddress(String fullAddress) {
		
		if(AddressUtils.isValidZipCode(fullAddress)) {
			setZip(fullAddress);
		} else {
			
			//First check if the last section is the zip (all numbers)
			int idx = fullAddress.lastIndexOf(" ");
			if(idx > -1) {
				String chunk = fullAddress.substring((idx+1));
				if(NumberUtils.isDigits(chunk)) {
					setZip(chunk);
					
					fullAddress = fullAddress.substring(0, idx);
				}
			}
			
			String[] split = fullAddress.split(",");
			if(split.length > 0) {
				setCity(split[0]);
				if(split.length > 1)
					setState(split[1]);
			}
		}
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	

	public String toEncodedUrlString() {
		StringBuilder builder = new StringBuilder();
		if(StringUtils.trimToNull(getAddress()) != null)
			builder.append(StringUtils.trimToEmpty(getAddress())  + " ");
		
		if(StringUtils.trimToNull(getCity()) != null)
			builder.append(StringUtils.trimToEmpty(getCity()) + " ");
		
		if(StringUtils.trimToNull(getState()) != null)
			builder.append(StringUtils.trimToEmpty(getState()) + " ");
		
		if(StringUtils.trimToNull(getZip()) != null)
			builder.append(StringUtils.trimToEmpty(getZip()) + " ");
		
		String result = StringUtils.trimToNull(builder.toString());
		if(result != null) {
			try {
			result = URLEncoder.encode(result, "UTF-8");
			}catch(Exception E) {
				logger.severe("Error encoding address ("+result+"): "+E.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * Build a composite string out of the trimmed version of the address, 
	 * city, state and zip so that subsequent requests with just one value
	 * will retrieve a hit
	 * @return
	 */
	public String getCompositeString() {
		StringBuilder result = new StringBuilder();
		result.append(StringUtils.trimToEmpty(address));
		result.append(StringUtils.trimToEmpty(city));
		result.append(StringUtils.trimToEmpty(state));
		result.append(StringUtils.trimToEmpty(zip));
		return result.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		GeoAddress other = (GeoAddress) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

}
