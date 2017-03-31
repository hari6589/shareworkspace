package com.core;

import org.json.JSONException;
import org.json.JSONObject;

public class ProxyLambdaTest {

	public static void main(String[] args) {
		JSONObject obj = new JSONObject();
		JSONObject header = new JSONObject();
		try {
			obj.put("statusCode", "SUCCESS");
			obj.put("body", "content");
			header.put("appName", "FCAC");
			header.put("tokenId", "ABCD");
			obj.put("headers", header);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(obj.toString());
//		String obj = "{
//		    "statusCode": httpStatusCode,
//		    "headers": { "headerName": "headerValue", ... },
//		    "body": "..."
//		}
	}

}
