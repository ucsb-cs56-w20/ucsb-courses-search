package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class Course {
    public String quarter;
    public String courseId;
    public String title;
    public List<Section> classSections;

    public Course() {
    }

    /**
     * Provided that there is at least one section in the list of sections, and that
     * the first section has at least one instructor in the list of instructors,
     * return a list of all of the instructors for the first section, separated by a
     * comma and a single space between each name.  Otherwise, returns an empty string.
     * 
     * @return instructor(s) of first section
     */
    public String mainInstructorList() {
        if (classSections == null)
            return "";
        if (classSections.size() == 0)
            return "";
        Section firstSection = classSections.get(0);
        if (firstSection.instructors == null)
            return "";
        if (firstSection.instructors.size() == 0)
            return "";

        List<Instructor> instructors = firstSection.instructors;
        List<String> instructorNames = instructors.stream().map((i) -> i.instructor).collect(Collectors.toList());
        String instructorsCommaSeparated = String.join(", ", instructorNames);
        return instructorsCommaSeparated;
    }
}
