package com.bfrc.dataaccess.model.quote;
 
import java.util.Date;

public class TpTireQuotesLedger implements java.io.Serializable {

    // Fields    
    private Long quoteId;
    private String payload;
    private Date updateDate;
     
	public Long getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Long quoteId) {
		this.quoteId = quoteId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result + ((quoteId == null) ? 0 : quoteId.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
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
		TpTireQuotesLedger other = (TpTireQuotesLedger) obj;
		if (quoteId == null) {
			if (other.quoteId != null)
				return false;
		} else if (!quoteId.equals(other.quoteId))
			return false;		
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		
		return true;
	}

}
