package com.bsro.controller.pricing;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bfrc.framework.dao.vehicle.ListVehiclesOperator;

@Controller
public class SelectOtherStoreBaseController {
	private final Log logger = LogFactory.getLog(SelectOtherStoreBaseController.class);
	
	protected ListVehiclesOperator listVehicles;
	protected String jsonView;
	
	public ListVehiclesOperator getListVehiclesOperator() {
		return listVehicles;
	}
	
	public void setListVehiclesOperator(ListVehiclesOperator listVehicles)
	{
		this.listVehicles = listVehicles;
	}
	
	public String getJSONView() {
		return jsonView;
	}
	
	public void setJSONView(String jsonView) {
		this.jsonView = jsonView;
	}
	
	public String getVehicleInfo(HttpServletRequest request, HttpSession session, Model m, String year, String make, String model, String reset) throws Exception {
		String step = "year";
		HashMap map = new HashMap();
		String modelYear = year;
		if(modelYear != null && !"".equals(modelYear)) {
			map.put("modelYear", modelYear);
			step = "make";
		}
		String makeName = make;
		if(makeName != null && !"".equals(makeName)) {
			map.put("makeName", makeName);
			step = "model";
		}
		String modelName = model;
		if(modelName != null && !"".equals(modelName)) {
			map.put("modelName", modelName);
			step = "submodel";
		}

		// by J.F @05.19.06
		// fix: start over from year when validation fails.   
		if(reset != null && !"".equals(reset)) {
			map = new HashMap();
			step = "year";
		}
		
		String result = listVehicles.operate(map);
		if(step != null) {
			result = step + "-" + result;
			if(step.equals("submodel")) {
				m.addAttribute("tpms", map.get("tpms"));
				m.addAttribute("acesVehicleId", map.get("acesVehicleId"));
			}
		}
		List l = (List)map.get("result");
		java.util.Map out = new LinkedHashMap();
		for(int i=0; i<l.size(); i++) {
			
			if("make".equals(step) || "model".equals(step)) {
				Object[] value = (Object[])l.get(i);
				out.put(value[1], value[0]);
			} else {
				String value = (String)l.get(i);
				out.put(value, value);
			}
			
		}
		m.addAttribute("result", out);
		
		return getJSONView();
	}
}
