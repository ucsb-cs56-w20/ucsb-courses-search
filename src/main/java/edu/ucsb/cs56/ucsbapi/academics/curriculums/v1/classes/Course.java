package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;

import lombok.Data;

@Data
public class Course {
    private String quarter;
    private String courseId;
    private String title;
    private List<Section> classSections;

    public Course () {}
}
