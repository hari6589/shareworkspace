package com.bsro.databean.vehicle;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.bfrc.pojo.lookup.SeoVehicleData;

/**
 * @author smoorthy
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FriendlySizeDataBean {
	
	private String rimSize;
	private String rimText;
	private List<TireSize> crossAspects;
	private SeoVehicleData seoVehicleData;
	
	public String getRimSize() {
		return rimSize;
	}
	
	public void setRimSize(String rimSize) {
		this.rimSize = rimSize;
	}
	
	public String getRimText() {
		return rimText;
	}
	
	public void setRimText(String rimText) {
		this.rimText = rimText;
	}
	
	public List<TireSize> getCrossAspects() {
		return crossAspects;
	}
	
	public void setCrossAspects(List<TireSize> crossAspects) {
		this.crossAspects = crossAspects;
	}
	
	public SeoVehicleData getSeoVehicleData() {
		return seoVehicleData;
	}
	
	public void setSeoVehicleData(SeoVehicleData seoVehicleData) {
		this.seoVehicleData = seoVehicleData;
	}
	
}
