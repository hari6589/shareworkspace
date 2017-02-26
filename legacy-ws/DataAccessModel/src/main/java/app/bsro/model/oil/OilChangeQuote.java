package app.bsro.model.oil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.oil.OilChange;
import com.bfrc.dataaccess.model.oil.OilFilter;
import com.bfrc.dataaccess.model.oil.OilType;

public class OilChangeQuote implements Serializable {
	private static final long serialVersionUID = -4322528356737780989L;

	private Long oilChangeQuoteId;
	
	private Long storeNumber;
	
	private Calendar createdDate;
	
	private String firstName;
	
	private String lastName;
	
	private OilVehicle vehicle;
	
	private OilType oilType;
	
	private Oil baseOil;
	
	private Oil additionalOil;
	
	private BigDecimal additionalQuarts;
	
	private OilChange oilChange;
	
	private OilFilter oilFilter;
	
	public BigDecimal baseOilOilChangeAndOilFilterSubtotal;
	
	public BigDecimal additionalOilSubtotal;
	
	public BigDecimal baseOilOilChangeOilFilterAndAdditionalOilSubtotal;

	private BigDecimal baseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion;

	private BigDecimal amountSavedWithPromotion;
	
	private BigDecimal percentSavedWithPromotion;
	

	private BigDecimal total;
	
	public Oil getBaseOil() {
		return baseOil;
	}

	public void setBaseOil(Oil baseOil) {
		this.baseOil = baseOil;
	}

	public Oil getAdditionalOil() {
		return additionalOil;
	}

	public void setAdditionalOil(Oil additionalOil) {
		this.additionalOil = additionalOil;
	}


	public OilChange getOilChange() {
		return oilChange;
	}

	public void setOilChange(OilChange oilChange) {
		this.oilChange = oilChange;
	}

	public OilFilter getOilFilter() {
		return oilFilter;
	}

	public void setOilFilter(OilFilter oilFilter) {
		this.oilFilter = oilFilter;
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


	public OilVehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(OilVehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Long getOilChangeQuoteId() {
		return oilChangeQuoteId;
	}

	public void setOilChangeQuoteId(Long oilChangeQuoteId) {
		this.oilChangeQuoteId = oilChangeQuoteId;
	}

	public BigDecimal getAmountSavedWithPromotion() {
		return amountSavedWithPromotion;
	}

	public void setAmountSavedWithPromotion(BigDecimal amountSavedWithPromotion) {
		this.amountSavedWithPromotion = amountSavedWithPromotion;
	}

	public BigDecimal getPercentSavedWithPromotion() {
		return percentSavedWithPromotion;
	}

	public void setPercentSavedWithPromotion(BigDecimal percentSavedWithPromotion) {
		this.percentSavedWithPromotion = percentSavedWithPromotion;
	}

	public BigDecimal getBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion() {
		return baseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion;
	}

	public void setBaseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion(BigDecimal baseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion) {
		this.baseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion = baseOilOilChangeOilFilterAndAdditionalOilSubtotalWithPromotion;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
