package com.bsro.service.tire;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.SeoVehicleDataDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;
import com.bsro.databean.vehicle.TireSize;
import com.bsro.databean.vehicle.RimDiameter;
import com.bsro.databean.vehicle.FriendlySizeDataBean;
import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;

@Service
public class TireSizeServiceImpl implements TireSizeService {
	
	@Autowired
	private VehicleDAO vehicleDao;
	
	@Autowired
	private SeoVehicleDataDAO seoVehicleDataDAO;
	
	@Override
	public Map<String, String> getCrossSectionOptions() throws Exception {
		return vehicleDao.getVehicleCrossSections();
	}

	@Override
	public Map<String, String> getAspectOptionsByCrossSection(String crossSection) throws Exception {
		return vehicleDao.getVehicleAspects(crossSection);
	}

	@Override
	public Map<String, String> getRimOptionsByCrossSectionAndAspect(String crossSection, String aspect) throws Exception {
		return vehicleDao.getVehicleRims(crossSection, aspect);
	}
	
	@Override
	public Map<String, String> getRimOptions() throws Exception {
		return vehicleDao.getVehicleRims();
	}
	
	@Override
	public List<RimDiameter> getRimDiameters() throws Exception{
		Map<String, String> rimOptions = vehicleDao.getVehicleRims();
		return getRimDiameters(rimOptions);
	}
	
	@Override
	public List<RimDiameter> getRimDiameters(String siteName) throws Exception{
		Map<String, String> rimOptions = vehicleDao.getVehicleRims(siteName);
		return getRimDiameters(rimOptions);
	}
	
	@Override
	public Map<String, String> getVehicleSizesByRim(String rim) throws Exception {
		return vehicleDao.getVehicleSizesByRim(rim);
	}
	
	@Override
	public FriendlySizeDataBean getVehicleSizesByRimSize(String rim, String siteName) throws Exception {
		List<Object> rimValues = new ArrayList<Object>();
		rimValues.add(rim);
		List<String> rimNames = TireCatalogUtils.formatSizes(rimValues, "rim");
		String formattedRimSize = rimNames.get(0).toString();
		
		Map<String, String> tireSizes = vehicleDao.getVehicleSizesByRim(rim, siteName);
		List<TireSize> data = new ArrayList<TireSize>();
		if (tireSizes != null && !tireSizes.isEmpty()) {
			for (Map.Entry<String, String> sizes : tireSizes.entrySet()) {
				String raw = sizes.getKey();
			    String formatted = sizes.getValue();
			    TireSize tireSize = new TireSize();
			    populateSizes(tireSize, raw, false);
			    populateSizes(tireSize, formatted, true);
			    data.add(tireSize);
			}
		}
		
		SeoVehicleData seoVehicleData = getSeoVehicleData(siteName, "SIZE_DIAMETER", "/size/"+rim+"/");
		FriendlySizeDataBean friendlySizeDataBean = new FriendlySizeDataBean();
		friendlySizeDataBean.setRimSize(rim);
		friendlySizeDataBean.setRimText(formattedRimSize);
		friendlySizeDataBean.setCrossAspects(data);
		if (seoVehicleData != null) {
			friendlySizeDataBean.setSeoVehicleData(seoVehicleData);
		}
		
		return friendlySizeDataBean;
	}
	
