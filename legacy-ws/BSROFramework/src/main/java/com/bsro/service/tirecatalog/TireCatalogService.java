package com.bsro.service.tirecatalog;

import com.bsro.databean.tirecatalog.TireDetailDataBean;

public interface TireCatalogService {
	 public TireDetailDataBean getTireDetailData(String tireBrand, String tireName, String tireId, String imgDir, String imgUri);
}
