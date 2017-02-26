package com.bfrc.pojo.contact;

import com.bfrc.*;

public class Attachment extends Base {
	public Attachment() {}
	public Attachment(String name, byte[] document) {
		this.name = name;
		this.document = document;
	}
	private String name;
	private byte[] document;

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
