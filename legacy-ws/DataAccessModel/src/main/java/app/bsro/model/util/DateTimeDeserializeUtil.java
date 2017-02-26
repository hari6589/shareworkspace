package app.bsro.model.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class DateTimeDeserializeUtil extends JsonDeserializer<Date> {

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	@Override
	public Date deserialize(JsonParser paramJsonParser,
			DeserializationContext paramDeserializationContext)
			throws IOException{
		String str = paramJsonParser.getText().trim();
		try {
			return dateFormat.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramDeserializationContext.parseDate(str);
	}

}
