package com.bfrc.dataaccess.core.orm.impl;

import com.bfrc.dataaccess.core.orm.IPersistentObject;

public abstract class PersistentObject implements IPersistentObject
{

	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer version;	
	
	
	public PersistentObject()
	{
		
	}
	
	
	public boolean equals( Object object )
	{
		if( object != null && object instanceof PersistentObject )
		{
			PersistentObject that = (PersistentObject) object;
			
			return ( this.id != null && this.id.equals(that.getId()) );
		}
		
		return false;
	}
	
	
	public int hashCode()
	{
		if( this.id != null )
		{
			return this.id.hashCode();
		}
		
		else
		{
			return super.hashCode();
		}
	}
	
	
	public String toString()
	{
		return this.getClass().getName() + "[id=" + id + "]";
	}
	

	public Long getId()
	{
		return id;
	}
	

	public Integer getVersion()
	{
		return version;
	}
	

	public void setId( Long id )
	{
		this.id = id;
	}
	

	public void setVersion(Integer version)
	{
		this.version = version;
	}
}
