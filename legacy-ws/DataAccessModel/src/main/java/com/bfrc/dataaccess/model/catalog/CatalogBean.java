/**
 * 
 */
package com.bfrc.dataaccess.model.catalog;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author smoorthy
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class CatalogBean {
	
	private Map<String, String> vehicleTypes;
	private Map<String,String> sortList;
	private List<String> filters;
	private List<Map<String, String>> displays;
	
	public Map<String, String> getVehicleTypes() {
		return vehicleTypes;
	}
	
	public void setVehicleTypes(Map<String, String> vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}
	
	public Map<String, String> getSortList() {
		return sortList;
	}
	
	public void setSortList(Map<String, String> sortList) {
		this.sortList = sortList;
	}
	
	public List<String> getFilters() {
		return filters;
	}

	public void setFilters(List<String> filters) {
		this.filters = filters;
	}

	public List<Map<String, String>> getDisplays() {
		return displays;
	}

	public void setDisplays(List<Map<String, String>> displays) {
		this.displays = displays;
	}
	
}
