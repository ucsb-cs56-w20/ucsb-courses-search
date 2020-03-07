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

    @GetMapping("/CSVDownload")
    public void downloadCSV(@RequestParam(name = "subjectArea", required = false) String subjectArea,
            @RequestParam(name = "quarter", required = false) String quarter,
            @RequestParam(name = "courseLevel", required = false) String courseLevel,
            @RequestParam(name = "dept", required = false) String dept,
            @RequestParam(name = "instructor", required = false) String instructor,
            @RequestParam(name = "beginQ", required = false) Integer beginQ,
            @RequestParam(name = "endQ", required = false) Integer endQ,
            @RequestParam(name = "course", required = false) String course,
            @RequestParam(name = "college", required = false) String college,
            @RequestParam(name = "area", required = false) String area,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");

        String json = "";
        CoursePage cp;
        String filename = "Search-Results.csv";

        if(instructor != null && beginQ != null && endQ != null){ //multiquarterInstructorSearch

            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            List<Course> courses = new ArrayList<Course>();
            for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {
                json = curriculumService.getJSON(instructor, qtr.getYYYYQ());
                CoursePage cp2 = CoursePage.fromJSON(json);
                courses.addAll(cp2.classes);
            }

            cp = CoursePage.fromJSON(json);
            cp.setClasses(courses);

        }else if(course != null && beginQ != null && endQ != null){

            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            List<Course> courses = new ArrayList<Course>();
            for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {
                json = curriculumService.getCourse(course, qtr.getValue());
                CoursePage cp2 = CoursePage.fromJSON(json);
                courses.addAll(cp2.classes);
            }

            cp = CoursePage.fromJSON(json);
            cp.setClasses(courses);

        }else if(quarter != null && dept == null && courseLevel == null && instructor == null && college == null && area == null){ //CS Department Seach

            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            json = curriculumService.getJSON("CMPSC", quarter, "A");
            cp = CoursePage.fromJSON(json);

        }else{

            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            json = curriculumService.getCSV(subjectArea, quarter, courseLevel, dept, instructor, course, college, area);
            cp = CoursePage.fromJSON(json);
        }

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }
}