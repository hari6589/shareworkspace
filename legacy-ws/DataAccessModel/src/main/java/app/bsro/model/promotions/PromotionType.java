package app.bsro.model.promotions;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author smoorthy
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class PromotionType {
	
	private String name;
	private String type;	
	private String title;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "PromotionType [name=" + name + ", type=" + type + ", title="
				+ title + "]";
	}	
}
