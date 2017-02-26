package com.bsro.comparator.tirepromotion;

import java.util.Comparator;

import com.bfrc.pojo.tirepromotion.TirePromotionDisplay;

public class TirePromotionDisplayComparatorByStartDate implements
		Comparator<TirePromotionDisplay> {

	@Override
	public int compare(TirePromotionDisplay o1, TirePromotionDisplay o2) {
		if(o1.getStartDate() == null){
			return -1;
		}else if(o2.getStartDate() == null){
			return 1;
		}else{
			return o1.getStartDate().compareTo(o2.getStartDate());
		}
	}

}
