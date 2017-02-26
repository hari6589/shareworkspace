package com.bfrc.framework.dao.catalog;

import java.util.Map;

import com.bfrc.framework.dao.*;
import com.bfrc.framework.businessdata.BusinessOperatorSupport;

public class ViewProductOperator extends BusinessOperatorSupport {

	private CatalogDAO catalogDAO;
	
	public Object operate(Object o) throws Exception {
		Map m = (Map)o;
		Integer idParam = (Integer)m.get("id");
		String image = (String)m.get("image");
		if(idParam != null) {
			int id = idParam.intValue();
			if(image != null) {
				byte[] imageData = null;
				if("pdf".equals(image))
					imageData = this.catalogDAO.getWarrantyPDF(id);
				if("brand".equals(image))
					imageData = this.catalogDAO.getBrandImage(id);
				else if("fact".equals(image))
					imageData = this.catalogDAO.getFactImage(id);
				else if("tech".equals(image) || "technology".equals(image))
					imageData = this.catalogDAO.getTechnologyImage(id);
				else if("tire".equals(image))
					imageData = this.catalogDAO.getTireImage(id);
				else if("tireName".equals(image))
					imageData = this.catalogDAO.getTireNameImage(id);
				else if("tiregroup".equals(image))
					imageData = this.catalogDAO.getTiregroupImage(id);
				if(imageData == null)
					imageData = new byte[0];
				m.put(RESULT, imageData);
				return "image";
			}
			m.put(RESULT, this.catalogDAO.getProductLinesInCategory(id));
			return "category";
		}
		Integer tire = ((Integer)m.get("tire"));
		m.put(RESULT, this.catalogDAO.getProductLine(tire.intValue()));
		m.put("sizes", this.catalogDAO.getProductsInLine(tire.intValue()));
		return "tire";
	}

	public CatalogDAO getCatalogDAO() {
		return this.catalogDAO;
	}

	public void setCatalogDAO(CatalogDAO catalogDAO) {
		this.catalogDAO = catalogDAO;
	}

	
}
