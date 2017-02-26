package app.bsro.model.promotions;

import java.io.Serializable;

import app.bsro.model.promotions.PromotionDisclaimerId;

public class PromotionDisclaimer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private PromotionDisclaimerId id;
	private String website;
    private String landingPageId;
    private String description;
    
    public PromotionDisclaimer(){
    	
    }
    
    public PromotionDisclaimer(PromotionDisclaimerId id, String description){
    	this.id = id;
    	this.description = description;
    }
    
    public PromotionDisclaimerId getId() {
		return this.id;
	}

	public void setId(PromotionDisclaimerId id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
