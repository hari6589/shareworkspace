package com.bsro.databean;

import com.bfrc.Bean;

public class BaseDataBean implements Bean{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1911093262246331319L;


public static final String NO_MATCH = "No Match";
   
   
   boolean isOK ;
   
   public void setOK(boolean isOK){
	   this.isOK = isOK;
   }
   
   public boolean getOK(){
	   return this.isOK;
   }
   
   private String errorMessage;
   
   public void setErrorMessage(String errorMessage){
	   this.errorMessage = errorMessage;
   }
   
   public String getErrorMessage(){
	   return this.errorMessage;
   }
   
}
