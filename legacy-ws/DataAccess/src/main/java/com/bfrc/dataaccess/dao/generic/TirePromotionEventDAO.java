package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.promotion.TirePromotionEvent;

public interface TirePromotionEventDAO extends IGenericOrmDAO<TirePromotionEvent, Long> {

	public Collection<TirePromotionEvent> findTirePromotionsBySitesAndStoreNumber(String siteName, Long storeNumber, String friendlyId);
	public Collection<TirePromotionEvent> findTirePromotionsBySiteAndStatus(String siteName, String statusFlag, String friendlyId);
	public Collection<Object[]> findDiscountAmountByStoreNumberMinQtyPromoId(long article, long storeNumber, byte minQty, long promoId);
	
}
