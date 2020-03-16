package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByClassroom {
    private String quarter;
    private String classroom;
    private String building;
    public SearchByClassroom() {}
    public SearchByClassroom( String quarter, String classroom, String building) {
        this.quarter = quarter;
        this.classroom = classroom;
        this.building = building;
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
    public String getBuilding() {
        return this.building;
    }
    public void setBuilding(String building) {
        this.building = building;
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