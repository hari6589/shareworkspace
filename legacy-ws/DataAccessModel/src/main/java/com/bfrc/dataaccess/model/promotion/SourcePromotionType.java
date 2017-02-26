package com.bfrc.dataaccess.model.promotion;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnore;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class SourcePromotionType {
    private String promoType;
    private String promoTypeDescription;
    private String reducePriceFlag;
    private Set<TirePromotionEvent> tirePromotionEvents = new HashSet<TirePromotionEvent>(0);
    private Set<TirePromotionEventHistory> tirePromotionEventHistories = new HashSet<TirePromotionEventHistory>(0);
	public String getPromoType() {
		return promoType;
	}
	public void setPromoType(String promoType) {
		this.promoType = promoType;
	}
	public String getPromoTypeDescription() {
		return promoTypeDescription;
	}
	public void setPromoTypeDescription(String promoTypeDescription) {
		this.promoTypeDescription = promoTypeDescription;
	}
	@JsonIgnore
	public String getReducePriceFlag() {
		return reducePriceFlag;
	}
	public void setReducePriceFlag(String reducePriceFlag) {
		this.reducePriceFlag = reducePriceFlag;
	}
	
	@JsonIgnore
	public Set<TirePromotionEvent> getTirePromotionEvents() {
		return tirePromotionEvents;
	}
	public void setTirePromotionEvents(Set<TirePromotionEvent> tirePromotionEvents) {
		this.tirePromotionEvents = tirePromotionEvents;
	}
	@JsonIgnore
	public Set<TirePromotionEventHistory> getTirePromotionEventHistories() {
		return tirePromotionEventHistories;
	}
	public void setTirePromotionEventHistories(
			Set<TirePromotionEventHistory> tirePromotionEventHistories) {
		this.tirePromotionEventHistories = tirePromotionEventHistories;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((promoType == null) ? 0 : promoType.hashCode());
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
		SourcePromotionType other = (SourcePromotionType) obj;
		if (promoType == null) {
			if (other.promoType != null)
				return false;
		} else if (!promoType.equals(other.promoType))
			return false;
		return true;
	}

    
}
