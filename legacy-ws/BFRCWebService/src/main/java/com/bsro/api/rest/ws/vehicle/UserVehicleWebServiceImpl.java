package com.bsro.api.rest.ws.vehicle;

import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bfrc.dataaccess.model.VehicleSearchResult;
import com.bfrc.dataaccess.svc.webdb.UserVehicleService;
import com.bsro.core.security.RequireValidToken;

@Component
public class UserVehicleWebServiceImpl implements UserVehicleWebService {

	@Autowired
	private UserVehicleService userVehicleService;

	private Logger log = Logger.getLogger(getClass().getName());
	
	@Override
	@RequireValidToken
	public VehicleSearchResult filter(String year, Long makeId, Long modelId, HttpHeaders headers) {

		log.info("{year:"+year+", makeId:"+makeId+", modelId:"+modelId+"}");
		year = StringUtils.trimToNull(year);
		
		VehicleSearchResult loResult = new VehicleSearchResult();
		
		if(year == null) {
			loResult = userVehicleService.listModelYears();
		} else if(year != null && (makeId == null || makeId.longValue() == 0)) {
			loResult = userVehicleService.listMakes(year);
		} else if(year != null && (makeId != null || makeId.longValue() > 0) && (modelId == null || modelId.longValue() == 0)) {
			loResult = userVehicleService.listModels(makeId, year);
		} else {
			loResult = userVehicleService.listSubModels(makeId, modelId, year);
		}
		
		return loResult;
	}
	
	@Override
	@RequireValidToken
	public Long search(String year, Long makeId, Long modelId,
			HttpHeaders headers) {
		// TODO Auto-generated method stub
		return Long.MIN_VALUE;
	}
	
}
