package app.bsro.model.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class LongDeserializeUtil extends JsonDeserializer<Long> {

	@Override
	public Long deserialize(JsonParser paramJsonParser,
			DeserializationContext paramDeserializationContext)
			throws IOException{
		String str = paramJsonParser.getText().trim();
		Long val;
		try {
			val = Long.parseLong(str);			
		} catch (Exception e) {
			return null;
		}
		return val;
	}

}
