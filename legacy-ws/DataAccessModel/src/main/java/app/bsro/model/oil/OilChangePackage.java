package app.bsro.model.oil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.oil.OilChange;
import com.bfrc.dataaccess.model.oil.OilChangeStorePrice;
import com.bfrc.dataaccess.model.oil.OilFilter;
import com.bfrc.dataaccess.model.oil.OilFilterPrice;
import com.bfrc.dataaccess.model.oil.OilStorePrice;
import com.bfrc.dataaccess.model.oil.OilType;

public class OilChangePackage implements Serializable {
	private static final long serialVersionUID = 1951748242605658574L;

	private OilType oilType;
	
	private Oil baseOil;
	private OilStorePrice baseOilPrice;
	
	private Oil additionalOil;
	private OilStorePrice additionalOilPrice;
	
	private Integer additionalOilQuantity;
	
	private BigDecimal additionalQuarts;
	
	private OilChange oilChange;
	private OilChangeStorePrice oilChangePrice;
	
	private OilFilter oilFilter;
	private OilFilterPrice oilFilterPrice;
	
	private Boolean isRecommended = Boolean.FALSE;
	
	private List<String> climatesForWhichRecommendationApplies;
	
	public BigDecimal baseOilOilChangeAndOilFilterSubtotal;
	
	public BigDecimal additionalOilSubtotal;
	
	public BigDecimal baseOilOilChangeOilFilterAndAdditionalOilSubtotal;

	private BigDecimal baseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion;

	private BigDecimal baseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion;
	
	private BigDecimal baseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion;

	public Oil getBaseOil() {
		return baseOil;
	}

	public void setBaseOil(Oil baseOil) {
		this.baseOil = baseOil;
	}

	public OilStorePrice getBaseOilPrice() {
		return baseOilPrice;
	}

	public void setBaseOilPrice(OilStorePrice baseOilPrice) {
		this.baseOilPrice = baseOilPrice;
	}

	public Oil getAdditionalOil() {
		return additionalOil;
	}

	public void setAdditionalOil(Oil additionalOil) {
		this.additionalOil = additionalOil;
	}

	public OilStorePrice getAdditionalOilPrice() {
		return additionalOilPrice;
	}

	public void setAdditionalOilPrice(OilStorePrice additionalOilPrice) {
		this.additionalOilPrice = additionalOilPrice;
	}

	public Integer getAdditionalOilQuantity() {
		return additionalOilQuantity;
	}

	public void setAdditionalOilQuantity(Integer additionalOilQuantity) {
		this.additionalOilQuantity = additionalOilQuantity;
	}

	public OilChange getOilChange() {
		return oilChange;
	}

	public void setOilChange(OilChange oilChange) {
		this.oilChange = oilChange;
	}

	public OilChangeStorePrice getOilChangePrice() {
		return oilChangePrice;
	}

	public void setOilChangePrice(OilChangeStorePrice oilChangePrice) {
		this.oilChangePrice = oilChangePrice;
	}

	public OilFilter getOilFilter() {
		return oilFilter;
	}

	public void setOilFilter(OilFilter oilFilter) {
		this.oilFilter = oilFilter;
	}

	public OilFilterPrice getOilFilterPrice() {
		return oilFilterPrice;
	}

	public void setOilFilterPrice(OilFilterPrice oilFilterPrice) {
		this.oilFilterPrice = oilFilterPrice;
	}

	public Boolean getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(Boolean isRecommended) {
		this.isRecommended = isRecommended;
	}	
	
	public List<String> getClimatesForWhichRecommendationApplies() {
		return climatesForWhichRecommendationApplies;
	}

	public void setClimatesForWhichRecommendationApplies(List<String> climatesForWhichRecommendationApplies) {
		this.climatesForWhichRecommendationApplies = climatesForWhichRecommendationApplies;
	}	
	

	public BigDecimal getBaseOilOilChangeAndOilFilterSubtotal() {
		return baseOilOilChangeAndOilFilterSubtotal;
	}

	public void setBaseOilOilChangeAndOilFilterSubtotal(BigDecimal baseOilOilChangeAndOilFilterSubtotal) {
		this.baseOilOilChangeAndOilFilterSubtotal = baseOilOilChangeAndOilFilterSubtotal;
	}

	public BigDecimal getAdditionalOilSubtotal() {
		return additionalOilSubtotal;
	}

	public void setAdditionalOilSubtotal(BigDecimal additionalOilSubtotal) {
		this.additionalOilSubtotal = additionalOilSubtotal;
	}

	public BigDecimal getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal() {
		return baseOilOilChangeOilFilterAndAdditionalOilSubtotal;
	}

	public void setBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(BigDecimal baseOilOilChangeOilFilterAndAdditionalOilSubtotal) {
		this.baseOilOilChangeOilFilterAndAdditionalOilSubtotal = baseOilOilChangeOilFilterAndAdditionalOilSubtotal;
	}

	public OilType getOilType() {
		return oilType;
	}

	public void setOilType(OilType oilType) {
		this.oilType = oilType;
	}

	public BigDecimal getAdditionalQuarts() {
		return additionalQuarts;
	}

	public void setAdditionalQuarts(BigDecimal additionalQuarts) {
		this.additionalQuarts = additionalQuarts;
	}

	public BigDecimal getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion() {
		return baseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion;
	}

	public void setBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion(BigDecimal baseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion) {
		this.baseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion = baseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion;
	}

	public BigDecimal getBaseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion() {
		return baseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion;
	}

	public void setBaseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion(
			BigDecimal baseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion) {
		this.baseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion = baseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion;
	}

	public BigDecimal getBaseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion() {
		return baseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion;
	}

	public void setBaseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion(
			BigDecimal baseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion) {
		this.baseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion = baseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion;
	}


}
