
package com.bfrc.dataaccess.core.util.spring;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;


public class DataAccessSessionFactoryBean extends LocalSessionFactoryBean implements ResourceLoaderAware
{
	protected ResourceLoader resourceLoader;
	protected String bindingJarName;
	
	
	@Override
	protected Configuration newConfiguration() throws HibernateException
	{
		Configuration config = super.newConfiguration();
		
		Resource resource = 
			this.getResourceLoader().getResource( this.getBindingJarName() );

		try
		{
			config.addJar( resource.getFile() );
		}
		
		catch( Exception e )
		{
			e.printStackTrace();
		}
				
		return config;
	}


	public String getBindingJarName()
	{
		return this.bindingJarName;
	}


	public void setBindingJarName(String bindingJarName)
	{
		this.bindingJarName = bindingJarName;
	}


	public void setResourceLoader( ResourceLoader resourceLoader )
	{
		this.resourceLoader = resourceLoader;		
	}


	public ResourceLoader getResourceLoader()
	{
		return this.resourceLoader;
	}
}
