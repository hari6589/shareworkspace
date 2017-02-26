package com.bsro.taglib.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bfrc.pojo.store.Store;


/*
 * Tag that generates the StoreDetail Link
 */
public class StoreDetailLinkTag extends TagSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Log	log	= LogFactory.getLog(StoreDetailLinkTag.class);


	/*
	 * Generates the link itself based on the following format:
	 * /store-detail/[store-number]/[address]-[city]-[state]-[zip]
	 * 
	 * The full link is URL encoded, lower case, and any spaces are replaced with a dash.
	 */
	public static String generateStoreDetailLink (Store store) {

		StringBuilder urlString = generateStoreDetailString(store);
		urlString.insert(0, "/");

		return urlString.toString();
	}
	
	/*
	 * Generates the link itself based on the following format:
	 * appRoot/store-detail/[store-number]/[address]-[city]-[state]-[zip]
	 * 
	 * The full link is URL encoded, lower case, and any spaces are replaced with a dash.
	 */
	public static String generateStoreDetailLink (String appRoot, Store store) {

		StringBuilder urlString = generateStoreDetailString(store);
		urlString.insert(0, appRoot);

		return urlString.toString();
	}

	private static StringBuilder generateStoreDetailString(Store store) {
		StringBuilder urlString = new StringBuilder(80);

		urlString.append("store-detail/");
		urlString.append(store.getStoreNumber()).append("/");

		appendUrlPart(urlString, store.getAddress());
		urlString.append("-");

		appendUrlPart(urlString, store.getCity());
		urlString.append("-");

		appendUrlPart(urlString, store.getState());
		urlString.append("-");

		appendUrlPart(urlString, store.getZip());
		return urlString;
	}


	/**
	 * Internal convenience method to handle duplicate logic building the link.
	 * 
	 * @param buffer
	 *            to append to.
	 * @param part
	 *            part to be appended (lower case, and spaces are exchanged for dashes)
	 */
	private static void appendUrlPart (StringBuilder builder, String part) {
		
		if (part != null) {

			part = part.toLowerCase().replace(' ', '-');

			try {
				builder.append(URLEncoder.encode(part, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				log.warn("Unable to encode URL parameters for Store Detail Link, therefore it was generated without encoding.", e);
				builder.append(part);
			}
		}
	}
	
	/**
	 * 
	 * @param store
	 * @return
	 */
	
	public static String cleanStoreName(String name) {

		if(name != null){
			name = name.replace("Store", "").trim();
			name = org.apache.commons.lang.StringEscapeUtils.escapeHtml(name);
		}

		return name;
	}
	
	/**
	 * Generate the title text for Store Locator pages
	 * 
	 * @param searchString
	 * @return
	 */
	public static String findAStoreMsg(String searchString) {
		String findStoreMsg = "";

		if(searchString != null && !"".equals(searchString)){
			searchString = StringEscapeUtils.escapeHtml(searchString);
			findStoreMsg = "No stores found for '" + searchString + "'. Please verify that you have entered a valid ZIP code or city, state and try your search again.";
		}
	
		return findStoreMsg;
	}
	
	public static String findAStoreMsgFCAC(String searchString) {
		String findStoreMsg = "";

		if(searchString != null && !"".equals(searchString)){
			searchString = StringEscapeUtils.escapeHtml(searchString);
			findStoreMsg = "No stores found for '" + searchString + "'. Please verify that you have entered a valid ZIP Code or (City and State) and try your search again.";
		}
	
		return findStoreMsg;
	}
	
}
