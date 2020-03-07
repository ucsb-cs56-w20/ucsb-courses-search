package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.downloaders.CoursePageToCSV;
import edu.ucsb.cs56.ucsb_courses_search.downloaders.PersonalScheduleToCSV;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import java.util.ArrayList;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVDownloadController {

    private Logger logger = LoggerFactory.getLogger(SearchByInstructorController.class);

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public CSVDownloadController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/downloadCSV/courseSearch")
    public void downloadCSV(@RequestParam(name = "subjectArea", required = true) String subjectArea,
            @RequestParam(name = "quarter", required = true) String quarter,
            @RequestParam(name = "courseLevel", required = true) String courseLevel,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(subjectArea, quarter, courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }
    @GetMapping("/downloadCSV/searchCSV_ByDepartment")
    public void downloadCSV_ByDepartment(@RequestParam(name = "dept", required = true) String dept,
            @RequestParam(name = "quarter", required = true) String quarter,
            @RequestParam(name = "courseLevel", required = true) String courseLevel,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(dept, quarter, courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/csDept")
    public void downloadCSV(@RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(quarter);

        CoursePage cp = CoursePage.fromJSON(json);



        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/byInstructor")
    public void downloadCSV(@RequestParam(name = "instructor", required = true) String instructor,
        @RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(instructor, quarter);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/multiquarterSearch")
    public void downloadCSV(
        @RequestParam(name = "instructor", required = true) String instructor,
        @RequestParam(name = "beginQ", required = true) int beginQ,
        @RequestParam(name = "endQ", required = true) int endQ,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = "";

        List<Course> courses = new ArrayList<Course>();
        for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {
            json = curriculumService.getJSON(instructor, qtr.getYYYYQ());
            CoursePage cp2 = CoursePage.fromJSON(json);
            courses.addAll(cp2.classes);
        }

        CoursePage cp = CoursePage.fromJSON(json);
        cp.setClasses(courses);
        CoursePageToCSV.writeSections(response.getWriter(), cp);
    
    }

    @GetMapping("/personalSchedule")
    public void downloadCSV(OAuth2AuthenticationToken token, HttpServletResponse response) throws IOException {
        
        
        
        if (token!=null) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("courseRepository="+courseRepository);
            Iterable<edu.ucsb.cs56.ucsb_courses_search.entity.Course> myclasses = courseRepository.findByUid(uid);
            //need full path, because there are two "Cource", java cannot import both
                 
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; file=mycourses.csv");
            PersonalScheduleToCSV.writeSections(response.getWriter(), myclasses);
        }

    }
}