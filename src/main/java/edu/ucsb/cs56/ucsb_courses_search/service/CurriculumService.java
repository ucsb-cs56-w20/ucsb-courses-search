package edu.ucsb.cs56.ucsb_courses_search.service;

public interface CurriculumService {
    public String getJSON(String subjectArea, String quarter, String courseLevel);
    public String getJSON(String instructor, String quarter);
    public String getJSON(String quarter);
    public String getGE(String college, String area,String quarter);
    public String getCourse(String course, int quarter);
    public String getCSV(String subjectArea, String quarter, String courseLevel, String dept, String instructor, String course, String college, String areas);
}