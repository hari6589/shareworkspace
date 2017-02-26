package com.bfrc.dataaccess.svc.geocode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import app.bsro.model.gas.stations.StationPrices;

import com.bfrc.dataaccess.exception.HttpException;
import com.bfrc.dataaccess.http.HttpResponse;
import com.bfrc.dataaccess.http.HttpUtils;

@Service(value="yahooGeocodeService")
@Deprecated
public class YahooGeocodeServiceImpl implements GeocodeService {

	private Logger logger = Logger.getLogger(getClass().getName());
	public GeoLatLong geocode(GeoAddress address, Map<StationPrices.Params, Object> params) {

		String addressUrl = address.toEncodedUrlString();
		if(addressUrl == null) return null;
		
		String uri = "http://where.yahooapis.com/geocode?appid=YD-9G7bey8_JXxQP6rxl.fBFGgCdNjoDMACQA--&location="+addressUrl;
		
		HttpResponse response = null;
		try {
			response = HttpUtils.get(uri, null);
		}catch(HttpException ex) {
			//Error logged in HttpUtils class
			return null;
		}
		
		GeoLatLong loResult = null;
		
		try {
			loResult = parse(response.getResponseBody());
		} catch (XPathExpressionException e) {
			logger.warning("[XPathExpressionException]: "+e.getMessage());
		} catch (ParserConfigurationException e) {
			logger.warning("[ParserConfigurationException]: "+e.getMessage());
		} catch (SAXException e) {
			logger.warning("[SAXException]: "+e.getMessage());
		} catch (IOException e) {
			logger.warning("[IOException]: "+e.getMessage());
		}
		
		return loResult;
	}
	
	
	/**
	 * Parse the XML coming back from Google into the Latitude and Longitude along with the countryCode.
	 * @param result
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	private GeoLatLong parse(String result) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(result.getBytes()));
		XPath xpath = XPathFactory.newInstance().newXPath();

		XPathExpression statusCodeExp = xpath.compile("//Error/text()");
		Integer statusCode = new Integer((String)statusCodeExp.evaluate(doc, XPathConstants.STRING));
		
		if(statusCode == null || statusCode != 0) {
			logger.severe("Yahoo geo call returned non 0 status code of "+statusCode);
			return null;
		}
		
		XPathExpression countryExp = xpath.compile("//countrycode/text()");
		String countryCode = StringUtils.trimToEmpty((String)countryExp.evaluate(doc, XPathConstants.STRING));
		if(!"US".equalsIgnoreCase(countryCode)) {
			logger.warning("A non-US country code was searched ("+countryCode+").");
			return null;
		}
		
		XPathExpression latitudeExp = xpath.compile("//latitude/text()");
		String latitude = StringUtils.trimToNull((String)latitudeExp.evaluate(doc, XPathConstants.STRING));
		if(latitude == null) {
			logger.warning("Latitude not returned in result: "+result);
			return null;
		}
		XPathExpression longitudeExp = xpath.compile("//longitude/text()");
		String longitude = StringUtils.trimToNull((String)longitudeExp.evaluate(doc, XPathConstants.STRING));
		if(longitude == null) {
			logger.warning("Longitude not returned in result: "+result);
			return null;
		}
		GeoLatLong geoLatLong = new GeoLatLong();
		
		geoLatLong.setLongitude((longitude));
		geoLatLong.setLatitude((latitude));
		geoLatLong.setCountryCode(StringUtils.trimToEmpty(countryCode));
		
		
		return geoLatLong;
	}

}
