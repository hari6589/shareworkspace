package com.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSort {

	public static void main(String[] args) {
		
		List<TestData> dataList = new ArrayList<TestData>();  
		
		TestData data = new TestData();
		
		data.setId(1);
		data.setName("Vikky");
		data.setAge(22);
		dataList.add(data);
		
		data = new TestData();
		data.setId(2);
		data.setName("Ari");
		data.setAge(26);
		dataList.add(data);
		
		data = new TestData();
		data.setId(3);
		data.setName("Gopi");
		data.setAge(24);
		dataList.add(data);
		
		System.out.println("Before: ");
		for(int i=0; i<dataList.size(); i++) {
			TestData dat = dataList.get(i);
			System.out.println(i + " : " + dat.getName());
		}
		
		Collections.sort(dataList);
		
		System.out.println("After: ");
		for(int i=0; i<dataList.size(); i++) {
			TestData dat = dataList.get(i);
			System.out.println(dat.getName());
		}

	}

}
