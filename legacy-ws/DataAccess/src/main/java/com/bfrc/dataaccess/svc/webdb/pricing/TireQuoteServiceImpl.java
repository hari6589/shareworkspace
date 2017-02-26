package com.bfrc.dataaccess.svc.webdb.pricing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.codehaus.jackson.JsonNode;
import app.bsro.model.tire.QuoteItem;
import app.bsro.model.tire.TirePromotion;
import app.bsro.model.tire.TireQuote;
import app.bsro.model.tire.TireSize;
import app.bsro.model.tire.VehicleFitment;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.svc.webdb.PromotionsService;
import com.bfrc.dataaccess.svc.webdb.TireQuoteService;
import com.bfrc.dataaccess.util.AddressUtils;
import com.bfrc.dataaccess.util.JsonObjectMapper;
import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.dataaccess.dao.generic.FitmentDAO;
import com.bfrc.dataaccess.dao.quote.QuoteDAO;
import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.dataaccess.model.myprofile.NCDVehicleHistory;
import com.bfrc.dataaccess.model.promotion.TirePromotionEvent;
import com.bfrc.dataaccess.model.quote.TpTireQuotesLedger;
import com.bfrc.dataaccess.model.vehicle.Fitment;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.ecommerce.ECommerceService;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireSearchUtils;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.pricing.TpArticleLog;
import com.bfrc.pojo.pricing.TpTpmsPrice;
import com.bfrc.pojo.pricing.TpUserLog;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.service.inventory.StoreInventoryService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;
import org.json.JSONObject;

/**
 * @author stallin sevugamoorthy
 *
 */
@Service
public class TireQuoteServiceImpl implements TireQuoteService {
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	StoreInventoryService storeInventoryService;
	
	@Autowired
	private PricingDAO pricingDAO;
	
	@Autowired
	private FitmentDAO fitmentDao;
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@Autowired
	private PromotionsService promotionsService;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	private PricingLocatorOperator pricingLocator;
	
	@Autowired
	TiresUtil tiresUtil;
	
