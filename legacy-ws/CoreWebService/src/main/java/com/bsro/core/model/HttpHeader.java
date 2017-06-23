package com.bsro.core.model;

public class HttpHeader {

	/**
	 * Listing of valid HttpHeader parameters this system will look for
	 * @author Brad Balmer
	 *
	 */
	public static enum Params {
		TOKEN_ID("tokenId"), 
		USER_ID("userId"),
		APP_NAME("appName"),
		APP_SOURCE("appSource"),
		APP_DEVICE("appDevice"),
		DEVICE_ID("deviceId"),
		USER_AGENT("user-agent"),
		DEBUG_CALL("debug");
		private String param;

		private Params(String value) {
			this.param = value;
		}
		public String getValue() {
			return this.param;
		}
	};
}
