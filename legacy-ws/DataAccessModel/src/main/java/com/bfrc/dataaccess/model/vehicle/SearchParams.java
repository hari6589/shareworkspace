/**
 * 
 */
package com.bfrc.dataaccess.model.vehicle;

/**
 * @author schowdhu
 *
 */
public enum SearchParams {
	YEAR("year"),
	YEARID("yearId"),
	MAKE("make"),
	MAKEID("makeId"),
	MODEL("model"),
	MODELID("modelId"),
	SUBMODEL("submodel"),
	SUBMODELID("submodelId"),
	ENGINESIZE("engineSize"),
	VEHICLEID("vehicleId");
	private String param;

	private SearchParams(String value) {
		this.param = value;
	}
	public String getValue() {
		return this.param;
	}
}