	@Autowired
	QuoteDAO quoteDao;
	
		
	protected static DecimalFormat zeroPaddedFormat = new java.text.DecimalFormat("000000");
	protected static DecimalFormat decimalValueFormatter = new java.text.DecimalFormat("##.00");
	protected static DecimalFormat df = new DecimalFormat("#,##0.00");
	private static String TIRESPLUS_LICENSEE_STORE = "TPL";
	String donationName="Round Up to Help Out";
    String donationArticle="7009019";
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public BSROWebServiceResponse createQuote(Long storeNumber, String article, String quantity, Long acesVehicleId,
			Integer tpms, String firstName, String lastName, String siteName, String emptyCart) {
		boolean hasMatchedSets = false;
		int count = 0;
		String zip = null;
		String frb = "";
		String rearQuantity = null;
		String rearArticle = null;
		String articleParam = null, rearArticleParam = null;
		String codeStr = null;
		String promoId = null;
		Long regularStoreNumber = null;
			    
	    Tire tire = null, rearTire = null;
	    TirePromotionEvent promo = null;
	    TpArticleLog tpArticleLog = null;
	    TpUserLog tpUserLog = null;
		Store store = null;
		TireVehicle tireVehicle = null;
		StoreInventory storeInventory = null;
		
		double discountAmount = 0.0;
		double frontDiscountAmount = 0.0;
	    double rearDiscountAmount = 0.0;
	    double frontDiscountAmountTotal = 0.0;
	    double rearDiscountAmountTotal = 0.0;
		
	    List tires = null;
	    List discountOffers = new ArrayList();
	    List rebateOffers = new ArrayList();
	    java.util.Map taxData = null;
	    		
		UserVehicle vehicle = new UserVehicle();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		/*
		 * 		Set the 'siteName' in Config for later use by services & dao's in BSRO Framework 
		 */
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		if (article != null && article.indexOf("_") > -1) {
			hasMatchedSets = true;
			frb = "B";
	    }
		
		if (quantity == null || quantity.equalsIgnoreCase("null")
                || quantity.trim().length() == 0) {
			if (hasMatchedSets) {
				quantity = "2";
				rearQuantity = "2";
	        } else {
	        	quantity = "4";
	        }
		} else {
			quantity = quantity.trim();
			if (hasMatchedSets) {
				rearQuantity = quantity.trim();
			}
		}
		
		if (StringUtils.isNullOrEmpty(acesVehicleId)) {
			vehicle = null;
		} else {
			tireVehicle = tireVehicleService.fetchVehicleByAcesVehicleId(acesVehicleId);
			if (tireVehicle == null) {
				response.setMessage("Invalid ACES vehicle id. No matching vehicle details found.");
				response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
				return response;
			}			
		}
		
		if (tireVehicle != null) {
			vehicle.setYear(tireVehicle.getYear());
			vehicle.setMake(tireVehicle.getMakeName());
			vehicle.setModel(tireVehicle.getModelName());
			vehicle.setSubmodel(tireVehicle.getSubmodelName());
		}
		
		/*
		 * 		Load the store for the input store number.
		 */
		store = storeDAO.findStoreById(storeNumber);
		if (store == null) {
			response.setMessage("Invalid store number.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
		
		if (TIRESPLUS_LICENSEE_STORE.equalsIgnoreCase(store.getStoreType().trim())) {
			zip = store.getZip().substring(0, 5);
			if(StringUtils.isNullOrEmpty(zip) || !AddressUtils.isValidZipCode(zip)) {
				response.setMessage("Invalid zip code: "+zip);
				response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
				return response;
			}			
		}
		
		/*
		 * 		Get the tire(s) for the input article number.
		 */
		if (TIRESPLUS_LICENSEE_STORE.equalsIgnoreCase(store.getStoreType().trim())) {
			regularStoreNumber = getRegularStoreNumber(siteName, store.getStoreType(), zip);
			tires = tireVehicleService.populateTiresByArticles(siteName, article, String.valueOf(storeNumber), String.valueOf(regularStoreNumber));
		} else {
			tires = tireVehicleService.populateTiresByArticles(siteName, article, String.valueOf(storeNumber));
		}
		
		if (tires == null || tires.size() == 0) {
			response.setMessage("No tires found for given article");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			
			return response;
		}
		
		tire = (Tire) tires.get(0);
		
		if(tires != null && tires.size()>1){
			//list may not always return first tire in the 0 position
            String[] frontTireString = article.split("_");
			if(frontTireString!=null&&frontTireString.length>0){
            	if(!String.valueOf(tire.getArticle()).equals(frontTireString[0])) {
            		tire = (Tire) tires.get(1);
            		 rearTire = (Tire) tires.get(0);
                     rearArticle = rearTire.getArticle() + "";
            		}else{
               		 	rearTire = (Tire) tires.get(1);
                        rearArticle = rearTire.getArticle() + "";
            		}
				}else{
              		 rearTire = (Tire) tires.get(1);
                     rearArticle = rearTire.getArticle() + "";
         		}
            tires.clear();
            tires.add(0, tire);
            tires.add(1,rearTire);
        }
		
		if (tire == null) {
			response.setMessage("No tire found for given article");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
		
		articleParam = String.valueOf(tire.getArticle());
        if (rearTire != null) {
        	rearArticleParam = String.valueOf(rearTire.getArticle());
        }
        
        /*
		 * 		Get the store inventory details for the input store number.
		 */
        storeInventory = tiresUtil.getStoreInventory(storeNumber, Long.valueOf(tire.getArticle()));
		
        /*
         * 		Log the quote details in TP_USER_LOG & TP_ARTICLE_LOG table.
         * 		Created TP_ARTICLE_ID will be the quoteId.
         */
        Long codeID = pricingDAO.logGetQuote(null, storeNumber, tires, vehicle, articleParam, rearArticleParam, quantity, rearQuantity, firstName, lastName, siteName);        
        codeStr = codeID.intValue() < 0 ? null : zeroPaddedFormat.format(codeID.longValue());        
        if (codeStr != null) {
        	tpArticleLog = pricingDAO.findTpArticleLogById(codeID);
        	tpUserLog = logTPUserData(firstName, lastName, store.getStoreNumber().longValue(), quantity, rearQuantity, tpArticleLog.getTpUserId());
        	tpArticleLog = logTPArticleTPMSPricingData(tpArticleLog, tpUserLog, tire, tireVehicle, store, tpms);
        }
        Map offerData = null;
        if(StringUtils.isNullOrEmpty(emptyCart)){
	        /*
			 * 		Get the promotions data for the input article number & store numbers.
			 */
	        count = Integer.parseInt(quantity);
	        Map offersData = promotionsService.loadTirePromotionData(siteName, Long.parseLong(storeNumber.toString()), null);
	        offerData = promotionsService.getOfferData(siteName, articleParam, rearArticleParam, storeNumber.toString(), quantity, offersData);
	        rebateOffers = (List) offerData.get("rebateOffers");
	        discountOffers = (List) offerData.get("discountOffers");
	        discountAmount = Double.parseDouble(offerData.get("discountAmount").toString());
	        frontDiscountAmount = Double.parseDouble(offerData.get("frontDiscountAmount").toString());
	        frontDiscountAmountTotal = Double.parseDouble(offerData.get("frontDiscountAmountTotal").toString());
	        rearDiscountAmount = Double.parseDouble(offerData.get("rearDiscountAmount").toString());
	        rearDiscountAmountTotal = Double.parseDouble(offerData.get("rearDiscountAmountTotal").toString());
	        
	        if (discountOffers.size() > 0) {
	            promo = (TirePromotionEvent) discountOffers.iterator().next();
	            promoId = String.valueOf(promo.getPromoId());
	        }
	        
	        /*
			 * 		Get the tax data for the input tire, article log & store.
			 * 		This data will have the most quote line items including shop supplies & tax.
			 */
	        taxData = ECommerceService.loadTaxData(tires, count, store, tpArticleLog, frontDiscountAmount, rearDiscountAmount);
	        
	        /*
	         * 		Update TP_ARTICLE_LOG table with the tax line items
	         */
	        tpArticleLog = logTPArticleTaxData(tpArticleLog, rearArticle, rearTire, taxData, frontDiscountAmount, rearDiscountAmount, promoId);
        }
        /*
         * 		Create tire quote response
         */        
        TireQuote tireQuote = createQuoteResponse(store, tire, rearTire, tpArticleLog, tpUserLog, tireVehicle, offerData, storeInventory, taxData, emptyCart);        
        if(!StringUtils.isNullOrEmpty(emptyCart)){
        	tireQuote.setEmptyCart(emptyCart);
	        TpTireQuotesLedger tpTireQuotesLedger = new TpTireQuotesLedger();
	    	tpTireQuotesLedger.setQuoteId(tpArticleLog.getTpArticleId());
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	ObjectWriter ow = objectMapper.writer();
	    	String jsonString = null;
	    	try {
	    		jsonString = ow.writeValueAsString(tireQuote);
	    	}catch (IOException e1) {
				System.err.println("IOException while trying to get build quote json");
				e1.printStackTrace();
			}catch (Exception e) {
				System.err.println("Exception while trying to get build quote json");
				e.printStackTrace();
			}	
	    	tpTireQuotesLedger.setPayload(jsonString);
	    	tpTireQuotesLedger.setUpdateDate(new Date());
	        quoteDao.saveTpTireQuotesLedger(tpTireQuotesLedger);
        }
        
        response.setPayload(tireQuote);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
        return response;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BSROWebServiceResponse getQuote(Long quoteId) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		TpArticleLog tpArticleLog = null;
	    TpUserLog tpUserLog = null;
	    Store store = null;
	    TireVehicle tireVehicle = null;
	    StoreInventory storeInventory = null;
	    java.util.Map taxData = null;
	    Tire tire = null, rearTire = null;
	    String article = null, rearArticle = null;
		String articleParam = null, rearArticleParam = null;
		TpTireQuotesLedger tpTireQuotesLedger = null;
		TireQuote tireQuote = null;
		
	    if (quoteId != null && Long.parseLong(""+quoteId) > 0L) {
	    		
	    	tpTireQuotesLedger = quoteDao.getTireQuotesLedger(quoteId);
	    	if(!StringUtils.isNullOrEmpty(tpTireQuotesLedger))
	    	{
	    		String jsonString = tpTireQuotesLedger.getPayload();
	    		JsonNode HybrisJson = null;
	    		JsonObjectMapper jsonObjectMapper = new JsonObjectMapper();
	    		try{
	    			jsonObjectMapper.configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
	    			jsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    			jsonObjectMapper.configure(DeserializationConfig.Feature.USE_BIG_DECIMAL_FOR_FLOATS, true);
	    			HybrisJson = jsonObjectMapper.readValue(jsonString, JsonNode.class);
	    			response.setPayload(HybrisJson);
	    			}catch(Exception e){
	    				e.printStackTrace();
	    			}
	    		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
	    		return response;		    		
	    	}
	    	else
	    	{	
	    		tpArticleLog = pricingDAO.findTpArticleLogById(quoteId);
		    	if (tpArticleLog != null) {
		    		article = String.valueOf(tpArticleLog.getArticleNumber());
			    	if (tpArticleLog.getRearArticle() != null && tpArticleLog.getRearArticle().longValue() > 0) {
			    		if (article != null) {
			    			article = article + "_" + tpArticleLog.getRearArticle();
		                    rearArticle = tpArticleLog.getRearArticle() + "";
		                }
			    	}
			    	
			    	/*
			    	 * 		Load the vehicle, quantity & web site details using TP_USER_ID from TP_USER_LOG table.
			    	 */
		    		tpUserLog = pricingDAO.findTpUserLogByUserId(new Long(tpArticleLog.getTpUserId()));
		    		if (tpUserLog == null) {
		    			response.setMessage("No vehicle details found for given quote id");
		    			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		    			
		    			return response;
		    		}
		    		
		    		/*
		    		 * 		Load the store details
		    		 */
		    		store = storeDAO.findStoreById(new Long(tpUserLog.getStoreNumber()));
		    		if (store == null) {
		    			response.setMessage("Invalid store number.");
		    			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		    			return response;
		    		}
		    		
		    		/*
		    		 * 		Load the tire vehicle details which gives the acesVehicleId & TPMS indicator values
		    		 */
		    		if (!StringUtils.isNullOrEmpty(tpUserLog.getModelYear()) && !StringUtils.isNullOrEmpty(tpUserLog.getMakeName()) && !StringUtils.isNullOrEmpty(tpUserLog.getModelName()) && !StringUtils.isNullOrEmpty(tpUserLog.getSubmodel())) {
			    		List<TireVehicle> tireVehicles = tireVehicleService.getVehiclesByYearMakeNameModelNameSubmodelName(Integer.valueOf(tpUserLog.getModelYear()), tpUserLog.getMakeName(), tpUserLog.getModelName(), tpUserLog.getSubmodel());
			    		if (tireVehicles != null && !tireVehicles.isEmpty()) {
			    			tireVehicle = tireVehicles.get(0);
			    		}
		    		}
		    		
		    		/*
		    		 * 		Retrieve the tire results
		    		 */
		    		List tires = tireVehicleService.populateTiresByArticles(tpUserLog.getWebSite(), article, String.valueOf(tpUserLog.getStoreNumber()));
		    		Map mappedTires = TireSearchUtils.getMappedTires(tires);
		    		if (tires == null || tires.size() == 0) {
		    			response.setMessage("No tire details found for given quote id");
		    			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
		    			
		    			return response;
		    		}
		    		
		    		tire = (Tire) tires.get(0);
		    		
		    		if(tires != null && tires.size()>1){
		    			//list may not always return first tire in the 0 position
		                String[] frontTireString = article.split("_");
		    			if(frontTireString!=null&&frontTireString.length>0) {
		                	if(!String.valueOf(tire.getArticle()).equals(frontTireString[0])) {
		                		tire = (Tire) tires.get(1);
		                		rearTire = (Tire) tires.get(0);
		                        rearArticle = rearTire.getArticle() + "";
		                	} else {
		                		rearTire = (Tire) tires.get(1);
		                        rearArticle = rearTire.getArticle() + "";
		                	}
		    			} else {
		    				rearTire = (Tire) tires.get(1);
		                    rearArticle = rearTire.getArticle() + "";
		    			}
		                tires.clear();
		                tires.add(0, tire);
		                tires.add(1,rearTire);
		            }
		    		
		    		if (tpArticleLog.getRetailPrice() != null && tpArticleLog.getRetailPrice().doubleValue() > 0) {
	                    tire.setRetailPrice(tpArticleLog.getRetailPrice().doubleValue());
	                }
		    		
	                if (tires.size() > 1) {
	                	rearTire = (Tire) mappedTires.get(rearArticle);
	                	if (rearTire != null) {
	                		rearTire.setRetailPrice(tpArticleLog.getRearRetailPrice().doubleValue());
	                	}
	                }
	                
		    		articleParam = String.valueOf(tire.getArticle());
		            if (rearTire != null) {
		            	rearArticleParam = String.valueOf(rearTire.getArticle());
		            }
		            
		            /*
		    		 * 		Get the store inventory details for the input store number.
		    		 */
		            storeInventory = tiresUtil.getStoreInventory(store.getStoreNumber(), Long.valueOf(tire.getArticle()));
		            
		            /*
		             * 		Load the offer data
		             */
		            Map offersData = promotionsService.loadTirePromotionData(tpUserLog.getWebSite(), Long.parseLong(String.valueOf(tpUserLog.getStoreNumber()).toString()), null);
		            Map offerData = promotionsService.getOfferData(tpUserLog.getWebSite(), articleParam, rearArticleParam, String.valueOf(tpUserLog.getStoreNumber()).toString(), String.valueOf(tpUserLog.getQuantity()), offersData);
		            
		            /*
		             * 		Load the tax data
		             */
		            taxData = ECommerceService.loadTaxData(tires, (int)tpUserLog.getQuantity(), store, tpArticleLog, 
		            		Double.parseDouble(offerData.get("frontDiscountAmount").toString()), Double.parseDouble(offerData.get("rearDiscountAmount").toString()), true, null);
		            
		            /*
		             * 		populate tire quote response and return...
		             */	            
		            tireQuote = createQuoteResponse(store, tire, rearTire, tpArticleLog, tpUserLog, tireVehicle, offerData, storeInventory, taxData, null);
		            response.setPayload(tireQuote);
		    		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		    		return response;
		    		
		    	} else {
		    		response.setMessage("Invalid quote id.");
	    			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
	    			
	    			return response;
		    	}
	    		
	    	}
	    	
	    	
	    	
	    }
	    	    
		return response;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BSROWebServiceResponse updateQuote(Long quoteId, JsonNode hybrisQuote) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		TpTireQuotesLedger tpTireQuotesLedger = quoteDao.getTireQuotesLedger(quoteId);
		if (tpTireQuotesLedger == null) {
			response.setMessage("Invalid quote id.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
		
	    ObjectMapper objectMapper = new ObjectMapper();
	    ObjectWriter ow = objectMapper.writer();
	    String jsonString = null;
	    try {
	    	jsonString = ow.writeValueAsString(hybrisQuote);
	    }catch (IOException e1) {
			System.err.println("IOException while trying to get build quote json");
			e1.printStackTrace();
		}catch (Exception e) {
			System.err.println("Exception while trying to get build quote json");
			e.printStackTrace();
		}
	    
	    tpTireQuotesLedger.setPayload(jsonString);
	    tpTireQuotesLedger.setUpdateDate(new Date());
        quoteDao.saveTpTireQuotesLedger(tpTireQuotesLedger);
        response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());        
		response.setMessage(ValidationConstants.UPDATE_SUCCESS);
        return response;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Long getRegularStoreNumber(String siteName, String storeType, String zip) {
		Store regularStore = null;
		if("TP".equalsIgnoreCase(siteName)){
			java.util.Map secondaryMap = storeDAO.getSecondaryStoreMap(siteName);
			Iterator i = secondaryMap.keySet().iterator();
			while(i.hasNext()) {
				String secondaryType = (String)i.next();
				if(secondaryType.equals(storeType.trim())) {
					HashMap m = new HashMap();
					try {
						m.put("zip", zip);
						m.put("licensee", new Boolean(false));
						m.put("pricing", "y");
						pricingLocator.operate(m);
						Store[] regularStores = (Store[])m.get("result");
						regularStore = regularStores[0];
					} catch(Exception ex) {
						try {
							m.put("full", new Boolean(true));
							pricingLocator.operate(m);
							Store[] regularStores = (Store[])m.get("result");
							regularStore = regularStores[0];							
						} catch(Exception fex) {
							return null;
						}
					}
				}
			}
		}
		return (regularStore != null) ? regularStore.getStoreNumber() : null;
	}
	
	public byte[] getPdf(Long quoteId) {
		byte[] pdfDocData = null;
		BSROWebServiceResponse response = getQuote(quoteId);
		if (response != null && response.getPayload() != null) {
			TireQuote tireQuote = (TireQuote) response.getPayload();
			pdfDocData = getQuotePDFDoc(tireQuote);
		}
		return pdfDocData;
	}
	
	private TpUserLog logTPUserData(String firstName, String lastName, long storeNumber, String qty, String rearQty, long tpUserId) {
		TpUserLog tpUserLog = pricingDAO.findTpUserLogByUserId(new Long(tpUserId));
        if (tpUserLog != null) {
            tpUserLog.setFirstName(firstName);
            tpUserLog.setLastName(lastName);
            tpUserLog.setStoreNumber(storeNumber);
            tpUserLog.setQuantity(Long.parseLong(qty));
            if (rearQty != null && !rearQty.isEmpty()) {
            	tpUserLog.setRearQuantity(Long.parseLong(rearQty));
            }
            pricingDAO.updateTpUserLog(tpUserLog);
        }
        return tpUserLog;
	}
	
	private TpArticleLog logTPArticleTPMSPricingData(TpArticleLog tpArticleLog, TpUserLog tpUserLog, Tire tire, TireVehicle tireVehicle, Store store, Integer tpms) {
		boolean hasTPMS = false;
		if (tpArticleLog != null) {
            tpArticleLog.setDonationName(donationName);
            tpArticleLog.setDonationArticle(Long.valueOf(donationArticle));
            if (tireVehicle != null) {
	            Collection<Fitment> list = fitmentDao.findByYearMakeModelAndSubModel(tireVehicle.getYear(), tireVehicle.getMakeName(), tireVehicle.getModelName(), tireVehicle.getSubmodelName());
	            Iterator<Fitment> i = list.iterator();
	            if (i != null && i.hasNext()) {
	            	Fitment fit = i.next();
	            	if(fit.isTpmsInd()) {
	            		hasTPMS = true;
	            	}                			
	            }
            }
            
            /*
             * 	Condition to enforce TPMS quote line items for TPMS set to yes (defaults to without TPMS support) 
             *  in search by vehicle widget - customized vehicles -. 
             * */
            if (!hasTPMS && (tpms != null && tpms.intValue() == 1)) {
            	hasTPMS = true;
            }
            
            /*
             * 	Condition to enforce TPMS quote line items 
             *  for DriveGuard tires when search by size.
             * 
             * */
            if (!hasTPMS && "DRIVEGUARD".equalsIgnoreCase(tire.getTireName())) {
            	hasTPMS = true;
            }
            
            if (hasTPMS) {
            	TpTpmsPrice tpTpmsPriceVSK = null;
            	TpTpmsPrice tpTpmsPriceVSKLabor = null;
            	
            	if (tireVehicle != null) {
            		tpTpmsPriceVSK = pricingDAO.getVehicleTpmsVSK(tireVehicle.getYear(), tireVehicle.getMakeName(), tireVehicle.getModelName(), tireVehicle.getSubmodelName(),store.getStoreNumber().longValue());
            		tpTpmsPriceVSKLabor = pricingDAO.getVehicleTpmsVSKLabor(tireVehicle.getYear(), tireVehicle.getMakeName(), tireVehicle.getModelName(), tireVehicle.getSubmodelName(),store.getStoreNumber().longValue());
            	} else {
            		tpTpmsPriceVSK = pricingDAO.getVehicleTpmsVSK(store.getStoreNumber().longValue());
            		tpTpmsPriceVSKLabor = pricingDAO.getVehicleTpmsVSKLabor(store.getStoreNumber().longValue());
            	}
            	
                if (tpTpmsPriceVSK != null) {
                        tpArticleLog.setTpmsVskArticleNumber(new Long(tpTpmsPriceVSK.getArticleNumber().longValue()));
                        tpArticleLog.setTpmsVskAmount(tpTpmsPriceVSK.getPrice());
                }
                
                if (tpTpmsPriceVSKLabor != null) {
                        tpArticleLog.setTpmsLaborArticleNumber(new Long(tpTpmsPriceVSKLabor.getArticleNumber().longValue()));
                        tpArticleLog.setTpmsLaborAmount(tpTpmsPriceVSKLabor.getPrice());
                }
            }
            
            pricingDAO.updateTpArticleLog(tpArticleLog);
    	}
    	return tpArticleLog;
	}
	
	@SuppressWarnings("rawtypes")
	private TpArticleLog logTPArticleTaxData(TpArticleLog tpArticleLog, String rearArticle, Tire rearTire, Map taxData, double frontDiscountAmount, double rearDiscountAmount, String promoId) {
		if (tpArticleLog != null) {
			if (tpArticleLog.getRearArticle() != null && tpArticleLog.getRearArticle().longValue() > 0) {
				if (rearArticle != null) {
					tpArticleLog.setRearBalanceLabor(new BigDecimal(rearTire.getWheelBalanceLabor()));
                    tpArticleLog.setRearBalanceWeight(new BigDecimal(rearTire.getWheelBalanceWeight()));
                    tpArticleLog.setRearDisposal(new BigDecimal(rearTire.getDisposalPrice()));//?
                    tpArticleLog.setRearExciseTax(new BigDecimal(rearTire.getExciseTax()));
                    tpArticleLog.setRearInstallFee(new BigDecimal(rearTire.getTireInstallPrice()));
                    tpArticleLog.setRearOnSale(rearTire.getOnSale());
                    tpArticleLog.setRearRetailPrice(new BigDecimal(rearTire.getRetailPrice()));
                    tpArticleLog.setRearTireFee(new BigDecimal(rearTire.getTireFee()));
                    tpArticleLog.setRearValveStem(new BigDecimal(rearTire.getValveStem()));
				}
			}
			tpArticleLog.setRetailTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("tireTax").toString())));
			tpArticleLog.setExciseTaxTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("exciseTax").toString())));
            tpArticleLog.setShopHazardSupplyAmount(new BigDecimal(Double.parseDouble(taxData.get("shopSupplies").toString())));
            tpArticleLog.setShopHazardSupplyTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("shopSuppliesTax").toString())));
            tpArticleLog.setValveStemTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("valveTax").toString())));
            tpArticleLog.setWheelBalanceTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("balancingTax").toString())));
            tpArticleLog.setDisposalTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("disposalTax").toString())));
            tpArticleLog.setTotal(new BigDecimal(Double.parseDouble(taxData.get("total").toString())));
            tpArticleLog.setFrontDiscountAmount(new BigDecimal(frontDiscountAmount));
            tpArticleLog.setRearDiscountAmount(new BigDecimal(rearDiscountAmount));
            tpArticleLog.setTpmsVskTaxAmount(new BigDecimal(Double.parseDouble(taxData.get("tpmsVskTax").toString())));
            if (promoId != null) {
            	tpArticleLog.setPromoId(new Long(promoId));
            }
            pricingDAO.updateTpArticleLog(tpArticleLog);
		}
		return tpArticleLog;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TireQuote createQuoteResponse(Store store, Tire tireResults, Tire rearTireResults, 
			TpArticleLog tpArticleLog, TpUserLog tpUserLog,
			TireVehicle tireVehicle, Map offerData, StoreInventory storeInventory, Map taxData, String emptyCart) {		
		VehicleFitment vehicleFitment = null;
		TireSize tireSize = null;
		app.bsro.model.tire.Tire tire = null;
		app.bsro.model.tire.Tire rearTire = null;
		TirePromotion tirePromotion = null;
		QuoteItem item = null;
		Object loadIndexPounds = null;
		Object speedRatingMPH = null;
				
		TireQuote tireQuote = new TireQuote();
		int quantity = (int)tpUserLog.getQuantity();
		/*
		 * 		Add vehicle details to response
		 */
		if (tireVehicle != null) {
			vehicleFitment = new VehicleFitment();
			vehicleFitment.setYear(tireVehicle.getYear());
			vehicleFitment.setMake(tireVehicle.getMakeName());
			vehicleFitment.setModel(tireVehicle.getModelName());
			vehicleFitment.setSubmodel(tireVehicle.getSubmodelName());
			vehicleFitment.setAcesVehicleId(tireVehicle.getAcesVehicleId());
			vehicleFitment.setTpmsInd(tireVehicle.getHasTpms() != null && tireVehicle.getHasTpms().intValue() == 1);			
		}
		
		/*
		 * 		Add tire details to response
		 */
		if (tireResults != null) {
			if (tireVehicle == null) {
				tireSize = new TireSize();
				tireSize.setCrossSection(tireResults.getCrossSection());
				tireSize.setAspectRation(tireResults.getAspect());
				tireSize.setRimSize(tireResults.getRimSize());
			}
			tire = new app.bsro.model.tire.Tire();
			loadIndexPounds = TireSearchUtils.getMappedLoadIndexAndLoadIndexValue().get(""+tireResults.getLoadIndex());
			speedRatingMPH = TireSearchUtils.getMappedSpeedRatingAndSpeedValue().get(tireResults.getSpeedRating());
			tire.setArticle(Long.valueOf(tireResults.getArticle()));
			tire.setBrand(tireResults.getBrand());
			tire.setTireName(tireResults.getTireName());
			tire.setTireGroupName(tireResults.getTireGroupName());
			tire.setTireClassName(tireResults.getTireClassName());
			tire.setTireSegmentName(tireResults.getTireSegmentName());
			tire.setTireSize(tireResults.getTireSize());
			tire.setTireType(tiresUtil.getTireType(tireResults));
			tire.setLoadIndex(Long.valueOf(tireResults.getLoadIndex()));
			if (loadIndexPounds != null) {
				tire.setLoadIndexPounds(Long.valueOf(loadIndexPounds.toString()));
			}
			tire.setLoadRange(tireResults.getLoadRange());
			tire.setVehType(tireResults.getVehtype());
			tire.setSpeedRating(tireResults.getSpeedRating());
			if (speedRatingMPH != null) {
				tire.setSpeedRatingMPH(speedRatingMPH.toString());
			}
			tire.setSidewallDescription(tireResults.getSidewallDescription());
			try {
				tire.setMileage(Long.valueOf(tireResults.getMileage()));
			} catch (Exception e) {
				
			}			
			tire.setTechnology(tireResults.getTechnology());
			tire.setWarrantyName(tireResults.getWarrantyName());
			tire.setDescription(tireResults.getDescription());
			tire.setRetailPrice(Double.valueOf(tireResults.getRetailPrice()));
			if (tireResults.getSalePrice() > 0) {
				tire.setSalePrice(Double.valueOf(tireResults.getSalePrice()));
			}
			tire.setStandardOptional(tireResults.getStandardOptional());
			tire.setFrb(tireResults.getFrontRearBoth());
			tire.setGenerateCatalogPage(tireResults.getGenerateCatalogPage());
			tire.setOemFlag(tireResults.getOemFlag());
			tire.setDiscontinued(tireResults.getDiscontinued());
			tire.setNotBrandedProduct(new Boolean(tireResults.isNotBrandedProduct()));
			tire.setTireBrandName(tiresUtil.getTireBrandName(tireResults));
			tire.setTireBrandImage(tiresUtil.getTireBrandImage(tireResults));
			tire.setTireImage(tiresUtil.getTireImage(tireResults));
		}
		if(StringUtils.isNullOrEmpty(emptyCart)){
			/*
			 * 		Add promotion details to response
			 */
			List discountOffers = (List) offerData.get("discountOffers");
			List rebateOffers = (List) offerData.get("rebateOffers");
			List allOffers = new ArrayList();
			if ((discountOffers != null && discountOffers.size() > 0) || (rebateOffers != null && rebateOffers.size() > 0)) {
				tirePromotion = new TirePromotion();
				if (rebateOffers != null && rebateOffers.size() > 0) {
					tirePromotion.setRebateOffers(rebateOffers);
					for (int i = 0; i < rebateOffers.size(); i++) {
						TirePromotionEvent promoEvent = (TirePromotionEvent) rebateOffers.get(i);
						allOffers.add(promoEvent);
					}
				}
				if (discountOffers != null && discountOffers.size() > 0) {
					tirePromotion.setDiscountOffers(discountOffers);
					tirePromotion.setDiscountAmount(Double.valueOf(decimalValueFormatter.format(Double.parseDouble(offerData.get("discountAmount").toString()))));
					tirePromotion.setFrontDiscountAmount(Double.valueOf(decimalValueFormatter.format(Double.parseDouble(offerData.get("frontDiscountAmount").toString()))));
					tirePromotion.setFrontDiscountAmountTotal(Double.valueOf(decimalValueFormatter.format(Double.parseDouble(offerData.get("frontDiscountAmountTotal").toString()))));
					tirePromotion.setRearDiscountAmount(Double.valueOf(decimalValueFormatter.format(Double.parseDouble(offerData.get("rearDiscountAmount").toString()))));
					tirePromotion.setRearDiscountAmountTotal(Double.valueOf(decimalValueFormatter.format(Double.parseDouble(offerData.get("rearDiscountAmountTotal").toString()))));
					
					TirePromotionEvent promo = (TirePromotionEvent) discountOffers.iterator().next();
					tirePromotion.setPromoId(promo.getPromoId());
					tirePromotion.setPromoName(promo.getPromoName());
					tirePromotion.setPromoDisplayName(promo.getPromoDisplayName());
					
					for (int i = 0; i < discountOffers.size(); i++) {
						TirePromotionEvent promoEvent = (TirePromotionEvent) discountOffers.get(i);
						allOffers.add(promoEvent);
					}
				}			
				if (!allOffers.isEmpty() && allOffers.size() > 0) {
					tirePromotion.setOffers(allOffers);
				}
			}
			
			/*
			 * 		Add tax details to response
			 */
			item = new QuoteItem();
			item.setTotalUnits(quantity);
			item.setUnitPrice(tireResults.getRetailPrice());
			item.setTotalTirePrice(tireResults.getRetailPrice() * (quantity/1.0));
			if (tirePromotion != null) {
				item.setDiscount(tirePromotion.getDiscountAmount());
			}
			if (Boolean.parseBoolean(taxData.get("isMobileTireInstallStore").toString()))
			{
				item.setTireMounting(Double.parseDouble(taxData.get("mobileTireInstallFee").toString()));
			}
			item.setWheelBalance(Double.parseDouble(taxData.get("balancing").toString()));
			if (taxData.get("tpmsVskArticleNumber") != null && Long.parseLong(taxData.get("tpmsVskArticleNumber").toString()) > 0) {
				item.setTpmsValveServiceKit(Double.parseDouble(taxData.get("tpmsVskAmount").toString()));
				item.setTpmsValveServiceKitLabor(Double.parseDouble(taxData.get("tpmsLaborAmount").toString()));
			} else {
				item.setValveStem(Double.parseDouble(taxData.get("valve").toString()));
			}
			if (Double.parseDouble(taxData.get("excise").toString()) > 0) {
				item.setExciseFee(Double.parseDouble(taxData.get("excise").toString()));
			}
			item.setStateEnvironmentalFee(Double.parseDouble(taxData.get("fee").toString()));
			item.setScrapTireRecyclingCharge(Double.parseDouble(taxData.get("disposal").toString()));
			item.setShopSupplies(Double.parseDouble(taxData.get("shopSupplies").toString()));
			if (taxData.get("taxRate") != null && Double.parseDouble(taxData.get("taxRate").toString()) > 0) {
				item.setTax(Double.parseDouble(taxData.get("tax").toString()));
				item.setTotal(Double.parseDouble(taxData.get("total").toString()));
			} else if (Double.parseDouble(taxData.get("shopSupplies").toString()) > 0) {
				item.setTotal(Double.parseDouble(taxData.get("totalNoTax").toString()));
			} else {
				item.setTotal(Double.parseDouble(taxData.get("totalNoTaxSupplies").toString()));
			}
			
			/*
			 * 		Add rear tire details to response
			 */
			if (rearTireResults != null) {
				rearTire = new app.bsro.model.tire.Tire();
				rearTire.setArticle(Long.valueOf(rearTireResults.getArticle()));
				rearTire.setTireSize(rearTireResults.getTireSize());
				rearTire.setRetailPrice(rearTireResults.getRetailPrice());
				
				item.setRearTotalUnits(quantity);
				item.setRearUnitPrice(rearTireResults.getRetailPrice());
				item.setRearTotalTirePrice(rearTireResults.getRetailPrice() * (quantity/1.0));
				
				tireQuote.setRearQuantity(Short.valueOf(""+quantity));
				tireQuote.setRearTire(rearTire);
			}
		}
		tireQuote.setTireQuoteId(tpArticleLog.getTpArticleId());
		tireQuote.setStoreNumber(store.getStoreNumber());
		tireQuote.setQuantity(Short.valueOf(""+quantity));
		tireQuote.setCreatedDate(tpUserLog.getRequestDate());
		tireQuote.setFirstName(tpUserLog.getFirstName());
		tireQuote.setLastName(tpUserLog.getLastName());
		
		if(StringUtils.isNullOrEmpty(emptyCart)){
			if (vehicleFitment != null) {
				tireQuote.setVehicleFitment(vehicleFitment);
			} else {
				tireQuote.setTireSize(tireSize);
			}
			
			tireQuote.setTire(tire);
			if (tirePromotion != null) {
				tireQuote.setTirePromotion(tirePromotion);
			}
			if (storeInventory != null) {
				tireQuote.setStoreInventory(storeInventory);
			}
			tireQuote.setQuoteItem(item);
		}else{
			if (vehicleFitment != null) {
				Long acesVehicleId = vehicleFitment.getAcesVehicleId();
				vehicleFitment = new VehicleFitment();
				vehicleFitment.setAcesVehicleId(acesVehicleId);				
				tireQuote.setVehicleFitment(vehicleFitment);
			} else {
				tireQuote.setTireSize(tireSize);
			}
			Long article = tire.getArticle();
			tire = new app.bsro.model.tire.Tire();
			tire.setArticle(article);
			tireQuote.setTire(tire);
			if (rearTireResults != null) {
				rearTire = new app.bsro.model.tire.Tire();
				rearTire.setArticle(Long.valueOf(rearTireResults.getArticle()));
				tireQuote.setRearTire(rearTire);
				tireQuote.setRearQuantity(Short.valueOf(""+quantity));
			}
		}
		
		return tireQuote;
	}
	
	public PdfPCell getPdfPCell(Phrase phrase, boolean rightAligned) {
		return getPdfPCell(phrase, rightAligned, false);
	}
	public PdfPCell getPdfPCell(Phrase phrase, boolean rightAligned, boolean bordered) {
		return getPdfPCell(phrase, rightAligned, bordered, -1);
	}
	public PdfPCell getPdfPCell(Phrase phrase, boolean rightAligned, boolean bordered, float padding) {
		PdfPCell cell = new PdfPCell(phrase);
		if (!bordered) {
			cell.setBorder(0);
		}
		if (rightAligned) {
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		}
		if (padding >= 0) {
			cell.setPadding(padding);
		}
		return cell;
	}
	
	public PdfPCell getPdfPCell(Image image, boolean fit, boolean rightAligned) {
		PdfPCell cell = new PdfPCell(image, fit);
		cell.setBorder(0);
		if (rightAligned) {
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		}
		return cell;
	}
	
	public byte[] getQuotePDFDoc(TireQuote tireQuote) {
		Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	    		PdfWriter.getInstance(document, baos);
	    		Store store = storeDAO.findStoreById(tireQuote.getStoreNumber());
	    		store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
	    		
	    		document.open();
	    		PdfPTable storeDetailTable = new PdfPTable(2);
	    		storeDetailTable.setSpacingBefore(1f);
	    		storeDetailTable.setSpacingAfter(1f);
	    		storeDetailTable.setWidthPercentage(100);
	    		storeDetailTable.getDefaultCell().setBorder(0);
	    		storeDetailTable.getDefaultCell().setPadding(1);
	    		storeDetailTable.setWidths(new float[]{1, 1});
	    		
	    		Image logoImage = null;
	    		PdfPCell logoCell = new PdfPCell();
	    		/*try {
					logoImage = Image.getInstance(new URL("http://dev01.firestonecompleteautocare.com/static-fcac/images/logo-fcac-print.jpg"));
					logoCell = getPdfPCell(logoImage, false, false);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    		if (logoCell == null) {
	    			logoCell = new PdfPCell();
	    		}*/
	    		Font storeInfoFont = new Font(FontFamily.TIMES_ROMAN, 12);
	    		//remove the html tags
	    		String storeHour = store.getStoreHour().replaceAll("<b>", "").replaceAll("</b>", "").replaceAll("<br />", "\n");
	    		//remove the ASCII characters
	    		String formattedStoreHour = storeHour.replaceAll("&#45;", " - ");
	    			
	    		logoCell.setBorder(Rectangle.NO_BORDER);
	    		if(store.getHolidayHourMessages() != null && !store.getHolidayHourMessages().isEmpty()){
					String HolidayHours = "";
					for(String holidayMessage  : store.getHolidayHourMessages()) {
							 HolidayHours += holidayMessage+ "\n";
						}
					PdfPCell phraseCell = new PdfPCell(new Paragraph(store.getPhone() + "\n" + "Store # "+ store.getStoreNumber() + "\n" + 
					store.getAddress() + 
	      									"\n" + store.getCity() + " " + store.getState() + " " + store.getZip() + 
					"\n" + formattedStoreHour, storeInfoFont ));
					
					Chunk HolidayHoursDisplay = new Chunk( HolidayHours + "\n", new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD,BaseColor.RED));
					PdfPCell holidayCell = getPdfPCell(new Paragraph(HolidayHoursDisplay),false);
					phraseCell.setPaddingLeft(95);
					phraseCell.setBorder(Rectangle.NO_BORDER);
					storeDetailTable.addCell(logoCell);
					storeDetailTable.addCell(phraseCell);
					storeDetailTable.addCell(getPdfPCell(new Paragraph(" "),false));
					holidayCell.setPaddingLeft(95);
			        storeDetailTable.addCell(holidayCell);
					document.add(storeDetailTable); 
				} else {
					PdfPCell storeInfoCell = new PdfPCell(new Paragraph(store.getPhone() + "\n" + "Store # "+ store.getStoreNumber() + "\n" + store.getAddress() + 
	      									"\n" + store.getCity() + " " + store.getState() + " " + store.getZip() + "\n" + formattedStoreHour, storeInfoFont));
					storeInfoCell.setPaddingLeft(100);
					storeInfoCell.setBorder(Rectangle.NO_BORDER);
					storeDetailTable.addCell(logoCell);
					storeDetailTable.addCell(storeInfoCell);
					document.add(storeDetailTable); 
				}
	    		document.add(new Paragraph("                    "));
	    		
	    		// Vehicle Details
	            if (tireQuote.getTireSize() != null) {
	            	Chunk vehicleDetail = new Chunk("Tire Quote: Size "+ tireQuote.getTireSize().getCrossSection() + "/" + tireQuote.getTireSize().getAspectRation() + " R" + tireQuote.getTireSize().getRimSize() + " Tires", new Font(FontFamily.TIMES_ROMAN, 21, Font.BOLD));
	    			document.add(vehicleDetail);
	            } else {
	    			Chunk vehicleDetail = new Chunk("Tire Quote: "+ tireQuote.getVehicleFitment().getYear() + " " + tireQuote.getVehicleFitment().getMake() + " " + tireQuote.getVehicleFitment().getModel() + " " + tireQuote.getVehicleFitment().getSubmodel(), new Font(FontFamily.TIMES_ROMAN, 21, Font.BOLD));
	    			document.add(vehicleDetail);
	    		}
	            
	    		document.add( Chunk.NEWLINE );
	                                    
	            PdfPTable headingTable = new PdfPTable(1);
	    		headingTable.setSpacingBefore(1f);
	    		headingTable.setSpacingAfter(1f);
	    		headingTable.setWidthPercentage(100);
	    		headingTable.getDefaultCell().setPadding(1);
	    		Chunk headline = new Chunk("Bring this Quote with you to the Store.", new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD));
	    		Chunk subhead = new Chunk("Save time by scheduling your appointment in advance." + 
	            		" To do so, you may call the store listed above or return to the site and schedule online.", new Font(FontFamily.TIMES_ROMAN, 12));
	            		
	    		headingTable.addCell(getPdfPCell(new Paragraph(headline), false));
	    		headingTable.addCell(getPdfPCell(new Paragraph(subhead), false));
	    		document.add(headingTable);
	    		
	    		document.add( Chunk.NEWLINE );
	                        
	    		PdfPTable dataTable = new PdfPTable(2);
	    		//dataTable.setSpacingBefore(1f);
	    		dataTable.setSpacingAfter(1f);
	    		dataTable.setWidthPercentage(100);
	    		dataTable.getDefaultCell().setPadding(1);
	    		dataTable.setWidths(new float[]{1, 1});
	    		
	    		PdfPTable tireDetailTable = new PdfPTable(2);
	    		tireDetailTable.setSpacingBefore(1f);
	    		tireDetailTable.setSpacingAfter(1f);
	    		tireDetailTable.setWidthPercentage(100);
	    		tireDetailTable.getDefaultCell().setBorder(0);
	    		tireDetailTable.getDefaultCell().setPadding(2);
	    		tireDetailTable.setWidths(new float[]{1, 2});
	    		PdfPCell imageCell = new PdfPCell();
	    		imageCell.setBorder(0);
			    /*String tireImageDefaultPath = (request.getAttribute("tireQuoteImage") != null) ? request.getAttribute("tireQuoteImage").toString() : "/static-fcac/images/tires/full-60/h175/NoPic.png";		
	    		String tireImagePath = request.getSession().getServletContext().getRealPath(tireImageDefaultPath);
	    		Image tireImage = Image.getInstance(tireImagePath);
	    		tireImage.scaleAbsolute(75,100);
	    		PdfPCell imageCell = getPdfPCell(tireImage, false, false);
	    					
	    		String tireBrandImageDefaultPath = (request.getAttribute("tireBrandImage") != null) ? request.getAttribute("tireBrandImage").toString() : null;
	    		String tireBrandImagePath = request.getSession().getServletContext().getRealPath(tireBrandImageDefaultPath);
	    		Image tireBrandImage = Image.getInstance(tireBrandImagePath);
	    		tireBrandImage.scaleAbsolute(100,25);*/
	    		PdfPCell articleCell = getPdfPCell(new Paragraph("Cell 2"), false);
	    		    		
	    		PdfPTable articleDetailTable = new PdfPTable(1);
	    		articleDetailTable.setWidthPercentage(100);
	    		articleDetailTable.getDefaultCell().setBorder(0);
	    		Font articleDetailFont = new Font(FontFamily.TIMES_ROMAN, 10);
	    		
	    		//articleDetailTable.addCell(getPdfPCell(tireBrandImage, false, false));
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph(tireQuote.getTire().getBrand() + " " + tireQuote.getTire().getTireName()), false));
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph("  * "+tireQuote.getTire().getMileage(), articleDetailFont), false));
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph("  * "+tireQuote.getTire().getTireType() + " Tires", articleDetailFont), false));
	    		if(tireQuote.getRearTire() != null) {
	    			articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Front Size: "+tireQuote.getTire().getTireSize(), articleDetailFont), false));
	    			articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Rear Size: "+tireQuote.getRearTire().getTireSize(), articleDetailFont), false));
	    		} else {
	    			articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Size: "+tireQuote.getTire().getTireSize(), articleDetailFont), false));
	    		}
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Article Number: "+tireQuote.getTire().getArticle() + (tireQuote.getRearTire()!=null?";"+tireQuote.getRearTire().getArticle():""), articleDetailFont), false));
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Load Index: "+tireQuote.getTire().getLoadIndex() + " (" + TireSearchUtils.getMappedLoadIndexAndLoadIndexValue().get(tireQuote.getTire().getLoadIndex() + "") + " lbs)", articleDetailFont), false));
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Speed Rating: "+tireQuote.getTire().getSpeedRating() + " (" + TireSearchUtils.getMappedSpeedRatingAndSpeedValue().get(tireQuote.getTire().getSpeedRating()) + " mph)", articleDetailFont), false));
	    		articleDetailTable.addCell(getPdfPCell(new Paragraph("  * Sidewall: "+tireQuote.getTire().getSidewallDescription(), articleDetailFont), false));
	    		articleCell.addElement(articleDetailTable);
				
	    		tireDetailTable.addCell(imageCell);
	    		tireDetailTable.addCell(articleCell);
	    		
	    		PdfPTable tireQuoteTable = new PdfPTable(2);
	    		tireQuoteTable.setWidthPercentage(100);
	    		tireQuoteTable.getDefaultCell().setBorder(1);
	    		tireQuoteTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	    		tireQuoteTable.setWidths(new float[]{2, 1});
	    		Font tireQuoteDetailFont = new Font(FontFamily.TIMES_ROMAN, 12);
	    		Font taxFont = new Font(FontFamily.TIMES_ROMAN, 12);
	    		taxFont.setColor(223, 41, 27);
	    		
	    		if(tireQuote.getRearTire() != null) {
	    			if(tireQuote.getTire().getRetailPrice() > 0.0)
	    			{
	    				PdfPCell fTireCell = getPdfPCell(new Paragraph(tireQuote.getQuantity() + " Front Tire(s) ($"+df.format(tireQuote.getTire().getRetailPrice())+" ea.)", tireQuoteDetailFont), true, true, 5);
	    				PdfPCell fTirePriceCell = getPdfPCell(new Paragraph("$"+df.format(tireQuote.getTire().getRetailPrice()*(tireQuote.getQuantity()/1.0)), tireQuoteDetailFont), false, true, 5);
						tireQuoteTable.addCell(fTireCell);
						tireQuoteTable.addCell(fTirePriceCell);
	    			}
	    			else
	    			{
	    				PdfPCell fTireCell = getPdfPCell(new Paragraph(tireQuote.getQuantity() + " Front Tire(s) ", tireQuoteDetailFont), true, true, 5);
	        			PdfPCell fTirePriceCell = getPdfPCell(new Paragraph("Contact store", tireQuoteDetailFont), false, true, 5);	
						tireQuoteTable.addCell(fTireCell);
						tireQuoteTable.addCell(fTirePriceCell);
	    			}
	    			
	    			if(tireQuote.getRearTire().getRetailPrice() > 0.0)
	    			{
	    				PdfPCell rTireCell = getPdfPCell(new Paragraph(tireQuote.getQuantity() + " Rear Tire(s) ($"+df.format(tireQuote.getRearTire().getRetailPrice())+" ea.)", tireQuoteDetailFont), true, true, 5);
	    				PdfPCell rTirePriceCell = getPdfPCell(new Paragraph("$"+df.format(tireQuote.getRearTire().getRetailPrice()*(tireQuote.getQuantity()/1.0)), tireQuoteDetailFont), false, true, 5);
						tireQuoteTable.addCell(rTireCell);
						tireQuoteTable.addCell(rTirePriceCell);
	    			}
	    			else
	    			{
	    				PdfPCell rTireCell = getPdfPCell(new Paragraph(tireQuote.getQuantity() + " Rear Tire(s)", tireQuoteDetailFont), true, true, 5);
	        			PdfPCell rTirePriceCell = getPdfPCell(new Paragraph("Contact store", tireQuoteDetailFont), false, true, 5);	
						tireQuoteTable.addCell(rTireCell);
						tireQuoteTable.addCell(rTirePriceCell);
	    			}
	    			
	    			
	    		} else {
	    			String qtyTires = "1".equals(tireQuote.getQuantity()) ? tireQuote.getQuantity()+" Tire" : tireQuote.getQuantity()+" Tires"; 
	    			PdfPCell tireCell = getPdfPCell(new Paragraph(qtyTires + " ($"+df.format(tireQuote.getTire().getRetailPrice())+" ea.)", tireQuoteDetailFont), true, true, 5);
	    			PdfPCell tirePriceCell = getPdfPCell(new Paragraph("$"+df.format(tireQuote.getTire().getRetailPrice()*(tireQuote.getQuantity()/1.0)), tireQuoteDetailFont), false, true, 5);
	    			tireQuoteTable.addCell(tireCell);
	    			tireQuoteTable.addCell(tirePriceCell);
	    		}
	    		
	    		/*
	    		 * 	ToDo
	    		 * 
	    		 * if(tireQuote.getTirePromotion() != null) {
	    			PdfPCell tirePromoCell = getPdfPCell(new Paragraph(promo.getPromoDisplayName(), tireQuoteDetailFont), true, true, 5);
	    			PdfPCell tirePromoPriceCell = getPdfPCell(new Paragraph("-$"+df.format(Double.parseDouble(request.getAttribute("discountAmount").toString())), tireQuoteDetailFont), false, true, 5);
	    			tireQuoteTable.addCell(tirePromoCell);
	    			tireQuoteTable.addCell(tirePromoPriceCell);
	    		}*/
	    		
	    		PdfPCell tireInstallCell = null;
	    		PdfPCell tireInstallPriceCell = null;
	    		tireInstallCell = getPdfPCell(new Paragraph("Tire Mounting", tireQuoteDetailFont), true, true, 5);
	    		tireInstallPriceCell = getPdfPCell(new Paragraph("FREE", tireQuoteDetailFont), false, true, 5);
	    		
	    		tireQuoteTable.addCell(tireInstallCell);
	    		tireQuoteTable.addCell(tireInstallPriceCell);
	    		if ("DRIVEGUARD".equalsIgnoreCase(tireQuote.getTire().getTireName())) {
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("DriveGuard Supplemental Road Hazard", tireQuoteDetailFont), true, true, 5));
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("FREE", tireQuoteDetailFont), false, true, 5));
	    		}
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("Tire/Wheel Alignment Check", tireQuoteDetailFont), true, true, 5));
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("FREE", tireQuoteDetailFont), false, true, 5));
	    		
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("Lifetime Free Tire Rotation", tireQuoteDetailFont), true, true, 5));
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("FREE", tireQuoteDetailFont), false, true, 5));
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("Computerized Wheel Balance", tireQuoteDetailFont), true, true, 5));
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getWheelBalance()), tireQuoteDetailFont), false, true, 5));
	    		if(tireQuote.getQuoteItem().getTpmsValveServiceKit() > 0) {
	    			if(tireQuote.getQuoteItem().getTpmsValveServiceKit() > 0) {
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph("TPMS Valve Service Kit", tireQuoteDetailFont), true, true, 5));
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getTpmsValveServiceKit()), tireQuoteDetailFont), false, true, 5));
	    			}
	    			if(tireQuote.getQuoteItem().getTpmsValveServiceKitLabor() > 0) {
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph("TPMS Valve Service Kit Labor", tireQuoteDetailFont), true, true, 5));
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getTpmsValveServiceKitLabor()), tireQuoteDetailFont), false, true, 5));
	    			}
	    		} else {
	    			if(tireQuote.getQuoteItem().getValveStem() > 0) {
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph("Valve Stem", tireQuoteDetailFont), true, true, 5));
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getValveStem()), tireQuoteDetailFont), false, true, 5));
	    			}
	    		}
	    		if(tireQuote.getQuoteItem().getExciseFee() > 0) {
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("Excise Tax", tireQuoteDetailFont), true, true, 5));
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getExciseFee()), tireQuoteDetailFont), false, true, 5));
	    		}
	    		if(tireQuote.getQuoteItem().getStateEnvironmentalFee() > 0) {
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("State Environmental Fee", tireQuoteDetailFont), true, true, 5));
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getStateEnvironmentalFee()), tireQuoteDetailFont), false, true, 5));
	    		}
	    		if(tireQuote.getQuoteItem().getScrapTireRecyclingCharge() > 0) {
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("Scrap Tire Recycling Charge", tireQuoteDetailFont), true, true, 5));
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getScrapTireRecyclingCharge()), tireQuoteDetailFont), false, true, 5));
	    		}
	    		if(tireQuote.getQuoteItem().getShopSupplies() > 0) {
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("Shop Supplies", tireQuoteDetailFont), true, true, 5));
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getShopSupplies()), tireQuoteDetailFont), false, true, 5));
	    		}
	    		/*
	    		 * ToDo
	    		 * 
	    		 * if(rebateOffers.size() > 0) {
	    			for(Iterator it = rebateOffers.iterator(); it.hasNext();){
						promo = (TirePromotionEvent)it.next();
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph(promo.getPromoDisplayName(), tireQuoteDetailFont), true, true, 5));
	    				tireQuoteTable.addCell(getPdfPCell(new Paragraph((promo.getPromoTotalTag()==null)?"":promo.getPromoTotalTag(), tireQuoteDetailFont), false, true, 5));
	    			}
	    		}*/
	    		if(tireQuote.getQuoteItem().getTax() > 0) {
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("Tax", tireQuoteDetailFont), true, true, 5));
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getTax()), taxFont), false, true, 5));
	    		}
	    		
	    		Font subTotalFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	    		tireQuoteTable.addCell(getPdfPCell(new Paragraph("Subtotal*", subTotalFont), true, true, 5));
	    		if(tireQuote.getQuoteItem().getTotal() > 0){
	    			tireQuoteTable.addCell(getPdfPCell(new Paragraph("$"+df.format(tireQuote.getQuoteItem().getTotal()), subTotalFont), false, true, 5));
	    		}
	    			
	    		PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
	      		PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
	      		cell1.setBorder(Rectangle.NO_BORDER);
	      		cell1.addElement(tireDetailTable);
				cell2.addElement(tireQuoteTable);
	      		dataTable.addCell(cell1);
	      		dataTable.addCell(cell2);
	    		
	            document.add(dataTable);
	            
	            document.add( Chunk.NEWLINE );
	            document.add( Chunk.NEWLINE );
	            
	            Font disclaimerFont = new Font(FontFamily.TIMES_ROMAN, 8);
	            Paragraph codeDetail = new Paragraph("Code #"+tireQuote.getTireQuoteId()+".     " + 
	            	"* Price valid for 30 days from " + 
	    			(new java.text.SimpleDateFormat("MMMM d, yyyy")).format(tireQuote.getCreatedDate()), disclaimerFont);
	            document.add(codeDetail);
	            
	    		LineSeparator sep = new LineSeparator();
	    		sep.setOffset(-3.0f);
	    		sep.setLineWidth(0.5f);
	    		document.add(sep);
	    		
	    		document.close();
	    } catch (DocumentException e) {
	    	System.err.println("DocumentException in generating pdf document");
	   	}
	    return baos.toByteArray();
	}
}