package edu.ucsb.cs56.ucsb_courses_search.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a course in the ucsb-courses-search MongoDB archive.
 * <br>
 * TODO: In the future, this class should be removed in favor of
 *  {@link edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course}. Because the MongoDB database simply stores
 *  snapshots of the UCSB courses API, both classes should have the same structure. Unfortunately, simply configuring
 *  {@link edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository} to use the existing Course class
 *  currently causes an NPE, likely due to something in
 *  {@link edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Section} being null. Oh well.
 *      - with love, Bryan Terce
 */
@Document(collection = "courses")
public class ArchivedCourse {
    private String courseId;
    private String quarter;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
