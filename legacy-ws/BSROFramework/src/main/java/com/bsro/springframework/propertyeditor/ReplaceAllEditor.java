package com.bsro.springframework.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReplaceAllEditor extends PropertyEditorSupport {
  Log logger = LogFactory.getLog(ReplaceAllEditor.class);
	
  private final boolean emptyAsNull;

  private String regex = null;
  private String replacement = null;
  
  public ReplaceAllEditor(boolean emptyAsNull, String regex, String replacement) {
    
    this.emptyAsNull = emptyAsNull;
    this.regex = regex;
    this.replacement = replacement;
  }

  public void setAsText(String text) {
    if (text == null) {
      setValue(null);
    }
    else {
      String value = text.trim();

      if ((this.emptyAsNull) && ("".equals(value))) {
        setValue(null);
      } else {
//   		logger.info("Before replaceAll: "+value);
   		value = value.replaceAll(regex, replacement);
//   		logger.info("After replaceAll: "+value);
    	setValue(value);
      }
    }
  }

  public String getAsText() {
    Object value = getValue();
    return ((value != null) ? value.toString() : "");
  }
}