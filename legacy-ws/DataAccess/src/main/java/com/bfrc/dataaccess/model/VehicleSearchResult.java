package com.bfrc.dataaccess.model;

import java.util.ArrayList;
import java.util.List;

public class VehicleSearchResult {

	private String type;
	private List<ValueTextBean> result;
	
	public VehicleSearchResult(){}
	public VehicleSearchResult(String type, List<ValueTextBean> result) {
		setType(type);
		setResult(result);
	}	
	
	public VehicleSearchResult(List<String> result) {
		setType("year");
		List<ValueTextBean> l = new ArrayList<ValueTextBean>();
		if(result != null) {
			for(String v : result)
				l.add(new ValueTextBean(v));
		}
		setResult(l);
	}
	
	public void setResult(List<ValueTextBean> result) {
		this.result = result;
	}
	public List<ValueTextBean> getResult() {
		return result;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
