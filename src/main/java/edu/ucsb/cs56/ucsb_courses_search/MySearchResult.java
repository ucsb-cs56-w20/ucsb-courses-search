package edu.ucsb.cs56.ucsb_courses_search;

public class MySearchResult  {

    public MySearchResult() {

    }

    private String subjectArea;
    private String quarter;
    private String courseLevel;
    private String instructor;

    public String getSubjectArea() {
        return this.subjectArea;
    }
    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public String getQuarter() {
        return this.quarter;
    }
    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getCourseLevel() {
        return this.courseLevel;
    }
    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    

} 