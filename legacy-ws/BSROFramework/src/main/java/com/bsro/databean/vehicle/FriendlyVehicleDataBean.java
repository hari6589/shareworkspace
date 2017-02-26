/**
 * 
 */
package com.bsro.databean.vehicle;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import com.bfrc.pojo.lookup.SeoVehicleData;

/**
 * @author smoorthy
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class FriendlyVehicleDataBean {
	
	private Long makeId;
	private String makeName;
	private String makeFriendlyName;
	private List<TireVehicleModel> models;
	private Long modelId;
	private String modelName;
	private String modelFriendlyName;
	private List<String> years;
	private String year;
	private List<TireVehicleSubModel> submodels;
	private Long submodelId;
	private String submodelName;
	private String submodelFriendlyName;
	private Long acesVehicleId;
	private Integer tpmsInd;
	private SeoVehicleData seoVehicleData;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getMakeId() {
		return makeId;
	}
	
	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}
	
	public String getMakeName() {
		return makeName;
	}
	
	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	
	public String getMakeFriendlyName() {
		return makeFriendlyName;
	}
	
	public void setMakeFriendlyName(String makeFriendlyName) {
		this.makeFriendlyName = makeFriendlyName;
	}
	
	public List<TireVehicleModel> getModels() {
		return models;
	}

	public void setModels(List<TireVehicleModel> models) {
		this.models = models;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getModelId() {
		return modelId;
	}
	
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getModelFriendlyName() {
		return modelFriendlyName;
	}
	
	public void setModelFriendlyName(String modelFriendlyName) {
		this.modelFriendlyName = modelFriendlyName;
	}
	
	public List<String> getYears() {
		return years;
	}

	public void setYears(List<String> years) {
		this.years = years;
	}

	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}

	public List<TireVehicleSubModel> getSubmodels() {
		return submodels;
	}

	public void setSubmodels(List<TireVehicleSubModel> submodels) {
		this.submodels = submodels;
	}

	public Long getSubmodelId() {
		return submodelId;
	}

	public void setSubmodelId(Long submodelId) {
		this.submodelId = submodelId;
	}

	public String getSubmodelName() {
		return submodelName;
	}

	public void setSubmodelName(String submodelName) {
		this.submodelName = submodelName;
	}

	public String getSubmodelFriendlyName() {
		return submodelFriendlyName;
	}

	public void setSubmodelFriendlyName(String submodelFriendlyName) {
		this.submodelFriendlyName = submodelFriendlyName;
	}

	public Long getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}

	public Integer getTpmsInd() {
		return tpmsInd;
	}

	public void setTpmsInd(Integer tpmsInd) {
		this.tpmsInd = tpmsInd;
	}
	
	public SeoVehicleData getSeoVehicleData() {
		return seoVehicleData;
	}
	
	public void setSeoVehicleData(SeoVehicleData seoVehicleData) {
		this.seoVehicleData = seoVehicleData;
	}

}
