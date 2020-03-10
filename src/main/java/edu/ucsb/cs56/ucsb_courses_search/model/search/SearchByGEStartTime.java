package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByGEStartTime {

    private String college;
    private String area;
    private String quarter;
    private int startT;

    public SearchByGEStartTime() {}

    public SearchByGEStartTime(String college, String area, String quarter, int startT) {
        this.college = college;
        this.area = area;
        this.quarter = quarter;
        this.startT = startT;
    }

    public String getCollege() {
        return this.college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getQuarter() {
        return this.quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public int getStartT(){
        return this.startT;
    }
    public void setStartT(int startT){
        this.startT = startT;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByGEStartTime)) {
            return false;
        }
        SearchByGEStartTime searchByGEStartTime = (SearchByGEStartTime) o;
            return Objects.equals(college, searchByGEStartTime.college) && Objects.equals(area, searchByGEStartTime.area) && Objects.equals(quarter, searchByGEStartTime.quarter) && (this.startT == searchByGEStartTime.startT);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, area, quarter, startT);
    }

    @Override
    public String toString() {
        return "{" +
            " college='" + getCollege() + "'" +
            ", area='" + getArea() + "'" +
            ", quarter='" + getQuarter() + "'" +
            ", startT='" + getStartT() + "'" +
            "}";
    }

}
