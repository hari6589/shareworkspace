/**
 * 
 */
package com.bfrc.dataaccess.model.store;

import java.io.Serializable;

/**
 * @author smoorthy
 *
 */
public class StoreMilitary implements Serializable {
	
	private static final long serialVersionUID = 3671335317411546580L;
	private long storeNumber;

    public StoreMilitary() {
    }

    public StoreMilitary(long storeNumber) {
       this.storeNumber = storeNumber;
    }
   
    public long getStoreNumber() {
        return this.storeNumber;
    }
    
    public void setStoreNumber(long storeNumber) {
        this.storeNumber = storeNumber;
    }
    
    @Override
    public int hashCode() {
    	return 36733 + (int)storeNumber;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (this == obj) {
    		return true;
    	}
    	if (obj == null) {
    		return false;
    	}
    	if (!(obj instanceof StoreMilitary)) {
    		return false;
    	}
    	StoreMilitary castObj = (StoreMilitary)obj;
    	
    	return (this.getStoreNumber() == castObj.getStoreNumber());
    }

}
