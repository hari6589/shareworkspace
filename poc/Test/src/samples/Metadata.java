package samples;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
	    
	private int c_id;
	private int employee_id;
	private int service_id;
	private String result;
	private int status_id;
	private String status_description;
	
	@JsonProperty("c_id")
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int c_id) {
		this.c_id = c_id;
	}
	
	@JsonProperty("employee_id")
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	@JsonProperty("service_id")
	public int getService_id() {
		return service_id;
	}
	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	@JsonProperty("result")
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	@JsonProperty("status_id")
	public int getStatus_id() {
		return status_id;
	}
	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}

	@JsonProperty("status_description")
	public String getStatus_description() {
		return status_description;
	}
	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}
	
	@Override
	public String toString() {
		return "Metadata [c_id=" + c_id + ", employee_id=" + employee_id
				+ ", service_id=" + service_id + ", result=" + result
				+ ", status_id=" + status_id + ", status_description="
				+ status_description + "]";
	}

}
