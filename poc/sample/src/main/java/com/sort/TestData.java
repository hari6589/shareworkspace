package com.sort;

public class TestData implements Comparable<TestData> {
	
	private int id;
	private String name;
	private int age;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public int compareTo(TestData o) {
		int age = ((TestData) o).getAge();

		//ascending order
		//return this.age - age;
		
		//descending order
		return age - this.age;
	}
	
}
