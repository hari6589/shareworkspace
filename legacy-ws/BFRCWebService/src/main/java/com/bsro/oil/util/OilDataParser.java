package com.bsro.oil.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.commands.read.GetChildrenNamesCommand;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bsro.oil.exception.OilDataException;
import java.io.IOException;




/**
 * Parser class to parse the data returned by the OATS web services
 * 
 * @author premier
 *
 */
public class OilDataParser implements OilDataConstants {

		private static final Log logger = LogFactory.getLog(OilDataParser.class);
	/**
	 * Parses the xml data response from the web service for "all years" for a given sector and returns
	 * the hash map
	 * 
	 * @param data
	 * @return
	 */
	public static LinkedHashMap<String, String> parseYears(String data) throws IOException
	{
				
		LinkedHashMap<String, String> sortedYearMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> yearMap = new LinkedHashMap<String, String>();


        try {
    		
    		Document xmlDocument  = getDOMDocument(data);
    		
    		if (xmlDocument != null) {
	            XPath xPath =  XPathFactory.newInstance().newXPath();
	
	        	NodeList yearsNodeList = (NodeList) xPath.compile(YEARS_EXPRESSION).evaluate(xmlDocument, XPathConstants.NODESET);
		        
		        for (int i = 0; i < yearsNodeList.getLength(); i++) {
		        	String yearValue = yearsNodeList.item(i).getFirstChild().getNodeValue();
		        	/**
		        	 * The following attribute retrieves href from OATS webservice.
		        	 * For example a href may contains a value href="/browse/retail/cars/1961 for a given year 1961
		        	 * 
		        	 * String attributeValue = (nodeList.item(i).getAttributes()).getNamedItem("href").getNodeValue();
		        	 */
		        	yearMap.put(yearValue, yearValue);
		        }
    		} else {
    			throw new OilDataException("Invalid XML Data to populate years");
    		}
        } catch (XPathExpressionException expressionException) {
        	OilDataException oilDataException = new OilDataException("XPath expression is not valid for years",expressionException);
        	oilDataException.printStackTrace();
        } catch (OilDataException oilDataException) {
        	logger.error("InValid XML Data for Years" + oilDataException);
        	oilDataException.printStackTrace();
        }
        
		if (yearMap != null && !yearMap.isEmpty()) {
			ArrayList<String> yearMapValues = new ArrayList<String>(yearMap.values());
			Collections.sort(yearMapValues,Collections.reverseOrder());  
			
			String lastValue = null;
			for (String value : yearMapValues) {
				if (lastValue == value) {
					// this is to prevent duplicates
					continue;
				}
	
				lastValue = value;
	
				for (String key : yearMap.keySet()) {
					if (yearMap.get(key) == value) {
						sortedYearMap.put(key, value);
						break;
					}
				}
			}
		}

		return sortedYearMap;
	}
	
	
	/**
	 * Parses the xml data response from the web service for all the "makes" 
	 * for a given year
	 * 
	 * @param data
	 * @return
	 */
	public static LinkedHashMap<String, String> parseMakes(String data) throws IOException
	{
		LinkedHashMap<String, String> sortedMakesMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> makesMap = new LinkedHashMap<String, String>();


        try {
	        
    		Document xmlDocument  = getDOMDocument(data);
    		
    		if (xmlDocument != null) {
	            XPath xPath =  XPathFactory.newInstance().newXPath();
	
	        	NodeList makesNodeList = (NodeList) xPath.compile(MAKE_EXPRESSION).evaluate(xmlDocument, XPathConstants.NODESET);
	        	
	        	if(useNewAPI){
	        		for (int i = 0; i < makesNodeList.getLength(); i++) {

	        			String makesValue = makesNodeList.item(i).getFirstChild().getNodeValue();
	        			makesMap.put(makesValue,makesValue);
	        		}
	        	}else{
	        		for (int i = 0; i < makesNodeList.getLength(); i++) {

	        			String makesValueWithRegion = makesNodeList.item(i).getFirstChild().getNodeValue();

	        			/**
	        			 * The following attribute retrieves href from OATS webservice.
	        			 * For example a href may contains a value href="/browse/retail/cars/2005/volvo_(us)" for a given make Volvo
	        			 * 
	        			 * String attributeValue = (nodeList.item(i).getAttributes()).getNamedItem("href").getNodeValue();
	        			 */

	        			if( makesValueWithRegion.endsWith("(US)")) {

	        				String makesValue = makesValueWithRegion.replace(" (US)", "");

	        				makesMap.put(makesValue.toLowerCase(),makesValue);
	        			}

	        		}
	        	}
    		} else {
    			throw new OilDataException("Invalid XML Data to populate Manufacturers");
    		}

        } catch (XPathExpressionException expressionException) {
        	OilDataException oilDataException = new OilDataException("XPath expression is not valid for Manufacturers",expressionException);
        	oilDataException.printStackTrace();
        } catch (OilDataException oilDataException) {
        	logger.error("InValid XML Data for Manufacturers" + oilDataException);
        	oilDataException.printStackTrace();
        }


		if (makesMap != null && !makesMap.isEmpty()) {
			ArrayList<String> makesMapValues = new ArrayList<String>(makesMap.values());
			Collections.sort(makesMapValues);  
			
			String lastValue = null;
			for (String value : makesMapValues) {
				if (lastValue == value) {
					// this is to prevent duplicates
					continue;
				}
	
				lastValue = value;
	
				for (String key : makesMap.keySet()) {
					if (makesMap.get(key) == value) {
						sortedMakesMap.put(key, value);
						break;
					}
				}
			} 
		}

		
		return sortedMakesMap;
	}
	
	
	/**
	 * Parses the xml data response from the web service for all the "makes" 
	 * for a given year
	 * 
	 * @param data
	 * @return
	 */
	public static LinkedHashMap<String,String> parseProducts(String data) throws IOException
	{
		
		LinkedHashMap<String, String> sortedProductMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> productMap = new LinkedHashMap<String, String>();
		try {
	        
    		Document xmlDocument  = getDOMDocument(data);
    		String productName = "";
    		String tierOrder = "";
    		
    		if (xmlDocument != null) {
	            XPath xPath =  XPathFactory.newInstance().newXPath();
	            if(useNewAPI){
	            	Node equipmentEngineNode = (Node) xPath.compile(ENGINE_EXPRESSION).evaluate(xmlDocument, XPathConstants.NODE);
	            	productMap = getProductDetailsFromNode(equipmentEngineNode);          	
	        	            	
	            }else{
	            	NodeList makesNodeList = (NodeList) xPath.compile(PRODUCT_EXPRESSION).evaluate(xmlDocument, XPathConstants.NODESET);

	            	for (int i = 0; i < makesNodeList.getLength(); i++) {
	            		productName = makesNodeList.item(i).getFirstChild().getNodeValue();
	            		tierOrder = makesNodeList.item(i).getAttributes().getNamedItem(TIER_ORDER).getNodeValue();
	            		productMap.put(tierOrder,productName);
	            	}
	            }
    		} else {
    			throw new OilDataException("Invalid XML Data to populate Products");
    		}

        } catch (XPathExpressionException expressionException) {
        	OilDataException oilDataException = new OilDataException("XPath expression is not valid for Products",expressionException);
        	oilDataException.printStackTrace();
        } catch (OilDataException oilDataException) {
        	logger.error("InValid XML Data for Products" + oilDataException);
        	oilDataException.printStackTrace();
        }
        
		if (productMap != null && !productMap.isEmpty()) {
			ArrayList<String> productMapValues = new ArrayList<String>(productMap.values());
			Collections.sort(productMapValues);  
			
			String lastValue = null;
			for (String value : productMapValues) {
				if (lastValue == value) {
					// this is to prevent duplicates
					continue;
				}
	
				lastValue = value;
	
				for (String key : productMap.keySet()) {
					if (productMap.get(key) == value) {
						sortedProductMap.put(key, value);
						break;
					}
				}
			} 
		}
		
		return sortedProductMap;

	}

