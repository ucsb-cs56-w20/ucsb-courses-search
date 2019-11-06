package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;

import lombok.Data;

@Data
public class Section {
    /** a unique number assigned to a section */
    public String enrollCode;
    /** section number of the course */
    public String section;
    /** session only for summer quarter */
    public String session;
    /** if the class is closed */
    public String classClosed;
    /** is course cancelled */
    public String courseCancelled;
    /**
     * Grading Options Code like Pass/No Pass (P/NP) Or Letter Grades (L).
     * 
     * @see <a href=
     *      "https://developer.ucsb.edu/content/student-record-code-lookups">
     *      https://developer.ucsb.edu/content/student-record-code-lookups</a>
     * 
     */
    public String gradingOptionCode;

    /** total number of enrollments in the course */
    public int enrolledTotal;
    /** max number of students can be enrolled in the section */
    public int maxEnroll;

    /** Secondary Status of the course */
    String secondaryStatus;

    /** Is department approval required for enrollment in the section */
    public boolean departmentApprovalRequired;

    /** Is instructor approval required for enrollment in the section */
    public boolean instructorApprovalRequired;

    /** Is there restriction on the level of the course */
    public String restrictionLevel;

    /** Is there restriction on the major of the student */
    public String restrictionMajor;

    /** Is there restriction on the major and pass time of the student */
    public String restrictionMajorPass;

    /** Is there restriction on the minor of the student */
    public String restrictionMinor;

    /** Is there restriction on the minor and pass time of the student */
    public String restrictionMinorPass;

    /** Concurrent courses for the section */
    public List<String> concurrentCourses;

    /**
     * List of {@link TimeLocation} objects for this course 
     */
    public List<TimeLocation> timeLocations;
    /**
     * List of {@link Instructor} objects for this course
     */
    public List<Instructor> instructors;

    public Section() {
    }
}