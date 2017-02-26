package app.bsro.model.scheduled.maintenance;

public class MaintenanceJobDetail implements Comparable<MaintenanceJobDetail> {

	private Integer articleNbr;
	private Double sequenceNbr;
	private String description;
	private String type;
	private Integer quantity;
	private Double price;
	private Double itemPrice;
	
	public MaintenanceJobDetail(){}
	
	public MaintenanceJobDetail(Integer articleNbr, Double sequenceNbr,
			String description, String type, Integer quantity, Double price) {
		super();
		this.articleNbr = articleNbr;
		this.sequenceNbr = sequenceNbr;
		this.description = description;
		this.type = type;
		this.quantity = quantity;
		this.price = new Double(price.doubleValue() * quantity);
		this.itemPrice = price;
	}
	public Integer getArticleNbr() {
		return articleNbr;
	}
	public void setArticleNbr(Integer articleNbr) {
		this.articleNbr = articleNbr;
	}
	public Double getSequenceNbr() {
		return sequenceNbr;
	}
	public void setSequenceNbr(Double sequenceNbr) {
		this.sequenceNbr = sequenceNbr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Double getItemPrice() {
		return itemPrice;
	}
	public int compareTo(MaintenanceJobDetail o) {
		if(o == null || o.getSequenceNbr() == null || this.getSequenceNbr() == null)
			return 0;
		else return this.getSequenceNbr().compareTo(o.getSequenceNbr());
	}
}
