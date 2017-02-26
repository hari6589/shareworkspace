package com.bsro.service.LocalPagesService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.pojo.keyvalue.KeyValueCategory;
import com.bfrc.pojo.keyvalue.KeyValueData;
import com.bsro.databean.location.LocationDataBean;
import com.bsro.databean.vehicle.VehicleDataBean;
import com.bsro.service.keyvalue.KeyValueService;

@Service
public class LocalPagesServiceImpl implements LocalPagesService {

	@Autowired
	KeyValueService keyValueService;
	
	@Override
	public List<LocationDataBean> getSpecifiedLocationList(String siteName, String listName) {
		List<KeyValueData> keyValueList = keyValueService.getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(siteName, KeyValueCategory.STATE_CITY_LOCALIZATION_MAPPING_LIST, listName);
		
		List<LocationDataBean> locationList = new ArrayList<LocationDataBean>();
		
		for (KeyValueData keyValue : keyValueList) {
			locationList.add(generateLocationataBeanFromStateCityLocalizationkeyValue(keyValue));
		}
		
		return locationList;
	}

	@Override
	public LocationDataBean findSpecifiedLocationStateAndCityInList(String siteName, String listName, String state, String city) {
		if (state != null && city != null) {
			return generateLocationataBeanFromStateCityLocalizationkeyValue(keyValueService.lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(siteName, KeyValueCategory.STATE_CITY_LOCALIZATION_MAPPING_LIST, listName, state.toLowerCase(), city.toLowerCase()));
		}
		
		return null;
	}

	@Override
	public List<VehicleDataBean> getSpecifiedVehicleList(String siteName, String listName) {
		List<KeyValueData> keyValueList = keyValueService.getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(siteName, KeyValueCategory.MAKE_MODEL_LOCALIZATION_MAPPING_LIST, listName);
		
		List<VehicleDataBean> vehicleList = new ArrayList<VehicleDataBean>();
		
		for (KeyValueData keyValue : keyValueList) {
			vehicleList.add(generateVehicleDataBeanFromMakeModelLocalizationkeyValue(keyValue));
		}
		
		return vehicleList;
	}

	@Override
	public VehicleDataBean findSpecifiedVehicleMakeAndModelInList(String siteName, String listName, String make, String model) {
		if (make != null && model != null) {
			return generateVehicleDataBeanFromMakeModelLocalizationkeyValue(keyValueService.lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(siteName, KeyValueCategory.MAKE_MODEL_LOCALIZATION_MAPPING_LIST, listName, make.toLowerCase(), model.toLowerCase()));
		}
		
		return null;
	}
	
	private VehicleDataBean generateVehicleDataBeanFromMakeModelLocalizationkeyValue(KeyValueData keyValue) {
		if (keyValue != null) {
			VehicleDataBean vehicleDataBean = new VehicleDataBean();
			vehicleDataBean.setMake(keyValue.getParentValue());
			vehicleDataBean.setUrlFriendlyMake(keyValue.getParentAlternateValue1());
			vehicleDataBean.setModel(keyValue.getChildValue());
			vehicleDataBean.setUrlFriendlyModel(keyValue.getChildAlternateValue1());
			return vehicleDataBean;
		}
		
		return null;
	}

	private LocationDataBean generateLocationataBeanFromStateCityLocalizationkeyValue(KeyValueData keyValue) {
		if (keyValue != null) {
			LocationDataBean locationDataBean = new LocationDataBean();
			locationDataBean.setState(keyValue.getParentValue());
			locationDataBean.setUrlFriendlyState(keyValue.getParentAlternateValue1());
			locationDataBean.setCity(keyValue.getChildValue());
			locationDataBean.setUrlFriendlyCity(keyValue.getChildAlternateValue1());
			return locationDataBean;
		}
		
		return null;
	}
}
