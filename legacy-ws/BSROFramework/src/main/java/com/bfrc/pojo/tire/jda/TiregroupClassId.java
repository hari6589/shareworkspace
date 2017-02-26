package com.bfrc.pojo.tire.jda;

// Generated Nov 23, 2007 4:02:41 PM by Hibernate Tools 3.2.0.beta8

/**
 * TiregroupClassId generated by hbm2java
 */
public class TiregroupClassId implements java.io.Serializable {

	// Fields    

	private long tiregroupId;

	private long classId;

	// Constructors

	/** default constructor */
	public TiregroupClassId() {
	}

	/** full constructor */
	public TiregroupClassId(long tiregroupId, long classId) {
		this.tiregroupId = tiregroupId;
		this.classId = classId;
	}

	// Property accessors
	public long getTiregroupId() {
		return this.tiregroupId;
	}

	public void setTiregroupId(long tiregroupId) {
		this.tiregroupId = tiregroupId;
	}

	public long getClassId() {
		return this.classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TiregroupClassId))
			return false;
		TiregroupClassId castOther = (TiregroupClassId) other;

		return (this.getTiregroupId() == castOther.getTiregroupId())
				&& (this.getClassId() == castOther.getClassId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getTiregroupId();
		result = 37 * result + (int) this.getClassId();
		return result;
	}

}
