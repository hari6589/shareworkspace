package com.aws.lambda.sample;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AWSSampleLambdaFunctionHandler implements RequestHandler<Object, Object> {
	
	public Object handleRequest(Object input, Context context) {
		return "Hello! This is a sample response from Lambda Sample API";
	}
	
}