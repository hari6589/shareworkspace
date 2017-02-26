package com.bfrc.framework.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

import com.bfrc.Config;
import com.bfrc.framework.dao.MediaCampaignDAO;
import com.bsro.constants.SessionConstants;

/**
 * @author smoorthy
 *
 */
public class MediaCampaignParamFilter implements Filter {
	
	private FilterConfig filterConfig;
	List<String> mcParameters = null;
	
	public void init(FilterConfig filterConfig) throws ServletException {  
        this.filterConfig = filterConfig;  
    }  
  
    public void destroy() {  
        this.filterConfig = null;  
    }
    
    @SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)   
            throws IOException, ServletException {
    		boolean wrapRequest = false;
    		HttpServletRequest request = (HttpServletRequest)req;
        	HttpServletResponse response = (HttpServletResponse)res;
        	if (mcParameters == null) {
        		ServletContext ctx = request.getSession().getServletContext();
	        	MediaCampaignDAO mediaCampaignDAO = (MediaCampaignDAO) Config.locate(ctx, "mediaCampaignDAO");
	        	mcParameters = mediaCampaignDAO.getAllCampaignParameterKeys();
        	}
        	
        	Map<String, String[]> mediaCampaignParameters = getMediaCampaignParameters(request);
        	if (mediaCampaignParameters != null && !mediaCampaignParameters.isEmpty()) {
        		WebUtils.setSessionAttribute(request, SessionConstants.MEDIA_CAMPAIGN_PARAMS, mediaCampaignParameters);
        	} else {
        		mediaCampaignParameters = (Map<String, String[]>) WebUtils.getSessionAttribute(request, SessionConstants.MEDIA_CAMPAIGN_PARAMS);
        		if (mediaCampaignParameters != null && !mediaCampaignParameters.isEmpty()) {
        			wrapRequest = true;
        		}
        	}
            try{
            	WebUtils.setSessionAttribute(request, SessionConstants.MEDIA_CAMPAIGN_QUERYSTRING, generateMediaCampaignQS(mediaCampaignParameters));
            	if (wrapRequest) {
            		MediaCampaignWrappedRequest mediaCampaignWrappedRequest = new MediaCampaignWrappedRequest((HttpServletRequest) request, mediaCampaignParameters);
            		chain.doFilter(mediaCampaignWrappedRequest, response);
            	} else {
            		chain.doFilter(request, response);
            	}
            }catch(Exception ex){
            	ex.printStackTrace();
            	System.err.println("\t\t\tMedia Campaign Filter Error processing request.getRequestURI(): " + request.getRequestURI());
            	chain.doFilter(request, response); 
            }
    }

	private String generateMediaCampaignQS(Map<String, String[]> mediaCampaignParameters) {
		String qs = "";
		if (mediaCampaignParameters != null && !mediaCampaignParameters.isEmpty()) {
			int index = 0;
			for (Map.Entry<String, String[]> entry : mediaCampaignParameters.entrySet()) {
			    String key = entry.getKey();
			    String[] value = entry.getValue();
			    if (index > 0) {
			    	qs += "&";
			    }
			    if (value != null && value.length > 0) {
			    	for (int i = 0; i < value.length; i++) {
			    		if (i == 0) {
			    			qs += key + "=" +value[i];
			    		} else {
			    			qs += ","+value[i];
			    		}
			    	}
			    }
			    index++;
			}
		}
		return qs;
	}

	@SuppressWarnings("unchecked")
	private Map<String, String[]> getMediaCampaignParameters(
			HttpServletRequest request) {
		Map<String, String[]> mediaCampaignParameters = new HashMap<String, String[]>();
		Enumeration<String> currParams = request.getParameterNames();
		while (currParams.hasMoreElements()){
			String key = currParams.nextElement();
			String[] pvalue = request.getParameterValues(key);
			if (mcParameters.contains(key)) {
				mediaCampaignParameters.put(key, pvalue);
			}
		}
		
		return mediaCampaignParameters;
	}
}
