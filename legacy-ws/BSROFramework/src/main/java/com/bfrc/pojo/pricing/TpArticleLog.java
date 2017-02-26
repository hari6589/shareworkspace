package com.bfrc.pojo.pricing;

// Generated Jun 18, 2007 10:31:30 AM by Hibernate Tools 3.2.0.beta8

import java.math.BigDecimal;

/**
 * TpArticleLog generated by hbm2java
 */
public class TpArticleLog implements java.io.Serializable {

	// Fields    

	private long tpArticleId;

	private long tpUserId;

	private long articleNumber;

	private String onSale;

	private BigDecimal retailPrice;

	private BigDecimal balanceWeight;

	private BigDecimal valveStem;

	private BigDecimal exciseTax;

	private BigDecimal tireFee;

	private BigDecimal disposal;

	private BigDecimal balanceLabor;
	
	private BigDecimal installFee;

	//cxs
	private BigDecimal roadHazardAmount;
	
	private BigDecimal shopHazardSupplyAmount;
	
	private BigDecimal retailTaxAmount;
	
	private BigDecimal wheelBalanceTaxAmount;
	
	private BigDecimal valveStemTaxAmount;
	
	private BigDecimal exciseTaxTaxAmount;
	
	private BigDecimal disposalTaxAmount;
	
	private BigDecimal shopHazardSupplyTaxAmount;
	
	private BigDecimal roadHazardTaxAmount;
	
	private BigDecimal total;
	
	  //cxs
	//Rear Tire attributes
	private Long rearArticle;

	private String rearOnSale;

	private BigDecimal rearRetailPrice;

	private BigDecimal rearBalanceWeight;

	private BigDecimal rearValveStem;

	private BigDecimal rearExciseTax;

	private BigDecimal rearTireFee;

	private BigDecimal rearDisposal;

	private BigDecimal rearBalanceLabor;
	
	private BigDecimal rearInstallFee;
	
	//tires offers cxs
    private BigDecimal frontDiscountAmount;
	
	private BigDecimal rearDiscountAmount;
	
	private Long promoId;
	
	//mowaa donation cxs
    private BigDecimal mowaaAmount;
    
    private BigDecimal tpmsVskAmount;
    private BigDecimal tpmsLaborAmount;
    private Long tpmsVskArticleNumber;
    private Long tpmsLaborArticleNumber;
    private BigDecimal tpmsVskTaxAmount;
    
    private String donationName;
    private BigDecimal donationAmount;
    private Long donationArticle;
    
	// Constructors

	/** default constructor */
	public TpArticleLog() {
	}

	/** full constructor version before ecommerce*/
	public TpArticleLog(long tpArticleId, long tpUserId, int articleNumber,
			String onSale, BigDecimal retailPrice, BigDecimal balanceWeight,
			BigDecimal valveStem, BigDecimal exciseTax, BigDecimal tireFee,
			BigDecimal disposal, BigDecimal balanceLabor, BigDecimal installFee) {
		this.tpArticleId = tpArticleId;
		this.tpUserId = tpUserId;
		this.articleNumber = articleNumber;
		this.onSale = onSale;
		this.retailPrice = retailPrice;
		this.balanceWeight = balanceWeight;
		this.valveStem = valveStem;
		this.exciseTax = exciseTax;
		this.tireFee = tireFee;
		this.disposal = disposal;
		this.balanceLabor = balanceLabor;
		this.installFee = installFee;
	}

	
	/** full constructor */
	public TpArticleLog(long tpArticleId, long tpUserId, int articleNumber,
			String onSale, BigDecimal retailPrice, BigDecimal balanceWeight,
			BigDecimal valveStem, BigDecimal exciseTax, BigDecimal tireFee,
			BigDecimal disposal, BigDecimal balanceLabor, BigDecimal installFee,
			BigDecimal roadHazardAmount,BigDecimal shopHazardSupplyAmount,BigDecimal retailTaxAmount,
			BigDecimal wheelBalanceTaxAmount,BigDecimal valveStemTaxAmount,BigDecimal exciseTaxTaxAmount,
			BigDecimal disposalTaxAmount,BigDecimal shopHazardSupplyTaxAmount,BigDecimal roadHazardTaxAmount,
			BigDecimal total) {
		this.tpArticleId = tpArticleId;
		this.tpUserId = tpUserId;
		this.articleNumber = articleNumber;
		this.onSale = onSale;
		this.retailPrice = retailPrice;
		this.balanceWeight = balanceWeight;
		this.valveStem = valveStem;
		this.exciseTax = exciseTax;
		this.tireFee = tireFee;
		this.disposal = disposal;
		this.balanceLabor = balanceLabor;
		this.installFee = installFee;
		this.roadHazardAmount = roadHazardAmount;
		this.shopHazardSupplyAmount = shopHazardSupplyAmount;
		this.retailTaxAmount = retailTaxAmount;
		this.wheelBalanceTaxAmount = wheelBalanceTaxAmount;
		this.valveStemTaxAmount = valveStemTaxAmount;
		this.exciseTaxTaxAmount = exciseTaxTaxAmount;
		this.disposalTaxAmount = disposalTaxAmount;
		this.shopHazardSupplyTaxAmount = shopHazardSupplyTaxAmount;
		this.roadHazardTaxAmount = roadHazardTaxAmount;
		this.total = total;
	}

