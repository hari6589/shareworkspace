package com.sample.sitescrap;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sample.sitescrap.pojo.Item;

public class SiteScrap {

	public String getData() {
		String searchQuery = "Iphone 6s";
		String url = "https://newyork.craigslist.org/search/sss?sort=rel&query=";
		String jsonString = "";

		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			String searchUrl = url + URLEncoder.encode(searchQuery, "UTF-8");
			jsonString = "This is the scrapped data from the url: " + searchUrl + "\n";

			HtmlPage page = client.getPage(searchUrl);

			List<HtmlElement> items = page.getByXPath("//p[@class='result-info']");
			if (items.isEmpty()) {
				System.out.println("No items found !");
			} else {
				List<Item> itemList = new ArrayList<Item>();
				for (HtmlElement item : items) {
					HtmlAnchor itemAnchor = ((HtmlAnchor) item.getFirstByXPath(".//a"));

					String itemName = itemAnchor.asText();
					String itemUrl = itemAnchor.getHrefAttribute();
					HtmlElement spanPrice = ((HtmlElement) item.getFirstByXPath(".//span[@class='result-price']"));

					// It is possible that an item doesn't have any price
					String itemPrice = spanPrice == null ? "no price" : spanPrice.asText();

					Item it = new Item(itemName, String.valueOf(spanPrice), itemUrl);
					itemList.add(it);
					// System.out.println(String.format("Name : %s Url : %s
					// Price : %s", itemName, itemPrice, itemUrl));
				}
				ObjectMapper mapper = new ObjectMapper();
				jsonString = mapper.writeValueAsString(itemList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}
