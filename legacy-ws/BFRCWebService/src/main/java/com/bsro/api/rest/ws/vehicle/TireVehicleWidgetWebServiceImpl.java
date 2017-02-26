/**
 * 
 */
package com.bsro.api.rest.ws.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;
import app.bsro.model.error.Errors;

import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bsro.service.tire.TireSizeService;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.core.security.RequireValidToken;

/**
 * @author smoorthy
 *
 */
@Component
public class TireVehicleWidgetWebServiceImpl implements
		TireVehicleWidgetWebService {
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@Autowired
	private TireSizeService tireSizeService;

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getYearOptions(HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		List<String> years = tireVehicleService.getYears();
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		data.put("year", years);
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(data);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getMakeOptions(String year,
			HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		List<TireVehicleMake> makes = tireVehicleService.getMakesByYear(year);
		Map<String, List<TireVehicleMake>> data = new HashMap<String, List<TireVehicleMake>>();
		data.put("make", makes);
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(data);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getModelOptions(String year, String makeId,
			HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		String make = null;
		
		make = tireVehicleService.getMakeNameByMakeId(makeId);
		List<TireVehicleModel> models = tireVehicleService.getModelsByYearAndMakeName(year, make);
		Map<String, List<TireVehicleModel>> data = new HashMap<String, List<TireVehicleModel>>();
		data.put("model", models);
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(data);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSubmodelOptions(String year,
			String makeId, String modelId, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		String make = null;
		String model = null;
		
		make = tireVehicleService.getMakeNameByMakeId(makeId);
		model = tireVehicleService.getModelNameByModelId(modelId);
		List<TireVehicleSubModel> submodels = tireVehicleService.getSubModelsByYearMakeAndModelName(year, make, model);
		Map<String, List<TireVehicleSubModel>> data = new HashMap<String, List<TireVehicleSubModel>>();
		data.put("submodel", submodels);
		
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(data);
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getCrossSectionOptions(HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			Map<String, String> cross = tireSizeService.getCrossSectionOptions();
			List<ValueTextBean> crossResult = new ArrayList<ValueTextBean>();
			for (Map.Entry<String, String> entry : cross.entrySet()) {
				crossResult.add(new ValueTextBean(entry.getKey(), entry.getValue()));
			}
			
			Map<String, List<ValueTextBean>> data = new HashMap<String, List<ValueTextBean>>();
			data.put("cross", crossResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(data);
		} catch (Throwable throwable) {
			return getErrorMessage("Problem retrieving tire size cross sections");
		}
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getAspectRatioOptions(String cross,
			HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			Map<String, String> aspect = tireSizeService.getAspectOptionsByCrossSection(cross);
			List<ValueTextBean> aspectResult = new ArrayList<ValueTextBean>();
			for (Map.Entry<String, String> entry : aspect.entrySet()) {
				aspectResult.add(new ValueTextBean(entry.getKey(), entry.getValue()));
			}
			
			Map<String, List<ValueTextBean>> data = new HashMap<String, List<ValueTextBean>>();
			data.put("aspect", aspectResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(data);
		} catch (Throwable throwable) {
			return getErrorMessage("Problem retrieving tire size aspects");
		}
		
		return response;
	}

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getRimOptions(String cross,
			String aspectRatio, HttpHeaders headers) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		try {
			Map<String, String> rim = tireSizeService.getRimOptionsByCrossSectionAndAspect(cross, aspectRatio);
			List<ValueTextBean> rimResult = new ArrayList<ValueTextBean>();
			for (Map.Entry<String, String> entry : rim.entrySet()) {
				rimResult.add(new ValueTextBean(entry.getKey(), entry.getValue()));
			}
			
			Map<String, List<ValueTextBean>> data = new HashMap<String, List<ValueTextBean>>();
			data.put("rim", rimResult);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
			response.setPayload(data);
		} catch (Throwable throwable) {
			return getErrorMessage("Problem retrieving tire size rims");
		}
		
		return response;
	}
	
	private BSROWebServiceResponse getErrorMessage(String message){
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Errors errors = new Errors();
		errors.getGlobalErrors().add(message);
		response.setErrors(errors);
		response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		response.setPayload(null);
		return response;
	}

}
