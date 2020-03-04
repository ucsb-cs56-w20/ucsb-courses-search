package edu.ucsb.cs56.ucsb_courses_search.controller;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs56.ucsb_courses_search.model.result.MySearchResult;
import edu.ucsb.cs56.ucsb_courses_search.model.search.InsSearch;
import edu.ucsb.cs56.ucsb_courses_search.model.search.InsSearchSpecific;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

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

    // Hard code value for quarters
    private static final String[] quarters = { "20174", "20181", "20182", "20183", "20184", "20191", "20192", "20193",
            "20194", "20201" };

    @GetMapping("/search/byinstructor")
    public String instructor(Model model) {
        model
                .addAttribute("searchObject", new MySearchResult());
        return "search/byinstructor/search";
    }

    @GetMapping("/search/byinstructor/results")
    public String singleQtrSearch(InsSearch insSearch, Model model) {
        model
                .addAttribute("insSearch", insSearch);

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
        

        // adds the json and CoursePage object as attributes so they can be accessed in
        // the html, e.g. ${json} or ${cp.classes}
        model
                .addAttribute("json", json);
        model
                .addAttribute("cp", cp);
        model
                .addAttribute("rows", rows);


        return "search/byinstructor/results";
    }

    @GetMapping("/search/byinstructor/specific") // /search/instructor/specific
    public String specifc(Model model) {
        model
                .addAttribute("searchObject", new MySearchResult());
        return "search/byinstructor/specific/search";
    }

    @GetMapping("/search/byinstructor/specific/results")
    public String singleQtrSearch(InsSearchSpecific insSearchSpecific, Model model) {
        model
                .addAttribute("insSearchSpecific", insSearchSpecific);

        // calls curriculumService method to get JSON from UCSB API
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
        model
                .addAttribute("quarters", Quarter
                        .quarterList("W20", "F83"));
        return "search/byinstructor/multiquarter/search";
    }

    @GetMapping("/search/byinstructor/multiquarter/results")
    public String search(
        @RequestParam(name = "instructor", required = true) 
        String instructor,
        @RequestParam(name = "beginQ", required = true) 
        int beginQ,
        @RequestParam(name = "endQ", required = true) 
        int endQ,
        Model model,
        SearchByInstructorMultiQuarter searchObject) {

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

            // Note: F83 seems to be the oldest data available in the API
            model.addAttribute("quarters",Quarter.quarterList("W20","F83"));
            return "search/byinstructor/multiquarter/results";
    }

}
