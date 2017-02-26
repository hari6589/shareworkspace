package com.bsro.taglib.bingmaps;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bfrc.pojo.store.Store;
import com.bsro.service.bingmaps.staticmap.BingMapsStaticMapService;


public class StaticMapDivImageTag extends TagSupport {

	private static final long					serialVersionUID	= 2L;
	private final static Log					log					= LogFactory.getLog( StaticMapDivImageTag.class );
	private static BingMapsStaticMapService		bingMapsStaticMapService;

	private Store								store;
	private Integer								width;
	private Integer								height;
	private Integer								sourceImageWidth;
	private Integer								sourceImageHeight;
	private Integer								mapShiftVertical;
	private Integer								mapShiftHorizontal;
	private Integer								zoom;
	private Integer								marginRight;
	private String								id;
	private String								cssClass;
	private String								imgFloat;
	private Boolean								hideTracking;


	/**
	 * Main tag work flow method. It writes out the img tag for GoogleMaps Static Map.
	 * It utilizes the GoogleMapsStaticMapService which generates the tag to use based on property file configuration.
	 * 
	 * It it encounters an error, it writes it to the log.
	 */
	public int doStartTag()
			throws JspException {

		try {

			boolean disableLiveMaps = false;

			if( "off".equalsIgnoreCase( pageContext.getRequest().getParameter( "staticmaps" ) ) ) {
				disableLiveMaps = true;
			}

			boolean hideTracking = false;

			if( getHideTracking() != null && getHideTracking() ) {
				hideTracking = true;
			}

			BingMapsStaticMapService service = getBingMapsStaticService( pageContext.getServletContext() );

			pageContext.getOut().write( service.getDivImgElement( getStore(), getWidth(), getHeight(), getSourceImageWidth(), getSourceImageHeight(), getZoom(), getMapShiftVertical(), getMapShiftHorizontal(), getId(), getCssClass(), getImageFloat(), getMarginRight(), hideTracking, disableLiveMaps ) );

		} catch (IOException ioe) {
			log.error( "StaticMap tag was not able to generate the <div>, therefore preventing a visitor from seeing a static map.", ioe );
		}

		return TagSupport.SKIP_BODY;
	}


	/**
	 * Returned a cached reference to the spring service. If one hasn't been previously retrieved, it is looked up in the ServletContext.
	 * 
	 * @param servletContext
	 * @return BingMapsClientSideService
	 */
	private BingMapsStaticMapService getBingMapsStaticService( ServletContext servletContext ) {

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
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id
	 *            the id to set
	 */
	public void setId( String id ) {
		this.id = id;
	}


	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}


	/**
	 * @param cssClass
	 *            the cssClass to set
	 */
	public void setCssClass( String cssClass ) {
		this.cssClass = cssClass;
	}
	
	/**
	 * @return the imgFloat
	 */
	public String getImageFloat() {
		return imgFloat;
	}


	/**
	 * @param imgFloat
	 *            the imgFloat to set
	 */
	public void setImageFloat( String imgFloat ) {
		this.imgFloat = imgFloat;
	}
	
	/**
	 * @return the marginRight
	 */
	public Integer getMarginRight() {
		return marginRight;
	}


	/**
	 * @param marginRight
	 *            the marginRight to set
	 */
	public void setMarginRight( Integer marginRight ) {
		this.marginRight = marginRight;
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
	 * @return the sourceImageWidth
	 */
	public Integer getSourceImageWidth() {
		return sourceImageWidth;
	}


	/**
	 * @param sourceImageWidth
	 *            the sourceImageWidth to set
	 */
	public void setSourceImageWidth( Integer sourceImageWidth ) {
		this.sourceImageWidth = sourceImageWidth;
	}


	/**
	 * @return the sourceImageHeight
	 */
	public Integer getSourceImageHeight() {
		return sourceImageHeight;
	}


	/**
	 * @param sourceImageHeight
	 *            the sourceImageHeight to set
	 */
	public void setSourceImageHeight( Integer sourceImageHeight ) {
		this.sourceImageHeight = sourceImageHeight;
	}


	/**
	 * @return the mapShiftVertical
	 */
	public Integer getMapShiftVertical() {
		return mapShiftVertical;
	}


	/**
	 * @param mapShiftVertical
	 *            the mapShiftVertical to set
	 */
	public void setMapShiftVertical( Integer mapShiftVertical ) {
		this.mapShiftVertical = mapShiftVertical;
	}


	/**
	 * @return the mapShiftHorizontal
	 */
	public Integer getMapShiftHorizontal() {
		return mapShiftHorizontal;
	}


	/**
	 * @param mapShiftHorizontal
	 *            the mapShiftHorizontal to set
	 */
	public void setMapShiftHorizontal( Integer mapShiftHorizontal ) {
		this.mapShiftHorizontal = mapShiftHorizontal;
	}

}
