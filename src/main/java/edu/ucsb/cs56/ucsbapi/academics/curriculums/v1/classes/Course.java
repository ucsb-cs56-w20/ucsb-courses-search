package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Course {

    private Logger logger = LoggerFactory.getLogger(Course.class);


    private String quarter;
    private String courseId;
    private String title;
    private List<Section> classSections;

    public Course() {
    }

    public String getQuarter() {
        return this.quarter;
    }

    public String getQuarterNoSpace() {
        return this.quarter.replaceAll("\\s","");
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getCourseKey() {
        return this.courseId.replaceAll("\\s","") + this.quarter.replaceAll("\\s","");
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Section> getClassSections() {
        return this.classSections;
    }

    public void setClassSections(List<Section> classSections) {
        this.classSections = classSections;
    }

   
    /**
     * Return the name of the main instructor(s) for the course, i.e. the lecture
     * (primary) section. If there is more than one, they are separated by commas
     * and a single space.
     * 
     * @return Name of primary instructor(s) for the course
     */
    public String mainInstructorList() {

        // TODO: This code assumes that the first section on the list is the primary,
        // i.e.
        // the lecture section that lists the main instructor(s) of record for the
        // course.
        // But is that the case? Is there a more robust way to check?
        // There is an instructor type field that we could check for instructor of
        // record status,
        // for example, and we could also check the enroll code and the secondaryStatus
        // on the sections.

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

    /**
     * get quarter in Qyy format, e.g. "F19", "W20", etc. instead of "20194" or
     * "20201"
     * 
     * @return quarter in Qyy format (e.g. "F19")
     */

    public String getQuarterQyy() {
        try {
            Quarter q = new Quarter(this.quarter);
            return q.toString();
        } catch (Exception e) {
            String msg = String.format("Error converting quarter \"%s\": ", this.quarter);
            logger.error(msg,e);
            return "";
        }
    }
}
