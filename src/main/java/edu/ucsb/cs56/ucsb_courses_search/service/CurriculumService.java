package edu.ucsb.cs56.ucsb_courses_search.service;

public interface CurriculumService {
    public String getJSON(String subjectArea, String quarter, String courseLevel);
    public String getJSON(String instructor, String quarter);
    public String getJSON(String quarter);
    public String getGE(String college, String area,String quarter);
    public String getGE(String college, String area,String quarter, int startT);
    public String getCourse(String course, int quarter);
<<<<<<< HEAD
    public String getCSV(String subjectArea, String quarter, String courseLevel, String dept, String instructor, String course, String college, String areas);
=======
    public String getFinalExam(String quarter, String enrollCode);
>>>>>>> ba8cc2d9bf3d7c0cd9f46e366d3f8b371e12e3a5
}