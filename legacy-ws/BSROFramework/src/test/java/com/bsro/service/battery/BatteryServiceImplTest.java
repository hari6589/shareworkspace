package com.bsro.service.battery;


import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import app.bsro.model.battery.Battery;

import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.pojo.interstatebattery.InterstateAutomobile;
import com.bfrc.pojo.interstatebattery.InterstateBatteryMaster;
import com.bfrc.pojo.interstatebattery.InterstateBatteryWebPrices;
import com.bsro.service.battery.BatteryService;
import com.bsro.service.battery.BatteryServiceImpl;

public class BatteryServiceImplTest {
	BatteryService service;
	private InterstateBatteryDAO batteryDAO;
	
	@Before
	public void setup() {
		service = new BatteryServiceImpl();
		batteryDAO = EasyMock.createNiceMock(InterstateBatteryDAO.class);
		service.setInterstateBatteryDAO(batteryDAO);
	}
	
	@After
	public void teardown() {
		service = null;
		batteryDAO = null;
	}
	
	//@Ignore("TODO")
	@Test
	public void testFindBatteriesByVehicle() {
		String year = "2010";
		String make = "Honda";
		String model = "Accord";
		String engine = "L4/2.4L";
		
		Map<String, InterstateBatteryWebPrices> webPricesMap = getBatteryWebPrices();
		Map<String, InterstateBatteryMaster> interstateBatteryMaster = getBatteryMasters();
		List<InterstateAutomobile> automobiles = createInterstateAutomobileList();
		
		EasyMock.expect(batteryDAO.getMappedBatteryWebPrices()).andReturn(webPricesMap).once();
		EasyMock.expect(batteryDAO.getMappedBatteryMaster()).andReturn(interstateBatteryMaster).once();
		EasyMock.expect(batteryDAO.getBatteryList(Short.valueOf(year), make, model, engine)).andReturn(automobiles).once();
		EasyMock.replay(batteryDAO);
		
		List<Battery> batteries = service.findBatteriesByVehicle(year, make, model, engine);
		assertEquals(batteries.size(), 3);
	}
	
	
	private List<InterstateAutomobile> createInterstateAutomobileList() {
		List<InterstateAutomobile> automobiles = new ArrayList<InterstateAutomobile>();
		
		InterstateAutomobile ia = new InterstateAutomobile();
		ia.setProductCode("I-35");
		ia.setProductLine("Automotive / Truck");
		ia.setMake("Honda");
		ia.setModel("Accord");
		ia.setYear(Short.valueOf("2010"));
		ia.setEngine("L4/2.4L");
		ia.setOptn("w/Automatic Transmission");
		ia.setBci("35");
		ia.setCca(Long.valueOf(450));
		
		InterstateAutomobile ia2 = new InterstateAutomobile();
		ia2.setProductCode("MT-35");
		ia2.setProductLine("Automotive / Truck");
		ia2.setMake("Honda");
		ia2.setModel("Accord");
		ia2.setYear(Short.valueOf("2010"));
		ia2.setEngine("L4/2.4L");
		ia2.setOptn("w/Automatic Transmission");
		ia2.setBci("35");
		ia2.setCca(Long.valueOf(450));
		
		InterstateAutomobile ia3 = new InterstateAutomobile();
		ia3.setProductCode("MTP-35");
		ia3.setProductLine("Automotive / Truck");
		ia3.setMake("Honda");
		ia3.setModel("Accord");
		ia3.setYear(Short.valueOf("2010"));
		ia3.setEngine("L4/2.4L");
		ia3.setOptn("w/Automatic Transmission");
		ia3.setBci("35");
		ia3.setCca(Long.valueOf(450));
		
		InterstateAutomobile iaBad = new InterstateAutomobile();
		iaBad.setProductCode("MT-51R");
		iaBad.setProductLine("Automotive / Truck");
		iaBad.setMake("Honda");
		iaBad.setModel("Civic");
		iaBad.setYear(Short.valueOf("2008"));
		iaBad.setEngine("L4/2.0L");
		iaBad.setOptn("US Models");
		iaBad.setBci("51R");
		iaBad.setCca(Long.valueOf(410));
		
		automobiles.add(ia);
		automobiles.add(ia2);
		automobiles.add(ia3);
		automobiles.add(iaBad);
		
		return automobiles;
	}
	
