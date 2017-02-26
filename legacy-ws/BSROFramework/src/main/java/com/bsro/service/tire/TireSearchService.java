package com.bsro.service.tire;

import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;

public interface TireSearchService {

	TirePromotionEvent getSpecialEventPromotion(long storeNumber, Tire front, Tire rear);

}