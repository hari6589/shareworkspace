package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.promotion.Promotion;

public interface PromotionDAO extends IGenericOrmDAO<Promotion, Long> {

	public Collection<Promotion> findBySiteStartExpirationAndType(String webSite, String friendly_id,  String startDate, 
			String expirationDate, String promoType, String friendlyId);
	
	public Collection<Promotion> findBySiteStartExpirationAndFriendlyId(String webSite, String startDate, 
			String expirationDate, String friendlyId);

	public Collection<Promotion> findByLandingPageId(String webSite, String startDate, 
			String expirationDate, String string, String friendlyId, String promotionType);
	
	public Collection<Promotion> findActiveRepairOffers(String webSite, String friendly_id, String startDate, 
			String expirationDate, String landingPageId, String friendlyId);
	
	public Collection<Promotion> findActiveMaintenanceOffers(String webSite, String friendly_id, String startDate, 
			String expirationDate, String landingPageId, String friendlyId);
	
}
