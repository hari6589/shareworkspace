package com.bfrc.dataaccess.properties;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Class to load system specific properties from the database instead of from local property files. 
 * @author Brad Balmer
 *
 */
public class DBPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer implements InitializingBean {

	private List<String> siteTypes;
	private DataSource dataSource;
	private Properties _SYSTEM_PROPERTIES;
	private PreparedStatement pStmt = null;
	private final String SQL = "SELECT ATTRIBUTE_NAME, ATTRIBUTE_VALUE FROM BFRC_APP_ATTRIBUTES WHERE STATUS_CODE = 'A' AND SITE_TYPE = ? ORDER BY SORT_CODE";
	
	@Override
	protected Properties mergeProperties() throws IOException {
		return _SYSTEM_PROPERTIES;
	}
	
	/**
	 * Called directly after the properties have been set (initialized) by Spring
	 */
	public void afterPropertiesSet() throws Exception {
		if(siteTypes == null)
			siteTypes = new ArrayList<String>();

		//Set the values into the _SYSTEM_PROPERTIES Object
		setSystemProperties();
	}
		
	/**
	 * Loop through the siteTypes and pull back from the database the specific properties, setting
	 * the name/value pairs into the _SYSTEM_PROPERTIES Object.
	 * @throws Exception
	 */
	private void setSystemProperties() throws Exception {
		_SYSTEM_PROPERTIES = new Properties();
		
		/*
		 * Loop through each group, selecting the attribute values for each group.
		 * Each name/value pair will be set in the _SYSTEM_PROPERTIES Property element.  The order
		 * of the groups DOES matter as attribute names will get overridden by subsequent name values.
		 */
		try {
			pStmt = dataSource.getConnection().prepareStatement(SQL);

			for(String siteType : siteTypes) {
				siteType = StringUtils.trimToNull(siteType);
				if(siteType != null) {
					
					ResultSet rs = null;
					try {
						pStmt.setString(1, siteType);
						rs = pStmt.executeQuery();
						while(rs.next()) {
							String name = StringUtils.trimToNull(rs.getString(1));
							String value = StringUtils.trimToEmpty(rs.getString(2));
							if(name != null)
								_SYSTEM_PROPERTIES.put(name, value);
						}
					}catch(SQLException e) {
						throw new Exception(e);
					}finally {
						if(rs != null) {
							try {
								rs.close();
								rs = null;
							}catch(Exception e){}
						}
					}
				}
			}
		}catch(SQLException e) {
			throw new Exception(e);
		} finally {
			try {
				if(pStmt != null) {
					try {
						pStmt.close();
						pStmt = null;
					}catch(Exception ex){}
				}
			}catch(Exception ex2){}
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public void setSiteTypes(List<String> siteTypes) {
		this.siteTypes = siteTypes;
	}
}
