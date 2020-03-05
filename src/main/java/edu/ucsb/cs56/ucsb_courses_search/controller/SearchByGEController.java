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
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import java.util.List;
import java.util.ArrayList;

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

        model.addAttribute("rows", rows);

        return "search/byge/multiarea/results";
    }
   

}
