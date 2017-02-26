package bsro.dynamodb.mapper.battery.engine;

import java.math.BigDecimal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="BatteryLifeDuration")
public class BatteryLife {
	
	private String batteryLifeZip;
	private long batteryLifeRegionId;
    private String batteryLifeRegionName;
    private BigDecimal batteryLifeAverage;
    
	@DynamoDBHashKey(attributeName="BatteryLifeZip3")  
	public String getBatteryLifeZip() {
		return batteryLifeZip;
	}
	public void setBatteryLifeZip(String batteryLifeZip) {
		this.batteryLifeZip = batteryLifeZip;
	}
	
	@DynamoDBAttribute(attributeName="BatteryLifeRegionId") 
	public long getBatteryLifeRegionId() {
		return batteryLifeRegionId;
	}
	public void setBatteryLifeRegionId(long batteryLifeRegionId) {
		this.batteryLifeRegionId = batteryLifeRegionId;
	}
	
	@DynamoDBAttribute(attributeName="BatteryLifeRegionName") 
	public String getBatteryLifeRegionName() {
		return batteryLifeRegionName;
	}
	public void setBatteryLifeRegionName(String batteryLifeRegionName) {
		this.batteryLifeRegionName = batteryLifeRegionName;
	}
	
	@DynamoDBAttribute(attributeName="BatteryLifeAverage") 
	public BigDecimal getBatteryLifeAverage() {
		return batteryLifeAverage;
	}
	public void setBatteryLifeAverage(BigDecimal batteryLifeAverage) {
		this.batteryLifeAverage = batteryLifeAverage;
	}
	
	
}
