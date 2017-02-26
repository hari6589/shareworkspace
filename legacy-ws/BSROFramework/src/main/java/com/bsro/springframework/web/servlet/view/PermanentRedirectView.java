package com.bsro.springframework.web.servlet.view;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.RedirectView;

public class PermanentRedirectView extends RedirectView {
	
	public PermanentRedirectView(String url) {
		super(url);
	}
	
	public PermanentRedirectView(String url, boolean contextRelative) {
		super(url, contextRelative);
	}
	
	
	@Override
	protected void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetUrl, boolean http10Compatible) throws IOException {
	      response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
	      response.setHeader("Location", response.encodeRedirectURL(targetUrl)); 
	}
}
