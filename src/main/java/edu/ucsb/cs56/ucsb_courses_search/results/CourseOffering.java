package edu.ucsb.cs56.ucsb_courses_search.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.type.AbstractStandardBasicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Section;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Instructor;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.TimeLocation;


public class CourseOffering {
    private Course course;
    private Section primary;
    private List<Section> secondaries;


    public Course getCourse() {
        return this.course;
    }

    public Section getPrimary() {
        return this.primary;
    }

    public List<Section> getSecondaries() {
        return this.secondaries;
    }

    public CourseOffering()  {
        secondaries = new ArrayList<Section>();
    }

    private static Logger logger = LoggerFactory.getLogger(CourseOffering.class);
 
    public String getQuarter() {
        return course.getQuarter();
    }

    public String getCourseId() {
        return course.getCourseId();
    }

    public String getTitle() {
        return course.getTitle();
    }

    /**
     * Return the name of the main instructor(s) for the course, i.e. the lecture
     * (primary) section. If there is more than one, they are separated by commas
     * and a single space.
     * 
     * @return Name of primary instructor(s) for the course
     */

    public String getInstructorList() {
        if (primary == null)
            return "";
        
        List<Instructor> instructors = primary.getInstructors();
        if ( (instructors== null ) || (instructors.size() == 0) )
            return "";
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
        String quarter = this.course.getQuarter();
        try {
            Quarter q = new Quarter(this.course.getQuarter());
            return q.toString();
        } catch (Exception e) {
            String msg = String.format("Error converting quarter \"%s\": ", quarter);
            logger.error(msg,e);
            return "";
        }
    }

    /**
     * Convert a CoursePage object, which is the result of a single call to the JSON API,
     * into a list of CourseOffering objects.  The main purpose is to group the secondaries (sections)
     * with the appropriate primaries to make it easier to process, understand, and format the results.
     */ 
    public static List<CourseOffering> fromCoursePage(CoursePage cp) {  
        ArrayList<CourseOffering> result = new ArrayList<CourseOffering>();
        for ( Course c : cp.getClasses() ) {
            // Use first two characters of section to group primary with section
            // Assumption: If last two chars are "00" it's the primary
            //   otherwise it's a seciton that goes with a matching primary

            HashMap<String, CourseOffering> primaries = new HashMap<String, CourseOffering>();
            
            for ( Section s: c.getClassSections() ) {
                String section = s.getSection();
                String prefix = section.substring(0,3);
                String suffix = section.substring(2,4);
                if (suffix.equals("00")) {
                    CourseOffering co = new CourseOffering();
                    co.primary = s;
                    co.course = c;
                    primaries.put(prefix,co);
                } else {
                    CourseOffering primary = primaries.get(prefix);
                    if (primary==null) {
                        logger.error("Could not find primary for prefix " + prefix);
                        logger.error("primaries = " + primaries);
                    } else {
                        logger.info("Adding secondary " + section + " to primary " + primary);
                        primary.secondaries.add(s);
                        logger.info("primaries=" + primaries);
                    }
                }
            }
            result.addAll(primaries.values());
        }
        return result;
    }


    @Override
    public String toString() {
        return "{" +
           course.getCourseId() + 
            "}";
    }


}
