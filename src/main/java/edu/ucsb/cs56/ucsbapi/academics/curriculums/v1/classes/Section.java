package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;

import lombok.Data;

@Data
public class Section {
    private String enrollCode;
    private int enrolledTotal;
    private int maxEnroll;
    private List<TimeLocation> timeLocations;
    private List<Instructor> instructors;

    public Section () {}
}