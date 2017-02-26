package com.bfrc.framework.util;

import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class RequestSanitizeFilter implements Filter
{
	private FilterConfig filterConfig;
	public void init(FilterConfig filterConfig) throws ServletException {  
        this.filterConfig = filterConfig;  
    }  
  
    public void destroy() {  
        this.filterConfig = null;  
    }  
  
    public boolean escapeFiltering(HttpServletRequest req){
    
    	String uri = req.getRequestURI().toLowerCase();
    	// Merging framework v2 approach (configure _RequestFilterEscapePages in properties files) with v1 approach (hard-code "/cms/")
        String patern = PropertyConfigUtil.Current().getProperty("_RequestFilterEscapePages","");
        
        if(ServerUtil.isNullOrEmpty(patern)){
        	patern = "/cms/";	
        }	
        
        try{
            return java.util.regex.Pattern.compile(patern).matcher(uri).find();
        }catch(Exception ex){
        	return false;
        }
    }
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)   
        throws IOException, ServletException { 
    	HttpServletRequest request = (HttpServletRequest)req;
    	HttpServletResponse response = (HttpServletResponse)res;
    	
        ServerUtil.debug("  request.getRequestURI(): " + request.getRequestURI());
    	ServerUtil.debug("form escape encoding: "+escapeFiltering(request));
        if(!escapeFiltering(request)){
        	try{
                chain.doFilter(new RequestWrapper(request), response); 
        	}catch(Exception ex){
        		ex.printStackTrace();
        		System.err.println("\t\t\tRequest Filter Error processing request.getRequestURI(): " + request.getRequestURI());
        		System.err.println("\t\t\tRequest Filter Error  escape encoding: "+escapeFiltering(request));
        		chain.doFilter(request, response); 
        	}
        }else{
        	chain.doFilter(request, response); 
        }
    }  
}