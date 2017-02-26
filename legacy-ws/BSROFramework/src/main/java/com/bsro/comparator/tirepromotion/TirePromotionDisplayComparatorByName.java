package com.bsro.comparator.tirepromotion;

import java.util.Comparator;

import com.bfrc.pojo.tirepromotion.TirePromotionDisplay;

public class TirePromotionDisplayComparatorByName implements
		Comparator<TirePromotionDisplay> {

	@Override
	public int compare(TirePromotionDisplay o1, TirePromotionDisplay o2) {		
		return o1.getPromoName().compareToIgnoreCase(o2.getPromoName());
	}

}
