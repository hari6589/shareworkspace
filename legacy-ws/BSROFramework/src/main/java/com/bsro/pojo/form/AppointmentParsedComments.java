package com.bsro.pojo.form;

import java.io.Serializable;

public class AppointmentParsedComments implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String quoteId = "", articleNo = "", tireDesc = "", tireSize = "", tireQty = "", tireUnitPrice = "";
	private String rearArticleNo = "", rearTireDesc = "", rearTireSize = "", rearTireQty = "", rearTireUnitPrice = "";
	private String rawComments ="";
	private boolean matchedSet;

	private String batteryQuoteId;

	private String batteryArticleNumber;

	private String batteryDescription;

	private String batteryWarranty;

	private String batteryUnitPrice;

	private boolean cameFromBatteryFunnel = false;
	
	public AppointmentParsedComments(String comments){
		rawComments = new String(comments);
		this.parseComments(comments);
	}
	
	public String getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public String getArticleNo() {
		return articleNo;
	}
	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}
	public String getTireDesc() {
		return tireDesc;
	}
	public void setTireDesc(String tireDesc) {
		this.tireDesc = tireDesc;
	}
	public String getTireSize() {
		return tireSize;
	}
	public void setTireSize(String tireSize) {
		this.tireSize = tireSize;
	}
	public String getTireQty() {
		return tireQty;
	}
	public void setTireQty(String tireQty) {
		this.tireQty = tireQty;
	}
	public String getTireUnitPrice() {
		return tireUnitPrice;
	}
	public void setTireUnitPrice(String tireUnitPrice) {
		this.tireUnitPrice = tireUnitPrice;
	}
	public String getRearArticleNo() {
		return rearArticleNo;
	}
	public void setRearArticleNo(String rearArticleNo) {
		this.rearArticleNo = rearArticleNo;
	}
	public String getRearTireDesc() {
		return rearTireDesc;
	}
	public void setRearTireDesc(String rearTireDesc) {
		this.rearTireDesc = rearTireDesc;
	}
	public String getRearTireSize() {
		return rearTireSize;
	}
	public void setRearTireSize(String rearTireSize) {
		this.rearTireSize = rearTireSize;
	}
	public String getRearTireQty() {
		return rearTireQty;
	}
	public void setRearTireQty(String rearTireQty) {
		this.rearTireQty = rearTireQty;
	}
	public String getRearTireUnitPrice() {
		return rearTireUnitPrice;
	}
	public void setRearTireUnitPrice(String rearTireUnitPrice) {
		this.rearTireUnitPrice = rearTireUnitPrice;
	}
	
	private void parseComments(String comments) {		
		int i=0, j=0, k=0;
		String temp = null;
		boolean matchedSet = false;
		if (comments != null ){
			if(comments.contains("tire quote id")){
				//parse it out
				//tire quote id:  1019910,  tire article number:  55140,  tire description: Bridgestone Dueler H/L400,  tire size: P255/55R18,  tire quantity: 4,  tire unit price: $158.99,  tire quote End;
				// example: tire+quote+id%3A++1019910%2C++tire+article+number%3A++55140%2C++tire+description%3A+Bridgestone+Dueler+H%2FL400%2C++tire+size%3A+P255%2F55R18%2C++tire+quantity%3A+4%2C++tire+unit+price%3A+%24158.99%2C++S
				i = comments.indexOf("tire quote id:  ");
				if (i > -1){
					i+=16; /* s/b start of tire quote ID */ 
					j = comments.indexOf(",", i);
					if (j >-1){
						quoteId = comments.substring(i, j);
					}
				}
				i = comments.indexOf("tire article number:  ");
				if (i > -1){
					i+=22; /* s/b start of tire article number */ 
					j = comments.indexOf(",", i);
					if (j >-1){
						temp = comments.substring(i, j);
						k = temp.indexOf(";");
						if (k > -1){
							matchedSet = true;
							articleNo = temp.substring(0, k);
							rearArticleNo = temp.substring(k+1);
						} else  {
							articleNo = temp;
						}
					}
				}
				i = comments.indexOf("tire description: ");
				if (i > -1){
					i+=18; /* s/b start of tire description */ 
					j = comments.indexOf(",", i);
					if (j >-1){
						temp = comments.substring(i, j);
						k = temp.indexOf(";");
						if (k > -1){
							tireDesc = temp.substring(0, k);
							rearTireDesc = temp.substring(k+1);
						} else  {
							tireDesc = temp;
						}
					}
				}
				i = comments.indexOf("tire size: ");
				if (i > -1){
					i+=11; /* s/b start of tire size */ 
					j = comments.indexOf(",", i);
					if (j >-1){
						temp = comments.substring(i, j);
						k = temp.indexOf(";");
						if (k > -1){
							tireSize = temp.substring(0, k);
							rearTireSize = temp.substring(k+1);
						} else  {
							tireSize = temp;
						}
					}
				}
				i = comments.indexOf("tire quantity: ");
				if (i > -1){
					i+=15; /* s/b start of tire quantity */ 
					j = comments.indexOf(",", i);
					if (j >-1){
						tireQty = comments.substring(i, j);
						if (matchedSet){
							rearTireQty = tireQty;
						}
					}
				}
				i = comments.indexOf("tire unit price: ");
				if (i > -1){
					i+=17; /* s/b start of tire unit price */ 
					j = comments.indexOf(",", i);
					if (j >-1){
						temp = comments.substring(i, j);
						k = temp.indexOf(";");
						if (k > -1){
							tireUnitPrice = temp.substring(0, k);
							rearTireUnitPrice = temp.substring(k+1);
						} else  {
							tireUnitPrice = temp;
						}
					}
				}
			}else if(comments.contains("battery quote id")){
				this.cameFromBatteryFunnel  = true;
				String[] splitComments = comments.split(",");
				this.batteryQuoteId = splitComments[0];
				//get the colon
				int colonIndex = batteryQuoteId.indexOf(":") + 1;
				batteryQuoteId = batteryQuoteId.substring(colonIndex).trim();
				
				this.batteryArticleNumber = splitComments[1];
				colonIndex = batteryArticleNumber.indexOf(":") + 1;
				batteryArticleNumber = batteryArticleNumber.substring(colonIndex).trim();
				
				this.batteryDescription = splitComments[2];
				colonIndex = batteryDescription.indexOf(":") + 1;
				batteryDescription = batteryDescription.substring(colonIndex).trim();				
				
				this.batteryWarranty = splitComments[3];
				colonIndex = batteryWarranty.indexOf(":") + 1;
				batteryWarranty = batteryWarranty.substring(colonIndex).trim();				
				
				this.batteryUnitPrice = splitComments[4];
				colonIndex = batteryUnitPrice.indexOf(":") + 1;
				batteryUnitPrice = batteryUnitPrice.substring(colonIndex).trim();
				
			}
		}
		setTireDesc(tireDesc);
		setMatchedSet(matchedSet);
		
	}
	
	public boolean getMatchedSet(){
		return matchedSet;
	}
	
	public void setMatchedSet(boolean matchedSet) {
		this.matchedSet = matchedSet;
		
	}

	public String getBatteryQuoteId() {
		return batteryQuoteId;
	}

	public void setBatteryQuoteId(String batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}

	public String getBatteryArticleNumber() {
		return batteryArticleNumber;
	}

	public void setBatteryArticleNumber(String batteryArticleNumber) {
		this.batteryArticleNumber = batteryArticleNumber;
	}

	public String getBatteryDescription() {
		return batteryDescription;
	}

	public void setBatteryDescription(String batteryDescription) {
		this.batteryDescription = batteryDescription;
	}

	public String getBatteryWarranty() {
		return batteryWarranty;
	}

	public void setBatteryWarranty(String batteryWarranty) {
		this.batteryWarranty = batteryWarranty;
	}

	public String getBatteryUnitPrice() {
		return batteryUnitPrice;
	}

	public void setBatteryUnitPrice(String batteryUnitPrice) {
		this.batteryUnitPrice = batteryUnitPrice;
	}

	public boolean isCameFromBatteryFunnel() {
		return cameFromBatteryFunnel;
	}

	public void setCameFromBatteryFunnel(boolean cameFromBatteryFunnel) {
		this.cameFromBatteryFunnel = cameFromBatteryFunnel;
	}

	public String getRawComments() {
		return rawComments;
	}

	public void setRawComments(String rawComments) {
		this.rawComments = rawComments;
	}

}
