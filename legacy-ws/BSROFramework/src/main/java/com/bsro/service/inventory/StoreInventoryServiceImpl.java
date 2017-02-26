/**
 * 
 */
package com.bsro.service.inventory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bsro.service.appointment.ScheduleAppointmentServiceImpl;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceServiceImpl;

/**
 * @author schowdhu
 *
 */
@Service("storeInventoryService")
public class StoreInventoryServiceImpl extends BSROWebserviceServiceImpl implements StoreInventoryService {
	
	private final Log logger = LogFactory.getLog(ScheduleAppointmentServiceImpl.class);
	private static final String PATH_WEBSERVICE_INVENTORY_INSTORE = "vehicle/tire/store-inventory";
	
	public StoreInventoryServiceImpl() {
		
	}
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}

	@Override
	public Map<Long, StoreInventory> getInventoryInStore(Long storeNumber) {
		
		BSROWebServiceResponse response = null;
		StringBuilder webservicePath = null;
		Map<Long, StoreInventory> inventoryMap = new HashMap<Long, StoreInventory>();
		Map<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("storeNumber", String.valueOf(storeNumber));
		try {
			webservicePath = new StringBuilder(PATH_WEBSERVICE_BASE).append(PATH_DELIMITER).append(PATH_WEBSERVICE_INVENTORY_INSTORE);
			response = (BSROWebServiceResponse) getWebservice(webservicePath.toString(), parameters, BSROWebServiceResponse.class);
		} catch (IOException e) {
			logger.error("Error calling webservice method at path "+ webservicePath + "in method getAvailableDays");
			e.printStackTrace();
		}
		if(response != null && response.getStatusCode().equalsIgnoreCase(BSROWebServiceResponseCode.SUCCESSFUL.name())){
			if(response.getPayload() !=null){
				@SuppressWarnings("unchecked")
				List<LinkedHashMap<String,Object>> results = (List<LinkedHashMap<String,Object>>)response.getPayload();
				if(results!=null && !results.isEmpty()){
					for(LinkedHashMap<String,Object> map : results){
						StoreInventory inventory = new StoreInventory();
						Long articleNo = Long.valueOf(String.valueOf(map.get("articleNumber")));
						Integer qtyOnHand = 0;
						if(map.containsKey("quantityOnHand")) qtyOnHand = (Integer)map.get("quantityOnHand");
						if(qtyOnHand < 0) qtyOnHand = 0;
						Integer qtyOnOrder = 0;
						if(map.containsKey("quantityOnOrder")) qtyOnOrder = (Integer)map.get("quantityOnOrder");
						if(qtyOnOrder < 0) qtyOnOrder = 0;
						inventory.setStoreNumber(storeNumber);
						inventory.setArticleNumber(articleNo);
						inventory.setQuantityOnHand(qtyOnHand);
						inventory.setQuantityOnOrder(qtyOnOrder);
						inventoryMap.put(articleNo, inventory);
					}
				}
				return inventoryMap;
			}
		}
		return null;
	}
}
