package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByGETwoAreas {

    public SearchByGETwoAreas() { }

    private String college;
    private String firstArea;
    private String secondArea;
    private String quarter;

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getFirstArea() {
        return this.firstArea;
    }

    public void setFirstArea(String firstArea) {
        this.firstArea = firstArea;
    }

    public String getSecondArea() {
        return this.secondArea;
    }

    public void setSecondArea(String secondArea) {
        this.secondArea = secondArea;
    }

    public String getQuarter() {
        return this.quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public SearchByGETwoAreas(String college, String firstArea, String secondArea, String quarter) {
        this.college = college;
        this.firstArea = firstArea;
        this.secondArea = secondArea;
        this.quarter = quarter;
    }

    public SearchByGETwoAreas college(String college) {
        this.college = college;
        return this;
    }

    public SearchByGETwoAreas firstArea(String firstArea) {
        this.firstArea = firstArea;
        return this;
    }

    public SearchByGETwoAreas secondArea(String secondArea) {
        this.secondArea = secondArea;
        return this;
    }

    public SearchByGETwoAreas quarter(String quarter) {
        this.quarter = quarter;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByGETwoAreas)) {
            return false;
        }
        SearchByGETwoAreas searchByGETwoAreas = (SearchByGETwoAreas) o;
        return Objects.equals(college, searchByGETwoAreas.college) && Objects.equals(firstArea, searchByGETwoAreas.firstArea) && Objects.equals(secondArea, searchByGETwoAreas.secondArea) && Objects.equals(quarter, searchByGETwoAreas.quarter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, firstArea, secondArea, quarter);
    }

    @Override
    public String toString() {
        return "{" +
            " college='" + getCollege() + "'" +
            ", firstArea='" + getFirstArea() + "'" +
            ", secondArea='" + getSecondArea() + "'" +
            ", quarter='" + getQuarter() + "'" +
            "}";
    }

}