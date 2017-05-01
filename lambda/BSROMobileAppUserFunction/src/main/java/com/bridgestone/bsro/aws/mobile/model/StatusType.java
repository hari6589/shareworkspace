package com.bridgestone.bsro.aws.mobile.model;

public enum StatusType {
	
	ACTIVE("A"),
	LOCKED("L"),
	INACTIVE("I");
	
	private String value;
	
	private StatusType(String s) {
		value = s;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return value;
	}
	
	public static StatusType getEnum(String s) {
		StatusType[] statuses = StatusType.values();
        for (StatusType status : statuses) {
            if (status.getValue().equals(s)) {
                return status;
            }
        }
        return ACTIVE;
	}

}