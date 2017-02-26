package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.vehicle.Fitment;

public interface FitmentDAO extends IGenericOrmDAO<Fitment, Long> {
	
	public Collection<Fitment> findByAcesVehicleId(Long acesVehicleId);
	public Collection<Fitment> findByAcesVehicleIdAndSubModel(Long acesVehicleId, String submodel);
	public Collection<String> findDistinctYears();
	public Collection<Object[]> findDistinctMakes(String modelYear);
	public Collection<Object[]> findDistinctModels(String modelYear, Long makeId);
	public Collection<Object[]> findDistinctSubModels(String modelYear, Long makeId, Long modelId);
	public Collection<Fitment> findByCarTireId(Long carTireId);
	public Collection<Fitment> findByYearMakeModelAndSubModel(String year, String make, String model,String submodel);
	public Collection<Fitment> findByYearMakeIdModelIdAndSubModelId(String year, Long makeId, Long modelId,Long submodelId);
}
