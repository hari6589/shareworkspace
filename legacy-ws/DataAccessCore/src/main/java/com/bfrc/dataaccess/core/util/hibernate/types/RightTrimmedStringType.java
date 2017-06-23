package com.bfrc.dataaccess.core.util.hibernate.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.usertype.UserType;
import org.springframework.util.StringUtils;


/**
 * This is a custom class taken from the Hibernate site, written by Paul Newport:  
 * 
 * 	https://www.hibernate.org/388.html
 * 
 * It has been modified to only right-trim Strings
 */

public class RightTrimmedStringType implements UserType
{
	public RightTrimmedStringType()
    {
    	super();
    }
	
	
	public int[] sqlTypes()
	{
		return new int[] { Types.CHAR };
	}
	
	
	public Class returnedClass()
	{
		return String.class; 
	}
	
	
	public boolean equals(Object x, Object y)
	{
		return (x == y) || (x != null && y != null && (x.equals(y)));
    }
	
	
	public Object nullSafeGet(ResultSet inResultSet, String[] names, Object o) throws SQLException
	{
		String val = (String) Hibernate.STRING.nullSafeGet(inResultSet, names[0]);
		return StringUtils.trimTrailingWhitespace( val );
	}
	
	
	public void nullSafeSet( PreparedStatement inPreparedStatement, Object o, int i ) throws SQLException
	{
		String val = (String) o;
		inPreparedStatement.setString(i, val);
    }
	
	
	public Object deepCopy(Object o)
	{
		if (o == null)
		{
			return null;
		}
		
		return new String(((String) o));
	}
	
	
	public boolean isMutable()
	{
		return false;
	}
	
	
	public Object assemble(Serializable cached, Object owner)
	{
		return cached;
	}
	
	
	public Serializable disassemble(Object value)
	{
		return (Serializable) value;
	}
	
	
	public Object replace(Object original, Object target, Object owner)
	{
		return original;
	}
	
	
	public int hashCode(Object x)
	{
		return x.hashCode();
	}
}
