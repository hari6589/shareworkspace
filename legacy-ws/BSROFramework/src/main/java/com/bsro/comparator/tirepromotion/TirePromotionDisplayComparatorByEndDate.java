package com.bsro.comparator.tirepromotion;

import java.util.Comparator;

import com.bfrc.pojo.tirepromotion.TirePromotionDisplay;

public class TirePromotionDisplayComparatorByEndDate implements
		Comparator<TirePromotionDisplay> {

	@Override
	public int compare(TirePromotionDisplay o1, TirePromotionDisplay o2) {
		if(o1.getEndDate() == null){
			return -1;
		}else if(o2.getEndDate() == null){
			return 1;
		}else{
			return o1.getEndDate().compareTo(o2.getEndDate());
		}		
	}

}
