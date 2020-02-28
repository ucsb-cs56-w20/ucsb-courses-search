package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByGE {

    private String college;
    private String area;
    private String quarter;

    public SearchByGE() {}

    public SearchByGE(String college, String area, String quarter) {
        this.college = college;
        this.area = area;
        this.quarter = quarter;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByGE)) {
            return false;
        }
        SearchByGE searchByGE = (SearchByGE) o;
        return Objects.equals(college, searchByGE.college) && Objects.equals(area, searchByGE.area) && Objects.equals(quarter, searchByGE.quarter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, area, quarter);
    }

    @Override
    public String toString() {
        return "{" +
            " college='" + getCollege() + "'" +
            ", area='" + getArea() + "'" +
            ", quarter='" + getQuarter() + "'" +
            "}";
    }

}
