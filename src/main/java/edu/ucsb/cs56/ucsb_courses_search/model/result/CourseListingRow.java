package edu.ucsb.cs56.ucsb_courses_search.model.result;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Section;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.TimeLocation;

public class CourseListingRow {

    public enum RowType {
        PRIMARY, SECONDARY, ADDITIONAL_TIME_LOCATION
    }

    private Course course;
    private RowType rowType;
    private Section section;
    private Section primary; //null if this is a primary
    private TimeLocation timeLocation;
    private boolean firstRow;
    private boolean lastRow;

    public boolean isFirstRow() {
        return this.firstRow;
    }

    @Override
    public String toString() {
        return "{" +
            " course='" + getCourse() + "'" +
            ", rowType='" + getRowType() + "'" +
            ", section='" + getSection() + "'" +
            ", timeLocation='" + getTimeLocation() + "'" +
            ", firstRow='" + isFirstRow() + "'" +
            ", lastRow='" + isLastRow() + "'" +
            "}";
    }

    
    public CourseListingRow(Course course, RowType rowType, Section section, Section primary, TimeLocation timeLocation,
            boolean firstRow, boolean lastRow) {
        this.course = course;
        this.rowType = rowType;
        this.section = section;
        this.primary = primary;
        this.timeLocation = timeLocation;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    /**
     * Convert a CoursePage object, which is the result of a single call to the JSON API,
     * into a list of CourseOffering objects.  The main purpose is to group the secondaries (sections)
     * with the appropriate primaries to make it easier to process, understand, and format the results.
     */ 
    public static List<CourseListingRow> fromCourseOfferings(List<CourseOffering> courseOfferings) {  
        ArrayList<CourseListingRow> result = new ArrayList<CourseListingRow>();
        for ( CourseOffering co : courseOfferings ) {
            
            // Add primary to result

            List<TimeLocation> timeLocations = co.getPrimary().getTimeLocations();
            TimeLocation primaryFirstTimeLocation = 
                timeLocations.size() == 0 ? null : timeLocations.get(0);

            CourseListingRow primary = new CourseListingRow(
                co.getCourse(),
                RowType.PRIMARY,
                co.getPrimary(),
                null,
                primaryFirstTimeLocation,
                true,
                co.getPrimary().getTimeLocations().size() <= 1 &&
                co.getSecondaries().size() == 0);
            result.add(primary);

            for (int i=1; i<co.getPrimary().getTimeLocations().size(); i++) {
                CourseListingRow timeLoc = new CourseListingRow(
                    co.getCourse(),
                    RowType.ADDITIONAL_TIME_LOCATION,
                    co.getPrimary(),
                    null,
                    co.getPrimary().getTimeLocations().get(i),
                    false,
                    (i == (co.getPrimary().getTimeLocations().size() - 1)) &&
                    co.getSecondaries().size() == 0);
                result.add(timeLoc);
            }
        
            List<Section> secondaries = co.getSecondaries();
            
            Section last = null;
            if (secondaries.size() > 0)
               last = secondaries.get(secondaries.size() - 1);

            for ( Section s: secondaries ) {

                List<TimeLocation> sectionTimeLocations = s.getTimeLocations();
                TimeLocation sectionFirstTimeLocation = 
                    sectionTimeLocations.size() == 0 ? null : sectionTimeLocations.get(0);
    

                CourseListingRow secondary = new CourseListingRow(
                    co.getCourse(),
                    RowType.SECONDARY,
                    s,
                    co.getPrimary(),
                    sectionFirstTimeLocation,
                    false,
                    s == last && co.getPrimary().getTimeLocations().size() <= 1 );

                result.add(secondary);

                for (int i=1; i<s.getTimeLocations().size(); i++) {
                    CourseListingRow timeLoc = new CourseListingRow(
                        co.getCourse(),
                        RowType.ADDITIONAL_TIME_LOCATION,
                        s,
                        co.getPrimary(),
                        s.getTimeLocations().get(i),
                        false,
                        s == last && 
                        (i == (co.getPrimary().getTimeLocations().size() - 1)));
                    result.add(timeLoc);
                }
            }      
        }
        return result;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Need to return ENUM as a String for Thymeleaf == and != against strings
    public String getRowType() {
        return this.rowType.toString();
    }

    public void setRowType(RowType rowType) {
        this.rowType = rowType;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public TimeLocation getTimeLocation() {
        return this.timeLocation;
    }

    public void setTimeLocation(TimeLocation timeLocation) {
        this.timeLocation = timeLocation;
    }

    public boolean isLastRow() {
        return this.lastRow;
    }

    public boolean getFirstRow() {
        return this.firstRow;
    }

    public void setFirstRow(boolean firstRow) {
        this.firstRow = firstRow;
    }

    public boolean getLastRow() {
        return this.lastRow;
    }

    public void setLastRow(boolean lastRow) {
        this.lastRow = lastRow;
    }

    public String getBuildingRoom() {
        if(this.timeLocation == null) {
            return "";
        }

        return this.getTimeLocation().building + " " + this.getTimeLocation().room;
    }

    public String getDays() {
        if(this.timeLocation == null) {
            return "";
        }

        return this.getTimeLocation().days;
    }

    public String getBeginTime() {
        if(this.timeLocation == null) {
            return "";
        }

        return this.getTimeLocation().beginTime;
    }

    public Section getPrimary() {
        return this.primary;
    }

    public void setPrimary(Section primary) {
        this.primary = primary;
    }

}