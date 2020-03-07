package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.downloaders.CoursePageToCSV;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVDownloadController {


    @Autowired
    private CurriculumService curriculumService;

    @GetMapping("/downloadCSV/courseSearch")
    public void downloadCSV_courseSearch(@RequestParam(name = "subjectArea", required = true) String subjectArea,
            @RequestParam(name = "quarter", required = true) String quarter,
            @RequestParam(name = "courseLevel", required = true) String courseLevel,
            HttpServletResponse response) throws IOException {

        String filename = subjectArea + "-" + quarter + "-" + courseLevel + ".csv";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        String json = curriculumService.getJSON(subjectArea, quarter, courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/departmentSearch")
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

    @GetMapping("/downloadCSV/csDepartmentSearch")
    public void downloadCSV_csDept(@RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON("CMPSC", quarter, "A");

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/instructorSearch")
    public void downloadCSV_instructor(@RequestParam(name = "instructor", required = true) String instructor,
        @RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getJSON(instructor, quarter);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/multiquarterInstructorSearch")
    public void downloadCSV_multiquarter(
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

    @GetMapping("/downloadCSV/courseNumberSearch")
    public void downloadCSV_courseNumber(
        @RequestParam(name = "course", required = true) String course,
        @RequestParam(name = "beginQ", required = true) int beginQ,
        @RequestParam(name = "endQ", required = true) int endQ,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = "";

        List<Course> courses = new ArrayList<Course>();
        for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {
            json = curriculumService.getCourse(course, qtr.getValue());
            CoursePage cp2 = CoursePage.fromJSON(json);
            courses.addAll(cp2.classes);
        }

        CoursePage cp = CoursePage.fromJSON(json);

        cp.setClasses(courses);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/downloadCSV/geSearch")
    public void downloadCSV_ge(@RequestParam(name = "college", required = true) String college,
            @RequestParam(name = "area", required = true) String area,
            @RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=courses.csv");

        String json = curriculumService.getGE(college, area, quarter);

        CoursePage cp = CoursePage.fromJSON(json);

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

}