package com.bfrc.pojo.tire.jda;

// Generated Nov 23, 2007 4:02:41 PM by Hibernate Tools 3.2.0.beta8

/**
 * TiregroupImageId generated by hbm2java
 */
public class TiregroupImageId implements java.io.Serializable {

	// Fields    

	private long tiregroupId;

	private String websource;

	// Constructors

	/** default constructor */
	public TiregroupImageId() {
	}

	/** full constructor */
	public TiregroupImageId(long tiregroupId, String websource) {
		this.tiregroupId = tiregroupId;
		this.websource = websource;
	}

	// Property accessors
	public long getTiregroupId() {
		return this.tiregroupId;
	}

	public void setTiregroupId(long tiregroupId) {
		this.tiregroupId = tiregroupId;
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
		if (!(other instanceof TiregroupImageId))
			return false;
		TiregroupImageId castOther = (TiregroupImageId) other;

		return (this.getTiregroupId() == castOther.getTiregroupId())
				&& ((this.getWebsource() == castOther.getWebsource()) || (this
						.getWebsource() != null
						&& castOther.getWebsource() != null && this
						.getWebsource().equals(castOther.getWebsource())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getTiregroupId();
		result = 37 * result
				+ (getWebsource() == null ? 0 : this.getWebsource().hashCode());
		return result;
	}

}
