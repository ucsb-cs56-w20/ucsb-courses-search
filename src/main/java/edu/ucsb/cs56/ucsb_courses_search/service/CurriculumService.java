package edu.ucsb.cs56.ucsb_courses_search.service;

public interface CurriculumService {
    public String getJSON(String subjectArea, String quarter, String courseLevel);
    public String getJSON(String instructor, String quarter);
    public String getJSON(String quarter);
    public String getGE(String college, String area,String quarter);
    public String getGE(String college, String area,String quarter, int startT);
    public String getCourse(String course, int quarter);
    public String getFinalExam(String quarter, String enrollCode);
}