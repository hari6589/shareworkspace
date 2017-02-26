package com.bsro.databean.store;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.store.Store;

public class StoreWidgetDataBean implements Serializable {
	private static final long serialVersionUID = 6467555989320828141L;
	
	private transient Log log = LogFactory.getLog(StoreWidgetDataBean.class);
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		// restore the (transient) log - this is unnecessary in newer versions of the commons logging library
		log = LogFactory.getLog(StoreWidgetDataBean.class);
	}
	
	private String zip;
	private String geoPoint;
	private String city;
	
	private String context;

	private StoreDataBean storeDataBean;
	private Store selectedStore;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Store> getStoreList(){
		if (storeDataBean != null) {
			return (List<Store>)storeDataBean.getStoreList();
		}
		return null;
	}
	
	/**
	 * This isn't really meant to be used - it's here to fulfil bean syntax requirements
	 * @param storeList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setStoreList(List<Store> storeList){
		if (storeDataBean == null) {
			storeDataBean = new StoreDataBean();
		}
		storeDataBean.setStoreList(storeList);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<Long, Long> getStoreNumberToMilesFromStartingPointMap(){
		if (storeDataBean != null) {
			return (Map<Long, Long>)storeDataBean.getMappedDistance();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setStoreNumberToMilesFromStartingPointMap(Map<Long, Long> storeNumberToMilesFromStartingPointMap){
		if (storeDataBean == null) {
			storeDataBean = new StoreDataBean();
		}
		storeDataBean.setMappedDistance(storeNumberToMilesFromStartingPointMap);
	}
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}

	public void selectStore(Long selectedStoreNumber) {
		int storeCount = 0;

		List<Store> stores = getStoreList();
		
		if (stores != null && stores.size() > 0) {
			storeCount = stores.size();
		}
		
		if (stores != null && !stores.isEmpty()) {
			Store mySelectedStore = null;
		
			if (storeCount > 0) {
				mySelectedStore = stores.get(0);
			}
			if (!ServerUtil.isNullOrEmpty(selectedStoreNumber)) {
				for (int i = 0; storeCount > 0 && i < stores.size(); i++) {
					if (selectedStoreNumber.equals(stores.get(i).getStoreNumber())) {
						mySelectedStore = stores.get(i);
						break;
					}
				}
			}
			// In case someone feeds us a bad selectedStoreNumber, do nothing
			if (mySelectedStore != null) {
				selectedStoreNumber = mySelectedStore.getStoreNumber();
				setSelectedStore(mySelectedStore);
			} else {
				if (log.isInfoEnabled()) {
					// This is ALL just for troubleshooting purposes...
					StringBuilder storeNumberList = new StringBuilder("");
					if (stores != null) {
						boolean isFirst = true;
						for (Store store : stores) {
							if (!isFirst) {
								storeNumberList.append(",");
							} else {
								isFirst = false;
							}
							storeNumberList.append(store.getStoreNumber());
						}
					}
					
					Writer result = new StringWriter();
				    PrintWriter printWriter = new PrintWriter(result);
				    Exception myExceptionToGenerateAStackTrace = new Exception("The purpose of this exception is simply to generate a stack trace");
				    myExceptionToGenerateAStackTrace.printStackTrace(printWriter);
					
					//log.info("Atempted to select store number "+selectedStoreNumber+" from list of store numbers including "+storeNumberList.toString()+". This was ignored and the selected store did not change. However, such a store number should not have been passed in the first place. The stack trace that led to this is: "+result.toString());
				}
			}
		}
	}
	public StoreDataBean getStoreDataBean() {
		return storeDataBean;
	}
	public void setStoreDataBean(StoreDataBean storeDataBean) {
		this.storeDataBean = storeDataBean;
	}
	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(String geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public void moveSelectedStoreToFront() {
		Store selectedStore = getSelectedStore();
		// move current store to the front
		for(int i=0; getStoreList() != null && i < getStoreList().size(); i++) {
			Store store = getStoreList().get(i);
			if(Long.valueOf(selectedStore.getStoreNumber()) == store.getNumber()) {
				selectedStore = getStoreList().remove(i);
				break;
			}
		}
		getStoreList().add(0, selectedStore);
	}

	public Store getSelectedStore() {
		return selectedStore;
	}

	public void setSelectedStore(Store selectedStore) {
		this.selectedStore = selectedStore;
	}
}