	/**
	 * Parses the xml data response from the web service for a given vehicle to get engine capacity info" 
	 * 
	 * @param data
	 * @return
	 */
	public static String parseCapacityForVehicle(String data) throws IOException
	{
		String engineCapacity = null;
		String capacityUnit = null;
        try {
	        
    		Document xmlDocument  = getDOMDocument(data);
    		
    		if (xmlDocument != null) {
    			XPath xPath =  XPathFactory.newInstance().newXPath();
    			if(useNewAPI){
    				
    				capacityUnit = (String)xPath.compile(CAPACITY_UNIT_EXPRESSION).evaluate(xmlDocument, XPathConstants.STRING);

    				Node equipmentEngineNode =  (Node)xPath.compile(ENGINE_EXPRESSION).evaluate(xmlDocument, XPathConstants.NODE);
    				engineCapacity = getOilCapacityFromNode(equipmentEngineNode, capacityUnit);

    			}else{
    				Node capacityForVehicleNode =  (Node)xPath.compile(CAPACITY_EXPRESSION).evaluate(xmlDocument, XPathConstants.NODE);

    				if ( capacityForVehicleNode != null) { 
    					engineCapacity = capacityForVehicleNode.getFirstChild().getNodeValue();
    				} 
    			}
    			if(StringUtils.isBlank(engineCapacity)) {
					//Default value for engine capacity
					engineCapacity = "5";
				}
		        
    		} else {
    			throw new OilDataException("Invalid XML Data to populate capacity");
    		}

        } catch (XPathExpressionException expressionException) {
        	OilDataException oilDataException = new OilDataException("XPath expression is not valid for Engine Capacity",expressionException);
        	oilDataException.printStackTrace();
        } catch (OilDataException oilDataException) {
        	logger.error("InValid XML Data for Engine Capacity" + oilDataException);
        	oilDataException.printStackTrace();
        }

       
		return engineCapacity;
	}

