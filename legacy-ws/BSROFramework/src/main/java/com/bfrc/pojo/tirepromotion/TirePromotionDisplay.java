package com.bfrc.pojo.tirepromotion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TirePromotionDisplay {
	private long promoId;
	private String promoName;
	private Date startDate;
    private Date endDate;
    private char statusFlag;
    private List<String> siteName;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    
	public long getPromoId() {
		return promoId;
	}
	public void setPromoId(long promoId) {
		this.promoId = promoId;
	}
	public String getPromoName() {
		return promoName;
	}
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public char getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(char statusFlag) {
		this.statusFlag = statusFlag;
	}
	
	public String getStartDateAsString(){
		String formattedDate = "";
		if(startDate != null){
			formattedDate = sdf.format(startDate);
		}
		return formattedDate;
	}
	
	public String getEndDateAsString(){
		String formattedDate = "";
		if(endDate != null){
			formattedDate = sdf.format(endDate);
		}
		return formattedDate;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("tireDisplayInfo:\n");
		sb.append("\tpromoId: ");sb.append(promoId);
		sb.append("\n\tpromoName: ");sb.append(promoName);
		sb.append("\n\tstartDate: ");sb.append(getStartDateAsString());
		sb.append("\n\tendDate: ");sb.append(getEndDateAsString());
		sb.append("\n\tstatusFlag: ");sb.append(statusFlag);
		return sb.toString();
	}   

}
