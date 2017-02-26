package com.bfrc.pojo.storeadmin;

import java.util.Date;

public class StoreAdminAnnouncement  implements java.io.Serializable {

	private long announcementId;
     private String friendlyId;
     private String brand;
     private String title;
     private String description;
	 private Long imageId;
     private Date startDate;
     private Date endDate;
     private String requestReason;
     private String denyReason;
     private Long articleNumber;
     private String createdBy;
     private Date createdDate;
     private String modifiedBy;
     private Date modifiedDate;
     private String status;
     private StoreAdminLibraryImage storeAdminLibraryImage;
     private String position;

    public StoreAdminAnnouncement() {
    }
    
    public long getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(long announcementId) {
		this.announcementId = announcementId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StoreAdminAnnouncement(long announcementId, Date createdDate) {
        this.announcementId = announcementId;
        this.createdDate = createdDate;
    }
    
    public String getFriendlyId() {
        return this.friendlyId;
    }
    
    public void setFriendlyId(String friendlyId) {
        this.friendlyId = friendlyId;
    }
    public String getBrand() {
        return this.brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getDenyReason() {
        return this.denyReason;
    }
    
    public void setDenyReason(String denyReason) {
        this.denyReason = denyReason;
    }
    public Long getArticleNumber() {
        return this.articleNumber;
    }
    
    public void setArticleNumber(Long articleNumber) {
        this.articleNumber = articleNumber;
    }
    public String getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getModifiedBy() {
        return this.modifiedBy;
    }
    
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    public Date getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public StoreAdminLibraryImage getStoreAdminLibraryImage() {
		return storeAdminLibraryImage;
	}

	public void setStoreAdminLibraryImage(
			StoreAdminLibraryImage storeAdminLibraryImage) {
		this.storeAdminLibraryImage = storeAdminLibraryImage;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}


