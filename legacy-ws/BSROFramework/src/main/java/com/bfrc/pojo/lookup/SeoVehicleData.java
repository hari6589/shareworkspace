package com.bfrc.pojo.lookup;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author smoorthy
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({ "globalId", "fileId"})
public class SeoVehicleData implements java.io.Serializable {
	
	private Long globalId;
	private String fileId;
	private String recordId;
	private String title;
	private String description;
	private String hero;
	private String cta;
	private String header1;
	private String content1;
	private String header2;
	private String content2;
	private String header3;
	private String content3;
	private String webSite;
	
	public SeoVehicleData() {
		
	}
	
	public SeoVehicleData(Long globalId) {
		this.globalId = globalId;
	}
	
	public SeoVehicleData(Long globalId, String fileId, String recordId, String title, String description, String hero, String cta, String header1, String content1,
			String header2, String content2, String header3, String content3, String webSite) {
		this.globalId = globalId;
		this.fileId = fileId;
		this.recordId = recordId;
		this.title = title;
		this.description = description;
		this.hero = hero;
		this.cta = cta;
		this.header1 = header1;
		this.content1 = content1;
		this.header2 = header2;
		this.content2 = content2;
		this.header3 = header3;
		this.content3 = content3;
		this.webSite = webSite;
	}
	
	public Long getGlobalId() {
		return globalId;
	}
	
	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}
	
	public String getFileId() {
		return fileId;
	}
	
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	public String getRecordId() {
		return recordId;
	}
	
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getHero() {
		return hero;
	}
	
	public void setHero(String hero) {
		this.hero = hero;
	}
	
	public String getCta() {
		return cta;
	}
	
	public void setCta(String cta) {
		this.cta = cta;
	}
	
	public String getHeader1() {
		return header1;
	}
	
	public void setHeader1(String header1) {
		this.header1 = header1;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
	public String getContent1() {
		return content1;
	}
	
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	
	public String getHeader2() {
		return header2;
	}
	
	public void setHeader2(String header2) {
		this.header2 = header2;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
	public String getContent2() {
		return content2;
	}
	
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	
	public String getHeader3() {
		return header3;
	}
	
	public void setHeader3(String header3) {
		this.header3 = header3;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
	public String getContent3() {
		return content3;
	}
	
	public void setContent3(String content3) {
		this.content3 = content3;
	}
	
	public String getWebSite() {
		return webSite;
	}
	
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	
	@Override
	public String toString() {
		return "SeoVehicleData [globalId = "+globalId+", fileId = "+fileId+", recordId = "+recordId+", title = "+title+", description = "+description+
				"hero = "+hero+", cta = "+cta+", header1 = "+header1+", content1 = "+content1+", header2 = "+header2+", content2 = "+content2+", header3 = "+header2+", "+content3+
				", webSite = "+webSite+" ]";
	}

}
