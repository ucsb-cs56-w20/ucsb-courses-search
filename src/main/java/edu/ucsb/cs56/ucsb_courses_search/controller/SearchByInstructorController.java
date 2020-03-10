package edu.ucsb.cs56.ucsb_courses_search.controller;

import java.util.*; //modify this so that it has access to all the arraylists and hashmaps

import edu.ucsb.cs56.ucsb_courses_search.model.result.MySearchResult;
import edu.ucsb.cs56.ucsb_courses_search.model.search.InsSearch;
import edu.ucsb.cs56.ucsb_courses_search.model.search.InsSearchSpecific;
import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_courses_search.service.CourseHistoryService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.YearOfCourseEnrollment;//COPY THIS
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter; //ALSO COPY THIS

import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByInstructorMultiQuarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByInstructorController {

    private Logger logger = LoggerFactory
            .getLogger(SearchByInstructorController.class);

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private QuarterListService quarterListService;

    @Autowired
    private CourseHistoryService courseHistoryService;

    @GetMapping("/search/byinstructor")
    public String instructor(Model model) {
        model
                .addAttribute("searchObject", new MySearchResult());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "search/byinstructor/search";
    }

    @GetMapping("/search/byinstructor/results")
    public String singleQtrSearch(InsSearch insSearch, Model model, RedirectAttributes redirAttrs) {
        model
                .addAttribute("insSearch", insSearch);

	if (insSearch.getInstructor() == ""){
	    redirAttrs.addFlashAttribute("alertDanger", "You cannot leave instructor blank");
            return "redirect:.";
        }

        // calls curriculumService method to get JSON from UCSB API
        String json = curriculumService
                .getJSON(insSearch
                        .getInstructor(),
                        insSearch
                                .getQuarter());

        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage
                .fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        HashMap<String, ArrayList<YearOfCourseEnrollment>> enrollmentClasses = courseHistoryService.getEnrollmentData(courseOfferings);

        // adds the json and CoursePage object as attributes so they can be accessed in
        // the html, e.g. ${json} or ${cp.classes}
        model
                .addAttribute("json", json);
        model
                .addAttribute("cp", cp);
        model
                .addAttribute("quarters", quarterListService.getQuarters());
        model
                .addAttribute("rows", rows);

                model.addAttribute("eh", enrollmentClasses); //BUT also copy this

	return "search/byinstructor/results";
    }

    //Copy this method
    /**
      This will extract the year's enrollment history from the database based on the quarter given

      @return returns a YearOfCourses object containing enro
    */
    public YearOfCourseEnrollment extractYearHistory(String quarter, String courseID, String presentQuarter)
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

    @GetMapping("/search/byinstructor/specific") // /search/instructor/specific
    public String specifc(Model model) {
        model
                .addAttribute("searchObject", new MySearchResult());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "search/byinstructor/specific/search";
    }

    @GetMapping("/search/byinstructor/specific/results")
    public String singleQtrSearch(InsSearchSpecific insSearchSpecific, Model model, RedirectAttributes redirAttrs) {
        model
                .addAttribute("insSearchSpecific", insSearchSpecific);

        // calls curriculumService method to get JSON from UCSB API

	if (insSearchSpecific.getInstructor() == ""){
	        redirAttrs.addFlashAttribute("alertDanger", "You cannot leave instructor blank");
            return "redirect:.";
        }

        String json = curriculumService
                .getJSON(insSearchSpecific
                        .getInstructor(),
                        insSearchSpecific
                                .getQuarter());

        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage
                .fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        // adds the json and CoursePage object as attributes so they can be accessed in
        // the html, e.g. ${json} or ${cp.classes}
        model
                .addAttribute("json", json);
        model
                .addAttribute("cp", cp);
        model
                .addAttribute("rows", rows);

        return "search/byinstructor/specific/results";
    }

    @GetMapping("/search/byinstructor/multiquarter") // search/instructor/multiquarter
    public String multi(Model model, SearchByInstructorMultiQuarter searchObject) {
        model
                .addAttribute("searchObject", new SearchByInstructorMultiQuarter());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "search/byinstructor/multiquarter/search";
    }

    @GetMapping("/search/byinstructor/multiquarter/results")
    public String search (
        @RequestParam(name = "instructor", required = true)
        String instructor,
        @RequestParam(name = "beginQ", required = true)
        int beginQ,
        @RequestParam(name = "endQ", required = true)
        int endQ,
        Model model,
        SearchByInstructorMultiQuarter searchObject,
        RedirectAttributes redirAttrs) {


	if (instructor  == ""){
            redirAttrs.addFlashAttribute("alertDanger", "You cannot leave instructor blank");
            return "redirect:.";
	}
        if(endQ<beginQ){
             redirAttrs.addFlashAttribute("alertDanger", "End quarter must be later than begin quarter");
             return "redirect:.";
        }

	    logger.info("GET request for /search/byinstructor/multiquarter/results");
            logger.info("beginQ=" + beginQ + " endQ=" + endQ);

            List<Course> courses = new ArrayList<Course>();
            for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {
                String json = curriculumService.getJSON(instructor, qtr.getYYYYQ());
                logger.info("qtr=" + qtr.getValue() + " json=" + json);
                CoursePage cp = CoursePage.fromJSON(json);
                courses.addAll(cp.classes);
            }


            model.addAttribute("courses", courses);

            List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);
            List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

            model.addAttribute("rows", rows);
            model.addAttribute("searchObject", searchObject );

            model.addAttribute("quarters", quarterListService.getQuarters());
            return "search/byinstructor/multiquarter/results";
    }

}
