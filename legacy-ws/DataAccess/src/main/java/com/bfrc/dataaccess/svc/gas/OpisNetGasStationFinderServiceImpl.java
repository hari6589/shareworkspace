package com.bfrc.dataaccess.svc.gas;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import app.bsro.model.gas.stations.StationPrice;

import com.bfrc.dataaccess.exception.HttpException;
import com.bfrc.dataaccess.http.HttpResponse;
import com.bfrc.dataaccess.http.HttpUtils;
import com.bfrc.dataaccess.svc.geocode.GeoLatLong;

@Service
public class OpisNetGasStationFinderServiceImpl implements
		GasStationFinderService {

	private Logger logger = Logger.getLogger(getClass().getName());
	private String tokenId = "auBZ2RoP9XcFUIgEdrgbYQ==";
	private String opisUrl = "https://services.opisnet.com/RealtimePriceService/RealtimePriceService.asmx/";
	
	public enum Sort {
		
		UNLEADED("U", "Unleaded"),
		MIDGRADE("M", "Midgrade"),
		PREMIUM("P", "Premium"),
		DIESEL("D", "Diesel");
		
		private String sortCd;
		private String sort;
		private Sort(String sortCd, String sort) {
			this.sortCd = sortCd;
			this.sort = sort;
		}
		
		public static Sort getByCode(String code) {
			code = StringUtils.trimToEmpty(code);
			Sort[] sorts = Sort.values();
			for(Sort s : sorts) {
				if(code.equals(s.sortCd))
					return s;
			}
			return UNLEADED;
		}
		
		public String getSortCd() {
			return this.sortCd;
		}
		
		public String getSort() {
			return this.sort;
		}
	}
	
	public List<StationPrice> findByGeoLocation(GeoLatLong geoLatLong, Sort sort) {
		return findByGeoLocation(geoLatLong, sort, 0);
	}
	
	public List<StationPrice> findByGeoLocation(GeoLatLong geoLatLong, Sort sort, int searchRadius) {
		
		 StringBuilder url = new StringBuilder(opisUrl);
		 try {
//			 url.append("GetLatLongSortedWithDistanceResults");
			 url.append("GetLatLongSortedResults");
			 url.append("?UserTicket="+URLEncoder.encode(getTicketId(),"utf-8"));
			 url.append("&Latitude="+URLEncoder.encode(geoLatLong.getLatitude().toString(),"utf-8"));
			 url.append("&Longitude="+URLEncoder.encode(geoLatLong.getLongitude().toString(),"utf-8"));
//             url.append("&distance="+searchRadius);
//             url.append("&isFilteredByDistance=false");
			 url.append("&SortByProduct="+URLEncoder.encode(sort.getSort(),"utf-8"));
//			 url.append("&UserLatitude="+URLEncoder.encode(geoLatLong.getLatitude().toString(),"utf-8"));
//			 url.append("&UserLongitude="+URLEncoder.encode(geoLatLong.getLongitude().toString(),"utf-8"));
			 
		} catch (UnsupportedEncodingException e) {
			logger.severe("Error building OPIS url using geoLatLong: "+geoLatLong.toString()+", sort: "+sort.getSort()+", radius: "+searchRadius+" - "+e.getMessage());
			return null;
		}
        HttpResponse httpResponse = null;
        try {
			httpResponse = HttpUtils.get(url.toString(), null);
		} catch (HttpException e) {
			logger.severe("Unable to make URL call to OPIS: "+url.toString()+" -"+e.getMessage());
			return null;
		}
        if(!httpResponse.isHttpValidResponse())
        	return null;
        
        List<StationPrice> stationPrices = null;
        try {
        	stationPrices = parseOpisGasStations(httpResponse.getResponseBody());
        }catch(Exception e) {
        	logger.severe("Unable to parse OPIS station results: "+e.getMessage());
        }
		
        return stationPrices;
	}
	

	public double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}
	
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	static private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	static private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	private String getTicketId() {

			String ticketId = null;
			try {
				String uri = opisUrl+"Authenticate?CustomerToken=" + tokenId;
				HttpResponse response = HttpUtils.get(uri, null);
				if(response.isHttpValidResponse()) {
					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response.getResponseBody().getBytes()));
					XPath xpath = XPathFactory.newInstance().newXPath();

					XPathExpression statusCodeExp = xpath.compile("//string/text()");
					ticketId = (String)statusCodeExp.evaluate(doc, XPathConstants.STRING);

				} else {
					logger.warning("OPIS call for ticketId returned invalid response: "+response.toString());
					ticketId = "";
				}
			} catch (Exception ex) {
				logger.severe("Error returning ticketId from OPIS: "+ex.getMessage());
				return "";
			}

			return StringUtils.trimToEmpty(ticketId);
		
	}
	
	private List<StationPrice> parseOpisGasStations(String result) throws JDOMException, Exception {
		
		SAXBuilder builder = new SAXBuilder();
		List<StationPrice> resultSet = new ArrayList<StationPrice>();

		org.jdom.Document document = (org.jdom.Document) builder.build(new StringReader(result));
		Element rootNode = document.getRootElement();
		List children = rootNode.getChildren();
		Element documentElement = null;
		for(int i=0;i<children.size();i++) {
			Element e = (Element)children.get(i);
			String eName = e.getName();
			if(eName.startsWith("diff")) {
				List diffChildren = e.getChildren();
				documentElement = (Element)diffChildren.get(0);
			}
		}

		if (documentElement != null) {
			try {
				List records = documentElement.getChildren();
				for (int i = 0; i < records.size(); i++) {
					Element record = (Element) (records.get(i));
					List datas = record.getChildren();
					StationPrice station = new StationPrice();
					station.setStationIdentifier("StationPrices"+(i+1));
					for (int d = 0; d < datas.size(); d++) {
						Element data = (Element) datas.get(d);
						
						if (data.getName().equals("Station_Name"))
							station.setStationName(data.getValue());
						
						if(data.getName().equals("Address1"))
							station.setAddress1(data.getValue());
						
						if(data.getName().equals("Address2"))
							station.setAddress2(data.getValue());
						
						if(data.getName().equals("City"))
							station.setCity(data.getValue());
						
						if(data.getName().equals("State"))
							station.setState(data.getValue());
						
						if(data.getName().equals("Zip"))
							station.setZip(data.getValue());
						
						if(data.getName().equals("Brand_Name"))
							station.setBrandName(data.getValue());
						
						if(data.getName().equals("Unleaded_Price"))
							station.setUnleadedPrice(new Double(data.getValue()));
						
						if(data.getName().equals("Unleaded_Date")) {
							try {
								DateTime dt = DateTime.parse(data.getValue());
								station.setUnleadedDate(dt.toDate());
							}catch(Exception e) {
								station.setUnleadedDate(new Date());
							}
						}
						
						if(data.getName().equals("MidGrade_Price"))
							station.setMidGradePrice(new Double(data.getValue()));
						
						if(data.getName().equals("MidGrade_Date")) {
							try {
								DateTime dt = DateTime.parse(data.getValue());
								station.setMidGradeDate(dt.toDate());
							}catch(Exception e) {
								station.setMidGradeDate(new Date());
							}
						}
						
						if(data.getName().equals("Premium_Price"))
							station.setPremiumPrice(new Double(data.getValue()));
						
						if(data.getName().equals("Premium_Date")) {
							try {
								DateTime dt = DateTime.parse(data.getValue());
								station.setPremiumDate(dt.toDate());
							}catch(Exception e) {
								station.setPremiumDate(new Date());
							}
						}
						
						if(data.getName().equals("Diesel_Price"))
							station.setDieselPrice(new Double(data.getValue()));
						
						if(data.getName().equals("Diesel_Date")) {
							try {
								DateTime dt = DateTime.parse(data.getValue());
								station.setDieselDate(dt.toDate());
							}catch(Exception e) {
								station.setDieselDate(new Date());
							}
						}
						
						if(data.getName().equals("LATITUDE"))
							station.setLatitude(new Double(data.getValue()));
						
						if(data.getName().equals("LONGITUDE"))
							station.setLongitude(new Double(data.getValue()));
						
						if(data.getName().equals("Phone"))
							station.setPhone(data.getValue());

					}
					resultSet.add(station);
					logger.fine(station.toString());

				}
			} catch (Exception E) {
				logger.severe("Error parsing result: " + E.getMessage() + " - " + result);
				throw new Exception(E);
			}
		}
		return resultSet;
	}

}
