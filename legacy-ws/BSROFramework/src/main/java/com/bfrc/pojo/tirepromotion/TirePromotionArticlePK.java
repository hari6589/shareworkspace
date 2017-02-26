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
public class TirePromotionArticlePK implements Serializable {
    //@Basic(optional = false)
    //@Column(name = "PROMO_ID")
    private long promoId;
    //@Basic(optional = false)
    //@Column(name = "ARTICLE")
    private long article;

    public TirePromotionArticlePK(long promoId, long article) {
        this.promoId = promoId;
        this.article = article;
    }

    public long getPromoId() {
        return promoId;
    }

    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }

    //@Override
    public int hashCode() {
        int hash = 0;
        hash += (int) promoId;
        hash += (int) article;
        return hash;
    }

    //@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TirePromotionArticlePK)) {
            return false;
        }
        TirePromotionArticlePK other = (TirePromotionArticlePK) object;
        if (this.promoId != other.promoId) {
            return false;
        }
        if (this.article != other.article) {
            return false;
        }
        return true;
    }

    //@Override
    public String toString() {
        return "com.bfrc.pojo.tirepromotion.TirePromotionArticlePK[promoId=" + promoId + ", article=" + article + "]";
    }

}
