package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.result.YearOfCourseEnrollment;
import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Section;
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
        String startQuarter = (new Quarter(quarterListService.getStartQuarter())).getYYYYQ();
        String endQuarter = (new Quarter(quarterListService.getEndQuarter())).getYYYYQ();

        String formattedID = String.format("%-8s%-5s", courseId.substring(0,courseId.indexOf(' ')).trim(), courseId.substring(courseId.indexOf(' ')+1, courseId.length()-1).trim());
        
        //String formattedID = "CMPSC    32  ";
        //System.out.println("{"+formattedID+"}");
        List<Course> courses = archivedCourseRepository.findByQuarterIntervalAndCourseId(endQuarter, startQuarter, formattedID);
        //System.out.println(archivedCourseRepository.findByQuarterIntervalAndCourseId("20201", "20202", "CMPSC    32  "));
        //System.out.println("formatted query: " + startQuarter + " " + endQuarter + " " + formattedID);
        //System.out.println(courses);
        return courses;
    };

    /** This will return an ArrayList<YearOfCourseEnrollment> that is populated with course history set up to whatever the endQuarter is set to
    *
    */
    private ArrayList<YearOfCourseEnrollment> populateCourseHistory(List<Course> courses) {

      HashMap<String,Integer> enrollmentNumbers = new HashMap<String, Integer>();
        //We need to iterate through all the courses and scan for ones we want
        for(Course c : courses)
        {
          //now lets scan sections
          for(Section s : c.getClassSections())
          {
            //This is pulled from CourseOffering.java to detect primary vs secondary ClassSections
            String suffix = s.getSection().substring(2,4);
            if (suffix.equals("00"))
            {
              //this means we havent seen this quarter yet
              if(enrollmentNumbers.get(c.getQuarter()) == 0)
              {
                enrollmentNumbers.put(c.getQuarter(), s.getEnrolledTotal());
              }
              //this means we're just adding numbers to a quarter
              else{
                enrollmentNumbers.replace(c.getQuarter(), enrollmentNumbers.get(c.getQuarter()) + s.getEnrolledTotal());
              }
            }
          }
        }

        //lets get the start and end quarters
        String startQuarter = (new Quarter(quarterListService.getStartQuarter())).getYYYYQ();
        String endQuarter = (new Quarter(quarterListService.getEndQuarter())).getYYYYQ();
        Quarter endQ = new Quarter(endQuarter); //this is in the past. not the present
        Quarter startQ = new Quarter(startQuarter);//this is the present not the past

        ArrayList<YearOfCourseEnrollment> yCE = new ArrayList<YearOfCourseEnrollment>();

        YearOfCourseEnrollment year = new YearOfCourseEnrollment(getSchoolYear(startQuarter));

        if(startQ.getQ() != "M")
        {
          Quarter tempQ = new Quarter(startQuarter);
          while(tempQ.getQ() != "M")
          {
            tempQ.increment();
            year.setQuarter(tempQ.getValue()%10, "TBD");
            System.out.println(tempQ);
            System.out.println(year);
          }
          //year.setQuarter(3, "TBD");
        }

        while(!startQ.equals(endQ))
        {
          Integer tempInt = enrollmentNumbers.get(startQ.getYYYYQ());
          if(tempInt != null)
            year.setQuarter(startQ.getValue()%10, Integer.toString(tempInt));

          startQ.decrement();
          if(startQ.getQ() == "M")
          {
            yCE.add(year);
            //System.out.println(startQ);
            year = new YearOfCourseEnrollment(getSchoolYear(startQ.getYYYYQ()));
          }
        }

        if(endQ.getQ() != "M")
        {
          yCE.add(year);
        }

        return yCE;
    }

    public HashMap<String, ArrayList<YearOfCourseEnrollment>> getEnrollmentData(List<CourseOffering> courseOfferings) {

        HashMap<String, ArrayList<YearOfCourseEnrollment>> enrollmentClasses = new HashMap<String, ArrayList<YearOfCourseEnrollment>>();

        // Let's handle the extraction of previous enrollment numbers
        for (CourseOffering offering : courseOfferings) {

            ArrayList<YearOfCourseEnrollment> enrollmentNumbersForCourse = new ArrayList<YearOfCourseEnrollment>();
            String courseId = offering.getCourse().getCourseId();
            //System.out.println(offering.getCourse().getQuarter());
            List<Course> coursesWithThisCourseId = getCourseHistory(courseId);
            Comparator<Course> sortByQuarter = (c1, c2) -> c1.getQuarter().compareTo(c2.getQuarter());

            coursesWithThisCourseId.sort(sortByQuarter);

            enrollmentNumbersForCourse = populateCourseHistory(coursesWithThisCourseId);



            /*
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
            */
            enrollmentClasses.put(courseId, enrollmentNumbersForCourse); // put the enrollment numbers in the hashmap with
                                                                    // the class id
            }

        return enrollmentClasses;

    }

    private String getSchoolYear(String year)
    {
      String output = "";
      String[] enrollmentNums = new String[4];

      int currentQuarter = Integer.parseInt(year.substring(year.length() - 1)); // get the current quarter

      String currentYear = year.substring(0, year.length() - 1);
      if (currentQuarter == 4) // if this is the fall quarter then year is "current/current+1"
      {
          output = currentYear + "/" + Integer.toString(Integer.parseInt(currentYear) + 1);
      } else // this means any other quarter so year is "current-1/current"
      {
          output = Integer.toString(Integer.parseInt(currentYear) - 1) + "/" + currentYear;
      }

      return output;
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
