package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByDept;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByDeptController {

    private Logger logger = LoggerFactory.getLogger(SearchByDeptController.class);

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private QuarterListService quarterListService;

    @GetMapping("/search/bydept")
    public String instructor(Model model, SearchByDept searchByDept) {
        model.addAttribute("searchByDept", new SearchByDept());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "search/bydept/search";
    }

    @GetMapping("/search/bydept/results")
    public String search(@RequestParam(name = "dept", required = true) String dept,
            @RequestParam(name = "quarter", required = true) String quarter,
            @RequestParam(name = "courseLevel", required = true) String courseLevel, Model model,
            SearchByDept searchByDept) {
        model.addAttribute("dept", dept);
        model.addAttribute("quarter", quarter);
        model.addAttribute("quarters", quarterListService.getQuarters());
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
