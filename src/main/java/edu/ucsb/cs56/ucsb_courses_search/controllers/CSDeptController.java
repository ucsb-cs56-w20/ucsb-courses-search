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
import java.util.HashSet;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CSDeptController {

    private Logger logger = LoggerFactory.getLogger(CSDeptController.class);

    private HashSet<String> classrooms = new HashSet<String>(Arrays.asList("PHELP 3525", "PHELP 3526", "PHELP 2510", "HFH 1132", "HFH 1152"));

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
    public String search(@RequestParam(name = "quarter", required = true) String quarter,
            Model model,
            SearchByDept searchByDept) {
        model.addAttribute("quarter", quarter);

        String json = curriculumService.getJSON("CMPSC", quarter, "A");
        CoursePage cp = CoursePage.fromJSON(json);

        List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);
        List<CourseListingRow> filteredRows = filter(rows);

        model.addAttribute("cp", cp);
        model.addAttribute("rows", filteredRows);

        return "csdept/classroom/results";
    }

    public List<CourseListingRow> filter(List<CourseListingRow> rows) {
        List<CourseListingRow> result = new ArrayList<CourseListingRow>();

        for(CourseListingRow row : rows) {
            if(classrooms.contains(row.getBuildingRoom())) {
                result.add(row);
            }            
        }

        return result;
    }


}
