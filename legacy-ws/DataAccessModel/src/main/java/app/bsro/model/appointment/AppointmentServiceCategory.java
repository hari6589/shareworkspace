/**
 * 
 */
package app.bsro.model.appointment;


/**
 * @author schowdhu
 *
 */
public class AppointmentServiceCategory {
	
    private Long serviceId;
    private String serviceDesc;
    private String serviceCategory;
    private Integer sortOrder;
    
	public AppointmentServiceCategory() {
	}
	
	/**
	 * @param serviceId
	 * @param serviceDesc
	 */
	public AppointmentServiceCategory(Long serviceId, String serviceDesc) {
		this.serviceId = serviceId;
		this.serviceDesc = serviceDesc;
	}


	/**
	 * @param serviceId
	 * @param serviceDesc
	 * @param serviceCategory
	 * @param addonService
	 */
	public AppointmentServiceCategory(Long serviceId, String serviceDesc,
			String serviceCategory) {
		this.serviceId = serviceId;
		this.serviceDesc = serviceDesc;
		this.serviceCategory = serviceCategory;
	}

	/**
	 * @return the serviceId
	 */
	public Long getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the serviceDesc
	 */
	public String getServiceDesc() {
		return serviceDesc;
	}
	/**
	 * @param serviceDesc the serviceDesc to set
	 */
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	/**
	 * @return the serviceCategory
	 */
	public String getServiceCategory() {
		return serviceCategory;
	}
	/**
	 * @param serviceCategory the serviceCategory to set
	 */
	
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
