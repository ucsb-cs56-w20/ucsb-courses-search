package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.downloaders.CoursePageToCSV;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;

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
            @RequestParam(name = "courseLevel", required = false) String courseLevel,
            @RequestParam(name = "dept", required = false) String dept,
            @RequestParam(name = "instructor", required = false) String instructor,
            @RequestParam(name = "beginQ", required = false) Integer beginQ,
            @RequestParam(name = "endQ", required = false) Integer endQ,
            @RequestParam(name = "course", required = false) String course,
            @RequestParam(name = "college", required = false) String college,
            @RequestParam(name = "area", required = false) String area,
            @RequestParam(name = "quarter", required = false) String quarter,
            HttpServletResponse response) throws IOException {

        String json = "";
        CoursePage cp;
        String filename = "";

        if(subjectArea != null ){
            filename += subjectArea + "-";
        }

        if(dept != null){
            filename += dept + "-";
        }

        if(instructor != null){
            filename += instructor + "-";
        }

        if(course != null){
            filename += course + "-";
        }

        if(college != null){
            filename += college + "-";
        }

        if(area != null){
            filename += area + "-";
        }

        if(beginQ != null && endQ != null){
            filename += beginQ + "-" + endQ + "-";
        }
        
        if(courseLevel != null){
            filename += courseLevel + "-";
        }

        filename += quarter + ".csv";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        if((instructor != null || course != null) && beginQ != null && endQ != null){ //multiquarter Search

            List<Course> courses = new ArrayList<Course>();
            for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {

                if(instructor != null){ // multiquarter instructor search
                    json = curriculumService.getJSON(instructor, qtr.getYYYYQ());
                }else if(course != null){ // multiquarter course search
                    json = curriculumService.getCourse(course, qtr.getValue());
                }

                CoursePage cp2 = CoursePage.fromJSON(json);
                courses.addAll(cp2.classes);
            }

            cp = CoursePage.fromJSON(json);
            cp.setClasses(courses);

        }else{

            json = curriculumService.getCSV(subjectArea, quarter, courseLevel, dept, instructor, course, college, area);
            cp = CoursePage.fromJSON(json);
        }

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/CSVDownload/csDept")
    public void downloadCSV(@RequestParam(name = "quarter", required = true) String quarter, HttpServletResponse response) throws IOException{
        
        String filename = "Search-Results.csv";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        String json = curriculumService.getJSON("CMPSC", quarter, "A");
        CoursePage cp = CoursePage.fromJSON(json);
        CoursePageToCSV.writeSections(response.getWriter(), cp);

    }
}