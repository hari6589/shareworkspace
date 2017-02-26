package com.bsro.service.gas;

import java.io.IOException;
import java.util.List;

import app.bsro.model.gas.stations.StationPrices;

import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;

public interface GasService {
	 public StationPrices findCheapGas(String latitude, String longitude) throws IOException;
	 
	 public List<BsroEpaMpgLookup> findMilesPerGallon(String vehicleYear, String vehicleMake, String vehicleModel) throws IOException;
}
