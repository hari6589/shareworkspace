package com.bfrc.dataaccess.core.util;

import java.io.File;
import java.net.URL;

public class PathUtil
{
	/**
	 * Determine the path to the jar file we are executing in.  Used internally
	 * to load mapping files contained within the jar.
	 * 
	 * @return The path to this jar file.
	 */
	public static final String findJarPath( Object object )
	{
		String myClass = '/' + object.getClass().getName().replace('.', '/') + ".class";
		URL myClassURL = object.getClass().getResource(myClass);
		String path = "";
		
		if( myClassURL.getProtocol().toLowerCase().equals("jar") )
        {
			String uri = myClassURL.toExternalForm();
			path = uri.substring(uri.indexOf("/"), uri.lastIndexOf("!/"));
        }
		
        return path;
	}
	
	
	public static final File findJarFile( Object object )
	{
		return new File( findJarPath(object) );
	}
}
