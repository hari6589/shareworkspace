package app.bsro.model.promotions;

import java.io.Serializable;

public class PromotionDisclaimerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String website;
    private String landingPageId;
    
    public PromotionDisclaimerId() {
    	
    }
    
    public PromotionDisclaimerId(String website, String landingPageId) {
		this.setWebsite(website);
		this.setLandingPageId(landingPageId);
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the landingPageId
	 */
	public String getLandingPageId() {
		return landingPageId;
	}

	/**
	 * @param landingPageId the landingPageId to set
	 */
	public void setLandingPageId(String landingPageId) {
		this.landingPageId = landingPageId;
	}
	
	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getLandingPageId() == null ? 0 : this.getLandingPageId()
						.hashCode());
		
		result = 37 * result
				+ (getWebsite() == null ? 0 : this.getWebsite().hashCode());
		
		return result;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PromotionDisclaimerId))
			return false;
		PromotionDisclaimerId castOther = (PromotionDisclaimerId) other;

		return ((this.getLandingPageId() == castOther.getLandingPageId()) || (this
				.getLandingPageId() != null
				&& castOther.getLandingPageId() != null && this.getLandingPageId()
				.equals(castOther.getLandingPageId())))
				&& ((this.getWebsite() == castOther.getWebsite()) || (this
						.getWebsite() != null
						&& castOther.getWebsite() != null && this
						.getWebsite().equals(castOther.getWebsite())));
	}
}
