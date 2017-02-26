package bsro.dynamodb.mapper.battery.engine;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="BatterySearch")
public class BatterySearch {
	
	private Integer automobileId;
	private Integer year;
	private String make;
	private String model;
	private String engine;
	private String modelSearch;
	private String engineSearch;
	private String searchResult;
	private String productCode;
	private String productLine;
	private String cca;
	private String productOption;
	
	
	@DynamoDBHashKey(attributeName="AutomobileId")  
	public Integer getAutomobileId() {
		return automobileId;
	}
	public void setAutomobileId(Integer automobileId) {
		this.automobileId = automobileId;
	}
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "MakeSearch-index", attributeName="ModelYear") 
    public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "MakeSearch-index", attributeName="Make")
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}

	@DynamoDBIndexHashKey(globalSecondaryIndexName = "ModelSearch-index", attributeName="ModelSearch")
	public String getModelSearch() {
		return modelSearch;
	}
	public void setModelSearch(String modelSearch) {
		this.modelSearch = modelSearch;
	}
	
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "ModelSearch-index", attributeName="Model")
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "EngineSearch-index", attributeName="EngineSearch")
	public String getEngineSearch() {
		return engineSearch;
	}
	public void setEngineSearch(String engineSearch) {
		this.engineSearch = engineSearch;
	}
	
	@DynamoDBIndexRangeKey(globalSecondaryIndexName = "EngineSearch-index", attributeName="Engine")
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	
	@DynamoDBIndexHashKey(globalSecondaryIndexName = "SearchResult-index", attributeName="SearchResult")
	public String getSearchResult() {
		return searchResult;
	}
	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}

	@DynamoDBAttribute(attributeName="ProductCode")
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	@DynamoDBAttribute(attributeName="ProductLine")
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	
	@DynamoDBAttribute(attributeName="CCA")
	public String getCca() {
		return cca;
	}
	public void setCca(String cca) {
		this.cca = cca;
	}
	
	@DynamoDBAttribute(attributeName="ProductOption")
	public String getProductOption() {
		return productOption;
	}
	public void setProductOption(String productOption) {
		this.productOption = productOption;
	}
}