	public SeoVehicleData getSeoVehicleData(String siteName, String fileId, String recordId) {
		SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, fileId, recordId);
		if (seoVehicleData == null) {
			if ("SIZE_DIAMETER".equalsIgnoreCase(fileId)) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, fileId, "/size/generic-diameter/");
			} else if ("SIZE_TIRE".equalsIgnoreCase(fileId)) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, fileId, "/size/generic-cross-aspect-rdiameter/");
			}
			
			if (seoVehicleData != null) {
				seoVehicleData = formatSeoVehicleData(seoVehicleData, fileId, recordId);
			}			
		}
		return seoVehicleData;
	}
	
	private void populateSizes(TireSize tireSize, String rawSize, boolean key) {
		String cross = null;
		String aspect = null;
		String rim = null;
		if (rawSize != null) {
			if (rawSize.indexOf("/") != -1) {
				int index = rawSize.indexOf("/");
				cross = rawSize.substring(0, index);
				if (!ServerUtil.isNullOrEmpty(cross)) {
					if (key)
						tireSize.setCrossText(cross);
					else
						tireSize.setCrossValue(cross);
				}
				
				rawSize = rawSize.substring(index+1);
				if (rawSize.indexOf("-") != -1) {
					index = rawSize.indexOf("-");
					aspect = rawSize.substring(0, index);
					if (!ServerUtil.isNullOrEmpty(aspect)) {
						if (key)
							tireSize.setAspectText(aspect);
						else
							tireSize.setAspectValue(aspect);
					}
					/*rim = rawSize.substring(index+1);
					if (!ServerUtil.isNullOrEmpty(rim)) {
						if (key)
							tireSize.setRimText(rim);
						else
							tireSize.setRimValue(rim);
					}*/
				}
			}
		}
	}
	
	public List<RimDiameter> getRimDiameters(Map<String, String> rimOptions) throws Exception{
		List<RimDiameter> rimDiameters = new ArrayList<RimDiameter>();
		if (rimOptions != null && !rimOptions.isEmpty()) {
			for (Map.Entry<String, String> rimOption : rimOptions.entrySet()) {
				if (!"1650".equals(rimOption.getKey())) {
					RimDiameter rimDiameter = new RimDiameter();
					rimDiameter.setRimText(rimOption.getValue());
					rimDiameter.setRimSize(rimOption.getKey());
					rimDiameters.add(rimDiameter);
				}
			}
		}
		return rimDiameters;
	}
	
	private SeoVehicleData formatSeoVehicleData(SeoVehicleData seoVehicleData, String fileId, String recordId) {
		String cross = null;
		String aspect = null;
		String rim = null;
		if (recordId != null) {
			int startIndex = recordId.indexOf("size/")+5;
			String sizeVal = recordId.substring(startIndex, recordId.length()-1);
			if (sizeVal.indexOf("-") != -1) {
				int hyStartIndex = sizeVal.indexOf("-");
				int hyEndIndex = sizeVal.lastIndexOf("-");
				cross = sizeVal.substring(0, hyStartIndex);
				aspect = sizeVal.substring(hyStartIndex+1, hyEndIndex);
				rim = sizeVal.substring(hyEndIndex+2, sizeVal.length());
			} else {
				rim = sizeVal;
			}
			
			if ("SIZE_DIAMETER".equalsIgnoreCase(fileId)) {
				formatSeoVehicleData(seoVehicleData, rim, 3);
				seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-diameter", rim));
			} else if ("SIZE_TIRE".equalsIgnoreCase(fileId)) {
				formatSeoVehicleData(seoVehicleData, cross, 1);
				formatSeoVehicleData(seoVehicleData, aspect, 2);
				formatSeoVehicleData(seoVehicleData, rim, 3);
				seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-cross-aspect-rdiameter", cross+"-"+aspect+"-r"+rim));
			}
		}
		
		return seoVehicleData;
	}
	
	private SeoVehicleData formatSeoVehicleData(SeoVehicleData seoVehicleData, String val, int fcode) {
		switch (fcode) {
		case 1:
			seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Cross}", val));
			seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Cross}", val));
			seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Cross}", val));
			seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Cross}", val));
			seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Cross}", val));
			seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Cross}", val));
			seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Cross}", val));
			seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Cross}", val));
			seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Cross}", val));
			seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Cross}", val));
			break;
		case 2:
			seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Aspect}", val));
			seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Aspect}", val));
			seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Aspect}", val));
			seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Aspect}", val));
			seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Aspect}", val));
			seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Aspect}", val));
			seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Aspect}", val));
			seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Aspect}", val));
			seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Aspect}", val));
			seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Aspect}", val));
			break;
		case 3:
			seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Diameter}", val));
			seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Diameter}", val));
			seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Diameter}", val));
			seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Diameter}", val));
			seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Diameter}", val));
			seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Diameter}", val));
			seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Diameter}", val));
			seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Diameter}", val));
			seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Diameter}", val));
			seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Diameter}", val));
			break;
		default:
			break;
	}
	
	return seoVehicleData;
	}

}
