package com.bsro.databean.vehicle;

import com.bsro.databean.BaseDataBean;

public class VehicleDataBean extends BaseDataBean {
	private String make;
	private String model;
	private String urlFriendlyMake;
	private String urlFriendlyModel;
	
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getUrlFriendlyMake() {
		return urlFriendlyMake;
	}
	public void setUrlFriendlyMake(String urlFriendlyMake) {
		this.urlFriendlyMake = urlFriendlyMake;
	}
	public String getUrlFriendlyModel() {
		return urlFriendlyModel;
	}
	public void setUrlFriendlyModel(String urlFriendlyModel) {
		this.urlFriendlyModel = urlFriendlyModel;
	}
}
