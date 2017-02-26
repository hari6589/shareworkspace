/**
 * 
 */
package com.bfrc.pojo.creditcard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import com.bfrc.framework.util.DateUtils;
import com.bfrc.pojo.contact.WebSite;

/**
 * @author schowdhury
 *
 */
@JsonIgnoreProperties({ "siteId", "creditCardImage", "website"})
public class CreditCardContent implements Serializable {

	private Integer siteId;
	private BigDecimal minimumPurchaseAmount;
	private Integer gracePeriodInMonths;
	private Date promotionStartDate;
	private Date promotionEndDate;
	private BigDecimal aprInPercent;
	private BigDecimal minimumFinanceCharge;
	private byte[] creditCardImage;
	private WebSite website;
	
	
	public CreditCardContent() {
	}

	public CreditCardContent(Integer siteId) {
		this.siteId = siteId;
	}

	public CreditCardContent(Integer siteId, BigDecimal minimumPurchaseAmount,
			Integer gracePeriodInMonths, Date promotionStartDate,
			Date promotionEndDate, BigDecimal aprInPercent,
			BigDecimal minimumFinanceCharge, byte[] creditCardImage) {
		this.siteId = siteId;
		this.minimumPurchaseAmount = minimumPurchaseAmount;
		this.gracePeriodInMonths = gracePeriodInMonths;
		this.promotionStartDate = promotionStartDate;
		this.promotionEndDate = promotionEndDate;
		this.aprInPercent = aprInPercent;
		this.minimumFinanceCharge = minimumFinanceCharge;
		this.creditCardImage = creditCardImage;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getMinimumPurchaseAmount() {
		return minimumPurchaseAmount;
	}

	public void setMinimumPurchaseAmount(BigDecimal minimumPurchaseAmount) {
		this.minimumPurchaseAmount = minimumPurchaseAmount;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Integer getGracePeriodInMonths() {
		return gracePeriodInMonths;
	}

	public void setGracePeriodInMonths(Integer gracePeriodInMonths) {
		this.gracePeriodInMonths = gracePeriodInMonths;
	}

	@JsonSerialize(using = DateUtils.class)
	public Date getPromotionStartDate() {
		return promotionStartDate;
	}

	public void setPromotionStartDate(Date promotionStartDate) {
		this.promotionStartDate = promotionStartDate;
	}

	@JsonSerialize(using = DateUtils.class)
	public Date getPromotionEndDate() {
		return promotionEndDate;
	}

	public void setPromotionEndDate(Date promotionEndDate) {
		this.promotionEndDate = promotionEndDate;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getAprInPercent() {
		return aprInPercent;
	}

	public void setAprInPercent(BigDecimal aprInPercent) {
		this.aprInPercent = aprInPercent;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public BigDecimal getMinimumFinanceCharge() {
		return minimumFinanceCharge;
	}

	public void setMinimumFinanceCharge(BigDecimal minimumFinanceCharge) {
		this.minimumFinanceCharge = minimumFinanceCharge;
	}

	public byte[] getCreditCardImage() {
		return creditCardImage;
	}

	public void setCreditCardImage(byte[] creditCardImage) {
		this.creditCardImage = creditCardImage;
	}

	public WebSite getWebsite() {
		return website;
	}

	public void setWebsite(WebSite website) {
		this.website = website;
	}
	
	
	
}
