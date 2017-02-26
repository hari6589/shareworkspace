package app.bsro.model.scheduled.maintenance;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import app.bsro.model.scheduled.maintenance.ncd.rtq.JobResult;

@XmlRootElement(name="job")
public class MaintenanceInvoice {

	private Date invoiceDate;
	private Integer invoiceId;
	private String company;
	private String storeType;
	private String storeName;
	private String storeNbr;
	private String storeAddress;
	private String storeCity;
	private String storeState;
	private String storeZip;
	private Integer mileage;
	private BigDecimal total;
	private String vehicleId;

	private List<MaintenanceJob> jobs = new ArrayList<MaintenanceJob>();
	private int size = 0;
	public MaintenanceInvoice() {
		super();
	}
	
	public MaintenanceInvoice(JobResult job, String vehicleId, String storeType, String storeName) {
		super();
		Date invoiceDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy HH:mm:ss a");
			String dt = job.getInvoiceDate();
			invoiceDate = sdf.parse(dt);
		}catch(Exception E) {
			E.printStackTrace();
		}
		this.invoiceDate = invoiceDate;
		this.invoiceId = job.getInvoiceNbr();
		this.company = job.getCompanyIndicator();
		this.storeNbr = job.getStoreNbr();
		this.storeAddress = job.getStoreAddress();
		this.storeCity = job.getStoreCity();
		this.storeState = job.getStoreState();
		this.storeZip = job.getStoreZip();
		this.mileage = job.getMileage();
		if(job.getInvoiceAmt() == null) this.total = new BigDecimal(0);
		else this.total = new BigDecimal(job.getInvoiceAmt());
		this.vehicleId = vehicleId;
		this.storeName = StringUtils.trimToEmpty(storeName);
		this.storeType = StringUtils.trimToEmpty(storeType);
		
		addJob(job, vehicleId);
	}

	public void addJob(JobResult job, String vehicleId) {
		boolean found = false;
		this.vehicleId = vehicleId;
		for(MaintenanceJob m : jobs) {
			if(m.getDescription().equals(StringUtils.trimToEmpty(job.getDescription()))) {
				found = true;
				m.addDetail(job);
			}
		}
		if(!found) {
			MaintenanceJob mj = new MaintenanceJob(job.getDescription(), job.getStatus());
			mj.addDetail(job);
			jobs.add(mj);
		}
	}

	public int getSize() {
		if(jobs == null) return 0;
		else return jobs.size();
	}
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStoreNbr() {
		return storeNbr;
	}

	public void setStoreNbr(String storeNbr) {
		this.storeNbr = storeNbr;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreCity() {
		return storeCity;
	}

	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}

	public String getStoreState() {
		return storeState;
	}

	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}
	
	public void setStoreZip(String storeZip) {
		this.storeZip = storeZip;
	}
	
	public String getStoreZip() {
		return storeZip;
	}

	public void setJobs(List<MaintenanceJob> jobs) {
		this.jobs = jobs;
	}
	
	public List<MaintenanceJob> getJobs() {
		return jobs;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	
	public Integer getMileage() {
		return mileage;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getTotal() {
		return total.setScale(2, BigDecimal.ROUND_HALF_DOWN);
	}
	
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public String getStoreName() {
		return storeName;
	}
	public String getStoreType() {
		return storeType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((invoiceId == null) ? 0 : invoiceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaintenanceInvoice other = (MaintenanceInvoice) obj;
		if (invoiceId == null) {
			if (other.invoiceId != null)
				return false;
		} else if (!invoiceId.equals(other.invoiceId))
			return false;
		return true;
	}
}