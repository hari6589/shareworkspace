package com.bfrc.dataaccess.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomDateTimeSerializer extends JsonSerializer<Date> {
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void serialize(Date value, JsonGenerator gen,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		System.out.println("value==================="+value.toString());
		gen.writeString(formatter.format(value));
		
	}
}
