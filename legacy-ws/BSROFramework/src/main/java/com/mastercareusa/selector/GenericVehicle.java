package com.mastercareusa.selector;

import java.io.Serializable;

public interface GenericVehicle extends Serializable {
    public long getAcesId();
    public int getYear();
    public String getMake();
    public String getModel();
    public String getSubmodel();
	public boolean isTpms();
	
    public void setAcesId(long acesId);
    public void setYear(int year);
    public void setMake(String make);
    public void setModel(String model);
    public void setSubmodel(String submodel);
	public void setTpms(boolean tpms);
}
