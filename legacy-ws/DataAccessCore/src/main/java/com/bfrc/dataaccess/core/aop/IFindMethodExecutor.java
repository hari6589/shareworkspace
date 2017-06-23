/**
 * 
 */
package com.bfrc.dataaccess.core.aop;

import java.lang.reflect.Method;
import java.util.Collection;

public interface IFindMethodExecutor<T>
{
	public Collection<T> executeFindMethod( Method method, final Object[] args );
}
