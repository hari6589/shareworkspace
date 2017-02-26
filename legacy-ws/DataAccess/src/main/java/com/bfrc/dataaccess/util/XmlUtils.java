package com.bfrc.dataaccess.util;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

public class XmlUtils {

	public static String escape(String value) {
		return StringEscapeUtils.escapeXml(StringUtils.trimToEmpty(value));
	}
	
	/**
	 * Helper function to recursively loop through the node given looking for a particular
	 * element by its name.
	 * @param elemName
	 * @param node
	 * @return
	 */
	public static Element findElement(String elemName, Element node) {
		List list = node.getChildren();
		if(list != null) {
			for(int i=0;i<list.size();i++) {
				Element l = (Element)list.get(i);
				String name = l.getName();
				if(elemName.equals(name)) {
					return l;
				} else {
					if(l.getChildren() != null && l.getChildren().size() > 0)
						return findElement(elemName, l);
				}
			}
		}
		return null;
	}
	
	
}
