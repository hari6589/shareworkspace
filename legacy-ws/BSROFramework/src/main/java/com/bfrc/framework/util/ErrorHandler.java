package com.bfrc.framework.util;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.Config;

public class ErrorHandler extends HttpServlet
{
	private final Log logger = LogFactory.getLog(ErrorHandler.class);

    public ErrorHandler()
    {
    	
    }
    public void init() throws ServletException {
        // Make sure to always call the super's init() first
        super.init();
        String appRoot = PropertyConfigUtil.Current().getProperty("appRoot") ==  null ? null : PropertyConfigUtil.Current().getProperty("appRoot");
        //String docRoot = this.getServletContext().getRealPath("");
        String viewRoot = PropertyConfigUtil.Current().getProperty("viewRoot") == null? null :PropertyConfigUtil.Current().getProperty("viewRoot");
        String contentRoot = PropertyConfigUtil.Current().getProperty("contentRoot") == null ? null : PropertyConfigUtil.Current().getProperty("contentRoot");
        errorPage404 = PropertyConfigUtil.Current().getProperty("errorPage404");
        errorPage500 = PropertyConfigUtil.Current().getProperty("errorPage500");
        ServletContext application = this.getServletContext();
        // I can find no evidence that this attribute is used anywhere, keeping it commented out for reference
        //application.setAttribute("docRoot", docRoot);
        // Framework "v2" sites all set these properties, "v1" sites do not.
        if (appRoot != null) {
        	application.setAttribute("appRoot", appRoot);
        }
        if (viewRoot != null) {
        	application.setAttribute("viewRoot", viewRoot);
        }
        if (contentRoot != null) {
        	application.setAttribute("contentRoot", contentRoot);
        }
        // For "framework v1" sites
	    if(errorPage500 == null)
	    	errorPage500 = "/utilities/pageerror.jsp";
	    if(errorPage404 == null)
	    	errorPage404 = "/utilities/pagenotfound.jsp";
    	/*
         System.out.println("appRoot____"+appRoot);
         System.out.println("docRoot____"+docRoot);
         System.out.println("viewRoot____"+viewRoot);
         System.out.println("contentRoot____"+contentRoot);
         */
   }
    private static String errorPage404;
    private static String errorPage500;    
    private static WebApplicationContext applicationCtx;
    private static ContactDAO contactDAO;
    private static MailManager mailManager;
    private static boolean email404Error = false;
    private static boolean email500Error = false;
    private static boolean printEmail = false;
    private static void locateBeans(HttpServletRequest req){
    	applicationCtx =  Config.getCtx(req.getSession().getServletContext());
    	contactDAO = (ContactDAO)applicationCtx.getBean("contactDAO"); 
    	mailManager = (MailManager)applicationCtx.getBean("mailManager"); 
    }
    private String[] listToArray(List l){
    	//fix the class cast exception
    	if(l != null && l.size()> 0){
    		String[] ss = new String[l.size()];
    		int i=0;
    		for(Iterator it = l.iterator(); it.hasNext();i++){
    			ss[i] = (String) it.next();
    		}
    		return ss;
    	}
    	return null;
    }
    private void processErrors(HttpServletRequest req,HttpServletResponse res){
    	    email500Error = false;
	    	String redirect = errorPage500;
	    	String uri = "";
	    	boolean debuginfo = false;
	        try{
	  	      if(applicationCtx == null || contactDAO == null || mailManager == null){
	  	    	  locateBeans(req);
	  	      }
	  	      int feedbackID = 0;
	  	      String strFeedbackID = getServletConfig().getInitParameter("feedbackID");
	  		  try{
	  			  feedbackID = Integer.parseInt(strFeedbackID);
	  		  }catch(Throwable ex){
	  			  feedbackID=85;//default to TP
	  		  }
	  		  String boxName = java.net.InetAddress.getLocalHost().getHostName() ;
	  		  // --- manipulate the flags --- //
	  		  if("8atHut3cHebruxer".equals(req.getParameter("token"))){
	  			  if("1".equals(req.getParameter("404flag"))){
	//  				//we can enable the email sendind in case of we want trace not found pages
	  				  email404Error = true;
	  			  }else if("0".equals(req.getParameter("404Flag"))){
	  				  email404Error = false;
	  			  }
	  			  if("1".equals(req.getParameter("500Flag"))){
	  				  email500Error = true;
	  			  }else if("0".equals(req.getParameter("500Flag"))){
	  				  //we can disable the email sendind in case of a flooded messages
	  				  email500Error = false;
	  			  }
	  			  if("1".equals(req.getParameter("printFlag"))){
	  				  printEmail = true;
	  			  }else if("0".equals(req.getParameter("printFlag"))){
	  				  printEmail = false;
	  			  }
				  if("1".equals(req.getParameter("_setdebug"))){
					  com.bfrc.framework.util.ServerUtil.DEBUG = true;
				  }else if("1".equals(req.getParameter("_removedebug"))){
					  com.bfrc.framework.util.ServerUtil.DEBUG = false;
				  }
	  			  
	  			  if(!"1".equals(req.getParameter("_debuginfo"))){
	  				  res.getWriter().println("Flags Updated on "+boxName);
	  			      return;
	  			  }else{
	  				debuginfo = true;
	  			  }
	  		  }
	  	      
	  	      //PrintWriter out = res.getWriter();
	  	      StringBuffer msg = new StringBuffer();
	  	      Object status_code = req.getAttribute("javax.servlet.error.status_code");
	  	      if(status_code != null && "404".equals(status_code.toString())) redirect = errorPage404;
	  	      if(status_code != null && !"404".equals(status_code.toString())) email500Error = true;
	  	      Object message = req.getAttribute("javax.servlet.error.message");
	  	      Object error_type = req.getAttribute("javax.servlet.error.exception_type");
	  	      Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
	  	      Object request_uri = req.getAttribute("javax.servlet.error.request_uri");
	  	      Object request_qs = req.getAttribute("javax.servlet.forward.query_string");
	  	      uri = request_uri+(request_qs == null ? "" : "?"+request_qs);
	  	      
	  	      String fullStackTrace = null;
	  	      
	  	      if (throwable != null) {
	  	    	  fullStackTrace = ExceptionUtils.getFullStackTrace(throwable);
	  	      }
	  	      
	  	      if (status_code != null && "404".equals(status_code.toString())) {
	  	    	  if (fullStackTrace != null && fullStackTrace.indexOf("JSPG0036E") > 0) {
	  	    		System.out.println(request_uri+(request_qs == null ? "" : "?"+request_qs )+"\n"+fullStackTrace);  
	  	    	  }
	  	      }
	  	      
	  	      msg.append("<HTML><BODY>\n");
	  	      msg.append("<style type=\"text/css\">");
	  		  msg.append("<!--");
	  		  msg.append("body { ");
	  		  msg.append(" 	margin: 0;");
	  		  msg.append("	padding: 0;");
	  		  msg.append("	font: normal 13px tahoma, Arial, Helvetica, sans-serif;");
	  		  msg.append("	color: #000000;");
	  		  msg.append("	text-align: left;");
	  		  msg.append("}");
	  		  msg.append("-->");
	  		  msg.append("</style>");
	  	      msg.append("<B>Status code:</B> " + status_code+"\n");
	  	      if (message != null) {
	  	    	  msg.append("<BR><B>Message</B>: " + org.apache.commons.lang.StringEscapeUtils.unescapeHtml(message.toString())+"\n");
	  	      } else {
	  	    	  msg.append("<BR><B>Message</B>: null\n");
	  	      }
	  	      msg.append("<BR><B>Error type</B>: " + error_type+"\n");
	  	      msg.append("<BR><B>Request URI</B>: " + request_uri+(request_qs == null ? "" : "?"+request_qs )+"\n");
	  	      msg.append("<BR><B>Box Name</B>: " + boxName+"\n");
	  	      msg.append("<HR><PRE>\n");
	  	      if (fullStackTrace != null) {
	  	    	  msg.append(fullStackTrace+"\n");
	  	      }
	  	      msg.append("</PRE>\n");
	  	      msg.append("<HR><PRE>\n");
	  			  java.util.Enumeration e = req.getHeaderNames();
               //request headers
	  	      if (e != null && e.hasMoreElements()) {
	  	          while (e.hasMoreElements()) {
	  	            String name = (String)e.nextElement();
	  	            msg.append(" " + name + ": " + req.getHeader(name)+"\n");
	  	          }
	  	      }
	  	    //session data
			e=req.getSession().getAttributeNames();
			if (e != null && e.hasMoreElements()) {
				while (e.hasMoreElements()) {
			        String name = (String)e.nextElement();
			        Object obj = req.getSession().getAttribute(name);
			        msg.append(" " + name + ": " + obj+"\n");
			        //System.out.println("-----------------"+obj.getClass().getName());
			        if(obj instanceof com.mastercareusa.selector.GenericVehicle){
			        	com.mastercareusa.selector.GenericVehicle item = (com.mastercareusa.selector.GenericVehicle)obj;
			        	msg.append(" \t\tacesId: " + item.getAcesId()+"\n");
			        	msg.append(" \t\tyear: " + item.getYear()+"\n");
			        	msg.append(" \t\tmake: " + item.getMake()+"\n");
			        	msg.append(" \t\tmodel: " + item.getModel()+"\n");
			        	msg.append(" \t\tsubmodel: " + item.getSubmodel()+"\n");
			        	msg.append(" \t\tisTpms: " + item.isTpms()+"\n");
			        }else if("com.bfrc.pojo.store.Store".equalsIgnoreCase(obj.getClass().getName())){
			        	com.bfrc.pojo.store.Store item = (com.bfrc.pojo.store.Store)obj;
			        	msg.append(" \t\tphone: " + item.getPhone()+"\n");
			        	msg.append(" \t\tstore number: " + item.getNumber()+"\n");
			        	msg.append(" \t\tstore name: " + item.getStoreName()+"\n");
			        	msg.append(" \t\tstore zip: " + item.getZip()+"\n");
			        }else if("com.bfrc.UserSessionData".equalsIgnoreCase(obj.getClass().getName())){
	                    msg.append(StringUtils.dumpBeanInfo(obj));
		            }
			    }
			}
	  	      msg.append(" User IP: "+req.getRemoteAddr()+"\n");
	  	      msg.append("</PRE>\n");
	  	      msg.append("</BODY></HTML>\n");
	  	      //send email only needed
	  	      String[] tos = null;
	  	      if(email500Error || email404Error){
	  			  Mail mail = new Mail();
	  			  List to = contactDAO.getToAddresses(new Integer(feedbackID));
	  			  if( to != null && to.size() > 0 ){
	  				tos = listToArray(to);
	  			    mail.setTo(tos);
	  			  }
	  			  else{ //always set the To
	  				tos = new String[]{"marketingitsupport@bfrc.com"};
	  			    mail.setTo(tos);
	  			    
	  			  }
	  			  List cc = contactDAO.getCCAddresses(new Integer(feedbackID));
	  			  if( cc != null && cc.size() > 0 )
	  			    mail.setCc(listToArray(cc));
	  			  List bcc = contactDAO.getCCAddresses(new Integer(feedbackID));
	  			  if( bcc != null && bcc.size() > 0 )
	  			    mail.setBcc(listToArray(bcc));
	  			  String from = contactDAO.getFrom();
	  			  String subject = "BFRC Web Site Error - "+contactDAO.getUrl();
	  			  mail.setFrom(from);
	  			  if(printEmail){
	  				  System.out.println("From: "+contactDAO.getFrom());
	  				  System.out.println("To: "+tos[0]);
	  				  System.out.println("Subject: "+subject);
	  			  }
	  		      //check if need send out message
	  		      if(status_code != null && (("404".equals(status_code.toString()) && email404Error) ||  (!"404".equals(status_code.toString()) && email500Error) ))
	  		      {
	  			       
	  			    	               
	  			          mail.setSubject(subject);
	  			          mail.setHtml(true);
	  			          mail.setBody(org.apache.commons.lang.StringEscapeUtils.unescapeHtml(msg.toString()));
	  			          if(printEmail){
	  			              System.out.println(msg);
	  			          }
	  			          
	  			          mailManager.sendMail(mail);
	  			      
	  		      }
	  	      }
	  	      if(debuginfo){
	        	res.getWriter().println(msg);
	          }
	  	      
	        } catch (Throwable ex) {//catch all Throwables to avoid infinite loop   
	  	         ex.printStackTrace();
	  	  }
			if(!debuginfo){
			    try{
			    	if(!StringUtils.isNullOrEmpty(uri) && 
			    			(uri.equalsIgnoreCase("/repair/feedback/getContact.action") 
			    					|| uri.equalsIgnoreCase("/tire_pricing/feedback/getContact.action")) ){
				    	java.util.Enumeration e = req.getHeaderNames();
				    	StringBuffer headerInfo = new StringBuffer();
				    	headerInfo.append("Debug info for invalid getContact.action URLs.\n");
				    	//request headers
						if (e != null && e.hasMoreElements()) {
						    while (e.hasMoreElements()) {
						      String name = (String)e.nextElement();
						      headerInfo.append(" " + name + ": " + req.getHeader(name)+"\n");
						    }
						    headerInfo.append(" User IP: "+req.getRemoteAddr()+"\n");
						    logger.info(headerInfo);
						}						
			    	}
			    	res.sendRedirect(redirect+"?uri="+uri);
			        return;
			    } catch (Throwable ex) {//catch all Throwables to avoid infinite loop   
			         ex.printStackTrace();
			    } 
			}
    	
    }
    public void service(HttpServletRequest req,
                    HttpServletResponse res)
      throws ServletException, java.io.IOException {
    	processErrors(req,res);
   }
    
}
