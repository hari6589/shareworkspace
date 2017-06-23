/**
 * 
 */
package com.bfrc.dataaccess.core.orm.impl;


import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bfrc.dataaccess.core.aop.IFindMethodExecutor;
import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;

public class HibernateDAO<T, ID extends Serializable> implements IGenericOrmDAO<T, ID>, IFindMethodExecutor<T> {
    
	Log logger = LogFactory.getLog( HibernateDAO.class );
    protected HibernateUtil hibernateUtil;
	private Class<T> clazz;
	
	
	public HibernateDAO( Class<T> clazz )
	{
		this.clazz = clazz;
	}


	public void delete( T object )
	{
		this.hibernateUtil.delete( object );
	}


	public Object execNamedQuery(
			String queryName, 
			Properties properties,
			Collection<String> parameters )
	{
		return this.hibernateUtil.execNamedQuery( queryName, properties, parameters );
	}


	public List execOrmQuery(
			String query, 
			Properties properties,
			Collection<String> parameters)
	{
		return this.hibernateUtil.execHqlQuery( query, properties, parameters );
	}
	
	
	public int execOrmUpdateQuery(
			String query, 
			Properties properties,
			Collection<String> parameters)
	{
		return this.hibernateUtil.execHqlUpdateQuery( query, properties, parameters );
	}
	
	
	public List execSqlQuery(
			String query, 
			Properties properties,
			Collection<String> parameters )
	{
		return this.hibernateUtil.execSqlQuery( query, properties, parameters );
	}
	
	
	public int execSqlUpdateQuery(
			String query, 
			Properties properties,
			Collection<String> parameters )
	{
		return this.hibernateUtil.execSqlUpdateQuery( query, properties, parameters );
	}


	public T get( ID id )
	{
		return (T) this.hibernateUtil.get( clazz, id );
	}


	public Collection<T> listAll()
	{
		return this.hibernateUtil.listAll( clazz );
	}
	
	
	public void bulkInsert( Collection objects )
	{
		this.hibernateUtil.bulkInsert( objects );
	}


	public T load( ID id )
	{
		return (T) this.hibernateUtil.load( clazz, id );
	}


	public ID save( T object )
	{
		return (ID) this.hibernateUtil.save( object );
	}


	public void setReadOnly( T object, boolean state )
	{
		this.hibernateUtil.setReadOnlyState( object, state );
	}


	public void update(T object)
	{
		this.hibernateUtil.saveOrUpdate( object );
	}


	public Collection<T> executeFindMethod(Method method, Object[] args)
	{
		ArrayList params = new ArrayList();
		Collections.addAll( params, args );
		
		return this.hibernateUtil.execNamedQuery( this.getQueryNameFromMethod(method), null, params );
	}
	
	
	private String getQueryNameFromMethod(Method method) {
      
		return clazz.getCanonicalName() + "." + method.getName();
	}


	public HibernateUtil getHibernateUtils()
	{
		return hibernateUtil;
	}


	public void setHibernateUtils(HibernateUtil hibernateUtil)
	{
		this.hibernateUtil = hibernateUtil;
	}
}
