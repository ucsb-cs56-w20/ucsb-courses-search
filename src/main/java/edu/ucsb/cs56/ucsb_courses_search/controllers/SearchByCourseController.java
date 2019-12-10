package edu.ucsb.cs56.ucsb_courses_search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.results.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.results.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.searches.SearchByCourse;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByCourseController {

    private Logger logger = LoggerFactory.getLogger(SearchByCourseController.class);

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/search/bycourse")
    public String instructor(Model model, SearchByCourse searchByCourse) {
        model.addAttribute("searchByCourse", new SearchByCourse());
        return "search/bycourse/search";
    }

    @GetMapping("/search/bycourse/results")
    public String search(@RequestParam(name = "course", required = true) String course,
            @RequestParam(name = "beginQ", required = true) int beginQ,
            @RequestParam(name = "endQ", required = true) int endQ, Model model, SearchByCourse searchByCourse) {
        model.addAttribute("course", course);
        model.addAttribute("beginQ", beginQ);
        model.addAttribute("endQ", endQ);

        List<Course> courses = new ArrayList<Course>();
        for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {
            String json = curriculumService.getCourse(course, qtr.getValue());
            logger.info("qtr=" + qtr.getValue() + " json=" + json);
            CoursePage cp = CoursePage.fromJSON(json);
            courses.addAll(cp.classes);
        }

        model.addAttribute("courses", courses);

        List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);
        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        List<CourseListingRow> primaryRows = rows.stream().filter(r -> r.getRowType().equals("PRIMARY"))
                .collect(Collectors.toList());

        model.addAttribute("rows", primaryRows);

        return "search/bycourse/results";
    }

}
