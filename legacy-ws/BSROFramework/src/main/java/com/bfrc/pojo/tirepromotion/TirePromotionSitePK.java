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
public class TirePromotionSitePK implements Serializable {
    //@Basic(optional = false)
    //@Column(name = "PROMO_ID")
    private long promoId;
    //@Basic(optional = false)
    //@Column(name = "SITE_NAME")
    private String siteName;

    public TirePromotionSitePK(long promoId, String siteName) {
        this.promoId = promoId;
        this.siteName = siteName;
    }

    public long getPromoId() {
        return promoId;
    }

    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    //@Override
    public int hashCode() {
        int hash = 0;
        hash += (int) promoId;
        hash += (siteName != null ? siteName.hashCode() : 0);
        return hash;
    }

    //@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TirePromotionSitePK)) {
            return false;
        }
        TirePromotionSitePK other = (TirePromotionSitePK) object;
        if (this.promoId != other.promoId) {
            return false;
        }
        if ((this.siteName == null && other.siteName != null) || (this.siteName != null && !this.siteName.equals(other.siteName))) {
            return false;
        }
        return true;
    }

    //@Override
    public String toString() {
        return "com.bfrc.pojo.tirepromotion.TirePromotionSitePK[promoId=" + promoId + ", siteName=" + siteName + "]";
    }

}
