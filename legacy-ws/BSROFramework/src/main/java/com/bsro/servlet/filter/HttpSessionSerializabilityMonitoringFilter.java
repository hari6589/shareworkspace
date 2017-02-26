package com.bsro.servlet.filter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpSessionSerializabilityMonitoringFilter implements Filter {

	private static final Log logger = LogFactory.getLog(HttpSessionSerializabilityMonitoringFilter.class);
	
	private FilterConfig filterConfig;
	
	private Boolean enabled;
	
	public void init(FilterConfig filterConfig) throws ServletException {  
        this.filterConfig = filterConfig;  
    }
	
	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
		chain.doFilter(request, response);
		if (enabled != null && enabled == true) {
			HttpSession session = ((HttpServletRequest)request).getSession();
			
			Enumeration attributeNames = session.getAttributeNames();
			int sessionSize = 0;
			StringBuilder sessionSizeReport = new StringBuilder();
			if (attributeNames != null && attributeNames.hasMoreElements()) {
				if (logger.isInfoEnabled()) {
					sessionSizeReport.append("=============================================== BEGIN SESSION REPORT: Session ID "+session.getId()+" ===============================================\n");
				}
				while (attributeNames.hasMoreElements()) {
					String attributeName = (String)attributeNames.nextElement();
					Object original = session.getAttribute(attributeName);
					if (original != null) {
						try {
							byte[] byteArray = SerializationUtils.serialize((Serializable)original);
							if (logger.isInfoEnabled()) {
								int sessionAttributeSize = byteArray.length;
								sessionSizeReport.append(attributeName+" is "+sessionAttributeSize+" byte\n");
								sessionSize += sessionAttributeSize;
							}
						} catch (Throwable throwable) {
							logger.error("sessionAttribute "+attributeName+" is not serializable! "+ExceptionUtils.getFullStackTrace(throwable));
						}
					}
				}
				if (logger.isInfoEnabled() && sessionSize > 5000) {
					sessionSizeReport.append("\nWARNING! Session size is "+sessionSize+" bytes: WebSphere session replication performance may suffer when it is over 5000 bytes\n");
				}
				if (logger.isInfoEnabled()) {
					sessionSizeReport.append("=============================================== END SESSION REPORT: Session ID "+session.getId()+" ===============================================\n");
					if (sessionSize > 5000) {
						logger.info("\n"+sessionSizeReport.toString());
					}
				}
			}
		}
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
