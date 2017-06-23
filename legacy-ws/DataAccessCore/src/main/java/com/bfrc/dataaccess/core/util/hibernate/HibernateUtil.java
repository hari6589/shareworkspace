package com.bfrc.dataaccess.core.util.hibernate;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Settings;
import org.hibernate.engine.SessionFactoryImplementor;


/**
 * Utility class for persisting and querying objects through Hibernate.
 * 
 * Note: There is not transaction participation done explicitly in this class;
 * however, this utility class does use the sessionFactory.getCurrentSession()
 * method. 
 */
public class HibernateUtil
{
    
	private Log logger = LogFactory.getLog( HibernateUtil.class );
    
    
	private boolean forceReadOnlyOverride = false;
    private boolean overrideReadOnlyState = false;
    
    private SessionFactory sessionFactory;
    
    
	/**
     * Default constructor.
	 */
	public HibernateUtil()
	{
	    super();
	}
    
	
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
    
    
    public SessionFactory getSessionFactory()
    {
        return this.sessionFactory;
    }


	public boolean isForceReadOnlyOverride()
	{
		return forceReadOnlyOverride;
	}


	public void setForceReadOnlyOverride(boolean forceReadOnlyOverride)
	{
		this.forceReadOnlyOverride = forceReadOnlyOverride;
	}


	public boolean getOverrideReadOnlyState()
	{
		return overrideReadOnlyState;
	}


	public void setOverrideReadOnlyState(boolean overrideReadOnlyState)
	{
		this.overrideReadOnlyState = overrideReadOnlyState;
	}


