package edu.ucsb.cs56.ucsb_courses_search.model.search;

public class InsSearch{
	private String instructor;
	private String quarter;
	
	public InsSearch(){
		instructor = "";
		quarter = "";
	}

	public void setInstructor(String s){
		this.instructor = s;
	}

	public void setQuarter(String q){
		this.quarter = q;
	}

	public String getInstructor(){
		return this.instructor;
	}

	public String getQuarter(){
		return this.quarter;
	}
}

