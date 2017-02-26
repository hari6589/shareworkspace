package com.bfrc.dataaccess.model.quote;
// Generated Sep 7, 2012 5:06:59 PM by Hibernate Tools 3.1.0.beta4

import java.math.BigDecimal;


/**
 * QuoteItem generated by hbm2java
 */

public class QuoteItem  implements java.io.Serializable {


    // Fields    

     private Long quoteItemId;
     private QuoteItemType quoteItemType;
     private Quote quote;
     private Long articleNumber;
     private String name;
     private Integer sequence;
     private BigDecimal pricePerUnit;
     private Integer quantity;


    // Constructors

    /** default constructor */
    public QuoteItem() {
    }
   
    // Property accessors

    public Long getQuoteItemId() {
        return this.quoteItemId;
    }
    
    public void setQuoteItemId(Long quoteItemId) {
        this.quoteItemId = quoteItemId;
    }

    public QuoteItemType getQuoteItemType() {
        return this.quoteItemType;
    }
    
    public void setQuoteItemType(QuoteItemType quoteItemType) {
        this.quoteItemType = quoteItemType;
    }

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}    
    
    public Long getArticleNumber() {
        return this.articleNumber;
    }
    
    public void setArticleNumber(Long articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Integer getSequence() {
        return this.sequence;
    }
    
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((articleNumber == null) ? 0 : articleNumber.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pricePerUnit == null) ? 0 : pricePerUnit.hashCode());
		result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
		result = prime * result + ((quote == null) ? 0 : quote.hashCode());
		result = prime * result + ((quoteItemId == null) ? 0 : quoteItemId.hashCode());
		result = prime * result + ((quoteItemType == null) ? 0 : quoteItemType.hashCode());
		result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
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
		QuoteItem other = (QuoteItem) obj;
		if (articleNumber == null) {
			if (other.articleNumber != null)
				return false;
		} else if (!articleNumber.equals(other.articleNumber))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pricePerUnit == null) {
			if (other.pricePerUnit != null)
				return false;
		} else if (!pricePerUnit.equals(other.pricePerUnit))
			return false;
		if (quantity == null) {
			if (other.quantity != null)
				return false;
		} else if (!quantity.equals(other.quantity))
			return false;
		if (quote == null) {
			if (other.quote != null)
				return false;
		} else if (!quote.equals(other.quote))
			return false;
		if (quoteItemId == null) {
			if (other.quoteItemId != null)
				return false;
		} else if (!quoteItemId.equals(other.quoteItemId))
			return false;
		if (quoteItemType == null) {
			if (other.quoteItemType != null)
				return false;
		} else if (!quoteItemType.equals(other.quoteItemType))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}
}
