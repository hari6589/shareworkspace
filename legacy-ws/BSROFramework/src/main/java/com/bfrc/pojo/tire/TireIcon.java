package com.bfrc.pojo.tire;


import java.util.Date;

public class TireIcon  implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 260541175732500536L;
	/**
	 * 
	 */
	// Constructors
	/** default constructor */
    public TireIcon() {
    }

    public TireIcon(String iconImage,String iconImageLarge,String name,String description) {
    	this.iconImage = iconImage;
    	this.iconImageLarge = iconImageLarge;
    	this.name = name;
    	this.description = description; 
    }
    
   
    private String iconImage;
    public String getIconImage(){ return this.iconImage;}
    public void setIconImage(String iconImage){ this.iconImage = iconImage; }
    
    private String iconImageLarge;
    public String getIconImageLarge(){ return this.iconImageLarge;}
    public void setIconImageLarge(String iconImageLarge){ this.iconImageLarge = iconImageLarge; }
    
    private String name;
    public String getName(){ return this.name;}
    public void setName(String name){ this.name = name; }
    
    private String description;
    public String getDescription(){ return this.description;}
    public void Description(String description){ this.description = description; }
   
}