package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import edu.ucsb.cs56.ucsb_courses_search.service.UCSBBuildingService;

import java.util.List;

@Controller()
public class SearchController {

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
            
        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        model.addAttribute("rows", rows);

        

        return "searchResults"; // corresponds to src/main/resources/templates/searchResults.html
    }



}
