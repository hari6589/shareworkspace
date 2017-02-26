package com.bfrc.pojo.tire;

/**
 * @author smoorthy
 *
 */
public class FitmentReplica implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long friendlyId;
	private String makeName;
    private String modelName;
    private String submodel;
    private String friendlyMakeName;
    private String friendlyModelName;
    private String friendlySubmodel;
    
    public FitmentReplica() {
    }
    
    public Long getFriendlyId() {
		return friendlyId;
	}

	public void setFriendlyId(Long friendlyId) {
		this.friendlyId = friendlyId;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getSubmodel() {
		return submodel;
	}

	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}

	public String getFriendlyMakeName() {
		return friendlyMakeName;
	}

	public void setFriendlyMakeName(String friendlyMakeName) {
		this.friendlyMakeName = friendlyMakeName;
	}

	public String getFriendlyModelName() {
		return friendlyModelName;
	}

	public void setFriendlyModelName(String friendlyModelName) {
		this.friendlyModelName = friendlyModelName;
	}

	public String getFriendlySubmodel() {
		return friendlySubmodel;
	}

	public void setFriendlySubmodel(String friendlySubmodel) {
		this.friendlySubmodel = friendlySubmodel;
	}
	
	@Override
	public String toString() {
		return "FitmentReplica [friendlyId="+ friendlyId +", makeName=" + makeName 
				+ ", modelName=" + modelName + ", submodel=" + submodel 
				+ ", friendlyMakeName=" + friendlyMakeName + ", friendlyModelName=" + friendlyModelName 
				+ ", friendlySubmodel=" + friendlySubmodel + "]";
	}
}
