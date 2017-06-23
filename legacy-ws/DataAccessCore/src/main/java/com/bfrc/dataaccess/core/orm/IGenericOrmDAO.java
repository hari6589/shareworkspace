package com.bfrc.dataaccess.core.orm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Properties;

public interface IGenericOrmDAO<T, ID extends Serializable>
{
	public T get( ID id );
	public T load( ID id );
	public ID save( T object );
	public void update( T object );
	public void delete( T object );
	
	public Collection<T> listAll();
	
	public Object execNamedQuery( String queryName, Properties properties, Collection<String> parameters );
	public Object execSqlQuery( String query, Properties properties, Collection<String> parameters );
	public Object execOrmQuery( String query, Properties properties, Collection<String> parameters );
	public int execOrmUpdateQuery( String query, Properties properties, Collection<String> parameters );
	public int execSqlUpdateQuery( String query, Properties properties, Collection<String> parameters );
	
	public void bulkInsert( Collection objects );	
	
	public void setReadOnly( T object, boolean state );
}
