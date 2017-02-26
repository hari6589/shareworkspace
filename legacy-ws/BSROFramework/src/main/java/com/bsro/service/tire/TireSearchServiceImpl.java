package com.bsro.service.tire;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.Config;
import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;

@Service("tireSearchService")
public class TireSearchServiceImpl implements TireSearchService {
	
	@Autowired
	TirePromotionDAO tirePromotionDAO;
	
	@Autowired
	Config config;
	

	@Override
	public TirePromotionEvent getSpecialEventPromotion(long storeNumber, Tire front, Tire rear){
		
		List<Long> articleIds = new ArrayList<Long>();
		articleIds.add(front.getArticle());
		if(rear != null){
			articleIds.add(rear.getArticle());
		}		
		
		List<TirePromotionEvent> tirePromotionEvents = tirePromotionDAO.getSpecialTirePromotionEvent(config.getSiteName(), storeNumber, articleIds);
		
		if(tirePromotionEvents.size() > 0){
			//just get the first one, there should only be one.
			return tirePromotionEvents.get(0);
		}
		return null;	
		
	}

}
