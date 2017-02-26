/**
 * 
 */
package com.bsro.taglib.bingmaps;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bsro.service.bingmaps.ajax.BingMapsAjaxClientService;

/**
 * @author schowdhu
 *
 */
@SuppressWarnings("serial")
public class AjaxMapControlTag extends TagSupport {

	private final static Log					log	= LogFactory.getLog(AjaxMapControlTag.class);
	private static BingMapsAjaxClientService	bingMapsAjaxClientService;


	/**
	 * Main tag work flow method. It writes out the java script embed tag for BingMaps.
	 * It utilizes the BingMapsAjaxClientService which generates the tag to use based on property file configuration.
	 * 
	 * It it encounters an error, it writes it to the log.
	 */
	public int doStartTag () throws JspException {
		try {

			BingMapsAjaxClientService service = getBingMapsAjaxClientService(pageContext.getServletContext());

			pageContext.getOut().write(service.getEmbedScript());

		} catch (IOException ioe) {
			log.error("Bing Maps AjaxTag failed to generate <script> tag, therefore preventing a visitor from seeing a map.", ioe);
		}

		return TagSupport.SKIP_BODY;
	}


	/**
	 * Returned a cached reference to the spring service. If one hasn't been previously retrieved, it is looked up in the ServletContext.
	 * 
	 * @param servletContext
	 * @return BingMapsAjaxClientService
	 */
	private BingMapsAjaxClientService getBingMapsAjaxClientService (ServletContext servletContext) {

		if (bingMapsAjaxClientService == null) {
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
			bingMapsAjaxClientService = (BingMapsAjaxClientService) context.getBean(BingMapsAjaxClientService.SPRING_KEY);
		}

		return bingMapsAjaxClientService;
	}
}