	// Property accessors
	public long getTpArticleId() {
		return this.tpArticleId;
	}

	public void setTpArticleId(long tpArticleId) {
		this.tpArticleId = tpArticleId;
	}

	public long getTpUserId() {
		return this.tpUserId;
	}

	public void setTpUserId(long tpUserId) {
		this.tpUserId = tpUserId;
	}

	public long getArticleNumber() {
		return this.articleNumber;
	}

	public void setArticleNumber(long articleNumber) {
		this.articleNumber = articleNumber;
	}

	public String getOnSale() {
		return this.onSale;
	}

	public void setOnSale(String onSale) {
		this.onSale = onSale;
	}

	public BigDecimal getRetailPrice() {
		return this.retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getBalanceWeight() {
		return this.balanceWeight;
	}

	public void setBalanceWeight(BigDecimal balanceWeight) {
		this.balanceWeight = balanceWeight;
	}

	public BigDecimal getValveStem() {
		return this.valveStem;
	}

	public void setValveStem(BigDecimal valveStem) {
		this.valveStem = valveStem;
	}

	public BigDecimal getExciseTax() {
		return this.exciseTax;
	}

	public void setExciseTax(BigDecimal exciseTax) {
		this.exciseTax = exciseTax;
	}

	public BigDecimal getTireFee() {
		return this.tireFee;
	}

	public void setTireFee(BigDecimal tireFee) {
		this.tireFee = tireFee;
	}

	public BigDecimal getDisposal() {
		return this.disposal;
	}

	public void setDisposal(BigDecimal disposal) {
		this.disposal = disposal;
	}

	public BigDecimal getBalanceLabor() {
		return this.balanceLabor;
	}

	public void setBalanceLabor(BigDecimal balanceLabor) {
		this.balanceLabor = balanceLabor;
	}

	public BigDecimal getInstallFee() {
		return this.installFee;
	}

	public void setInstallFee(BigDecimal installFee) {
		this.installFee = installFee;
	}

	
//cxs add Rear Tire
	
	public Long getRearArticle() {
		return this.rearArticle;
	}

	public void setRearArticle(Long rearArticle) {
		this.rearArticle = rearArticle;
	}

	public String getRearOnSale() {
		return this.rearOnSale;
	}

	public void setRearOnSale(String rearOnSale) {
		this.rearOnSale = rearOnSale;
	}

	public BigDecimal getRearRetailPrice() {
		return this.rearRetailPrice;
	}

	public void setRearRetailPrice(BigDecimal rearRetailPrice) {
		this.rearRetailPrice = rearRetailPrice;
	}

	public BigDecimal getRearBalanceWeight() {
		return this.rearBalanceWeight;
	}

	public void setRearBalanceWeight(BigDecimal rearBalanceWeight) {
		this.rearBalanceWeight = rearBalanceWeight;
	}

	public BigDecimal getRearValveStem() {
		return this.rearValveStem;
	}

	public void setRearValveStem(BigDecimal rearValveStem) {
		this.rearValveStem = rearValveStem;
	}

	public BigDecimal getRearExciseTax() {
		return this.rearExciseTax;
	}

	public void setRearExciseTax(BigDecimal rearExciseTax) {
		this.rearExciseTax = rearExciseTax;
	}

	public BigDecimal getRearTireFee() {
		return this.rearTireFee;
	}

	public void setRearTireFee(BigDecimal rearTireFee) {
		this.rearTireFee = rearTireFee;
	}

	public BigDecimal getRearDisposal() {
		return this.rearDisposal;
	}

	public void setRearDisposal(BigDecimal rearDisposal) {
		this.rearDisposal = rearDisposal;
	}

	public BigDecimal getRearBalanceLabor() {
		return this.rearBalanceLabor;
	}

	public void setRearBalanceLabor(BigDecimal rearBalanceLabor) {
		this.rearBalanceLabor = rearBalanceLabor;
	}

	public BigDecimal getRearInstallFee() {
		return this.rearInstallFee;
	}

	public void setRearInstallFee(BigDecimal rearInstallFee) {
		this.rearInstallFee = rearInstallFee;
	}
	
	
	public BigDecimal getRoadHazardAmount() {
		return this.roadHazardAmount;
	}

	public void setRoadHazardAmount(BigDecimal roadHazardAmount) {
		this.roadHazardAmount = roadHazardAmount;
	}
	
	public BigDecimal getShopHazardSupplyAmount() {
		return this.shopHazardSupplyAmount;
	}
	
	public void setShopHazardSupplyAmount(BigDecimal shopHazardSupplyAmount) {
		this.shopHazardSupplyAmount = shopHazardSupplyAmount;
	}
	

	public BigDecimal getRetailTaxAmount() {
		return this.retailTaxAmount;
	}
	
	public void setRetailTaxAmount(BigDecimal retailTaxAmount) {
		this.retailTaxAmount = retailTaxAmount;
	}
	
	public BigDecimal getWheelBalanceTaxAmount() {
		return this.wheelBalanceTaxAmount;
	}

	public void setWheelBalanceTaxAmount(BigDecimal wheelBalanceTaxAmount) {
		this.wheelBalanceTaxAmount = wheelBalanceTaxAmount;
	}
	
	public BigDecimal getValveStemTaxAmount() {
		return this.valveStemTaxAmount;
	}

	public void setValveStemTaxAmount(BigDecimal valveStemTaxAmount) {
		this.valveStemTaxAmount = valveStemTaxAmount;
	}
	
	public BigDecimal getExciseTaxTaxAmount() {
		return this.exciseTaxTaxAmount;
	}

	public void setExciseTaxTaxAmount(BigDecimal exciseTaxTaxAmount) {
		this.exciseTaxTaxAmount = exciseTaxTaxAmount;
	}
	
	public BigDecimal getDisposalTaxAmount() {
		return this.disposalTaxAmount;
	}

	public void setDisposalTaxAmount(BigDecimal disposalTaxAmount) {
		this.disposalTaxAmount = disposalTaxAmount;
	}
	
	public BigDecimal getShopHazardSupplyTaxAmount() {
		return this.shopHazardSupplyTaxAmount;
	}

	public void setShopHazardSupplyTaxAmount(BigDecimal shopHazardSupplyTaxAmount) {
		this.shopHazardSupplyTaxAmount = shopHazardSupplyTaxAmount;
	}
	
	public BigDecimal getRoadHazardTaxAmount() {
		return this.roadHazardTaxAmount;
	}

	public void setRoadHazardTaxAmount(BigDecimal roadHazardTaxAmount) {
		this.roadHazardTaxAmount = roadHazardTaxAmount;
	}
	
	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getFrontDiscountAmount() {
		return this.frontDiscountAmount;
	}

	public void setFrontDiscountAmount(BigDecimal frontDiscountAmount) {
		this.frontDiscountAmount = frontDiscountAmount;
	}
	
	public BigDecimal getRearDiscountAmount() {
		return this.rearDiscountAmount;
	}

	public void setRearDiscountAmount(BigDecimal rearDiscountAmount) {
		this.rearDiscountAmount = rearDiscountAmount;
	}
	
	public Long getPromoId() {
		return this.promoId;
	}

	public void setPromoId(Long promoId) {
		this.promoId = promoId;
	}
	
	public BigDecimal getMowaaAmount() {
		return this.mowaaAmount;
	}

	public void setMowaaAmount(BigDecimal mowaaAmount) {
		this.mowaaAmount = mowaaAmount;
	}
	
    public BigDecimal getTpmsVskAmount() {
        return this.tpmsVskAmount;
    }
    
    public void setTpmsVskAmount(BigDecimal tpmsVskAmount) {
        this.tpmsVskAmount = tpmsVskAmount;
    }
    public BigDecimal getTpmsLaborAmount() {
        return this.tpmsLaborAmount;
    }
    
    public void setTpmsLaborAmount(BigDecimal tpmsLaborAmount) {
        this.tpmsLaborAmount = tpmsLaborAmount;
    }
    
    public Long getTpmsVskArticleNumber() {
        return this.tpmsVskArticleNumber;
    }
    
    public void setTpmsVskArticleNumber(Long tpmsVskArticleNumber) {
        this.tpmsVskArticleNumber = tpmsVskArticleNumber;
    }
    public Long getTpmsLaborArticleNumber() {
        return this.tpmsLaborArticleNumber;
    }
    
    public void setTpmsLaborArticleNumber(Long tpmsLaborArticleNumber) {
        this.tpmsLaborArticleNumber = tpmsLaborArticleNumber;
    }
    
    public BigDecimal getTpmsVskTaxAmount() {
        return this.tpmsVskTaxAmount;
    }
    
    public void setTpmsVskTaxAmount(BigDecimal tpmsVskTaxAmount) {
        this.tpmsVskTaxAmount = tpmsVskTaxAmount;
    }
    
    public String getDonationName() {
        return this.donationName;
    }
    
    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }
    public BigDecimal getDonationAmount() {
        return this.donationAmount;
    }
    
    public void setDonationAmount(BigDecimal donationAmount) {
        this.donationAmount = donationAmount;
    }
    
    public Long getDonationArticle() {
        return this.donationArticle;
    }
    
    public void setDonationArticle(Long donationArticle) {
        this.donationArticle = donationArticle;
    }
}
