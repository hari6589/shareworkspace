package com.bsro.comparator.tirepromotion;

import java.util.Comparator;

import com.bfrc.pojo.tirepromotion.TirePromotionDisplay;

public class TirePromotionDisplayComparatorById implements
		Comparator<TirePromotionDisplay> {

	@Override
	public int compare(TirePromotionDisplay o1, TirePromotionDisplay o2) {
		long promoId1 = o1.getPromoId();
		long promoId2 = o2.getPromoId();
		if(promoId1 < promoId2){
			return -1;
		}else if(promoId1 > promoId2){
			return 1;
		}else return 0;
	}

}
