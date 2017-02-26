package com.bfrc.framework.util;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;

import com.bfrc.Config;
import com.bfrc.pojo.*;
import com.bfrc.framework.dao.*;
import com.bfrc.pojo.promotion.*;
import com.bfrc.pojo.tirepromotion.*;
import com.bfrc.pojo.realestate.*;
import com.hibernate.dao.base.*;

public class DynamicContentUtil {
   public static String SPACER_IMAGE="R0lGODlhAQABAPAAAAAAAP///yH5BAEAAAEALAAAAAABAAEAAAICTAEAOwAAAAAAAAA=";
   private static final String NULL_TEXT = "NULL"; 
   public static String getFileType(String type){
	   String filetype="image/jpeg";
	    if("gif".equals(type)){
	        filetype="image/gif";
	    }else if("pdf".equals(type)){
	        filetype="application/pdf";
	    }
	    return filetype;
   }
   private static TirePromotionDAO tirePromotionDAO = null;
   private static PromotionDAO promotionDAO= null;
   private static RealEstateDAO realEstateDAO= null;
   private static CatalogDAO catalogDAO = null;
   
   private static void locateBeans(HttpServletRequest request){
	   if(promotionDAO == null){
		   ServletContext ctx = request.getSession().getServletContext();
		   try{
		       promotionDAO = (PromotionDAO)com.bfrc.Config.locate(ctx, "promotionDAO");
		   }catch(Exception ex){}
		   try{
		       tirePromotionDAO = (TirePromotionDAO)com.bfrc.Config.locate(ctx, "tirePromotionDAO");
		   }catch(Exception ex){} 
	       try{
		       realEstateDAO = (RealEstateDAO)com.bfrc.Config.locate(ctx, "realEstateDAO");
	       }catch(Exception ex){}
	       try{
		       catalogDAO = (CatalogDAO)com.bfrc.Config.locate(ctx,"catalogDAO");
	       }catch(Exception ex){}
	   }
	   
   }
   public static byte[] getBinaryContent(HttpServletRequest request)
   {
	   locateBeans(request);
	    //--- image id ---//
	    String id = request.getParameter("i");
	    //--- file type ---//
	    String type = request.getParameter("t");
	    //--- id type (used id is landing page id, etc) ---//
	    String id_type = request.getParameter("it");
	    //--- which  images(thumb, flash icon, etc)  ---//
	    String which = request.getParameter("w");
	    //--- image source, promotion, real property, catalog, etc ---//
	    String src = request.getParameter("src");
	    //--- image size ---//
	    String strImageWidth = request.getParameter("iw");
	    String strImageHeight = request.getParameter("ih");
	    int imgW=-1, imgH = -1;
	    boolean doResize = false;
	    if(!StringUtils.isNullOrEmpty(strImageWidth)){
	    	try{
	    		imgW = Integer.parseInt(strImageWidth);
	    	}catch(Exception ex){
	    		StringBuilder logMessage = new StringBuilder();
	        	logMessage.append("\n================== Error in DynamicContentUtil(v2) - parsing image width");
	        	
	        	logMessage.append("\n\tstrImageWidth:"); logMessage.append(strImageWidth == null ? NULL_TEXT : strImageWidth);
	        	
	        	//get stack trace as string
	        	StringWriter stackTrace = new StringWriter();
	    	    ex.printStackTrace(new PrintWriter(stackTrace));
	    	   	logMessage.append("\n\tEXCEPTION STACK TRACE:\n");
	    	   	logMessage.append(stackTrace.toString());
	    	   	
	        	logMessage.append("\n==================");
	        	System.err.println(logMessage.toString());	    		
	    	}
	    }
	    if(!StringUtils.isNullOrEmpty(strImageHeight)){
	    	try{
	    		imgH = Integer.parseInt(strImageHeight);
	    	}catch(Exception ex){
	    		StringBuilder logMessage = new StringBuilder();
	        	logMessage.append("\n================== Error in DynamicContentUtil(v2) - parsing image height");
	        	
	        	logMessage.append("\n\tstrImageHeight:"); logMessage.append(strImageHeight == null ? NULL_TEXT : strImageHeight);
	        	
	        	//get stack trace as string
	        	StringWriter stackTrace = new StringWriter();
	    	    ex.printStackTrace(new PrintWriter(stackTrace));
	    	   	logMessage.append("\n\tEXCEPTION STACK TRACE:\n");
	    	   	logMessage.append(stackTrace.toString());
	    	   	
	        	logMessage.append("\n==================");
	        	System.err.println(logMessage.toString());
	    	}
	    }
	    if((imgW > 0 && imgW < 2000) || (imgH > 0 && imgH < 2000)){
	    	if(imgW < 0)
	    		imgW=-1;
	    	if(imgH < 0)
	    		imgH=-1;
			doResize = true;
	    }
	    TirePromotionEvent promotion = null;
	    PromotionImages promotion2 = null;
	    RealestateStoreGallery storeGallery = null;
	    RealestateSurplusProperty surplusProperty = null;
	    //--- 20091104 releases: add validation by CS --//
	    boolean useDefault = false;
	    if(StringUtils.isNullOrEmpty(id))
	    	useDefault = true;
	    byte[] bytes = null;
	    try{
	    	if(!useDefault){
		    	if("mc".equals(src)){
		    		//do nothing
		        }else if("p".equals(src)){
		            if("lpid".equals(id_type)){
		                promotion2 = promotionDAO.getPromotionImagesByLandingPageID(id);
		            }else{
		                promotion2 = promotionDAO.getPromotionImagesById(id);
		            }
		            if(promotion2 == null)
		            	useDefault = true;
		        }else if ("re".equals(src)){
		        	if(realEstateDAO != null){
				        if("property".equals(id_type)){
					        surplusProperty = realEstateDAO.findRealestateSurplusProperty(id);
					        if(surplusProperty == null)
				            	useDefault = true;
				        }else{
					        storeGallery = realEstateDAO.findRealestateStoreGallery(id);
					        if(storeGallery == null)
				            	useDefault = true;
				        }
		        	}
		        }else if ("cat".equals(src)){
		        	
		        }else{
		            promotion = tirePromotionDAO.getTirePromotion(id);
		        }
	    	}
	    }catch(Exception ex){
	        //do nothing
	    	useDefault = true;
	    }
	    
	    if(!useDefault){
	    	try{
		        if("p".equals(src)){
		            if("lp".equals(which)){
		                if(promotion2.getLandingPageIcon() != null)
		                    bytes = promotion2.getLandingPageIcon();
		                else
		                    useDefault = true;
		            }else if("image".equals(which)){
		                if(promotion2.getImage() != null)
		                    bytes = promotion2.getImage();
		                else
		                    useDefault = true;
		            }else if("thumb".equals(which)){
		                if(promotion2.getThumb() != null)
		                    bytes = promotion2.getThumb();
		                else
		                    useDefault = true;
		            }else if("flashIcon".equals(which)){
		                if(promotion2.getFlashIcon() != null)
		                    bytes = promotion2.getFlashIcon();
		                else
		                    useDefault = true;
		            }
		        }else if ("re".equals(src)){
				    if("property".equals(id_type)){
						if("image".equals(which)){
						    if(surplusProperty.getLargeImage() != null)
							bytes = surplusProperty.getLargeImage();
						    else
							useDefault = true;
						}else if("thumb".equals(which)){
						    if(surplusProperty.getThumbImage() != null)
							bytes = surplusProperty.getThumbImage();
						    else
							useDefault = true;
						}
				    }else{
						if("image".equals(which)){
						    if(storeGallery.getLargeImage() != null)
							bytes = storeGallery.getLargeImage();
						    else
							useDefault = true;
						}else if("thumb".equals(which)){
						    if(storeGallery.getThumbImage() != null)
							bytes = storeGallery.getThumbImage();
						    else
							useDefault = true;
						}
				    }
		        }else if ("cat".equals(src)){
		        	if("pdf".equals(which))
		        		bytes = catalogDAO.getWarrantyPDF(Long.parseLong(id));
					if("brand".equals(which))
						bytes = catalogDAO.getBrandImage(Long.parseLong(id));
					else if("fact".equals(which))
						bytes = catalogDAO.getFactImage(Long.parseLong(id));
					else if("tech".equals(which) || "technology".equals(which))
						bytes = catalogDAO.getTechnologyImage(Long.parseLong(id));
					else if("tire".equals(which))
						bytes = catalogDAO.getTireImage(Long.parseLong(id));
					else if("tireName".equals(which))
						bytes = catalogDAO.getTireNameImage(Long.parseLong(id));
					else if("tiregroup".equals(which))
						bytes = catalogDAO.getTiregroupImage(Long.parseLong(id));
		        }else if ("mc".equals(src)){
		        }else{
		            if("image".equals(which)){
		                if(promotion.getPromoLargeImg() != null)
		                    bytes = promotion.getPromoLargeImg();
		                else
		                    useDefault = true;
		            }else if("thumb".equals(which)){
		                if(promotion.getPromoSmallImg() != null)
		                    bytes = promotion.getPromoSmallImg();
		                else
		                    useDefault = true;
		            }else if("pdf".equals(which)){
		                if(promotion.getPromoPdf() != null)
		                    bytes = promotion.getPromoPdf();
		                else
		                    useDefault = true;
		            }
		        }
	    	}catch(Exception ex){
	    		useDefault = true;
	    		ex.printStackTrace(System.out);
		    }
	    }
	    if(doResize){
	    	try{
	    	   bytes= ImageUtils.resizeImage(bytes, imgW, imgH);
	    	}catch(Exception ex){
	    		useDefault = true;
	    	}
	    }
	    if(bytes == null || useDefault){
	    	try{
	    		//bytes = org.apache.commons.codec.binary.Base64.decodeBase64(SPACER_IMAGE);
	    	    bytes = new sun.misc.BASE64Decoder().decodeBuffer(SPACER_IMAGE);
	    	}catch(Exception ex){
	    		StringBuilder logMessage = new StringBuilder();
	        	logMessage.append("\n================== Error in DynamicContentUtil(v2) getting default image");
	        	
	        	logMessage.append("\n\ttype:"); logMessage.append(type == null ? NULL_TEXT : type);
	        	logMessage.append("\n\tid_type:"); logMessage.append(id_type == null ? NULL_TEXT : id_type);
	        	logMessage.append("\n\twhich:"); logMessage.append(which == null ? NULL_TEXT : which);
	        	logMessage.append("\n\tsrc:"); logMessage.append(src == null ? NULL_TEXT : src);
	        	logMessage.append("\n\tstrImageWidth:"); logMessage.append(strImageWidth == null ? NULL_TEXT : strImageWidth);
	        	logMessage.append("\n\tstrImageHeight:"); logMessage.append(strImageHeight == null ? NULL_TEXT : strImageHeight);
	        	logMessage.append("\n\tbytes:"); logMessage.append(bytes == null ? NULL_TEXT : "BYTES NOT NULL");
	        	logMessage.append("\n\tuseDefault:"); logMessage.append(useDefault);
	        	
	        	//get stack trace as string
	        	StringWriter stackTrace = new StringWriter();
	    	    ex.printStackTrace(new PrintWriter(stackTrace));
	    	   	logMessage.append("\n\tEXCEPTION STACK TRACE:\n");
	    	   	logMessage.append(stackTrace.toString());
	    	   	
	        	logMessage.append("\n==================");
	        	System.err.println(logMessage.toString());	    		
	    	}
	    }
	   return bytes;
   }
}
