package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByClassroom {

    private String quarter;
    private String classroom;
    private String department;

    public SearchByClassroom() {}

    public SearchByClassroom( String quarter, String classroom, String department) {
        this.quarter = quarter;
        this.classroom = classroom;
        this.department = department;
    }

    public String getQuarter() {
        return this.quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getClassroom() {
        return this.classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

  
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByClassroom)) {
            return false;
        }
     SearchByClassroom searchByClassroom =  (SearchByClassroom) o;
        return Objects.equals(classroom, searchByClassroom.classroom) && Objects.equals(quarter, searchByClassroom.quarter);
    }

    @Override
    public int hashCode() {
        return Objects.hash( quarter, classroom);
    }

    @Override
    public String toString() {
        return "{" +
            ", quarter='" + getQuarter() + "'" +
            ", classroom='" + getClassroom() + "'" +
            "}";
    }

   
  

} 