package com.bfrc.dataaccess.svc.webdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.WebServiceRequestsDAO;
import com.bfrc.dataaccess.model.system.BsroWebServiceRequests;

@Service
public class WebServiceRequestServiceImpl implements WebServiceRequestsService {

	@Autowired
	private WebServiceRequestsDAO webServiceRequestsDao;
	
	public enum WebServiceId {
		NCD_WEB_SERVICE(1);
		
		private Integer id;
		private WebServiceId(Integer id) {
			this.id = id;
		}
		public Integer getId() {
			return this.id;
		}
	}
	
	public enum ServiceCode {
		
		ACTIVE("Y"),
		INACTIVE("N");
		
		private String code;
		private ServiceCode(String code) {
			this.code = code;
		}
		public String getCode() {
			return this.code;
		}
	}
	
	public void save(BsroWebServiceRequests record) {
		
//		BsroWebServiceRequests rqsts = getRequest(WebServiceId.valueOf(record.getWebServiceId().toString()));
//		if(rqsts != null) {
//			rqsts.setWebServiceCode(record.getWebServiceCode());
//			rqsts.setWebServiceMessage(record.getWebServiceMessage());
//			webServiceRequestsDao.save(rqsts);
//		} else {
//			throw new RuntimeException("The WEB_SERVICE_ID ("+record.getWebServiceId()+") is not a valid value.");
//		}
//		WebServiceId id = WebServiceId.valueOf(record.getWebServiceId().toString());
//		if(id == null) throw new RuntimeException("The WEB_SERVICE_ID value ("+record.getWebServiceId()+") is not valid.");
		
		
		webServiceRequestsDao.save(record);
	}
	
	public BsroWebServiceRequests getRequest(WebServiceId id) {
		return webServiceRequestsDao.get(id.getId());
	}
}
