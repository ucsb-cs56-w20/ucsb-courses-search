package edu.ucsb.cs56.ucsb_courses_search.model.search;

import java.util.Objects;

public class SearchByInstructorMultiQuarter {

    public SearchByInstructorMultiQuarter() {}

    private int beginQ;
    private int endQ;
    private String instructor;


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
       /* //or message to say end must be later than 
        if(this.beginQ > this.endQ){
            throw new IllegalArgumentException();
        }*/
        this.endQ = endQ;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SearchByInstructorMultiQuarter)) {
            return false;
        }
        SearchByInstructorMultiQuarter searchByInstructorMultiQuarter = (SearchByInstructorMultiQuarter) o;
        return beginQ == searchByInstructorMultiQuarter.beginQ && endQ == searchByInstructorMultiQuarter.endQ && Objects.equals(instructor, searchByInstructorMultiQuarter.instructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginQ, endQ, instructor);
    }


    @Override
    public String toString() {
        return "{" +
            " beginQ='" + getBeginQ() + "'" +
            ", endQ='" + getEndQ() + "'" +
            ", instructor='" + getInstructor() + "'" +
            "}";
    }


}
