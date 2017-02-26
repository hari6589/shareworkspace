package com.bfrc.pojo.tire.jda;

// Generated Nov 23, 2007 4:02:41 PM by Hibernate Tools 3.2.0.beta8

/**
 * TireWebsourceId generated by hbm2java
 */
public class TireWebsourceId implements java.io.Serializable {

	// Fields    

	private long sku;

	private String websource;

	// Constructors

	/** default constructor */
	public TireWebsourceId() {
	}

	/** full constructor */
	public TireWebsourceId(long sku, String websource) {
		this.sku = sku;
		this.websource = websource;
	}

	// Property accessors
	public long getSku() {
		return this.sku;
	}

	public void setSku(long sku) {
		this.sku = sku;
	}

	public String getWebsource() {
		return this.websource;
	}

	public void setWebsource(String websource) {
		this.websource = websource;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TireWebsourceId))
			return false;
		TireWebsourceId castOther = (TireWebsourceId) other;

		return (this.getSku() == castOther.getSku())
				&& ((this.getWebsource() == castOther.getWebsource()) || (this
						.getWebsource() != null
						&& castOther.getWebsource() != null && this
						.getWebsource().equals(castOther.getWebsource())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getSku();
		result = 37 * result
				+ (getWebsource() == null ? 0 : this.getWebsource().hashCode());
		return result;
	}

}
