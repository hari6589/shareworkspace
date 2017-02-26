package app.bsro.model.scheduled.maintenance.ncd.rtq;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EDWRTQReturn")
public class VehicleSearchReturn {
	
	private VehicleSearchRecordSet recordset;
	
	private String recordsProcessed;
	
	private String statusMessage;

	@XmlElement(name="EDWRTQRecordSet")
	public VehicleSearchRecordSet getRecordset() {
		return recordset;
	}
	public void setRecordset(VehicleSearchRecordSet recordset) {
		this.recordset = recordset;
	}

	@XmlElement(name="records_processed")
	public String getRecordsProcessed() {
		return recordsProcessed;
	}
	public void setRecordsProcessed(String recordsProcessed) {
		this.recordsProcessed = recordsProcessed;
	}

	@XmlElement(name="status_message")
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	@Override
	public String toString() {
		return "EDWRTQReturn [recordset=" + recordset + ", recordsProcessed="
				+ recordsProcessed + ", statusMessage=" + statusMessage + "]";
	}
	
	
}
