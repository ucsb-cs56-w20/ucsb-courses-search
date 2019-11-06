package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;

import lombok.Data;

@Data
public class Course {
    public String quarter;
    public String courseId;
    public String title;
    public List<Section> classSections;

    public Course () {}
}
