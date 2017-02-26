package app.bsro.model.scheduled.maintenance;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="searchResults")
public class JobResults {

	private List<MaintenanceInvoice> invoices;
	
	public JobResults(){}

	public void setInvoices(List<MaintenanceInvoice> invoices) {
		this.invoices = invoices;
	}
	
	public void addInvoice(MaintenanceInvoice invoice) {
		if(invoices == null) invoices = new ArrayList<MaintenanceInvoice>();
		invoices.add(invoice);
	}

	@XmlElement(name="jobs")
	public List<MaintenanceInvoice> getInvoices() {
		return invoices;
	}
	
}
