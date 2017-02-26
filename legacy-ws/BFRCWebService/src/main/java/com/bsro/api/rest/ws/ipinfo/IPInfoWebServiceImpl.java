/**
 * 
 */
package com.bsro.api.rest.ws.ipinfo;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

import javax.ws.rs.core.HttpHeaders;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.error.Errors;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.util.ValidationConstants;
import com.bfrc.framework.util.StringUtils;
import com.bsro.core.security.RequireValidToken;

/**
 * @author schowdhury
 *
 */

@Component
public class IPInfoWebServiceImpl implements IPInfoWebService {
	
	private Log logger = LogFactory.getLog(IPInfoWebServiceImpl.class);
	
	@Autowired
	IPInfoWebServiceConfig ipInfoWebServiceConfig;

	@Override
	@RequireValidToken
	public BSROWebServiceResponse getZipByIp(String ipAddress, HttpHeaders headers) {
		String zip = null;
		BSROWebServiceResponse wsResponse = new BSROWebServiceResponse();
		Errors errors = new Errors();
			
		TrustStrategy trustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] x509Certificates,
					String s) throws CertificateException {
				return true;
			}
		};

		SSLContext sslContext = null;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			sslContext = SSLContexts.custom()
					.loadTrustMaterial(trustStore, trustStrategy).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext, new String[] { "TLSv1.2" }, null,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslsf).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
				registry);
		HttpClientBuilder clientBuilder = HttpClients.custom();

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(Integer.parseInt(this.ipInfoWebServiceConfig.getConnectionTimeout())).setSocketTimeout(Integer.parseInt(this.ipInfoWebServiceConfig.getConnectionTimeout()))
				.setConnectionRequestTimeout(Integer.parseInt(this.ipInfoWebServiceConfig.getConnectionTimeout()))
				.setStaleConnectionCheckEnabled(true).build();

		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setConnectionManager(connectionManager);
		/*HttpHost proxy = new HttpHost("172.16.8.32", 8080);
		clientBuilder.setProxy(proxy);*/
		CloseableHttpClient httpClient = clientBuilder.build();
		

		try {
			// Prepare URL
			MessageDigest md = MessageDigest.getInstance("MD5");	
			String apikey = this.ipInfoWebServiceConfig.getApikey();
			String secret = this.ipInfoWebServiceConfig.getSecret();

			long timeInSeconds = (long)(System.currentTimeMillis()/1000);
			String input = apikey + secret + timeInSeconds;
			md.update(input.getBytes());
			String sig = String.format("%032x", new BigInteger(1, md.digest()));
			String endpoint = this.ipInfoWebServiceConfig.getEndpoint();		
			String url = endpoint + ipAddress + "?apikey=" + apikey + "&sig=" + sig + "&format=json";
			
			HttpGet httpget = new HttpGet(url);
		
			// Execute the request
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
	
			// Process the response
			if (entity != null) {
				InputStream is = entity.getContent();
				JSONObject json = (JSONObject) JSONSerializer.toJSON( IOUtils.toString(is) );
				JSONObject ipinfo = json.getJSONObject("ipinfo");	
				int countryCF, stateCF, cityCF = 0;
				
				if (!ipinfo.isNullObject()) {
					// network data
					JSONObject net = ipinfo.getJSONObject("Network");	
					// ip routing type
					String ipRoutingType = net.getString("ip_routing_type");
					if (ipRoutingType.matches("mobile.*")) {
						countryCF = new Integer(this.ipInfoWebServiceConfig.getMobileCountryCF());
						stateCF   = new Integer(this.ipInfoWebServiceConfig.getMobileStateCF());
						cityCF 	  = new Integer(this.ipInfoWebServiceConfig.getMobileCityCF());
					} else {
						countryCF = new Integer(this.ipInfoWebServiceConfig.getDesktopCountryCF());
						stateCF = new Integer(this.ipInfoWebServiceConfig.getDesktopStateCF());
						cityCF = new Integer(this.ipInfoWebServiceConfig.getDesktopCityCF());
					}
					// location data
					JSONObject loc = ipinfo.getJSONObject("Location");	
					// country data
					JSONObject countryData = loc.getJSONObject("CountryData");

					boolean yCountryCF = (countryData.getString("country_code").equalsIgnoreCase("us") && 
							             countryData.getInt("country_cf") >= countryCF);					
					// state data
					JSONObject stateData = loc.getJSONObject("StateData");
					Object stateObj = stateData.get("state_cf");
					boolean yStateCF = (!StringUtils.isNullOrEmpty(stateObj)) ? stateData.getInt("state_cf") >= stateCF : false; 
					// city data
					JSONObject cityData = loc.getJSONObject("CityData");
					Object cityObj = cityData.get("city_cf");
					boolean yCityCF = (!StringUtils.isNullOrEmpty(cityObj)) ? cityData.getInt("city_cf") >= cityCF : false;

					// set zip
					if (yCountryCF && yStateCF && yCityCF) {
						zip = cityData.getString("postal_code");
					}else{
						errors.getGlobalErrors().add(ValidationConstants.COUNTRY_CITY_STATE_GEODATA_MISSING.toString());
						wsResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
						wsResponse.setErrors(errors);
						httpClient.getConnectionManager().shutdown();
						return wsResponse;
					}
				}else{
					errors.getGlobalErrors().add(ValidationConstants.IPINFO_OBJECT__MISSING.toString());
					wsResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
					wsResponse.setErrors(errors);
					EntityUtils.consume(entity);
					httpClient.getConnectionManager().shutdown();
					return wsResponse;
				}
			}else{
				errors.getGlobalErrors().add(ValidationConstants.GEOCODE_RESPONSE_ENTITY_ABSENT.toString());
				wsResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
				wsResponse.setErrors(errors);
				EntityUtils.consume(entity);
				httpClient.getConnectionManager().shutdown();
				return wsResponse;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errors.getGlobalErrors().add(ValidationConstants.GEOCODE_RESPONSE_ENTITY_ABSENT.toString());
			wsResponse.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.toString());
			wsResponse.setErrors(errors);			
			httpClient.getConnectionManager().shutdown();
			return wsResponse;
		} finally {
			// shut down the connection manager to ensure immediate deallocation of all system resources.
			httpClient.getConnectionManager().shutdown();
		}
		
		logger.info("ZIP="+zip);
		wsResponse.setPayload(zip);
		wsResponse.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		httpClient.getConnectionManager().shutdown();

		return wsResponse;
	}
}
