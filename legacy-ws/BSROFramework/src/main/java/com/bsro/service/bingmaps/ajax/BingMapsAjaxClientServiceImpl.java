/**
 * 
 */
package com.bsro.service.bingmaps.ajax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author schowdhu
 *
 */
public class BingMapsAjaxClientServiceImpl implements BingMapsAjaxClientService {
	
	private final Log	log	= LogFactory.getLog( BingMapsAjaxClientServiceImpl.class );

	// Internal State
	private boolean	fieldChanged	= false;
	private String	embedScript;

	// Bean fields
	private String	bingMapsUrl;
	private String  version;
	private String  language;
	private boolean includeVersion;
	private boolean includeLanguage;
	private boolean secureCall;
	
	@Override
	public String getEmbedScript() {
		if( fieldChanged || embedScript == null ) {
			fieldChanged = false;
			embedScript = buildEmbedScript();
		}
		return embedScript;
	}
	
	public String buildEmbedScript(){
		StringBuilder html = new StringBuilder( 200 );

		html.append( "<script type=\"text/javascript\" charset=\"UTF-8\" src=\"" );
		html.append( getBingMapsUrl());
		html.append( "?" );
		if(isIncludeVersion()){
			html.append( "v=" ).append( getVersion() );
		}
		if(isIncludeLanguage()){
			html.append( "&mkt=" ).append( getLanguage() );
		}
		
		if( isSecureCall()){
			html.append( "&s=" ).append( "1" );
		}
		html.append( "\"></script>" );
		
		log.info("script="+html);
		return html.toString();
	}

	/**
	 * @return the bingMapsUrl
	 */
	public String getBingMapsUrl() {
		return bingMapsUrl;
	}

	/**
	 * @param bingMapsUrl the bingMapsUrl to set
	 */
	public void setBingMapsUrl(String bingMapsUrl) {
		this.bingMapsUrl = bingMapsUrl;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the secureCall
	 */
	public boolean isSecureCall() {
		return secureCall;
	}

	/**
	 * @param secureCall the secureCall to set
	 */
	public void setSecureCall(boolean secureCall) {
		this.secureCall = secureCall;
	}

	/**
	 * @return the includeVersion
	 */
	public boolean isIncludeVersion() {
		return includeVersion;
	}

	/**
	 * @param includeVersion the includeVersion to set
	 */
	public void setIncludeVersion(boolean includeVersion) {
		this.includeVersion = includeVersion;
	}

	/**
	 * @return the includeLanguage
	 */
	public boolean isIncludeLanguage() {
		return includeLanguage;
	}

	/**
	 * @param includeLanguage the includeLanguage to set
	 */
	public void setIncludeLanguage(boolean includeLanguage) {
		this.includeLanguage = includeLanguage;
	}
}
