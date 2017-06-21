package hello;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Hello implements RequestHandler<Object, Object> {

	public Object handleRequest(Object input, Context context){ {
		return "Hello 123";
	}

}
