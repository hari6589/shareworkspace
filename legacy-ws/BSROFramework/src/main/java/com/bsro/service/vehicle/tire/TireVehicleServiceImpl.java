package com.bsro.service.vehicle.tire;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.VehicleDAO;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.rebrand.BSROWebserviceServiceImpl;

@Service
public class TireVehicleServiceImpl extends BSROWebserviceServiceImpl implements TireVehicleService {
	
	@Autowired
	private VehicleDAO vehicleDao;
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}
	
	@Override
	public Map<String, String> getYearOptions() throws Exception {
		@SuppressWarnings("unchecked")
		List<String> years = (List<String>)vehicleDao.getYearList();
		
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (years != null) {
			for (String year : years) {
				options.put(year, year);
			}
		}
		
		return options;
	}

	@Override
	public Map<String, String> getMakeOptionsByYear(String year) throws Exception {
		List<TireVehicleMake> makes = vehicleDao.getMakesByYear(year);
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (makes != null) {
			for (TireVehicleMake make : makes) {
				options.put(make.getName(), make.getName());
			}
		}
		
		return options;
	}

	@Override
	public Map<String, String> getModelOptionsByYearAndMakeName(String year, String makeName) throws Exception {
		List<TireVehicleModel> models = vehicleDao.getModelsByYearAndMakeName(year, makeName);
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (models != null) {
			for (TireVehicleModel model : models) {
				options.put(model.getName(), model.getName());
			}
		}
		
		return options;
	}

	@Override
	public Map<String, String> getSubModelOptionsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception {
		List<TireVehicleSubModel> submodels = vehicleDao.getSubModelsByYearAndMakeNameAndModelName(year, makeName, modelName);

		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (submodels != null) {
			for (TireVehicleSubModel submodel : submodels) {
				options.put(submodel.getName(), submodel.getName());
			}
		}
		
		return options;
	}

	@Override
	public List<TireVehicle> getVehiclesByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception {
		List<TireVehicle> results = vehicleDao.getVehiclesByYearMakeNameModelName(year, makeName, modelName);
		
		if (results != null) {
			return results;
		} else {
			return new ArrayList<TireVehicle>();
		}
	}
	
	@Override
	public Map<String, String> getMakeOptions() throws Exception {
		List<TireVehicleMake> makes = vehicleDao.getAllMakes();
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (makes != null) {
			for (TireVehicleMake make : makes) {
				options.put(make.getName(), make.getName());
			}
		}
		
		return options;
	}
	
	@Override
	public Map<String, String> getModelOptionsByMakeName(String makeName) throws Exception {
		List<TireVehicleModel> models = vehicleDao.getModelsByMakeName(makeName);
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (models != null) {
			for (TireVehicleModel model : models) {
				options.put(model.getName(), model.getName());
			}
		}
		
		return options;		
	}
	
	@Override
	public Map<String, String> getSubModelOptionsByMakeNameAndModelName(String makeName, String modelName) throws Exception {
		List<TireVehicleSubModel> submodels = vehicleDao.getSubModelsByMakeNameAndModelName(makeName, modelName);

		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (submodels != null) {
			for (TireVehicleSubModel submodel : submodels) {
				options.put(submodel.getName(), submodel.getName());
			}
		}
		
		return options;
	}
	
	@Override
	public List<TireVehicle> getVehiclesByMakeNameModelNameSubmodelName(String makeName, String modelName, String submodelName) throws Exception {
		List<TireVehicle> results = vehicleDao.getVehiclesByMakeNameModelNameSubmodelName(makeName, modelName, submodelName);
		
		if (results != null) {
			return results;
		} else {
			return new ArrayList<TireVehicle>();
		}
	}
}