	/**
	 * Execute an HQL query.  Results are returned as a list.  This method will
	 * never return null.  If there are no results, an empty list will be
	 * returned.
	 * 
	 * @param hql HQL query to execute.
	 * @return Query results.
	 */
	@Deprecated
	public final List executeHQLQuery(String hql) {
        
		List list = new ArrayList();
		
        Session session = this.getSessionFactory().getCurrentSession();
		
		try {
			Query query = session.createQuery( hql );
			this.setQueryAttributeOverrides( query );
			
			list = query.list();
			
		} catch(Exception ex) {
			logger.error("EoDataAccess.executeHQLQuery() " +
                    "has thrown an Exception:", ex);
		}	
		
		return list;
	}
	
	
	public final List listAll( Class clazz )
	{
		List list = new ArrayList();
		Session session = this.getSessionFactory().getCurrentSession();
		
		try
		{
			Criteria criteria = session.createCriteria( clazz );
			list = criteria.list();
		}
		
		catch( Exception ex )
		{
			logger.error( "Failure during object list:  ", ex );
		}	
		
		return list;
	}
	
	
	/**
	 * This uses 'hibernate.jdbc.batch_size' to determine the flush and clear
	 * frequency.  To tune performance, set and tune that value in your main
	 * configuration file.  
	 * 
	 * We may also want to temporarily disable the second-level cache during 
	 * the insert.
	 * 
	 * @param objects
	 */
	public final void bulkInsert( Collection objects )
	{	
		Session session = this.getSessionFactory().getCurrentSession();
		Settings settings = 
			((SessionFactoryImplementor) this.getSessionFactory()).getSettings();
		
		int jdbcBatchSize = settings.getJdbcBatchSize();
		logger.info( "Bulk insert.  Using batch size = " + jdbcBatchSize );
		
		if( objects != null )
		{
			int index = 0;
			for( Iterator it = objects.iterator(); it.hasNext(); )
			{
				session.save( it.next() );
				
				if( index++ % jdbcBatchSize == 0 )
				{
					session.flush();
					session.clear();
				}
			}
		}
	}
	
	
	public final List execHqlQuery( 
			String hql, 
			Properties properties, 
			Collection<String> parameters )
	{
		List list = new ArrayList();
		Session session = this.getSessionFactory().getCurrentSession();
		
		try
		{
			Query query = session.createQuery( hql );
			list = this.execQuery( query, properties, parameters );
		}
		
		catch( Exception ex )
		{
			logger.error( "Failure while executing HQL '" + hql + "'", ex );
		}	
		
		return list;
	}
	
	
	public final int execHqlUpdateQuery( 
			String hql, 
			Properties properties, 
			Collection<String> parameters )
	{
		int affectedRows = 0;
		Session session = this.getSessionFactory().getCurrentSession();
		
		try
		{
			Query query = session.createQuery( hql );
			affectedRows = this.execUpdateQuery( query, properties, parameters );
		}
		
		catch( Exception ex )
		{
			logger.error( "Failure while executing HQL '" + hql + "'", ex );
		}	
		
		return affectedRows;
	}
	
	
	public final List execSqlQuery( 
			String sql, 
			Properties properties, 
			Collection<String> parameters )
	{
		List list = new ArrayList();
		Session session = this.getSessionFactory().getCurrentSession();
		
		try
		{
			SQLQuery query = session.createSQLQuery( sql );
			list = this.execQuery( query, properties, parameters );
		}
		
		catch( Exception ex )
		{
			logger.error( "Failure while executing SQL '" + sql + "'", ex );
		}	
		
		return list;
	}
	
	
	public final int execSqlUpdateQuery( 
			String sql, 
			Properties properties, 
			Collection<String> parameters )
	{
		int affectedRows = 0;
		Session session = this.getSessionFactory().getCurrentSession();
		
		try
		{
			SQLQuery query = session.createSQLQuery( sql );
			affectedRows = this.execUpdateQuery( query, properties, parameters );
		}
		
		catch( Exception ex )
		{
			logger.error( "Failure while executing SQL '" + sql + "'", ex );
		}	
		
		return affectedRows;
	}
	
	
	private List execQuery( 
			Query query, 
			Properties properties, 
			Collection<String> parameters ) throws Exception
	{
		if( properties != null )
		{
			query.setProperties( properties );				
		}
		
		if( parameters != null )
		{
			assignParametersToQuery(parameters, query);				
		}
		
		return query.list();
	}
	
	
	private int execUpdateQuery( 
			Query query, 
			Properties properties, 
			Collection<String> parameters ) throws Exception
	{
		if( properties != null )
		{
			query.setProperties( properties );				
		}
		
		if( parameters != null )
		{
			assignParametersToQuery(parameters, query);				
		}
		
		return query.executeUpdate();
	}
	
	
	public final List execNamedQuery( 
			String queryName,
			Properties properties,
			Collection<String> parameters )
	{
		List list = new ArrayList();
		Session session = this.getSessionFactory().getCurrentSession();
		
		try
		{
			Query query = session.getNamedQuery( queryName );
			
			if( properties != null )
			{
				query.setProperties( properties );				
			}
			
			if( parameters != null )
			{
				assignParametersToQuery(parameters, query);				
			}
			
			list = query.list();
		}
		
		catch( Exception e )
		{
			logger.error( "execNamedQuery has failed while executing named query '" + queryName + "'", e );
		}
		
		return list;
	}


	private void assignParametersToQuery(Collection<String> parameters, Query query) {
		int index = 0;
		
		int arrayIndex = 0;
		
		for( Iterator it = parameters.iterator(); it.hasNext(); )
		{
			Object parameter = it.next();
			if(!(parameter instanceof Object[]))
			{
				if(!(parameter instanceof Enum))
				{
					query.setString( index++, String.valueOf(parameter));
				}
				else
				{
					query.setParameter( index++, parameter );
				}
			}
			else
			{
				String parameterName = "array" + (++arrayIndex);
				query.setParameterList( parameterName, (Object[])parameter );
			}
		}
	}


	/**
	 * Execute a named HQL parameter query and returns the results as a list.
	 * 
	 * This query will never return null.  If there are no results, an empty 
	 * list is returned.
	 * 
	 * @param hqlQueryName The name of the query to execute.
	 * @param parameters The query parameters.
	 * @return List of results.  Empty if nothing found.
	 */
	@Deprecated
	public final List executeNamedParameterListQuery( String hqlQueryName,
            List parameters) {
        
		List list = new ArrayList();
		
        Session session = this.getSessionFactory().getCurrentSession();
		
		Query query = session.getNamedQuery( hqlQueryName );
		this.setQueryAttributeOverrides( query );
		
		assignParametersToQuery(parameters, query);
		
		list = query.list();			
		
		return list;
	}
	

