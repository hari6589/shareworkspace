package app.bsro.model.maintenance;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@XmlRootElement(name="categories")
public class ScheduledMaintenanceCategories implements Serializable {
	private static final long serialVersionUID = 2250013667791417083L;
	private int required;
	private int periodic;
	private int checks;
	private int milestones;
	
	public ScheduledMaintenanceCategories(){}
	
	public ScheduledMaintenanceCategories(int required, int periodic,
			int checks, int milestones) {
		super();
		this.required = required;
		this.periodic = periodic;
		this.checks = checks;
		this.milestones = milestones;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public int getRequired() {
		return required;
	}
	public void setRequired(int required) {
		this.required = required;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public int getPeriodic() {
		return periodic;
	}
	public void setPeriodic(int periodic) {
		this.periodic = periodic;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public int getChecks() {
		return checks;
	}
	public void setChecks(int checks) {
		this.checks = checks;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public int getMilestones() {
		return milestones;
	}
	public void setMilestones(int milestones) {
		this.milestones = milestones;
	}
	
}
