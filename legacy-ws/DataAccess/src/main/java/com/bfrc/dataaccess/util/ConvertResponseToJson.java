/**
 * 
 */
package com.bfrc.dataaccess.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;

import app.bsro.model.webservice.BSROWebServiceResponse;

/**
 * @author schowdhu
 *
 */
public class ConvertResponseToJson {
	
	@Autowired
	private JsonObjectMapper jsonObjectMapper;
	
	
	public String getJson(BSROWebServiceResponse response){
		
		/*
		SimpleModule customDateTimeModule = new SimpleModule("CustomDateTimeModule", new Version(1, 0, 0, null)); 
		isoDateTimeModule.addSerializer(new CustomDateTimeSerializer()); 
		mapper.registerModule(isoDateTimeModule);
		*/

		jsonObjectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		jsonObjectMapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, false);
		if(response.getPayload()== null){
			//jsonObjectMapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
			jsonObjectMapper.setSerializationInclusion(Inclusion.NON_NULL);
		}
		ObjectWriter ow = jsonObjectMapper.writer().withDefaultPrettyPrinter();
		String responseJson = "";
		try {
			responseJson = ow.writeValueAsString(response);
		} catch (JsonGenerationException e1) {
			System.err.println("JSONException while trying to get build request data");
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			System.err.println("JSON Mapping Exception while trying to get build request from pojo");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.err.println("IOException while trying to get build json request from pojo");
			e1.printStackTrace();
		}
		return responseJson;
	}

}
