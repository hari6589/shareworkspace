package com.bfrc.dataaccess.core.orm;

import java.io.Serializable;

public interface IPersistentObject extends Serializable
{
	public Long getId();
	public void setId( Long id );
	
	public Integer getVersion();
	public void setVersion( Integer version );
}
