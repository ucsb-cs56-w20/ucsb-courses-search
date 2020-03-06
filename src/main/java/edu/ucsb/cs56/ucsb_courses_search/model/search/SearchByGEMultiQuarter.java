package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByGEMultiQuarter {
    private String college;
    private String area;
    private int beginQ;
    private int endQ;

    public SearchByGEMultiQuarter() { } 

    public SearchByGEMultiQuarter(String college, String area, String year, int beginQ, int endQ) {
        this.college = college;
        this.area = area;
        this.beginQ = beginQ;
        this.endQ = endQ;
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

    public int getBeginQ() {
        return this.beginQ;
    }

    public void setBeginQ(int beginQ) {
        this.beginQ = beginQ;
    }

    public int getEndQ() {
        return this.endQ;
    }

    public void setEndQ(int endQ) {
        this.endQ = endQ;
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
                              Objects.equals(area, SearchByGEMultiQuarter.area) && beginQ == SearchByGEMultiQuarter.beginQ && endQ == SearchByGEMultiQuarter.endQ ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(college, area, beginQ, endQ);
    }

    @Override
    public String toString() {
        return "{" +
            " college='" + getCollege() + "'" +
            ", area='" + getArea() + "'" +
            ", begin quarter='" + getBeginQ() + "'" +
            ", end quarter='" + getEndQ() + "'" +
            "}";
    }

}
