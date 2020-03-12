package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchGEByDays {

    private String college;
    private String area;
    private String quarter;
    private String days;

    public SearchGEByDays() {}

    public SearchGEByDays(String college, String area, String quarter, String days) {
        this.college = college;
        this.area = area;
        this.quarter = quarter;
        this.days = days;
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

    public String getDays(){
        return this.days;
    }
    public void setDays(String days){
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchGEByDays)) {
            return false;
        }
        SearchGEByDays searchGEByDays = (SearchGEByDays) o;
            return Objects.equals(college, searchGEByDays.college) && Objects.equals(area, searchGEByDays.area) && Objects.equals(quarter, searchGEByDays.quarter) && Objects.equals(days, searchGEByDays.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, area, quarter, days);
    }

    @Override
    public String toString() {
        return "{" +
            " college='" + getCollege() + "'" +
            ", area='" + getArea() + "'" +
            ", quarter='" + getQuarter() + "'" +
            ", days='" + getDays() + "'" +
            "}";
    }

}
