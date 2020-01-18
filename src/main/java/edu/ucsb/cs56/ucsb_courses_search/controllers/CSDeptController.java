package edu.ucsb.cs56.ucsb_courses_search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.results.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.results.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.searches.SearchByDept;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CSDeptController {

    private Logger logger = LoggerFactory.getLogger(CSDeptController.class);

    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/csdept/search/classroom")
    public String instructor(Model model, SearchByDept searchByDept) {
        SearchByDept sbd = new SearchByDept();
        sbd.setDept("CMPSC");
        sbd.setCourseLevel("A");
        model.addAttribute("searchByDept", sbd);
        return "csdept/classroom/search";
    }

    @GetMapping("/csdept/search/classroom/results")
    public String search(@RequestParam(name = "dept", required = true) String dept,
            @RequestParam(name = "quarter", required = true) String quarter,
            @RequestParam(name = "courseLevel", required = true) String courseLevel, Model model,
            SearchByDept searchByDept) {
        model.addAttribute("dept", dept);
        model.addAttribute("quarter", quarter);
        model.addAttribute("courseLevel", courseLevel);

        String json = curriculumService.getJSON(dept, quarter, courseLevel);
        CoursePage cp = CoursePage.fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        model.addAttribute("cp", cp);
        model.addAttribute("rows", rows);

        return "search/bydept/results";
    }

}
