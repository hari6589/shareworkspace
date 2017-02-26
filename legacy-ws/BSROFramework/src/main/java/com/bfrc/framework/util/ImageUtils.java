/**
 * 
 * Modified from jWebApp 
 * 
 * URL and Licenses
 * go to http://www.softwaresensation.com
 */

package com.bfrc.framework.util;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;



public class ImageUtils 
{
	public static byte[] resizeImage(byte[] in, int thumbWidth, int thumbHeight) throws Exception {
		return  resizeImage(in,thumbWidth,thumbHeight,90);
	}
	public static byte[] resizeImage(byte[] in, int thumbWidth) throws Exception {
		return  resizeImage(in,thumbWidth,-1,90);
	}
	
	public static byte[] resizeImageByHeight(byte[] in, int thumbHeight) throws Exception {
		return  resizeImage(in,-1,thumbHeight,90);
	}
	public static byte[] resizeImage(byte[] in, int thumbWidth, int thumbHeight,int quality) throws Exception {
		Image image = Toolkit.getDefaultToolkit().createImage(in);
	    MediaTracker mediaTracker = new MediaTracker(new Container());
	    mediaTracker.addImage(image, 0);
	    mediaTracker.waitForID(0);
	    // determine thumbnail size from WIDTH and HEIGHT
	    double thumbRatio = 0;
	    int imageWidth = image.getWidth(null);
	    int imageHeight = image.getHeight(null);
	    double imageRatio = (double)imageWidth / (double)imageHeight;
	    if(thumbWidth >0 && thumbHeight > 0){
	        thumbRatio = (double)thumbWidth / (double)thumbHeight;
		    if (thumbRatio < imageRatio) {
		      thumbHeight = (int)(thumbWidth / imageRatio);
		    } else {
		      thumbWidth = (int)(thumbHeight * imageRatio);
		    }
	    }else{
	    	if(thumbWidth >0){
	    		thumbHeight = (int)(thumbWidth / imageRatio);
	    	}else if(thumbHeight >0){
	    		thumbWidth = (int)(thumbHeight * imageRatio);
	    	}
	    }
	    // draw original image to thumbnail image object and
	    // scale it to the new size on-the-fly
	    BufferedImage thumbImage = new BufferedImage(thumbWidth, 
	      thumbHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = thumbImage.createGraphics();
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
	    // save thumbnail image to OUTFILE
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	    JPEGEncodeParam param = encoder.
	      getDefaultJPEGEncodeParam(thumbImage);
	    quality = Math.max(0, Math.min(quality, 100));
	    param.setQuality((float)quality / 100.0f, false);
	    encoder.setJPEGEncodeParam(param);
	    encoder.encode(thumbImage);
	    return out.toByteArray();
	}
      
}
