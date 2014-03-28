package com.example.vaadindemo.domain;

public class Person {
	
	private String firstName;
	private int yob;
	
	public Person (){}
	
	public Person(String firstName, int yob) {
		super();
		this.firstName = firstName;
		this.yob = yob;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public int getYob() {
		return yob;
	}
	public void setYob(int yob) {
		this.yob = yob;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", yob=" + yob + "]";
	}

}
