/*
 * Created on Nov 6, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.bfrc.pojo.contact;

/**
 * @author jfang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mail {
	private String[] to = new String[0];
	private String[] cc = new String[0];
	private String[] bcc = new String[0];
	private String replyTo;
	private String from;
	private String subject;
	private String body;
	private boolean html = false;
	private Attachment[] attachments;
	
	public void addAttachment(Attachment a) {
		if(attachments == null) {
			attachments = new Attachment[1];
			attachments[0] = a;
			return;
		}
		Attachment[] temp = attachments;
		int len = temp.length;
		attachments = new Attachment[len + 1];
		for(int i=0; i<len; i++)
			attachments[i] = temp[i];
		attachments[len] = a;
	}

	public Attachment[] getAttachments() {
		return attachments;
	}
	public void setAttachments(Attachment[] attachments) {
		this.attachments = attachments;
	}
	public boolean isHtml() {
		return this.html;
	}
	public void setHtml(boolean html) {
		this.html = html;
	}
	/**
	 * @return Returns the bcc.
	 */
	public String[] getBcc() {
		return this.bcc;
	}
	/**
	 * @param bcc The bcc to set.
	 */
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return Returns the body.
	 */
	public String getBody() {
		return this.body;
	}
	/**
	 * @param body The body to set.
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return Returns the cc.
	 */
	public String[] getCc() {
		return this.cc;
	}
	/**
	 * @param cc The cc to set.
	 */
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return this.from;
	}
	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return Returns the replyTo.
	 */
	public String getReplyTo() {
		return this.replyTo;
	}
	/**
	 * @param replyTo The replyTo to set.
	 */
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return this.subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the to.
	 */
	public String[] getTo() {
		return this.to;
	}
	/**
	 * @param to The to to set.
	 */
	public void setTo(String[] to) {
		this.to = to;
	}
}
