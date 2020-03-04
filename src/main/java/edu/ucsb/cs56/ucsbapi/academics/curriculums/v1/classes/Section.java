package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.stream.Collectors;

public class Section {

    public String meetTime() {
        if (this.timeLocations.size() < 1) {
            return "";
        }
        
        TimeLocation tl0 = timeLocations.get(0);

        return tl0.beginTime + "-" + tl0.endTime;
    }

    public String meetDay() {
        if (this.timeLocations.size() < 1) {
            return "";
        }
        return timeLocations.get(0).days;
    }

    public String location() {
        if (this.timeLocations.size() < 1) {
            return "";
        }
        
        TimeLocation tl0 = timeLocations.get(0);

        return tl0.building + "-" + tl0.room;
    }

    /** a unique number assigned to a section */
    public String getEnrollCode() {
        return this.enrollCode;
    }

    /** a unique number assigned to a section */
    public void setEnrollCode(String enrollCode) {
        this.enrollCode = enrollCode;
    }

    /** section number of the course */
    public String getSection() {
        return this.section;
    }

    /** section number of the course */
    public void setSection(String section) {
        this.section = section;
    }

    /** session only for summer quarter */
    public String getSession() {
        return this.session;
    }

    /** session only for summer quarter */
    public void setSession(String session) {
        this.session = session;
    }

    public String getClassClosed() {
        return this.classClosed;
    }

    public void setClassClosed(String classClosed) {
        this.classClosed = classClosed;
    }

    public String getCourseCancelled() {
        return this.courseCancelled;
    }

    public void setCourseCancelled(String courseCancelled) {
        this.courseCancelled = courseCancelled;
    }

    public String getGradingOptionCode() {
        return this.gradingOptionCode;
    }

    public void setGradingOptionCode(String gradingOptionCode) {
        this.gradingOptionCode = gradingOptionCode;
    }

    public int getEnrolledTotal() {
        return this.enrolledTotal;
    }

    public void setEnrolledTotal(int enrolledTotal) {
        this.enrolledTotal = enrolledTotal;
    }

    public int getMaxEnroll() {
        return this.maxEnroll;
    }

    public void setMaxEnroll(int maxEnroll) {
        this.maxEnroll = maxEnroll;
    }

    public String getSecondaryStatus() {
        return this.secondaryStatus;
    }

    public void setSecondaryStatus(String secondaryStatus) {
        this.secondaryStatus = secondaryStatus;
    }

    public boolean isDepartmentApprovalRequired() {
        return this.departmentApprovalRequired;
    }

    public boolean getDepartmentApprovalRequired() {
        return this.departmentApprovalRequired;
    }

    public void setDepartmentApprovalRequired(boolean departmentApprovalRequired) {
        this.departmentApprovalRequired = departmentApprovalRequired;
    }

    public boolean isInstructorApprovalRequired() {
        return this.instructorApprovalRequired;
    }

    public boolean getInstructorApprovalRequired() {
        return this.instructorApprovalRequired;
    }

    public void setInstructorApprovalRequired(boolean instructorApprovalRequired) {
        this.instructorApprovalRequired = instructorApprovalRequired;
    }

    public String getRestrictionLevel() {
        return this.restrictionLevel;
    }

    public void setRestrictionLevel(String restrictionLevel) {
        this.restrictionLevel = restrictionLevel;
    }

    public String getRestrictionMajor() {
        return this.restrictionMajor;
    }

    public void setRestrictionMajor(String restrictionMajor) {
        this.restrictionMajor = restrictionMajor;
    }

    public String getRestrictionMajorPass() {
        return this.restrictionMajorPass;
    }

    public void setRestrictionMajorPass(String restrictionMajorPass) {
        this.restrictionMajorPass = restrictionMajorPass;
    }

    public String getRestrictionMinor() {
        return this.restrictionMinor;
    }

    public void setRestrictionMinor(String restrictionMinor) {
        this.restrictionMinor = restrictionMinor;
    }

    public String getRestrictionMinorPass() {
        return this.restrictionMinorPass;
    }

    public void setRestrictionMinorPass(String restrictionMinorPass) {
        this.restrictionMinorPass = restrictionMinorPass;
    }

    public List<String> getConcurrentCourses() {
        return this.concurrentCourses;
    }

    public void setConcurrentCourses(List<String> concurrentCourses) {
        this.concurrentCourses = concurrentCourses;
    }

    public List<TimeLocation> getTimeLocations() {
        return this.timeLocations;
    }

    public void setTimeLocations(List<TimeLocation> timeLocations) {
        this.timeLocations = timeLocations;
    }

    public List<Instructor> getInstructors() {
        return this.instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

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
    public String secondaryStatus;

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

    public boolean isSection(){
        return (Integer.parseInt(this.section) % 100 != 0);
    }


  /**
     * Return the name of the instructor(s) for the section. 
     * If there is more than one, they are separated by commas
     * and a single space.
     * 
     * @return Name of instructor(s) for the section.
     */
    public String instructorList() { 
        List<String> instructorNames = this.instructors.stream().map((i) -> i.instructor).collect(Collectors.toList());
        String instructorsCommaSeparated = String.join(", ", instructorNames);
        return instructorsCommaSeparated;
    }

    @Override
    public String toString() {
        return "{" +
            " enrollCode='" + getEnrollCode() + "'" +
            ", section='" + getSection() + "'" +
            ", session='" + getSession() + "'" +
            ", classClosed='" + getClassClosed() + "'" +
            ", courseCancelled='" + getCourseCancelled() + "'" +
            ", gradingOptionCode='" + getGradingOptionCode() + "'" +
            ", enrolledTotal='" + getEnrolledTotal() + "'" +
            ", maxEnroll='" + getMaxEnroll() + "'" +
            ", secondaryStatus='" + getSecondaryStatus() + "'" +
            ", departmentApprovalRequired='" + isDepartmentApprovalRequired() + "'" +
            ", instructorApprovalRequired='" + isInstructorApprovalRequired() + "'" +
            ", restrictionLevel='" + getRestrictionLevel() + "'" +
            ", restrictionMajor='" + getRestrictionMajor() + "'" +
            ", restrictionMajorPass='" + getRestrictionMajorPass() + "'" +
            ", restrictionMinor='" + getRestrictionMinor() + "'" +
            ", restrictionMinorPass='" + getRestrictionMinorPass() + "'" +
            ", concurrentCourses='" + getConcurrentCourses() + "'" +
            ", timeLocations='" + getTimeLocations() + "'" +
            ", instructors='" + getInstructors() + "'" +
            "}";
    }
}