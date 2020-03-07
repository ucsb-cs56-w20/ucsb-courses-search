package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.result.YearOfCourseEnrollment;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import edu.ucsb.cs56.ucsb_courses_search.service.UCSBBuildingService;

import java.util.*;

@Controller()
public class SearchController {

    //this is for setting how many years to go back in time.
    int YEARS_OF_HISTORY = 10;

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/searchResults")
    public String search(
        @RequestParam(name = "subjectArea", required = true)
        String subjectArea,
        @RequestParam(name = "quarter", required = true)
        String quarter,
        @RequestParam(name = "courseLevel", required = true)
        String courseLevel,
        Model model
        ) {
        model.addAttribute("subjectArea", subjectArea);
        model.addAttribute("quarter", quarter);
        model.addAttribute("courseLevel", courseLevel);

        String json = curriculumService.getJSON(subjectArea,quarter,courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);
        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);
        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        //START COPYING HERE
        HashMap<String, ArrayList<YearOfCourseEnrollment>> enrollmentClasses = new HashMap<String, ArrayList<YearOfCourseEnrollment>>();

        //Let's handle the extraction of previous enrollment numbers
        for(CourseOffering offering : courseOfferings)
        {
          //this list will store the previous enrollment numbers of a single course
          ArrayList<YearOfCourseEnrollment> enrollmentNumbers = new ArrayList<YearOfCourseEnrollment>();
          //now lets loop as many times as needed for the last however many YEARS_OF_HISTORY
          for(int i = 0; i < YEARS_OF_HISTORY; i++)
          {
            String quarterToExtract = Integer.toString(Integer.parseInt(quarter) - (i * 10));
            //now lets get that year history from the quarter we've parsed
            YearOfCourseEnrollment yearEnroll = extractYearHistory(quarterToExtract, offering.getCourse().getCourseId(), quarter);
            System.out.println(yearEnroll);
            enrollmentNumbers.add(yearEnroll);
          }

          enrollmentClasses.put(offering.getCourse().getCourseId(), enrollmentNumbers); //put the enrollment numbers in the hashmap with the class id
        }
        //End copy here

        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        model.addAttribute("rows", rows);
        model.addAttribute("eh", enrollmentClasses);

        return "searchResults"; // corresponds to src/main/resources/templates/searchResults.html
    }

    //Copy this method
    /**
      This will extract the year's enrollment history from the database based on the quarter given

      @return returns a YearOfCourses object containing enro
    */
    private YearOfCourseEnrollment extractYearHistory(String quarter, String courseID, String presentQuarter)
    {
      String year = "";
      String[] enrollmentNums = new String[4];

      int currentQuarter = Integer.parseInt(quarter.substring(quarter.length()-1)); //get the current quarter

      String currentYear = quarter.substring(0, quarter.length()-1);
      if(currentQuarter == 4) //if this is the fall quarter then year is "current/current+1"
      {
        year = currentYear + "/" + Integer.toString(Integer.parseInt(currentYear) + 1);
      }
      else //this means any other quarter so year is "current-1/current"
      {
        year = Integer.toString(Integer.parseInt(currentYear) - 1) + "/" + currentYear;
      }
      //first set to fall quarter of the current year
      //System.out.println(quarter);
      Quarter stepQuarter = new Quarter(quarter);
      while(stepQuarter.getQ() != "F")
      {
        stepQuarter.decrement();
      }

      //now that we're at the beginning of the "year" lets go through each quarter and extract enrollment numbers
      for(int i = 0 ; i < 4; i++)
      {
        if(Integer.parseInt(presentQuarter) < Integer.parseInt(stepQuarter.getYYYYQ())) //First lets check if the quarter is in the future.
        {
          enrollmentNums[i] = "TBD";

          continue;
        }
        else
        {
          //now the quarter is present or past. lets get enrollment numbers
          //thse two lines will get the course info for the selected quarter and course id and parse it into a readable thing
          String json = curriculumService.getCourse(courseID,stepQuarter.getValue());
          CoursePage cp = CoursePage.fromJSON(json);

          if(cp.getClasses().size() <= 0) //this means there were no classes held that quarter
          {
            enrollmentNums[i] = "-";

          }
          else //now this is a valid class lets get the enrollment numbers
          {
            //if there's multiple courses, we need a way to loop through them
            int enrolled = 0;
            List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);
            for(CourseOffering offer : courseOfferings)
            {
              enrolled += offer.getPrimary().getEnrolledTotal();
            }
            //now we have the enrollment numbers let's put them into a string and save
            enrollmentNums[i] = Integer.toString(enrolled);
          }
        }
        stepQuarter.increment();
      }
      //System.out.println(year + " | " + enrollmentNums[0] + " | " + enrollmentNums[1] + " | " + enrollmentNums[2] + " | " + enrollmentNums[3]);
      return new YearOfCourseEnrollment(year, enrollmentNums[0],enrollmentNums[1],enrollmentNums[2],enrollmentNums[3]);

    }
}
