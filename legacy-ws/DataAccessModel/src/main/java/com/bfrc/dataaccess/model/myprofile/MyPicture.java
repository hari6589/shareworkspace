/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"image","lastModifiedDate"})
public class MyPicture {

	private Long myPictureId;
	private PictureType pictureType;
	private byte[] image;
	private String notes;
	private Date lastModifiedDate;
	private Long pictureObjectId;
	
	
	/**
	 * 
	 */
	public MyPicture() {
	}
	
	/**
	 * @param pictureType
	 * @param image
	 */
	public MyPicture(PictureType pictureType, byte[] image) {
		this.pictureType = pictureType;
		this.image = image;
	}

	public Long getMyPictureId() {
		return myPictureId;
	}
	public void setMyPictureId(Long myPictureId) {
		this.myPictureId = myPictureId;
	}
	public PictureType getPictureType() {
		return pictureType;
	}
	public void setPictureType(PictureType pictureType) {
		this.pictureType = pictureType;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public Long getPictureObjectId() {
		return pictureObjectId;
	}

	public void setPictureObjectId(Long pictureObjectId) {
		this.pictureObjectId = pictureObjectId;
	}

	@Override
	public String toString() {
		return "MyPicture [myPictureId=" + myPictureId + ", pictureType="
				+ pictureType + ", notes=" + notes + ", lastModifiedDate="
				+ lastModifiedDate + ", pictureObjectId=" + pictureObjectId
				+ "]";
	}

	
	
	
}
