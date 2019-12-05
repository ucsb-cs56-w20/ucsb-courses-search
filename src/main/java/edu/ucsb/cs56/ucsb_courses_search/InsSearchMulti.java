package edu.ucsb.cs56.ucsb_courses_search;

public class InsSearchMulti{
	private String instructor;
	private String beginQuarter;
	private String endQuarter;
	
	public InsSearchMulti(){
		instructor = "";
		beginQuarter = "";
		endQuarter = "";
	}

	public void setInstructor(String s){
		this.instructor = s;
	}

	public void setBeginQuarter(String q){
		this.beginQuarter = q;
	}

	public void setEndQuarter(String q){
		this.endQuarter = q;
	}

	public String getInstructor(){
		return this.instructor;
	}

	public String getBeginQuarter(){
		return this.beginQuarter;
	}

	public String getEndQuarter(){
		return this.endQuarter;
	}	
}