	private Map<String, InterstateBatteryWebPrices> getBatteryWebPrices() {
		Map<String, InterstateBatteryWebPrices> webPricesMap = new HashMap<String, InterstateBatteryWebPrices>();
		
		InterstateBatteryWebPrices i = new InterstateBatteryWebPrices();
		i.setProduct("I");
		i.setWebPrice(BigDecimal.valueOf(69.99));
		i.setTradeinCredit(BigDecimal.valueOf(20.00));
		i.setRegularPrice(BigDecimal.valueOf(79.99));
		
		InterstateBatteryWebPrices mt = new InterstateBatteryWebPrices();
		mt.setProduct("MT");
		mt.setWebPrice(BigDecimal.valueOf(89.99));
		mt.setTradeinCredit(BigDecimal.valueOf(20.00));
		mt.setRegularPrice(BigDecimal.valueOf(99.99));
		
		InterstateBatteryWebPrices mtp = new InterstateBatteryWebPrices();
		mtp.setProduct("MTP");
		mtp.setWebPrice(BigDecimal.valueOf(109.99));
		mtp.setTradeinCredit(BigDecimal.valueOf(20.00));
		mtp.setRegularPrice(BigDecimal.valueOf(119.99));
		
		webPricesMap.put("I", i);
		webPricesMap.put("MT", mt);
		webPricesMap.put("MTP", mtp);
		
		return webPricesMap;
	}
	
	private Map<String, InterstateBatteryMaster> getBatteryMasters() {
		Map<String, InterstateBatteryMaster> batteryMastersMap = new HashMap<String, InterstateBatteryMaster>();
		
		InterstateBatteryMaster bm1 = new InterstateBatteryMaster();
		bm1.setProduct("I");
		bm1.setProductCode("I-35");
		bm1.setPartNumber(Long.valueOf(7003964));
		bm1.setTotalWarranty(Long.valueOf(48));
		bm1.setReplacementWarranty(Long.valueOf(18));
		bm1.setCca(Long.valueOf(450));
		bm1.setRcMinutes(Long.valueOf(80));
		
		InterstateBatteryMaster bm2 = new InterstateBatteryMaster();
		bm2.setProduct("MT");
		bm2.setProductCode("MT-35");
		bm2.setPartNumber(Long.valueOf(7098515));
		bm2.setTotalWarranty(Long.valueOf(60));
		bm2.setReplacementWarranty(Long.valueOf(24));
		bm2.setCca(Long.valueOf(550));
		bm2.setRcMinutes(Long.valueOf(100));
		
		InterstateBatteryMaster bm3 = new InterstateBatteryMaster();
		bm3.setProduct("MTP");
		bm3.setProductCode("MTP-35");
		bm3.setPartNumber(Long.valueOf(7031658));
		bm3.setTotalWarranty(Long.valueOf(72));
		bm3.setReplacementWarranty(Long.valueOf(30));
		bm3.setCca(Long.valueOf(640));
		bm3.setRcMinutes(Long.valueOf(100));
		
		InterstateBatteryMaster bmBad = new InterstateBatteryMaster();
		bmBad.setProduct("I");
		bmBad.setProductCode("I-24");
		bmBad.setPartNumber(Long.valueOf(7098698));
		bmBad.setTotalWarranty(Long.valueOf(48));
		bmBad.setReplacementWarranty(Long.valueOf(18));
		bmBad.setCca(Long.valueOf(530));
		bmBad.setRcMinutes(Long.valueOf(90));
		
		batteryMastersMap.put(bm1.getProductCode(), bm1);
		batteryMastersMap.put(bm2.getProductCode(), bm2);
		batteryMastersMap.put(bm3.getProductCode(), bm3);
//		batteryMastersMap.put(bm3.getProductCode(), bmBad);
		
		return batteryMastersMap;
	}
}
