package bsro.dynamodb.mapper.battery.year;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="BatteryYear")
public class Battery {
	
	private Integer year;
	
	@DynamoDBHashKey(attributeName="ModelYear")  
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	} 
}
