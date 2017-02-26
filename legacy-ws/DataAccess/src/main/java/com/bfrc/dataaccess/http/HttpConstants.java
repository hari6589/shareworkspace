package com.bfrc.dataaccess.http;

/**
 * Class to hold the different constant values used by the {@link HttpUtils} class.
 * @author Brad Balmer
 *
 */
public class HttpConstants {
	
	/**
	 * HttpHeader parameter values
	 * @author Brad Balmer
	 *
	 */
	public enum HttpHeaderParameters {

		SOAP_ACTION("SOAPAction"), 
		CONTENT_TYPE("Content-Type");
		private String param;

		private HttpHeaderParameters(String value) {
			this.param = value;
		}
		public String getParam() {
			return this.param;
		}
	};
	
	/**
	 * Different Charset values
	 * @author Brad Balmer
	 *
	 */
	public enum MessageCharset {

		UTF8("UTF-8"), 
		UTF16("UTF-16"),
		ISO8859_1("ISO-8859-1"),
		US_ASCII("US-ASCII");
		private String param;

		private MessageCharset(String value) {
			this.param = value;
		}
		public String getCharset() {
			return this.param;
		}
	};	
	
	/**
	 * Different Content Type values
	 * @author Brad Balmer
	 *
	 */
	public enum ContentType {

		TEXT_XML("text/xml");
		private String param;

		private ContentType(String value) {
			this.param = value;
		}
		public String getContentType() {
			return this.param;
		}
	};
	
}
