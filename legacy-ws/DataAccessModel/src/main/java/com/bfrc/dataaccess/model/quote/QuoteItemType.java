package com.bfrc.dataaccess.model.quote;
// Generated Sep 7, 2012 5:06:59 PM by Hibernate Tools 3.1.0.beta4



/**
 * QuoteItemType generated by hbm2java
 */

public class QuoteItemType  implements java.io.Serializable {


    // Fields    

     private Long quoteItemTypeId;
     private String friendlyId;
     private String name;


    // Constructors

    /** default constructor */
    public QuoteItemType() {
    }
   
    // Property accessors

    public Long getQuoteItemTypeId() {
        return this.quoteItemTypeId;
    }
    
    public void setQuoteItemTypeId(Long quoteItemTypeId) {
        this.quoteItemTypeId = quoteItemTypeId;
    }

    public String getFriendlyId() {
        return this.friendlyId;
    }
    
    public void setFriendlyId(String friendlyId) {
        this.friendlyId = friendlyId;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friendlyId == null) ? 0 : friendlyId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((quoteItemTypeId == null) ? 0 : quoteItemTypeId.hashCode());
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
		QuoteItemType other = (QuoteItemType) obj;
		if (friendlyId == null) {
			if (other.friendlyId != null)
				return false;
		} else if (!friendlyId.equals(other.friendlyId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (quoteItemTypeId == null) {
			if (other.quoteItemTypeId != null)
				return false;
		} else if (!quoteItemTypeId.equals(other.quoteItemTypeId))
			return false;
		return true;
	}

}