	@Deprecated
	public final List executeNamedParameterListQuery( 
			String hqlQueryName,
			Integer maxResults,
			List parameters )
	{
        
		List list = new ArrayList();
		
        Session session = this.getSessionFactory().getCurrentSession();
		
		Query query = session.getNamedQuery( hqlQueryName );
		query.setMaxResults( maxResults );
		this.setQueryAttributeOverrides( query );
		
		assignParametersToQuery(parameters, query);
		
		list = query.list();			
		
		return list;
	}
	
	
	/**
	 * Returns a unique, mapped object.
	 * 
	 * Note that if the provided query contains joins of any type, the returned
	 * object will be an Object[].  Each element of the Object[] will contain an
	 * instance of a class specified in the join.
	 * 
	 * As an example:
	 * 	<code>from Price P, Items I</code> 
	 * 
	 * returns Object[]{ Price, Items }
	 * 
	 * @param hqlQueryName named query to execute
	 * @param parameters Query Parameters
	 * @return A unique, mapped object.  may return Object[] in certain
     *       circumstances.
	 */
	public final Object executeNamedParameterObjectQuery(
            String hqlQueryName, List parameters) {
        
		Object object = null;
		
		Session session = this.getSessionFactory().getCurrentSession();
		
		Query query = session.getNamedQuery(hqlQueryName);
		this.setQueryAttributeOverrides( query );

		assignParametersToQuery(parameters, query);
		
		object = query.uniqueResult();
		
		this.setObjectReadOnlyState( session, object );		
		return object;
	}
	
	
	/**
	 * Persist changes to a pre-existing object.  If the object does not already 
	 * exist, it will be created.
	 * 
	 * @param object The object to update.
	 */
	public final void update(Object object) {
        
		Session session = this.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(object);			
	}
	
    
	/**
	 * Persists a new object.  If the object already exists, it will be updated
	 * instead.
	 * 
	 * @param object The object to save.
	 */
	public final void insert(Object object) {
        
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(object);
        session.flush();
        session.refresh(object);
	}
	
	
	/**
	 * Persists a new object.  If the object already exists, it will be updated
	 * instead.
	 * 
	 * @param object The object to save.
	 */
	public final void saveOrUpdate(Object object) {
        
		Session session = this.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(object);
        session.flush();
        session.refresh(object);
	}
	
   
    /**
     * Persists a new Object.
     * 
     * @param object
     * @return the generated identifier.
     */
    public final Serializable save(Object object) {
        
		Session session = this.getSessionFactory().getCurrentSession();
		Serializable id = session.save(object);
        
        return id;
    }
    
    
	/**
	 * Delete an object from the database.
	 * 
	 * @param object The object to delete.
	 */
	public final void delete(Object object) {
        
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(object);			
	}
	
   
    /**
     * Retrieve an Object.  This method differs from load in that a null
     * value will be returned if no object can be found that has the 
     * specified id object.  The load method will throw a runtime exception
     * if no record can be found.
     * 
     * @param clazz
     * @param id
     * @return
     */
    public final Object get(Class clazz, Serializable id) {
        
		Object object = null;
		
		Session session = this.getSessionFactory().getCurrentSession();
		object = session.get(clazz, id);
		
		this.setObjectReadOnlyState( session, object );
		return object;
    }
    
	
	/**
	 * Load an object.
	 * 
	 * @param clazz The class of the object we want to load.
	 * @param id The unique identifier of the object we want to load.
	 * @return The loaded object if successful.  Otherwise, throws Exception.
	 */
	public final Object load(Class clazz, Serializable id) {
        
		Object object = null;
		
		Session session = this.getSessionFactory().getCurrentSession();
		object = session.load(clazz, id);
		
		this.setObjectReadOnlyState( session, object );
		return object;
	}
	
	
	public final void setReadOnlyState( Object object, boolean state ) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.setReadOnly( object, state );
	}
	
	
	private final void setQueryAttributeOverrides( Query query ) {
		if( this.isForceReadOnlyOverride() ) {
			query.setReadOnly( this.getOverrideReadOnlyState() );
		}
		
		query.setCacheable( true );
	}
	
	
	private final void setObjectReadOnlyState( Session session, Object object ) {
		if( this.isForceReadOnlyOverride() ) {
			session.setReadOnly( object, this.getOverrideReadOnlyState() );
		}
	}
}
