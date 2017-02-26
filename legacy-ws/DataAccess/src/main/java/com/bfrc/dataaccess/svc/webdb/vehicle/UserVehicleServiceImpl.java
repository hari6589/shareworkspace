package com.bfrc.dataaccess.svc.webdb.vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.FitmentDAO;
import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.VehicleSearchResult;
import com.bfrc.dataaccess.model.vehicle.UserVehicle;
import com.bfrc.dataaccess.svc.webdb.UserVehicleService;

@Service
public class UserVehicleServiceImpl implements UserVehicleService {

	@Autowired
	private FitmentDAO fitmentDAO;
	
	public VehicleSearchResult listModelYears() {
		return new VehicleSearchResult(new ArrayList<String>(fitmentDAO.findDistinctYears()));
	}

	public VehicleSearchResult listMakes(String year) {	
		return new VehicleSearchResult("make", convertCollectionObjectArray(fitmentDAO.findDistinctMakes(year)));
	}

	public VehicleSearchResult listModels(Long makeId, String year) {
		return new VehicleSearchResult("model", convertCollectionObjectArray(fitmentDAO.findDistinctModels(year, makeId)));
	
	}

	public VehicleSearchResult listSubModels(Long makeId, Long modelId, String year) {
		return new VehicleSearchResult("submodel", convertCollectionObjectArray(fitmentDAO.findDistinctSubModels(year, makeId, modelId)));
	}

	public UserVehicle search(String year, Long makeId, Long modelId, String subModelId) {

		return null;
	}
	
	private List<ValueTextBean> convertCollectionObjectArray(Collection<Object[]> objs) {
		if(objs == null) return null;
		List<ValueTextBean> loResult = new ArrayList<ValueTextBean>(objs.size());
		for(Object[] o : objs) {
			String value = (o[0]).toString();
			String id = (o[1]).toString();
			loResult.add(new ValueTextBean(id, value));
		}
		return loResult;
	}

}
