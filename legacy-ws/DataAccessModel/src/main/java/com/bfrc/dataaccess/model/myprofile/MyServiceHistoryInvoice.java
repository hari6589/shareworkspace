/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.bfrc.dataaccess.model.store.Store;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"vehicleHistory", "store"})
@JsonPropertyOrder({"id", "invoiceDate", "company", "total","mileage", 
	"source","jobTitle", "jobDetail", "type", "storeNbr"})
public class MyServiceHistoryInvoice  {

	private Long myServiceHistInvoiceId;
	
	private MyServiceHistoryVehicle vehicleHistory;
	
	private Date invoiceDate;

	private String company;

	private BigDecimal total;
	
	private Long mileage;
	
	private String source;
	
	private String jobTitle;
	
	private String jobDetail;
	
	private Integer type;
	
	private String storeNbr;
	
	private Store store;
	
	private List<MyServiceHistoryJob> jobs = new ArrayList<MyServiceHistoryJob>();

	
	/**
	 * 
	 */
	public MyServiceHistoryInvoice() {
	}
	/**
	 * @return the myServiceHistInvoiceId
	 */
	@JsonProperty("id")
	public Long getMyServiceHistInvoiceId() {
		return myServiceHistInvoiceId;
	}
	/**
	 * @param myServiceHistInvoiceId the myServiceHistInvoiceId to set
	 */
	public void setMyServiceHistInvoiceId(Long myServiceHistInvoiceId) {
		this.myServiceHistInvoiceId = myServiceHistInvoiceId;
	}

	
	/**
	 * @return the vehicleHistory
	 */
	public MyServiceHistoryVehicle getVehicleHistory() {
		return vehicleHistory;
	}
	/**
	 * @param vehicleHistory the vehicleHistory to set
	 */
	public void setVehicleHistory(MyServiceHistoryVehicle vehicleHistory) {
		this.vehicleHistory = vehicleHistory;
	}
	/**
	 * @return the invoiceDate
	 */
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}
	/**
	 * @param total the totalPrice to set
	 */
	@JsonProperty("totalPrice")
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	/**
	 * @return the mileage
	 */
	public Long getMileage() {
		return mileage;
	}
	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}
	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	/**
	 * @return the jobDetail
	 */
	public String getJobDetail() {
		return jobDetail;
	}
	/**
	 * @param jobDetail the jobDetail to set
	 */
	public void setJobDetail(String jobDetail) {
		this.jobDetail = jobDetail;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	
	
	/**
	 * @return the storeNbr
	 */
	public String getStoreNbr() {
		return storeNbr;
	}
	/**
	 * @param storeNbr the storeNbr to set
	 */
	public void setStoreNbr(String storeNbr) {
		this.storeNbr = storeNbr;
	}
	/**
	 * @return the store
	 */
	public Store getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(Store store) {
		this.store = store;
	}
	/**
	 * @return the jobs
	 */
	public List<MyServiceHistoryJob> getJobs() {
		return jobs;
	}
	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(List<MyServiceHistoryJob> jobs) {
		this.jobs = jobs;
	}
	@Override
	public String toString() {
		return "MyServiceHistoryInvoice [myServiceHistInvoiceId="
				+ myServiceHistInvoiceId + ", invoiceDate=" + invoiceDate
				+ ", company=" + company + ", total=" + total + ", mileage="
				+ mileage + ", source=" + source + ", jobTitle=" + jobTitle
				+ ", jobDetail=" + jobDetail + ", type=" + type + ", storeNbr="
				+ storeNbr + ", store=" + store + ", jobs=" + jobs + "]";
	}

}
