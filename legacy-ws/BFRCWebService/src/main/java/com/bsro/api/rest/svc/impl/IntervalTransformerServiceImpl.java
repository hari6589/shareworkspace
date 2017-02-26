package com.bsro.api.rest.svc.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import app.bsro.model.maintenance.ServiceMaintenanceTO;

import com.bfrc.dataaccess.model.maintenance.Intervals;
import com.bsro.api.rest.svc.TransformerService;

public class IntervalTransformerServiceImpl implements TransformerService<ServiceMaintenanceTO, Intervals> {

	public ServiceMaintenanceTO transform(Intervals o) {
		if(o == null || o.getId() == null) 
			return null;
		
		ServiceMaintenanceTO to = new ServiceMaintenanceTO();
		to.setAcesVehicleId(o.getId().getAcesVehicleId());
		to.setServiceType(StringUtils.trimToEmpty(o.getId().getServiceType()));
		to.setMileageInterval(o.getId().getMileageInterval());
		if(o.getComponents() != null)
			to.setComponentTxt(StringUtils.trimToEmpty(o.getComponents().getComponent()));
		if(o.getOperationTypes() != null)
			to.setOperationTypeTxt(StringUtils.trimToEmpty(o.getOperationTypes().getOperationType()));
		
		to.setFootnoteTxt(o.getFootnoteTxt());
		to.setTimeInterval(o.getTimeInterval());
		to.setTimeUnits(o.getTimeUnits());
		return to;
	}
	
	public List<ServiceMaintenanceTO> transform(List<Intervals> o) {
		if(o == null) 
			return null;
		List<ServiceMaintenanceTO> tos = new ArrayList<ServiceMaintenanceTO>(o.size());
		for(Intervals i : o) {
			tos.add(transform(i));
		}
		return tos;
	}
}
