/**
 * 
 */
package com.bfrc.dataaccess.util;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * @author schowdhu
 *
 */
public class JsonObjectMapper extends ObjectMapper {

	public JsonObjectMapper() {
        super();
        configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(DeserializationConfig.Feature.AUTO_DETECT_CREATORS, true);
        configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        setDateFormat(new SimpleDateFormat("MM-dd-yyyy hh:mm"));
    }
}
 