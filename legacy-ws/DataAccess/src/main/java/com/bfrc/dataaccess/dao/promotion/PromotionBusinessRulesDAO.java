package com.bfrc.dataaccess.dao.promotion;

import com.bfrc.dataaccess.model.promotion.PromotionBusinessRules;

public interface PromotionBusinessRulesDAO  {

	
	public PromotionBusinessRules findOilPromotionByPriceType(String productSubType);
	
	
	
	
}
