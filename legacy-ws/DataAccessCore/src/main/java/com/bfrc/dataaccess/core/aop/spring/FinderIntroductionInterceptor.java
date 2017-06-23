/**
 * 
 */
package com.bfrc.dataaccess.core.aop.spring;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;

import com.bfrc.dataaccess.core.aop.IFindMethodExecutor;

public class FinderIntroductionInterceptor implements IntroductionInterceptor
{

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable
	{
		IFindMethodExecutor genericDAO = (IFindMethodExecutor) methodInvocation.getThis();
		
		String methodName = methodInvocation.getMethod().getName();
		
		if( methodName.startsWith("find") )
		{
			Object[] arguments = methodInvocation.getArguments();
			
			return genericDAO.executeFindMethod( methodInvocation.getMethod(), arguments );
		}
		
		else
		{
			return methodInvocation.proceed();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.aop.DynamicIntroductionAdvice#implementsInterface(java.lang.Class)
	 */
	public boolean implementsInterface( Class clazz )
	{
		return clazz.isInterface() && IFindMethodExecutor.class.isAssignableFrom( clazz );
	}

}
