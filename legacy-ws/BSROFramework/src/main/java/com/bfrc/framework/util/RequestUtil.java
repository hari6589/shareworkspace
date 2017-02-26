/**
 * 
 * Modified from jWebApp 
 * 
 * URL and Licenses
 * go to http://www.softwaresensation.com
 */

package com.bfrc.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletRequestWrapper;  
import com.bfrc.security.*;
public final class RequestUtil 
{
	/**
     * Loads your object with request parameter data.  "obj" is an object of any type 
     * containing set methods.  Any set method matching the name of a parameter will
     * be set with the request parameter value.
     *
     * @param obj an object with set methods
     *
     * @return object with set methods
     */
    public static <T> T fillObjectFromRequest(T obj, HttpServletRequest request) 
      {
        return fillObjectFromRequest(obj, request, true, false, null, false);
      }
    
    /**
     * Loads your object with request parameter data.  "obj" is an object of any type 
     * containing set methods.  Any set method matching the name of a parameter will
     * be set with the request parameter value.
     *
     * @param obj an object with set methods
     * @param ignoreValueNames ignore the value names in the set
     * @param setNulls set or ignore null values
     *
     * @return object with set methods
     */
    public static <T> T fillObjectFromRequest(T obj, HttpServletRequest request, Set<String> ignoreValueNames, boolean setNulls) 
      {
        return fillObjectFromRequest(obj, request, true, false, ignoreValueNames, setNulls);
      }
    
