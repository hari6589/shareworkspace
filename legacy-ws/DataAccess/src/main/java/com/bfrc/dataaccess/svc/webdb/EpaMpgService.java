package com.bfrc.dataaccess.svc.webdb;

import java.util.List;

import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;

public interface EpaMpgService {

	/**
	 * Finds the EPA MPG data based on the Aces Vehicle id passed
	 * @param acesVehicleId
	 * @return
	 */
	public List<BsroEpaMpgLookup> findByAcesVehicleId(Long acesVehicleId);
	
	/**
	 * Finds the EPA MPG data based on the year/make/model passed in.  This data should come from the 
	 * BSRO FITMENT table
	 * @param year
	 * @param make
	 * @param model
	 * @return
	 */
	public List<BsroEpaMpgLookup> findByYearMakeModel(String year, String make, String model);
	
}
