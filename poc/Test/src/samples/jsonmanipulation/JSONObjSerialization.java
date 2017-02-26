package samples.jsonmanipulation;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONObjSerialization {

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		JSONObjSerialization obj = new JSONObjSerialization();
		ObjectMapper mapper = new ObjectMapper();
		//String jsonString = '"\"{\\r\\n\\t\\t\\t\\t\\t\\t\\\"resource\\\":\\\"bridgestone\\\",\\r\\n\\t\\t\\t\\t\\t\\t\\\"action\\\":\\\"rules\\\",\\r\\n\\t\\t\\t\\t\\t\\t\\\"request\\\":\\\"Rules\\\",\\r\\n\\t\\t\\t\\t\\t\\t\\\"result\\\":\\\"success\\\",\\r\\n\\t\\t\\t\\t\\t\\t\\\"count\\\":\\\"1\\\"\\n,\\\"data\\\":{\\\"c_id\\\":\\\"1243\\\",\\\"employee_id\\\":\\\"7274\\\",\\\"service_id\\\":\\\"2745\\\",\\\"addons\\\":null,\\\"status_id\\\":\\\"4088\\\",\\\"status_description\\\":\\\"Scheduled\\\"}}\""';
		String jsonString = "";
		AppointmentMetaData mdata = (AppointmentMetaData)deSerializeJSON(jsonString, AppointmentMetaData.class);
		System.out.println(mdata);
	}

	private static Object deSerializeJSON(String json, Class clazz){
		ObjectMapper objectMapper = null;
		Object obj = null;
		try {
			obj = objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

}