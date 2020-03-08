package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.stream.Collectors;

public class Section {

    public String meetTime() {
        if (this.timeLocations.size() < 1) {
            return "";
        }
        
        TimeLocation tl0 = timeLocations.get(0);

        return tl0.getBeginTime() + "-" + tl0.getEndTime();
    }

    public String meetDay() {
        if (this.timeLocations.size() < 1) {
            return "";
        }
        return timeLocations.get(0).getDays();
    }

    public String location() {
        if (this.timeLocations.size() < 1) {
            return "";
        }
        
        TimeLocation tl0 = timeLocations.get(0);

        return tl0.getBuilding() + "-" + tl0.getRoom();
    }

    /** a unique number assigned to a section */
    public String getEnrollCode() {
        if (this.enrollCode != null){
            return this.enrollCode;
        }
        return "N/A";
    }

    /** a unique number assigned to a section */
    public void setEnrollCode(String enrollCode) {
        this.enrollCode = enrollCode;
    }

    /** section number of the course */
    public String getSection() {
        if (this.section != null){
            return this.section;
        }
        return "N/A";
    }

    /** section number of the course */
    public void setSection(String section) {
        this.section = section;
    }

    /** session only for summer quarter */
    public String getSession() {
        if (this.session != null){
            return this.session;
        }
        return "N/A";
    }

    /** session only for summer quarter */
    public void setSession(String session) {
        this.session = session;
    }

    public String getClassClosed() {
        if (this.classClosed != null){
            return this.classClosed;
        }
        return "Y";
    }

    public void setClassClosed(String classClosed) {
        this.classClosed = classClosed;
    }

    public String getCourseCancelled() {
        if (this.courseCancelled != null){
            return this.courseCancelled;
        }
        return "N/A";
    }

    public void setCourseCancelled(String courseCancelled) {
        this.courseCancelled = courseCancelled;
    }

    public String getGradingOptionCode() {
        if (this.gradingOptionCode != null){
            return this.gradingOptionCode;
        }
        return "N/A";
    }

    public void setGradingOptionCode(String gradingOptionCode) {
        this.gradingOptionCode = gradingOptionCode;
    }

    public int getEnrolledTotal() {
        if (this.enrolledTotal != null){
            return this.enrolledTotal;
        }
        return -1;
    }

    public void setEnrolledTotal(int enrolledTotal) {
        this.enrolledTotal = enrolledTotal;
    }

    public int getMaxEnroll() {
        if (this.maxEnroll != null){
            return this.maxEnroll;
        }
        return -1;
    }

    public void setMaxEnroll(int maxEnroll) {
        this.maxEnroll = maxEnroll;
    }

    public String getSecondaryStatus() {
        if (this.secondaryStatus != null){
            return this.secondaryStatus;
        }
        return "N/A";
    }

    public void setSecondaryStatus(String secondaryStatus) {
        this.secondaryStatus = secondaryStatus;
    }

    public boolean isDepartmentApprovalRequired() {
        if(this.departmentApprovalRequired){
            return true;
        }
        return false;    
    }

    public boolean getDepartmentApprovalRequired() {
        if(this.departmentApprovalRequired){
            return true;
        }
        return false;
    }

    public void setDepartmentApprovalRequired(boolean departmentApprovalRequired) {
        this.departmentApprovalRequired = departmentApprovalRequired;
    }

    public boolean isInstructorApprovalRequired() {
        if(this.instructorApprovalRequired){
            return true;
        }
        return false;
    }

    public boolean getInstructorApprovalRequired() {
        if(this.instructorApprovalRequired){
            return true;
        }
        return false;
    }

    public void setInstructorApprovalRequired(boolean instructorApprovalRequired) {
        this.instructorApprovalRequired = instructorApprovalRequired;
    }

    public String getRestrictionLevel() {
        if (this.restrictionLevel != null){
            return this.restrictionLevel;
        }
        return "N/A";
    }

    public void setRestrictionLevel(String restrictionLevel) {
        this.restrictionLevel = restrictionLevel;
    }

    public String getRestrictionMajor() {
        if (this.restrictionMajor != null){
            return this.restrictionMajor;
        }
        return "N/A";
    }

    public void setRestrictionMajor(String restrictionMajor) {
        this.restrictionMajor = restrictionMajor;
    }

    public String getRestrictionMajorPass() {
        if (this.restrictionMajorPass != null){
            return this.restrictionMajorPass;
        }
        return "N/A";
    }

    public void setRestrictionMajorPass(String restrictionMajorPass) {
        this.restrictionMajorPass = restrictionMajorPass;
    }

    public String getRestrictionMinor() {
        if (this.restrictionMinor != null){
            return this.restrictionMinor;
        }
        return "N/A";
    }

    public void setRestrictionMinor(String restrictionMinor) {
        this.restrictionMinor = restrictionMinor;
    }

    public String getRestrictionMinorPass() {
        if (this.restrictionMinorPass != null){
            return this.restrictionMinorPass;
        }
        return "N/A";
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
    private String enrollCode;
    /** section number of the course */
    private String section;
    /** session only for summer quarter */
    private String session;
    /** if the class is closed */
    private String classClosed;
    /** is course cancelled */
    private String courseCancelled;
    /**
     * Grading Options Code like Pass/No Pass (P/NP) Or Letter Grades (L).
     * 
     * @see <a href=
     *      "https://developer.ucsb.edu/content/student-record-code-lookups">
     *      https://developer.ucsb.edu/content/student-record-code-lookups</a>
     * 
     */
    private String gradingOptionCode;

    /** total number of enrollments in the course */
    private Integer enrolledTotal;
    /** max number of students can be enrolled in the section */
    private Integer maxEnroll;

    /** Secondary Status of the course */
    private String secondaryStatus;

    /** Is department approval required for enrollment in the section */
    private boolean departmentApprovalRequired;

    /** Is instructor approval required for enrollment in the section */
    private boolean instructorApprovalRequired;

    /** Is there restriction on the level of the course */
    private String restrictionLevel;

    /** Is there restriction on the major of the student */
    private String restrictionMajor;

    /** Is there restriction on the major and pass time of the student */
    private String restrictionMajorPass;

    /** Is there restriction on the minor of the student */
    private String restrictionMinor;

    /** Is there restriction on the minor and pass time of the student */
    private String restrictionMinorPass;

    /** Concurrent courses for the section */
    private List<String> concurrentCourses;

    /**
     * List of {@link TimeLocation} objects for this course 
     */
    private List<TimeLocation> timeLocations;
    /**
     * List of {@link Instructor} objects for this course
     */
    private List<Instructor> instructors;

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
        List<String> instructorNames = this.instructors.stream().map((i) -> i.getInstructor()).collect(Collectors.toList());
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