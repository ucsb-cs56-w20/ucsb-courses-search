package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.service.FinalExamService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByGE;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByGEMultiQuarter;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByGEStartTime;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchGEByDays;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import java.util.ArrayList;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.TimeLocation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByGEController {

    private Logger logger = LoggerFactory.getLogger(SearchByGEController.class);

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private FinalExamService finalExamService;

    @GetMapping("/search/byge")
    public String search(Model model, SearchByGE searchByGE) {
        model.addAttribute("searchByGE", new SearchByGE());
        return "search/byge/search";
    }

    @GetMapping("/search/byge/results")
    public String searchResults(@RequestParam(name = "college", required = true) String college,
            @RequestParam(name = "area", required = true) String area,
            @RequestParam(name = "quarter", required = true) String quarter, Model model,
            SearchByGE searchByGE) {
        model.addAttribute("college", college);
        model.addAttribute("area", area);
        model.addAttribute("quarter", quarter);

        String json = curriculumService.getGE(college, area, quarter);
        CoursePage cp = CoursePage.fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        rows = finalExamService.assignFinalExams(rows);

        model.addAttribute("cp", cp);
        model.addAttribute("rows", rows);

        return "search/byge/results";
    }
    

    
    @GetMapping("/search/byge/multiquarter")
    public String instructor(Model model) {
        model.addAttribute("searchByGEMultiQuarter", new SearchByGEMultiQuarter());
        model.addAttribute("quarters", Quarter.quarterList("W20", "F83"));
        return "search/byge/multiquarter/search";
    }


    @GetMapping("search/byge/multiquarter/results")
    public String searchByGEMultiQuarter(@RequestParam(name = "college", required = true) String college,
    @RequestParam(name = "area", required = true) String area,
    @RequestParam(name = "beginQ", required = true) int beginQ,
    @RequestParam(name = "endQ", required = true) int endQ, 
    Model model) {


        List<Course> courses = new ArrayList<Course>();
        model.addAttribute("college", college);
        model.addAttribute("area", area);

        for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {

            logger.info("qtr=" + qtr.getValue());
            String json = curriculumService.getGE(college, area, qtr.getYYYYQ());
            if(! "{\"error\": \"401: Unauthorized\"}".equals(json)){
                CoursePage cp = CoursePage.fromJSON(json);
                courses.addAll(cp.classes);
            }
        }

        List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);
        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        model.addAttribute("courses", courses);
        model.addAttribute("searchByGEMultiQuarter", new SearchByGEMultiQuarter());
        model.addAttribute("quarters",Quarter.quarterList("W20","F83"));
        model.addAttribute("rows", rows);
        
        return "search/byge/multiquarter/results";
    }

    @GetMapping("/search/byge/multiarea")
    public String multiarea(Model model, SearchByGE searchByMultiArea) {
        model.addAttribute("searchByMultiArea", new SearchByGE());
        return "search/byge/multiarea/search";
    }

    @GetMapping("/search/byge/multiarea/results")
    public String multiarearesult(@RequestParam(name = "college", required = true) String college,
            @RequestParam(name = "area", required = true) ArrayList<String> area,
            @RequestParam(name = "quarter", required = true) String quarter, Model model,
            SearchByGE searchByGE) {
        model.addAttribute("college", college);
        for(int i = 0; i < area.size(); i++){
                model.addAttribute("area", area.get(i));
        }
        model.addAttribute("quarter", quarter);

        List<Course> courses = new ArrayList<Course>();

        for(int j = 0; j < area.size(); j++){
                String json = curriculumService.getGE(college, area.get(j), quarter);
                CoursePage cp = CoursePage.fromJSON(json);
                courses.addAll(cp.classes);
        }

        model.addAttribute("courses", courses);
        List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        rows = finalExamService.assignFinalExams(rows);

        model.addAttribute("rows", rows);

        return "search/byge/multiarea/results";
    }

    @GetMapping("/search/byge/starttime")
    public String startTime(Model model, SearchByGEStartTime searchByGEStartTime) {
        model.addAttribute("searchByGEStartTime", new SearchByGEStartTime());
        model.addAttribute("quarters", Quarter.quarterList("W20", "W19"));
        return "search/byge/starttime/search";
    }

    @GetMapping("search/byge/starttime/results")
    public String startTimeResults(@RequestParam(name = "college", required = true) String college,
    @RequestParam(name = "area", required = true) String area,
    @RequestParam(name = "quarter", required = true) String quarter, 
    @RequestParam(name = "startT", required = true) int startT,
    Model model) {

        model.addAttribute("college", college);
        model.addAttribute("area", area);
        //model.addAttribute("quarter", quarter);
        model.addAttribute("startT", startT);

        String json = curriculumService.getGE(college, area, quarter, startT);
        CoursePage cp = CoursePage.fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        String time = String.valueOf( startT / 100 ) + ":" 
        + ((startT % 100 == 0) ? "00" : String.valueOf(startT % 100));

        if(startT / 100 < 10)
            time = "0" + time;

        ArrayList<CourseOffering> offerings = new ArrayList<CourseOffering>();

        for(CourseOffering c : courseOfferings){

            Course course = c.getCourse();

            String beginT = "";

            if(course.getClassSections().get(0).getTimeLocations().size() >= 1) 
                beginT =course.getClassSections().get(0).getTimeLocations().get(0).getBeginTime();

            if(beginT.equals(time)){
                offerings.add(c);
            }
        }

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(offerings);

        rows = finalExamService.assignFinalExams(rows);
        
        model.addAttribute("searchByGEStartTime", new SearchByGEStartTime());
        model.addAttribute("quarters",Quarter.quarterList("W20","W19"));
        model.addAttribute("rows", rows);
        return "search/byge/starttime/results";
    }
   
    @GetMapping("/search/byge/days")
    public String days(Model model, SearchGEByDays searchGEByDays) {
        model.addAttribute("searchGEByDays", new SearchGEByDays());
        model.addAttribute("quarters", Quarter.quarterList("W20", "W19"));
        return "search/byge/days/search";
    }

    @GetMapping("search/byge/days/results")
    public String daysResults(@RequestParam(name = "college", required = true) String college,
    @RequestParam(name = "area", required = true) String area,
    @RequestParam(name = "quarter", required = true) String quarter, 
    @RequestParam(name = "days", required = true) String days,
    Model model) {

        model.addAttribute("college", college);
        model.addAttribute("area", area);
        model.addAttribute("quarter", quarter);
        model.addAttribute("days", days);

        String json = curriculumService.getGE(college, area, quarter, days);
        CoursePage cp = CoursePage.fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);
        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        model.addAttribute("searchGEByDays", new SearchGEByDays());
        model.addAttribute("quarters",Quarter.quarterList("W20","W19"));
        model.addAttribute("rows", rows);
        return "search/byge/days/results";
    }


}
