package com.bfrc.dataaccess.svc.webdb.gas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.generic.BsroEpaMpgLookupDAO;
import com.bfrc.dataaccess.dao.generic.FitmentDAO;
import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;
import com.bfrc.dataaccess.model.vehicle.Fitment;
import com.bfrc.dataaccess.svc.webdb.EpaMpgService;

@Service
public class EpaMpgServiceImpl implements EpaMpgService {

	@Autowired
	private BsroEpaMpgLookupDAO bsroEpaMpgLookupDao;
	@Autowired
	private FitmentDAO fitmentDao;
	@Resource(name="mpgTransmissions")
	private Map<String, String> mpgTransmissions;
	@Resource(name="mpgDrives")
	private Map<String, String> mpgDrives;
	@Resource(name="mpgFuels")
	private Map<String, String> mpgFuels;
	
	public List<BsroEpaMpgLookup> findByAcesVehicleId(Long acesVehicleId) {
		Collection<Fitment> vehicles = fitmentDao.findByAcesVehicleId(acesVehicleId);
		if(vehicles != null && vehicles.size() > 0) {
			Fitment fitment = vehicles.iterator().next();
			return findByYearMakeModel(fitment.getModelYear(), fitment.getMakeName(), fitment.getModelName());
		} else return null;
	}
	
	public List<BsroEpaMpgLookup> findByYearMakeModel(String year, String make,
			String model) {
		Collection<BsroEpaMpgLookup> epas = bsroEpaMpgLookupDao.findByYearMakeModel(year, StringUtils.trimToEmpty(make).toUpperCase(), StringUtils.trimToEmpty(model).toUpperCase()+"%");
		List<BsroEpaMpgLookup> loResult = new ArrayList<BsroEpaMpgLookup>();
		if(epas == null) return null;
		Iterator<BsroEpaMpgLookup> iter = epas.iterator();
		while(iter.hasNext()) {
			BsroEpaMpgLookup lkup = iter.next();
			decorate(lkup);
			loResult.add(lkup);
		}
		return loResult;
	}
	
	/**
	 * Takes the BsroEpaMpgLookup bean and populates the transmission, drives and fuelType values based on their respective code values
	 * @param lkup
	 */
	private void decorate(BsroEpaMpgLookup lkup) {

		lkup.setTransmission( getTransMission(lkup.getTransmissionCd()) );
		lkup.setDrive( StringUtils.trimToEmpty( mpgDrives.get(lkup.getDriveCd()) ) );
		lkup.setFuelType( StringUtils.trimToEmpty( mpgFuels.get(lkup.getFuelTypeCd()) ) );
		
	}
	
	private String getTransMission(String transmission) {
		if(StringUtils.trimToNull(transmission) == null)
            return "Other";
        String key = transmission;
        if(Character.isDigit(transmission.charAt(transmission.length()-1))){
            String num_speed = transmission.substring(transmission.length()-1);
            key = transmission.substring(0,transmission.length()-1);
            return mpgTransmissions.get(key)+"("+num_speed+"-speed)";
        }
        String s = (String)mpgTransmissions.get(key);
        if(s == null)
            return "Other";
        return s;
	}
	
}