	/**
	 * Parses the xml data response from the web service for all the "models" 
	 * for a given year and a make
	 * 
	 * @param data
	 * @return
	 */
	public static LinkedHashMap<String, String> parseModels(String data, String yearId) throws IOException
	{
		LinkedHashMap<String, String> sortedModelsMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> modelsMap = new LinkedHashMap<String, String>();

		// Prepare a new expression with yearId to remove duplicate models presents in xml response file.
		String modelExpression  = MODEL_EXPRESSION + yearId + "]";
		
        try {

    		Document xmlDocument  = getDOMDocument(data);
    		
    		if (xmlDocument != null) {
	            XPath xPath =  XPathFactory.newInstance().newXPath();
	            if(useNewAPI){
	            	
	            	NodeList vehicleNodeList = (NodeList) xPath.compile(MODEL_EXPRESSION_NEW).evaluate(xmlDocument, XPathConstants.NODESET);
	            	for (int i = 0; i < vehicleNodeList.getLength(); i++) {
	            		String modelHref = (vehicleNodeList.item(i).getAttributes()).getNamedItem(MODEL_HREF).getNodeValue();
	            		String modelHrefTruncated="";
	            		StringTokenizer stk = new StringTokenizer(modelHref, "/");
	            		while (stk.hasMoreTokens()){
	            			modelHrefTruncated = stk.nextToken();
	            		}
	            		
	            		NodeList vehicleNode  = vehicleNodeList.item(i).getChildNodes();
	            		
	            		for (int j = 0; j < vehicleNode.getLength(); j++) {
	            			if (vehicleNode.item(j).getNodeName().equalsIgnoreCase(MODEL)){

	            				String modelName  = vehicleNode.item(j).getFirstChild().getNodeValue();
	            				String modelNameTruncated = modelName;
	            				//truncate the model names and then trim trailing spaces before adding 
	            				if(!modelName.isEmpty() && modelName.indexOf("(") > 1)
	            					modelNameTruncated = modelName.substring(0, modelName.indexOf("(")-1).trim();
	            				modelsMap.put(modelHrefTruncated,modelNameTruncated );

	            			}
	            		} 
	            	}
	            	
	            }else{
	
	            	NodeList vehicleNodeList = (NodeList) xPath.compile(modelExpression).evaluate(xmlDocument, XPathConstants.NODESET);

	            	/*
	            	 * 1. Retrieve all vehicles
	            	 * 2. Get guid 
	            	 * 3. Retrieve models for a given year.
	            	 */

	            	for (int i = 0; i < vehicleNodeList.getLength(); i++) {

	            		String modelGUID = (vehicleNodeList.item(i).getAttributes()).getNamedItem(MODEL_GUID).getNodeValue();


	            		NodeList vehicleNode  = vehicleNodeList.item(i).getChildNodes();

	            		for (int j = 0; j < vehicleNode.getLength(); j++) {


	            			if (vehicleNode.item(j).getNodeName().equalsIgnoreCase(MODEL)){

	            				String modelName  = vehicleNode.item(j).getFirstChild().getNodeValue();

	            				modelsMap.put(modelGUID,modelName );

	            			}
	            		} 
	            	}
	            }
    		} else {
    			throw new OilDataException("Invalid XML Data to populate Models");
    		}

        } catch (XPathExpressionException expressionException) {
        	OilDataException oilDataException = new OilDataException("XPath expression is not valid for models",expressionException);
        	oilDataException.printStackTrace();

        } catch (OilDataException oilDataException) {
        	logger.error("InValid XML Data for Models" + oilDataException);
        	oilDataException.printStackTrace();
        }

		
		if (modelsMap != null && !modelsMap.isEmpty()) {
			ArrayList<String> modelMapValues = new ArrayList<String>(modelsMap.values());
			Collections.sort(modelMapValues);  
			
			String lastValue = null;
			for (String value : modelMapValues) {
				if (lastValue == value) {
					// this is to prevent duplicates
					continue;
				}
	
				lastValue = value;
	
				for (String key : modelsMap.keySet()) {
					if (modelsMap.get(key) == value) {
						sortedModelsMap.put(key, value);
						break;
					}
				}
			} 
		}
    		
        return sortedModelsMap;
	}
	
	
	public static Document getDOMDocument(String data) throws IOException {
		Document xmlDocument = null;
		try {
			InputSource is= new InputSource(new StringReader(data));
	        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder =  builderFactory.newDocumentBuilder();
	        xmlDocument = builder.parse(is);

		} catch (ParserConfigurationException configException) {
			OilDataException oilDataException = new OilDataException("XML Data is not valid or empty",configException);
			oilDataException.printStackTrace();
		} catch (SAXException saxException) {
			OilDataException oilDataException = new OilDataException("Unable to parse XML Data",saxException);
			oilDataException.printStackTrace();
		}
		return xmlDocument;
	}

