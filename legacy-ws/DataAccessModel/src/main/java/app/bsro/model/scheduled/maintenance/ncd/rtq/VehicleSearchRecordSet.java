package app.bsro.model.scheduled.maintenance.ncd.rtq;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class VehicleSearchRecordSet {

	private List<VehicleSearchResult> records;

	@XmlElement(name="EDWRTQRecord")
	public List<VehicleSearchResult> getRecords() {
		return records;
	}

	public void setRecords(List<VehicleSearchResult> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "EDWRTQRecordSet [recordSet=" + records + "]";
	}

}
