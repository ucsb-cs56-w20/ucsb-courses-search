package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByCourse {

    public SearchByCourse() {}

    private int beginQ;
    private int endQ;
    private String course;

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

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "{" +
            " beginQ='" + beginQ + "'" +
            ", endQ='" + endQ + "'" +
            ", course='" + course + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByCourse)) {
            return false;
        }
        SearchByCourse searchByCourse = (SearchByCourse) o;
        return beginQ == searchByCourse.beginQ && endQ == searchByCourse.endQ && Objects.equals(course, searchByCourse.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginQ, endQ, course);
    }
   
} 