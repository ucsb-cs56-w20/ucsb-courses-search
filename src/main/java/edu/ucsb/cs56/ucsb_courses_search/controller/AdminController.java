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
import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private QuarterListService quarterListService;

    @Autowired
    private ArchivedCourseRepository archivedCourseRepository;

    @GetMapping("/admin/archived/search")
    public String search(Model model,  SearchByDept searchByDept) {
        logger.info("Entering search controller");
        model.addAttribute("searchByDept", new SearchByDept());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "admin/archived/search";
    }

    @GetMapping("/admin/archived/search/results")
    public String search(@RequestParam(name = "dept", required = true) String dept,
             @RequestParam(name = "quarter", required = true) String quarter,
             Model model,
             SearchByDept searchByDept) {
        logger.info("Entering results controller method");

        model.addAttribute("dept", dept);
        model.addAttribute("quarter", quarter);
        model.addAttribute("quarters", quarterListService.getQuarters());

        List<Course> courses = archivedCourseRepository.findByQuarterAndDepartment(quarter, dept);
        List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);
        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        model.addAttribute("rows", rows);

        return "admin/archived/results";
    }
}
