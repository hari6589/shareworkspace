package com.bfrc.dataaccess.model.aces;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "submodelName"})
public class VehicleSubmodel {
	private Long submodelId;
	
	private String submodelName;

	@JsonProperty("id")
	public Long getSubmodelId() {
		return submodelId;
	}

	@JsonProperty("id")
	public void setSubmodelId(Long submodelId) {
		this.submodelId = submodelId;
	}

	public String getSubmodelName() {
		return submodelName;
	}

	public void setSubmodelName(String submodelName) {
		this.submodelName = submodelName;
	}
	
	
}
