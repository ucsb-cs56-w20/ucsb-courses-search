package edu.ucsb.cs56.ucsb_courses_search;

public interface CurriculumService {
    public String getJSON(String subjectArea, String quarter, String courseLevel);
    public String getJSON(String instructor, String quarter);
    public String getCourse(String course, int quarter);
}