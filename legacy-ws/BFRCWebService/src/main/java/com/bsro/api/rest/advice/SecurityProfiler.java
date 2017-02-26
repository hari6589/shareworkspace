package com.bsro.api.rest.advice;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthenticationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.resteasy.specimpl.HttpHeadersImpl;

import com.bsro.core.model.HttpHeader;
import com.bsro.core.security.RequireValidAppNameAndToken;
import com.bsro.core.svc.AuthenticateService;

@Aspect
public class SecurityProfiler {

	private AuthenticateService authenticateService;
	private Logger log = Logger.getLogger(getClass().getName());

	@Pointcut("execution(@com.bsro.core.security.RequireValidToken * *(..))")
	public void applyTokenOnlySecurity() {
	}

	@Pointcut("execution(@com.bsro.core.security.RequireValidAppNameAndToken * *(..))")
	public void applyAppNameAndTokenSecurity() {
	}
	
	/**
	 * Left this to be @Around so we could add other checks (or timing) if needed in the future.
	 * This function controls whether or not the web service is called.  The HttpHeader parameter
	 * MUST be a part of the Arguments or this will fail (as long as isRequired() returns true).
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("applyTokenOnlySecurity()")
	public Object validateToken(ProceedingJoinPoint pjp) throws Throwable {

		//Debug the function being called
		if (log.isLoggable(Level.FINE)) {
			debug(pjp);
		}

		Object[] args = pjp.getArgs();
		boolean hasHeaders = false;
		for (Object arg : args) {
			if (arg instanceof HttpHeadersImpl) {
				hasHeaders = true;
				authenticateService.validateToken((HttpHeaders)arg);
			}
		}
		
		//If the HttpHeaders Object was NOT passed and we are requiring security for these calls
		// then throw an Exception
		if(!hasHeaders && authenticateService.isRequired()) {
			throw new AuthenticationException("Headers not available");
		}
		
//		long start = System.currentTimeMillis();
//		System.out.println("Going to call the method.");
		Object output = pjp.proceed();

//		System.out.println("Method execution completed.");
//		long elapsedTime = System.currentTimeMillis() - start;
//		System.out.println("Method execution time: " + elapsedTime + " milliseconds.");
		return output;

	}
	
	/**
	 * Left this to be @Around so we could add other checks (or timing) if needed in the future.
	 * This function controls whether or not the web service is called.  The HttpHeader parameter
	 * MUST be a part of the Arguments or this will fail (as long as isRequired() returns true).
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("applyAppNameAndTokenSecurity()")
	public Object validateAppNameAndToken(ProceedingJoinPoint pjp) throws Throwable {

		//Debug the function being called
		if (log.isLoggable(Level.FINE)) {
			debug(pjp);
		}
		
		Method method = ((MethodSignature)pjp.getSignature()).getMethod();
		if (method.getDeclaringClass().isInterface()) {
			method = pjp.getTarget().getClass().getDeclaredMethod(pjp.getSignature().getName(), method.getParameterTypes());
		}
		@SuppressWarnings("unchecked")
		RequireValidAppNameAndToken myAnnotation = (RequireValidAppNameAndToken) method.getAnnotation(RequireValidAppNameAndToken.class);
		String serviceName = myAnnotation.value();		
		
		if (serviceName == null || serviceName.isEmpty()) {
			log.warning("This service does not have a name");
			throw new AuthenticationException();
		}
		
		Object[] args = pjp.getArgs();
		boolean hasHeaders = false;
		for (Object arg : args) {
			if (arg instanceof HttpHeadersImpl) {
				hasHeaders = true;
				authenticateService.validateAppNameAndToken((HttpHeaders)arg, serviceName);
			}
		}
		
		//If the HttpHeaders Object was NOT passed and we are requiring security for these calls
		// then throw an Exception
		if(!hasHeaders && authenticateService.isRequired()) {
			log.warning("No headers were provided");
			throw new AuthenticationException();
		}
		
//		long start = System.currentTimeMillis();
//		System.out.println("Going to call the method.");
		Object output = pjp.proceed();

//		System.out.println("Method execution completed.");
//		long elapsedTime = System.currentTimeMillis() - start;
//		System.out.println("Method execution time: " + elapsedTime + " milliseconds.");
		return output;

	}	
	
	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}
	
	private void debug(ProceedingJoinPoint pjp) {

		Signature signature = pjp.getSignature();
		log.fine(signature.toString());
		Object[] args = pjp.getArgs();
		for (Object arg : args) {
			if (arg instanceof HttpHeadersImpl) {
				log.fine(" HEADERS:");
				HttpHeaders headers = (HttpHeaders)arg;
				MultivaluedMap<String, String> mapHeaders = headers.getRequestHeaders();
				HttpHeader.Params[] secParams = HttpHeader.Params.values();
				for(int i=0;i<secParams.length;i++) {
					HttpHeader.Params secParam = secParams[i];
					String key = secParam.getValue();
					String value = mapHeaders.getFirst(key);
					log.fine("  "+key + " : "+StringUtils.trimToEmpty(value));
				}
			} else {
				log.fine(" ARG: "+arg);
			}
		}
	}
	
}
