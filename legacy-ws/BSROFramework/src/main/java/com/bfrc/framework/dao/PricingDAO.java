package com.bfrc.framework.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.bfrc.framework.dao.pricing.Price;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.alignment.AlignmentPricing;
import com.bfrc.pojo.alignment.AlignmentPricingQuote;
import com.bfrc.pojo.pricing.TpArticleLog;
import com.bfrc.pojo.pricing.TpHierarchyPrice;
import com.bfrc.pojo.pricing.TpStoreList;
import com.bfrc.pojo.pricing.TpTireMaster;
import com.bfrc.pojo.pricing.TpTpmsPrice;
import com.bfrc.pojo.pricing.TpUserLog;
import com.bfrc.pojo.pricing.MobileTireInstallFee;
import com.bfrc.pojo.tire.Fitment;
import com.bfrc.pojo.tire.Tire;

public interface PricingDAO {
	Price getPrice(int articleNumber, long storeNumber);
	List getProducts(Fitment f);
	long logGetProducts(Long store, UserVehicle vehicle);
	
    //	cxs
	public TpArticleLog findTpArticleLogById(Long id);
	public void createTpUserLog(TpUserLog tpUserLog);
	public void updateTpUserLog(TpUserLog tpUserLog);
	public void deleteTpUserLog(TpUserLog tpUserLog);
	
	
	public List findTpTireFeesByStoreNumber(Long storeNumber );
	//cxs
	public TpUserLog findTpUserLogByUserId(Long id);
	public void createTpArticleLog(TpArticleLog tpArticleLog) throws DataAccessException;
	public void updateTpArticleLog(TpArticleLog tpArticleLog) throws DataAccessException;
	public void deleteTpArticleLog(TpArticleLog tpArticleLog) throws DataAccessException;
	
	public TpStoreList findTpStoreListByStoreNumber(Object id) throws DataAccessException;
	public TpTireMaster findTpTireMasterByArticle(Object id) throws DataAccessException;
	public TpHierarchyPrice findTpHierarchyPriceByBookIdAndLine(Object bookId, Object line) throws DataAccessException;
	public TpHierarchyPrice findTpHierarchyPriceByStoreNumberAndArticle(Object storeNumber, Object article) throws DataAccessException;
	public Long logGetQuote(Long userId, Long storeNumber,List list, UserVehicle vehicle, String article, String rearArticle, String qty, String firstName, String lastName) ;
	public Long logGetQuote(Long userId, Long storeNumber,List list, UserVehicle vehicle, String article, String rearArticle, String qty, String rearQty, String firstName, String lastName) ;
	public Long logGetQuote(Long userId, Long storeNumber,Tire tire, UserVehicle vehicle,String qty, String firstName, String lastName) ;
	public Long logGetQuote(Long userId, Long storeNumber,List list, UserVehicle vehicle, String article, String rearArticle, String qty, String rearQty, String firstName, String lastName, String siteName);
	
	//---TPMS Valve Service Kit ---//
	TpTpmsPrice getVehicleTpmsPrice(String priceType, Long storeNumber);
	TpTpmsPrice getVehicleTpmsVSK(Long storeNumber);
	TpTpmsPrice getVehicleTpmsVSKLabor(Long storeNumber);
	
	TpTpmsPrice getVehicleTpmsPrice(String year, String make, String model, String submodel, String priceType, Long storeNumber);
	TpTpmsPrice getVehicleTpmsVSK(String year, String make, String model, String submodel, Long storeNumber);
	TpTpmsPrice getVehicleTpmsVSKLabor(String year, String make, String model, String submodel, Long storeNumber);
	
	boolean isAlignmentPricingExclusion(Object year, String make, String model);
	AlignmentPricing getAlignmentPricing(Object storeNumber);
	public Long createAlignmentPricingQuote(AlignmentPricingQuote alignmentQuote);
	
	public List getGlobalSppressedTireArticles();
	public Map getStoreNumberMappedSppressedTireArticles();
	public Map<Long, List<String>> getGlobalVehTypeDisplayIDSuppressedTireArticles();
	public void touchSuppressionCacheController();
	
	public double[] getTirePricingPriceRange(Long displayId);
	public MobileTireInstallFee findMobileTireInstallFeeByStoreNumber(Long storeNumber);
	
	public AlignmentPricingQuote getAlignmentQuotebyQuoteId(Long quoteId);
	public void updateAlignmentPricingQuote(AlignmentPricingQuote alignmentQuote) throws DataAccessException;
	
	
}