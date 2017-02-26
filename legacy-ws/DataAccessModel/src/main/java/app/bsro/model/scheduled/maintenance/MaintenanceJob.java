package app.bsro.model.scheduled.maintenance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import app.bsro.model.scheduled.maintenance.ncd.rtq.JobResult;

@XmlRootElement(name="job")
public class MaintenanceJob {

	private String description;
	private boolean authorized;
	private List<MaintenanceJobDetail> details = new ArrayList<MaintenanceJobDetail>();
	
	public MaintenanceJob() {
		super();
	}
	
	public MaintenanceJob(String description, String strAuth) {
		this.description = description;
		strAuth = StringUtils.trimToEmpty(strAuth);
		if("N".equals(strAuth))
			this.authorized = false;
		else this.authorized = true;
	}

	public void addDetail(JobResult result) {
		
		//DO NOT include XPRODTYPE = 5 (itemType) as these are
		// the notes that the employees write and BSRO is a bit
		// nervous letting the end-user see them as they are not
		// always flattering.
		
		Map<Integer, String> validProductTypes = new HashMap<Integer, String>(2);
		validProductTypes.put(0, "Part");
		validProductTypes.put(1, "Labor");
		// validProductTypes.put(5, "Note");
		
		
		
		Integer itemType = result.getItemType();
		if(itemType != null && validProductTypes.containsKey(itemType)) {
			details.add(new MaintenanceJobDetail(
					result.getArticleNbr(), 
					result.getSequencNbr(), 
					result.getItemDescription(), 
					result.getItemType().toString(), 
					result.getQty(), 
					result.getItemPrice()));
		}
		Collections.sort(details);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MaintenanceJobDetail> getDetails() {
		return details;
	}

	public void setDetails(List<MaintenanceJobDetail> details) {
		this.details = details;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
	
	public boolean isAuthorized() {
		return authorized;
	}
	
}