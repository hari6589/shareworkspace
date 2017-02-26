/*
 * Vehicle.java
 *
 * Created on August 7, 2002, 1:32 PM
 */

package com.bfrc.pojo.tire;

/**
 *
 * @author  eak
 * @version 
 */
public class Vehicle implements java.io.Serializable, Cloneable {

    public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	protected String acesId;
    protected String year;
    protected String make;
    protected String model;
    protected String submodel;
    private String tpms;
    
    /** Creates new Vehicle */
    public Vehicle() {
    }

    public String getAcesId() { return acesId; }
    public String getYear() { return year; }
    public String getMake() { return make; }
    public String getModel() {return model; }
    public String getSubmodel() { return submodel; }

    public void setAcesId(String param) { acesId = param; }
    public void setYear(String param) { year = param; }
    public void setMake(String param) { make = param; }
    public void setModel(String param) { model = param; }
    public void setSubmodel(String param) { submodel = param; }

    public String getTpms() { return tpms;}

	public void setTpms(String tpms) {this.tpms = tpms;}
	public boolean isTpms() {return "1".equals(tpms);}

	
}
