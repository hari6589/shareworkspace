package com.aws.lambda.sample;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sample.sitescrap.SiteScrap;

public class SiteScrapSampleLambdaFunction implements RequestHandler<Object, Object> {

	public Object handleRequest(Object input, Context context) {
		SiteScrap siteScrap = new SiteScrap();
		return siteScrap.getData();
	}

}