package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.tire.jda.*;

public interface CatalogDAO {
	public static final String BEST_IN_CLASS_LINEIDS=",73663,49970,51260,73664,23246,66262,76698,66263,66264,74159,73355,76283,50777,58525,64470,72965,63937,72967,76171,836,72968,58527,73281,67688,59942,75625,72739,63733,840,50926,50927,49321,49322,47316,75976,46044,76061,53063,72976,69680,69681,50921,";
	List getProductLinesInCategory(long id);
	List getProductLinesInCategory(long id, String storeType);
	Display getProductLine(long id);
	List getProductsInLine(long id);
	List getProductsInLine(long id, boolean POS);
	List getProductsInLine(long id, String storeType);
	List getProductsInLine(long id, String storeType, boolean POS);
	List getCrossSections();
	List getAspectRatios();
	List getAspectRatios(String cross);
	List getRimSizes();
	List getRimSizes(String cross, String aspect);
	Configuration getProduct(long id);
	Fact getFact(long id);
	Technology getTechnology(long id);
	byte[] getBrandImage(long id);
	byte[] getFactImage(long id);
	byte[] getTechnologyImage(long id);
	byte[] getTiregroupImage(long id);
	byte[] getTireImage(long id);
	byte[] getTireNameImage(long id);
	byte[] getWarrantyPDF(long id);
	Object getVehicleSizes(Object o);
	
	List getBestInClassTires();
	List getSiteTires();
}
