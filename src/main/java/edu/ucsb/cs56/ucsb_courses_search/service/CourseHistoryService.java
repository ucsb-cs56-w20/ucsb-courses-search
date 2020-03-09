package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.result.YearOfCourseEnrollment;
import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseHistoryService {
    @Autowired
    QuarterListService quarterListService;

    @Autowired
    ArchivedCourseRepository archivedCourseRepository;

    @Autowired
    private CurriculumService curriculumService;

    public List<Course> getCourseHistory(String courseId) {
        String startQuarter = quarterListService.getStartQuarter();
        String endQuarter = quarterListService.getEndQuarter();
        List<Course> courses = archivedCourseRepository.findByQuarterIntervalAndCourseId(startQuarter, endQuarter,
                courseId);
        return courses;
    };

    // TODO 

    private YearOfCourseEnrollment locateByAcademicYear(String academicYear,
    HashMap<String,YearOfCourseEnrollment> ayToStats) {
        // if there is an entry for this academic year, return it
        // otherwise make a new one for this academic year with the stats all initially TBD
        // and stick it in the HashMap
        for(Map.Entry<String, YearOfCourseEnrollment> entry : ayToStats.entrySet()){
            String acYearKey = entry.getKey();
            if(acYearKey == academicYear){
                return entry.getValue();
            }
            else{
                ayToStats.put(academicYear, new YearOfCourseEnrollment(academicYear, "TBD", "TBD", "TBD", "TBD"));
            }
        }
        return ayToStats.get(academicYear);
    }

    public HashMap<String, ArrayList<YearOfCourseEnrollment>> getEnrollmentData(List<CourseOffering> courseOfferings) {

        HashMap<String, ArrayList<YearOfCourseEnrollment>> enrollmentClasses = new HashMap<String, ArrayList<YearOfCourseEnrollment>>();
        ArrayList<YearOfCourseEnrollment> enrollmentNumbers = new ArrayList<YearOfCourseEnrollment>();
        // Let's handle the extraction of previous enrollment numbers
        for (CourseOffering offering : courseOfferings) {
            String courseId = offering.getCourse().getCourseId();

            List<Course> coursesWithThisCourseId = getCourseHistory(courseId);
            Comparator<Course> sortByQuarter = (c1, c2) -> c1.getQuarter().compareTo(c2.getQuarter());

            coursesWithThisCourseId.sort(sortByQuarter);

            HashMap<String,YearOfCourseEnrollment> ayToStats = new HashMap<String,YearOfCourseEnrollment>();
            


            for (Course c : coursesWithThisCourseId) {
                String academicYear = Quarter.qyyyyToAcademicYear(c.getQuarter());
                int index = Quarter.qyyyyToAcademicYearIndex(c.getQuarter());
                String indexToString = Integer.toString(index);
                // is this academicYear already in ayToStats?

                YearOfCourseEnrollment yoce = locateByAcademicYear(academicYear, ayToStats);
                // update the yoce object using the index
                yoce = extractYearHistory(academicYear, courseId, indexToString);
            }

            // iterate over the hashmaps and stick all the YOCE objects into the enrollmentNumbers list.
            // pulling from ayToStats and putting into enrollmentNumbers 
            for(Map.Entry<String, YearOfCourseEnrollment> ent : ayToStats.entrySet()){
                YearOfCourseEnrollment val = ent.getValue();
                enrollmentNumbers.add(val);
            }
            enrollmentClasses.put(courseId, enrollmentNumbers); // put the enrollment numbers in the hashmap with
                                                                    // the class id
            }
        
        return enrollmentClasses;
        
    }

    /**
     * This will extract the year's enrollment history from the database based on
     * the quarter given
     * 
     * @return returns a YearOfCourses object containing enro
     */
    private YearOfCourseEnrollment extractYearHistory(String quarter, String courseID, String presentQuarter) {
        String year = "";
        String[] enrollmentNums = new String[4];

        int currentQuarter = Integer.parseInt(quarter.substring(quarter.length() - 1)); // get the current quarter

        String currentYear = quarter.substring(0, quarter.length() - 1);
        if (currentQuarter == 4) // if this is the fall quarter then year is "current/current+1"
        {
            year = currentYear + "/" + Integer.toString(Integer.parseInt(currentYear) + 1);
        } else // this means any other quarter so year is "current-1/current"
        {
            year = Integer.toString(Integer.parseInt(currentYear) - 1) + "/" + currentYear;
        }
        // first set to fall quarter of the current year
        // System.out.println(quarter);
        Quarter stepQuarter = new Quarter(quarter);
        while (stepQuarter.getQ() != "F") {
            stepQuarter.decrement();
        }

        // now that we're at the beginning of the "year" lets go through each quarter
        // and extract enrollment numbers
        for (int i = 0; i < 4; i++) {
            if (Integer.parseInt(presentQuarter) < Integer.parseInt(stepQuarter.getYYYYQ())) // First lets check if the
                                                                                             // quarter is in the
                                                                                             // future.
            {
                enrollmentNums[i] = "TBD";

                continue;
            } else {
                // now the quarter is present or past. lets get enrollment numbers
                // thse two lines will get the course info for the selected quarter and course
                // id and parse it into a readable thing
                String json = curriculumService.getCourse(courseID, stepQuarter.getValue());
                CoursePage cp = CoursePage.fromJSON(json);

                if (cp.getClasses().size() <= 0) // this means there were no classes held that quarter
                {
                    enrollmentNums[i] = "-";

                } else // now this is a valid class lets get the enrollment numbers
                {
                    // if there's multiple courses, we need a way to loop through them
                    int enrolled = 0;
                    List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);
                    for (CourseOffering offer : courseOfferings) {
                        enrolled += offer.getPrimary().getEnrolledTotal();
                    }
                    // now we have the enrollment numbers let's put them into a string and save
                    enrollmentNums[i] = Integer.toString(enrolled);
                }
            }
            stepQuarter.increment();
        }
        // System.out.println(year + " | " + enrollmentNums[0] + " | " +
        // enrollmentNums[1] + " | " + enrollmentNums[2] + " | " + enrollmentNums[3]);
        return new YearOfCourseEnrollment(year, enrollmentNums[0], enrollmentNums[1], enrollmentNums[2],
                enrollmentNums[3]);

    }
    // }
}