package com.bsro.springframework.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.webflow.execution.repository.BadlyFormattedFlowExecutionKeyException;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;
import org.springframework.webflow.execution.repository.snapshot.SnapshotNotFoundException;

/**
 * This is deliberately narrow (BadlyFormattedFlowExecutionKeyExceptions and FlowExecutionRestorationFailureExceptions with a cause of FlowExecutionRestorationFailureException)
 * because we those are easy-to-trigger exceptions that we can't control and for which there is no solution other than to bounce users back to the beginning of the flow.
 * 
 * We don't cover other sorts of exceptions because we don't want to inadvertently "hide" real problems.
 * 
 * @author mholmes
 *
 */
public class WebFlowExecutionRepositoryExceptionResolver extends AbstractHandlerExceptionResolver {
	Log log = LogFactory.getLog(WebFlowExecutionRepositoryExceptionResolver.class);
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,  HttpServletResponse response, Object handler, Exception ex) {
    	String url = "";
		if (ex != null && (ex instanceof FlowExecutionRestorationFailureException || ex instanceof BadlyFormattedFlowExecutionKeyException)) {
			if (ex instanceof FlowExecutionRestorationFailureException) {
				if (ex.getCause() == null) {
					throw new RuntimeException("In exception resolver with url:"+url+" root cause of exception is null", ex);
				} else if (!(ex.getCause() instanceof SnapshotNotFoundException)) {
					throw new RuntimeException("In exception resolver with url:"+url+" root cause of exception is of type "+ex.getCause().getClass()+", which is unsupported", ex);
				}
			}
			
		    String contextPath = request.getContextPath();   // /mywebapp
			String servletPath = request.getServletPath();   // /servlet/MyServlet
		    String pathInfo = request.getPathInfo();         // /a/b;c=123
		    String queryString = request.getQueryString();          // d=789

		    if (queryString != null) {
		    	// If there is a query string, and one of the two supported types of exceptions are encountered, it indicates an
		    	// invalid execution parameter, either due to a manipulated URL or some kind of bad user session state.
		    	// So, we redirect back to the beginning of the flow, with no query string.
		    	url = url + contextPath + servletPath;
			
		    	if (pathInfo != null) {
		    		url = url + pathInfo;
		    	}
			
		    	if (log.isDebugEnabled()) {
		    		log.debug("Redirecting user from "+url+"?"+queryString+" to "+url);
		    	}
		    	return new ModelAndView(new RedirectView(url));
		    } else {
		    	// if one of these exceptions is encountered in the absence of a query string, clearly we don't want to redirect back to the same url again, and
		    	// this is an unexpected situation, so throw a runtime exception
		    	throw new RuntimeException("Unexpected exception for url:"+url+" with no query string", ex);
		    }
		} else {
			//do normal processing
			return null;
		}
	}
}
