package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByDept {

    private String dept;
    private String quarter;
    private String courseLevel;

    public SearchByDept() {}

    public SearchByDept(String dept, String quarter, String courseLevel) {
        this.dept = dept;
        this.quarter = quarter;
        this.courseLevel = courseLevel;
    }

    public String getDept() {
        return this.dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
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

  
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByDept)) {
            return false;
        }
        SearchByDept searchByDept = (SearchByDept) o;
        return Objects.equals(dept, searchByDept.dept) && Objects.equals(quarter, searchByDept.quarter) && Objects.equals(courseLevel, searchByDept.courseLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dept, quarter, courseLevel);
    }

    @Override
    public String toString() {
        return "{" +
            " dept='" + getDept() + "'" +
            ", quarter='" + getQuarter() + "'" +
            ", courseLevel='" + getCourseLevel() + "'" +
            "}";
    }

   
  

} 