    /**
     * Loads your object with request parameter data.  "obj" is an object of any type 
     * containing set methods.  Any set method matching the name of a parameter will
     * be set with the request parameter value.
     *
     * @param obj an object with set methods
     * @param fillPublicSetMethods fill the public set methods
     * @param fillPublicFields fill the public fields
     * @param ignoreValueNames ignore the value names in the set
     * @param setNulls set or ignore null values
     *
     * @return object with set methods
     */
    public static <T> T fillObjectFromRequest(T obj, HttpServletRequest request, boolean fillPublicSetMethods, boolean fillPublicFields, Set<String> ignoreValueNames, boolean setNulls) 
      {
        try
          {
              fillObject(obj, request, fillPublicSetMethods, fillPublicFields, ignoreValueNames, setNulls);

            return obj;
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
        return null;
      }
    
    /**
     * Fill the object with values from the given getHandler.
     * 
     * @param getHandler the GetHandler providing values for the object being filled
     * @param object the object being filled
     *
     * @return the object passed in
     */
    
      public static <T> T fillObject(T object, HttpServletRequest request) 
        {
          return fillObject(object, request,null, true, false, null, false);
        }
      
    /**
     * Fill the object with values from the given getHandler.
     * 
     * @param getHandler the GetHandler providing values for the object being filled
     * @param object the object being filled
     * @param ignoreValueNames ignore the value names in the set
     * @param setNulls set or ignore null values
     *
     * @return the object passed in
     */
    
      public static <T> T fillObject(T object, HttpServletRequest request,Set<String> ignoreValueNames, boolean setNulls) 
        {
          return fillObject(object, request, null, true, false, ignoreValueNames, setNulls);
        }
      
    /**
     * Fill the object with values from the given getHandler.
     * 
     * @param getHandler the GetHandler providing values for the object being filled
     * @param object the object being filled
     * @param fillPublicSetMethods fill public set methods of the object
     * @param fillPublicFields fill public fields of the object
     * @param ignoreValueNames ignore the value names in the set
     * @param setNulls set or ignore null values
     *
     * @return the object passed in
     */
    
      public static <T> T fillObject(T object, HttpServletRequest request,boolean fillPublicSetMethods, boolean fillPublicFields, Set<String> ignoreValueNames, boolean setNulls) 
        {
          return fillObject(object, request, null, fillPublicSetMethods, fillPublicFields, ignoreValueNames, setNulls);
        }

    /**
     * Fill the object with values from the given getHandler.
     * 
     * @param getHandler the GetHandler providing values for the object being filled
     * @param object the object being filled
     * @param c the declaring class that methods and fields must exist in or null for no restriction
     * @param fillPublicSetMethods fill public set methods of the object
     * @param fillPublicFields fill public fields of the object
     * @param ignoreValueNames ignore the value names in the set
     * @param setNulls set or ignore null values
     *
     * @return the object passed in
     */
    
      public static <T> T fillObject(T object, HttpServletRequest request,Class<T> c, boolean fillPublicSetMethods, boolean fillPublicFields, Set<String> ignoreValueNames, boolean setNulls) 
        {
          Set memberSet = new HashSet();
          
          if (fillPublicSetMethods)
            {
              Method[] methods = c != null ? c.getMethods() : object.getClass().getMethods();
              
              for (int i = 0; i < methods.length; i++)
                {
                  Method method = methods[i];
                  String methodName = method.getName();

                  if (c == null || methods[i].getDeclaringClass().equals(c))
                    if (methodName.startsWith("set") && !methodName.equalsIgnoreCase("set"))
                      {
                        String valueName = methodName.substring(3,4).toLowerCase() + methodName.substring(4);

                        if (!memberSet.contains(valueName))
                          {
                            memberSet.add(valueName);

                            if (method.getParameterTypes().length > 0 && (ignoreValueNames == null || !ignoreValueNames.contains(valueName)))
                              {
                                try
                                  {
                                    Object value = get(valueName, method.getParameterTypes()[0], request);

                                    if (value == null)
                                      value = get(StringUtils.camelCaseToLowerCaseUnderline(valueName), method.getParameterTypes()[0],request);

                                    if (value == null)
                                      value = get(valueName.toLowerCase(), method.getParameterTypes()[0], request);

                                    if (setNulls || !StringUtils.isNullOrEmpty(value))
                                      if (method.getParameterTypes() != null && method.getParameterTypes().length == 1)
                                        method.invoke(object, new Object[] { 
                                        		StringUtils.convertObject(method.getParameterTypes()[0], 
                                        				value) 
                                        				});
                                  }
                                catch (Exception e) { 
                                	e.printStackTrace();
                                }
                              }
                          }
                      }
                }
            }
          
          if (fillPublicFields)
            {
              Field[] fields = c != null ? c.getFields() : object.getClass().getFields();
              
              for (int i = 0; i < fields.length; i++)
                {
                  String fieldName = fields[i].getName();
                  
                  if (c == null || fields[i].getDeclaringClass().equals(c))
                    if (!memberSet.contains(fieldName))
                      {
                        memberSet.add(fieldName);

                        if (ignoreValueNames == null || !ignoreValueNames.contains(fieldName))
                          {
                            try
                              {
                                Object value = get(fieldName, fields[i].getType(), request);

                                if (value == null)
                                  value = get(fieldName.toLowerCase(), fields[i].getType(), request);

                                if (value == null)
                                  value = get(StringUtils.camelCaseToLowerCaseUnderline(fieldName), fields[i].getType(), request);

                                if (setNulls || value != null)
                                  fields[i].set(object, new Object[] { StringUtils.convertObject(fields[i].getType(), value) });
                              }
                            catch (Exception e) { 
                            	e.printStackTrace();
                            }
                          }
                      }
                }
            }
          
          return object;
        }

      /**
       * Implement this interface for ObjectFiller to gain access to your data.
       * If your object has a field that doesn't match up to data, 
       * throw ItemNotFoundException instead of returning null so ObjectFiller 
       * doesn't set the field to null.
       */
      
          public static Object get(String key, Class objectType, HttpServletRequest request){
        	  String prefix = request.getAttribute("_pojoPrefix") == null ? "" : (String)request.getAttribute("_pojoPrefix");
        	  String[] arr = request.getParameterValues(prefix+key);
              
              if (arr != null && !objectType.isArray())
                return arr[0];
                 
              return arr;
        	  
          }
          
          public static final String DO_POST_FLAG="_dopost";
          //--- determine a page request need processing form ---//
          public static boolean doProcessForm(HttpServletRequest request){
          	if("1".equals(request.getParameter(DO_POST_FLAG)))
          		return true;
          	else if("POST".equalsIgnoreCase(request.getMethod()))
          		return true;
          	return false;

          }
      
}
