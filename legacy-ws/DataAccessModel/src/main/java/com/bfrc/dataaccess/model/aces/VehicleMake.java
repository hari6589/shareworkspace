package com.bfrc.dataaccess.model.aces;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "makeName"})
public class VehicleMake {
	
	private Long makeId;
	
	private String makeName;

	@JsonProperty("id")
	public Long getMakeId() {
		return makeId;
	}
	@JsonProperty("id")
	public void setMakeId(Long makeId) {
		this.makeId = makeId;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
	
	

}
