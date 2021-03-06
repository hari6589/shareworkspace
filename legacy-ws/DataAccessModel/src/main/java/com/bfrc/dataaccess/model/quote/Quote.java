package com.bfrc.dataaccess.model.quote;
// Generated Sep 7, 2012 5:06:59 PM by Hibernate Tools 3.1.0.beta4

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Quote generated by hbm2java
 */

public class Quote  implements java.io.Serializable {


    // Fields    

     private Long quoteId;
     private QuoteType quoteType;
     private Long storeNumber;
     private String vehicleId;
     private Integer vehicleYear;
     private String vehicleMake;
     private String vehicleModel;
     private String vehicleSubmodel;
     private String vehicleEngine;
     private String vehicleModelSubmodelEngine;
     private String zip;
     private String webSite;
     private Calendar createdDate;
     private String firstName;
     private String lastName;
     private String promotionType;
     private Long promotionId;
     private Set<QuoteItem> quoteItems = new HashSet<QuoteItem>(0);


    // Constructors

    /** default constructor */
    public Quote() {
    }
   
    // Property accessors

    public Long getQuoteId() {
        return this.quoteId;
    }
    
    public void setQuoteId(Long quoteId) {
        this.quoteId = quoteId;
    }

    public QuoteType getQuoteType() {
        return this.quoteType;
    }
    
    public void setQuoteType(QuoteType quoteType) {
        this.quoteType = quoteType;
    }

    public Long getStoreNumber() {
        return this.storeNumber;
    }
    
    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }
    
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getVehicleYear() {
        return this.vehicleYear;
    }
    
    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleMake() {
        return this.vehicleMake;
    }
    
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return this.vehicleModel;
    }
    
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleSubmodel() {
        return this.vehicleSubmodel;
    }
    
    public void setVehicleSubmodel(String vehicleSubmodel) {
        this.vehicleSubmodel = vehicleSubmodel;
    }

    public String getVehicleEngine() {
        return this.vehicleEngine;
    }
    
    public void setVehicleEngine(String vehicleEngine) {
        this.vehicleEngine = vehicleEngine;
    }

    public String getVehicleModelSubmodelEngine() {
        return this.vehicleModelSubmodelEngine;
    }
    
    public void setVehicleModelSubmodelEngine(String vehicleModelSubmodelEngine) {
        this.vehicleModelSubmodelEngine = vehicleModelSubmodelEngine;
    }

    public String getZip() {
        return this.zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getWebSite() {
        return this.webSite;
    }
    
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Calendar getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPromotionType() {
        return this.promotionType;
    }
    
    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public Long getPromotionId() {
        return this.promotionId;
    }
    
    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Set<QuoteItem> getQuoteItems() {
        return this.quoteItems;
    }
    
    public void setQuoteItems(Set<QuoteItem> quoteItems) {
        this.quoteItems = quoteItems;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((promotionId == null) ? 0 : promotionId.hashCode());
		result = prime * result + ((promotionType == null) ? 0 : promotionType.hashCode());
		result = prime * result + ((quoteId == null) ? 0 : quoteId.hashCode());
		result = prime * result + ((quoteType == null) ? 0 : quoteType.hashCode());
		result = prime * result + ((storeNumber == null) ? 0 : storeNumber.hashCode());
		result = prime * result + ((vehicleEngine == null) ? 0 : vehicleEngine.hashCode());
		result = prime * result + ((vehicleId == null) ? 0 : vehicleId.hashCode());
		result = prime * result + ((vehicleMake == null) ? 0 : vehicleMake.hashCode());
		result = prime * result + ((vehicleModel == null) ? 0 : vehicleModel.hashCode());
		result = prime * result + ((vehicleModelSubmodelEngine == null) ? 0 : vehicleModelSubmodelEngine.hashCode());
		result = prime * result + ((vehicleSubmodel == null) ? 0 : vehicleSubmodel.hashCode());
		result = prime * result + ((vehicleYear == null) ? 0 : vehicleYear.hashCode());
		result = prime * result + ((webSite == null) ? 0 : webSite.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
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
		Quote other = (Quote) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (promotionId == null) {
			if (other.promotionId != null)
				return false;
		} else if (!promotionId.equals(other.promotionId))
			return false;
		if (promotionType == null) {
			if (other.promotionType != null)
				return false;
		} else if (!promotionType.equals(other.promotionType))
			return false;
		if (quoteId == null) {
			if (other.quoteId != null)
				return false;
		} else if (!quoteId.equals(other.quoteId))
			return false;
		if (quoteType == null) {
			if (other.quoteType != null)
				return false;
		} else if (!quoteType.equals(other.quoteType))
			return false;
		if (storeNumber == null) {
			if (other.storeNumber != null)
				return false;
		} else if (!storeNumber.equals(other.storeNumber))
			return false;
		if (vehicleEngine == null) {
			if (other.vehicleEngine != null)
				return false;
		} else if (!vehicleEngine.equals(other.vehicleEngine))
			return false;
		if (vehicleId == null) {
			if (other.vehicleId != null)
				return false;
		} else if (!vehicleId.equals(other.vehicleId))
			return false;
		if (vehicleMake == null) {
			if (other.vehicleMake != null)
				return false;
		} else if (!vehicleMake.equals(other.vehicleMake))
			return false;
		if (vehicleModel == null) {
			if (other.vehicleModel != null)
				return false;
		} else if (!vehicleModel.equals(other.vehicleModel))
			return false;
		if (vehicleModelSubmodelEngine == null) {
			if (other.vehicleModelSubmodelEngine != null)
				return false;
		} else if (!vehicleModelSubmodelEngine.equals(other.vehicleModelSubmodelEngine))
			return false;
		if (vehicleSubmodel == null) {
			if (other.vehicleSubmodel != null)
				return false;
		} else if (!vehicleSubmodel.equals(other.vehicleSubmodel))
			return false;
		if (vehicleYear == null) {
			if (other.vehicleYear != null)
				return false;
		} else if (!vehicleYear.equals(other.vehicleYear))
			return false;
		if (webSite == null) {
			if (other.webSite != null)
				return false;
		} else if (!webSite.equals(other.webSite))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

}
