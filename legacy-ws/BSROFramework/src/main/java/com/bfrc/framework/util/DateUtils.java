package com.bfrc.framework.util;

import java.io.IOException;
import java.util.Date;

import  org.codehaus.jackson.map.JsonSerializer;
import  org.codehaus.jackson.map.SerializerProvider;
import  org.codehaus.jackson.JsonGenerator;
import  org.codehaus.jackson.JsonProcessingException;
import org.joda.time.DateTime;

/**
 * @author smoorthy
 *
 */
public class DateUtils extends JsonSerializer<Date> {
	
	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
		DateTime dt = new DateTime(value.getTime());
	    gen.writeString(dt.toString());
	}

}
