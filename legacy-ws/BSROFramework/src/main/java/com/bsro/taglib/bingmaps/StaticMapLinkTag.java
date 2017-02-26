package com.bsro.taglib.bingmaps;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bfrc.pojo.store.Store;
import com.bsro.service.bingmaps.staticmap.BingMapsStaticMapService;


public class StaticMapLinkTag extends TagSupport {

	private static final long					serialVersionUID	= 1L;
	private final static Log					log					= LogFactory.getLog( StaticMapLinkTag.class );
	private static BingMapsStaticMapService		bingMapsStaticMapService;

	private Store								store;
	private Integer								height;
	private Integer								width;
	private Integer								zoom;
	private Boolean								encodeAmpersands;
	private Boolean								hideTracking;


	/**
	 * Utility method, designed for EL support. It uses the service to generate the string.
	 * 
	 * @param pageContext
	 * @param store
	 * @param height
	 * @param width
	 * @param zoom
	 * @return
	 */
	public static String getLink( PageContext pageContext, Store store, Integer width, Integer height, Integer zoom, Boolean encodeAmpersandsObj, Boolean hideTracking ) {

		if( pageContext == null ) {
			log.error( "getLink called with null PageContext. Returning empty string." );
			return "";
		}

		boolean disableLiveMaps = false;

		if( "off".equalsIgnoreCase( pageContext.getRequest().getParameter( "staticmaps" ) ) ) {
			disableLiveMaps = true;
		}

		boolean disableTracking = false;

		if( hideTracking != null && hideTracking ) {
			disableTracking = true;
		}

		boolean encodeAmpersands = false;

		if( encodeAmpersandsObj != null && encodeAmpersandsObj ) {
			encodeAmpersands = true;
		}

		BingMapsStaticMapService service = getBingMapsStaticService( pageContext.getServletContext() );

		String link = service.getLink( store, width, height, zoom, encodeAmpersands, disableTracking, disableLiveMaps );

		if( link != null ) {
			return link;
		} else {
			return "";
		}

	}


	/**
	 * Main tag work flow method. It writes out the img tag for BingMaps Static Map.
	 * It utilizes the BingMapsStaticMapService which generates the tag to use based on property file configuration.
	 * 
	 * It it encounters an error, it writes it to the log.
	 */
	public int doStartTag()
			throws JspException {

		try {

			pageContext.getOut().write( getLink( pageContext, getStore(), getWidth(), getHeight(), getZoom(), getEncodeAmpersands(), getHideTracking() ) );

		} catch (IOException ioe) {
			log.error( "Google Maps EmbedTag failed to generate <script> tag, therefore preventing a visitor from seeing a map.", ioe );
		}

		return TagSupport.SKIP_BODY;
	}


	/**
	 * Returned a cached reference to the spring service. If one hasn't been previously retrieved, it is looked up in the ServletContext.
	 * 
	 * @param servletContext
	 * @return BingMapsStaticMapService
	 */
	private static BingMapsStaticMapService getBingMapsStaticService( ServletContext servletContext ) {

		if( bingMapsStaticMapService == null ) {
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext( servletContext );
			bingMapsStaticMapService = (BingMapsStaticMapService) context.getBean( BingMapsStaticMapService.SPRING_KEY );
		}

		return bingMapsStaticMapService;
	}


	/**
	 * @return the store
	 */
	public com.bfrc.pojo.store.Store getStore() {
		return store;
	}


	/**
	 * @param store
	 *            the store to set
	 */
	public void setStore( com.bfrc.pojo.store.Store store ) {
		this.store = store;
	}


	/**
	 * @return the height
	 */
	public Integer getHeight() {
		return height;
	}


	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight( Integer height ) {
		this.height = height;
	}


	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}


	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth( Integer width ) {
		this.width = width;
	}


	/**
	 * @return the zoom
	 */
	public Integer getZoom() {
		return zoom;
	}


	/**
	 * @param zoom
	 *            the zoom to set
	 */
	public void setZoom( Integer zoom ) {
		this.zoom = zoom;
	}


	/**
	 * @return the hideTracking
	 */
	public Boolean getHideTracking() {
		return hideTracking;
	}


	/**
	 * @param hideTracking
	 *            the hideTracking to set
	 */
	public void setHideTracking( Boolean hideTracking ) {
		this.hideTracking = hideTracking;
	}


	/**
	 * @return the encodeAmpersands
	 */
	public Boolean getEncodeAmpersands() {
		return encodeAmpersands;
	}


	/**
	 * @param encodeAmpersands
	 *            the encodeAmpersands to set
	 */
	public void setEncodeAmpersands( Boolean encodeAmpersands ) {
		this.encodeAmpersands = encodeAmpersands;
	}

}
