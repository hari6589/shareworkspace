package com.bridgestone.bsro.aws.appointment.util;

import java.io.IOException;
import java.util.Properties;

public class AuthenticateService {
	public boolean authenticateService(Properties properties, String tokenId, String appName) throws IOException{
		if(tokenId!=null && appName!=null && !tokenId.equals("") && !appName.equals("") && properties.containsKey(appName)) {
			if(properties.getProperty(appName).equals(tokenId)) {
				return true;
			}
		}
		return false;
	}
}
