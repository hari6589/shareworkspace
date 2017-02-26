package app.bsro.model.util;

import java.io.IOException;

import  org.codehaus.jackson.map.JsonSerializer;
import  org.codehaus.jackson.map.SerializerProvider;
import  org.codehaus.jackson.JsonGenerator;
import  org.codehaus.jackson.JsonProcessingException;

/**
 * @author smoorthy
 *
 */

public class HTMLUtils extends JsonSerializer<String> {
	
	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
		if (value != null && !value.isEmpty()) {
			String txt = value.replaceAll("\\<.*?>","");
			gen.writeString(txt);
		}
	}

}
