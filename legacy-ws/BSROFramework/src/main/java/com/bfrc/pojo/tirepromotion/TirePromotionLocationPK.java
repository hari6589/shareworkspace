/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bfrc.pojo.tirepromotion;

import java.io.Serializable;

/**
 *
 * @author css
 */
//@Embeddable
public class TirePromotionLocationPK implements Serializable {
    //@Basic(optional = false)
    //@Column(name = "PROMO_ID")
    private long promoId;
    //@Basic(optional = false)
    //@Column(name = "STORE_NUMBER")
    private long storeNumber;

    public TirePromotionLocationPK(long promoId, long storeNumber) {
        this.promoId = promoId;
        this.storeNumber = storeNumber;
    }

    public long getPromoId() {
        return promoId;
    }

    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }

    public long getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(long storeNumber) {
        this.storeNumber = storeNumber;
    }

    //@Override
    public int hashCode() {
        int hash = 0;
        hash += (int) promoId;
        hash += (int) storeNumber;
        return hash;
    }

    //@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TirePromotionLocationPK)) {
            return false;
        }
        TirePromotionLocationPK other = (TirePromotionLocationPK) object;
        if (this.promoId != other.promoId) {
            return false;
        }
        if (this.storeNumber != other.storeNumber) {
            return false;
        }
        return true;
    }

    //@Override
    public String toString() {
        return "com.bfrc.pojo.tirepromotion.TirePromotionLocationPK[promoId=" + promoId + ", storeNumber=" + storeNumber + "]";
    }

}