	/**
	 * @param node
	 * @param capacityUnit
	 * @return the engine capacity in quarts of oil.
	 */
	private static String getOilCapacityFromNode(Node node, String capacityUnit){
		if ( node != null) { 
			NodeList equipmentEngineNodeChildren = node.getChildNodes();
			for(int i=0; i < equipmentEngineNodeChildren.getLength(); i++){
				if(equipmentEngineNodeChildren.item(i).getNodeName().equalsIgnoreCase(DISPLAY_CAPACITY_NODE)){
					String engineCapacityGallons = equipmentEngineNodeChildren.item(i).getTextContent();
					if(!StringUtils.isBlank(engineCapacityGallons) && CAPACITY_US_GALLONS.equalsIgnoreCase(capacityUnit)){
						//converting gallons to quarts (1 us-gallons = 4 quarts)
						double capacityInQuarts = (new java.math.BigDecimal(engineCapacityGallons)).doubleValue() * 4;
						return String.valueOf(capacityInQuarts);
					}
				}
			}    					
		}
		return "";
	}
	
	/**
	 * @param node equipment application node
	 * @return a map with tier order as key and product name as value
	 */
	private static LinkedHashMap<String, String> getProductDetailsFromNode(Node node){
		LinkedHashMap<String, String> productMap = new LinkedHashMap<String, String>();
		if ( node != null) { 
			NodeList childNodeList = node.getChildNodes();
        	
        	for (int i = 0; i < childNodeList.getLength(); i++) {
        		if(childNodeList.item(i).getNodeName().equalsIgnoreCase(PRODUCT)){
        			String productName = childNodeList.item(i).getFirstChild().getTextContent();
        			String tierOrder = childNodeList.item(i).getAttributes().getNamedItem(TIER).getNodeValue();
        			productMap.put(tierOrder, productName);
        		}
        	}
		}
		
		return productMap;
	}
}
