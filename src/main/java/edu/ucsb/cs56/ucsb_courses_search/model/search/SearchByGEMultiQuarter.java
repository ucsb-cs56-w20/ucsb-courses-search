package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByGEMultiQuarter {
    private String college;
    private String area;
    private String year;

    public SearchByGEMultiQuarter() { } 

    public SearchByGEMultiQuarter(String college, String area, String year) {
        this.college = college;
        this.area = area;
        this.year = year;
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

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByGEMultiQuarter)) {
            return false;
        }
        SearchByGEMultiQuarter SearchByGEMultiQuarter = (SearchByGEMultiQuarter) o;
        return Objects.equals(college, SearchByGEMultiQuarter.college) && 
                              Objects.equals(area, SearchByGEMultiQuarter.area) && 
                              Objects.equals(year, SearchByGEMultiQuarter.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, area, year);
    }

    @Override
    public String toString() {
        return "{" +
            " college='" + getCollege() + "'" +
            ", area='" + getArea() + "'" +
            ", quarter='" + getYear() + "'" +
            "}";
    }

}
