package com.sample.sitescrap.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Item {

	private String title;
	private String price;
	private String url;

	public Item(String title, String price, String url) {
		super();
		this.title = title;
		this.price = price;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonIgnore
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
