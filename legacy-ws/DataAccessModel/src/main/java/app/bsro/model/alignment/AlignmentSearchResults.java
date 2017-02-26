/**
 * 
 */
package app.bsro.model.alignment;

import java.math.BigDecimal;

import app.bsro.model.tire.VehicleFitment;

/**
 * @author css105543
 *
 */
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AlignmentSearchResults {

	// alignment pricing data
	private short alignmentPricingId;
    private BigDecimal standardPricing;
    private BigDecimal lifetimePricing;
    private BigDecimal threeYearPricing;
    private long standardArticle;
    private long lifetimeArticle;
    private Long threeYearArticle;
    private String storeType;
    private String district;
    
    private String storeNumber;
    
    // vehicle information
    private VehicleFitment fitment;
    
   
    
    
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public short getAlignmentPricingId() {
		return alignmentPricingId;
	}

	public void setAlignmentPricingId(short alignmentPricingId) {
		this.alignmentPricingId = alignmentPricingId;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getStandardPricing() {
		return standardPricing;
	}

	public void setStandardPricing(BigDecimal standardPricing) {
		this.standardPricing = standardPricing;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getLifetimePricing() {
		return lifetimePricing;
	}

	public void setLifetimePricing(BigDecimal lifetimePricing) {
		this.lifetimePricing = lifetimePricing;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public BigDecimal getThreeYearPricing() {
		return threeYearPricing;
	}

	public void setThreeYearPricing(BigDecimal threeYearPricing) {
		this.threeYearPricing = threeYearPricing;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public long getStandardArticle() {
		return standardArticle;
	}

	public void setStandardArticle(long standardArticle) {
		this.standardArticle = standardArticle;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public long getLifetimeArticle() {
		return lifetimeArticle;
	}

	public void setLifetimeArticle(long lifetimeArticle) {
		this.lifetimeArticle = lifetimeArticle;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getThreeYearArticle() {
		return threeYearArticle;
	}

	public void setThreeYearArticle(Long threeYearArticle) {
		this.threeYearArticle = threeYearArticle;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public VehicleFitment getFitment() {
		return fitment;
	}

	public void setFitment(VehicleFitment fitment) {
		this.fitment = fitment;
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

}
