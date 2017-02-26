package app.bsro.model.scheduled.maintenance.ncd.rtq;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class JobRecordSet {

	private List<JobResult> records;

	@XmlElement(name="EDWRTQRecord")
	public List<JobResult> getRecords() {
		return records;
	}

	public void setRecords(List<JobResult> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "EDWRTQRecordSet [records=" + records + "]";
	}

}
