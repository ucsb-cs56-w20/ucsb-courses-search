package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByGE;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByGEMultiQuarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByGEController {

    private Logger logger = LoggerFactory.getLogger(SearchByGEController.class);

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/search/byge")
    public String instructor(Model model, SearchByGE searchByGE) {
        model.addAttribute("searchByGE", new SearchByGE());
        return "search/byge/search";
    }

    @GetMapping("/search/byge/results")
    public String search(@RequestParam(name = "college", required = true) String college,
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
    Model model,
    SearchByGEMultiQuarter SearchByGEMultiQuarter) {


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

}
