package com.bfrc.pojo.appointment.rules;

// Generated Aug 2, 2012 12:55:53 PM by Hibernate Tools 3.4.0.CR1

/**
 * AppointmentRuleSiteId generated by hbm2java
 */
public class AppointmentRuleSiteId implements java.io.Serializable {

	private int appointmentRuleId;
	private int siteId;

	public AppointmentRuleSiteId() {
	}

	public AppointmentRuleSiteId(int appointmentRuleId, int siteId) {
		this.appointmentRuleId = appointmentRuleId;
		this.siteId = siteId;
	}

	public int getAppointmentRuleId() {
		return this.appointmentRuleId;
	}

	public void setAppointmentRuleId(int appointmentRuleId) {
		this.appointmentRuleId = appointmentRuleId;
	}

	public int getSiteId() {
		return this.siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AppointmentRuleSiteId))
			return false;
		AppointmentRuleSiteId castOther = (AppointmentRuleSiteId) other;

		return (this.getAppointmentRuleId() == castOther.getAppointmentRuleId())
				&& (this.getSiteId() == castOther.getSiteId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAppointmentRuleId();
		result = 37 * result + this.getSiteId();
		return result;
	}

}
