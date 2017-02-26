package com.bsro.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

public class HibernateSessionMonitoringFilter implements Filter {

	private static final Log logger = LogFactory.getLog(HibernateSessionMonitoringFilter.class);
	
	private SessionFactory sessionFactory;
	
	private String sessionFactoryName;
	
	private Boolean enabled;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public String getSessionFactoryName() {
		return sessionFactoryName;
	}

	public void setSessionFactoryName(String sessionFactoryName) {
		this.sessionFactoryName = sessionFactoryName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	private FilterConfig filterConfig;
	
	public void init(FilterConfig filterConfig) throws ServletException {  
        this.filterConfig = filterConfig;  
    }
	
	@Override
	public void destroy() {
		this.filterConfig = null;
		if (this.sessionFactory != null && !this.sessionFactory.isClosed()) {
			this.sessionFactory.close();
		}	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {		
		chain.doFilter(request, response);
		
		if (enabled != null && enabled == true) {
			monitorAndLogOpenSessions(sessionFactory, sessionFactoryName);
		}
	}
	
	private void monitorAndLogOpenSessions(SessionFactory aSessionFactory, String aSessionFactoryName) {
		if (aSessionFactory != null) {
			Statistics stats = sessionFactory.getStatistics();
			if (!stats.isStatisticsEnabled()) {
				stats.setStatisticsEnabled(true);
			}
			
			long opened = stats.getSessionOpenCount();
			long closed = stats.getSessionCloseCount();
			
			long stillOpen = opened - closed;
			
			if (stillOpen != 0) {
				if (logger.isWarnEnabled()) {
					logger.warn("+========================================================+\nWARNING: "+stillOpen+" Unclosed Hibernate Sessions Detected in "+aSessionFactoryName+": Opened "+opened+", closed "+closed+"\n"+"+========================================================+");
				}
			}
		}
	}

}
