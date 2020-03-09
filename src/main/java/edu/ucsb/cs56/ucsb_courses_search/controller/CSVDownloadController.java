package edu.ucsb.cs56.ucsb_courses_search.controller;

import edu.ucsb.cs56.ucsb_courses_search.downloaders.CoursePageToCSV;
import edu.ucsb.cs56.ucsb_courses_search.downloaders.PersonalScheduleToCSV;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CSVDownloadController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

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
            @RequestParam(name = "quarter", required = false) String quarter, HttpServletResponse response)
            throws IOException {

        String json = "";
        CoursePage cp;
        String filename = "";

        if (subjectArea != null) {
            filename += subjectArea + "-";
        }

        if (dept != null) {
            filename += dept + "-";
        }

        if (instructor != null) {
            filename += instructor.toUpperCase() + "-";
        }

        if (course != null) {
            filename += course + "-";
        }

        if (college != null) {
            filename += college + "-";
        }

        if (area != null) {
            filename += area + "-";
        }

        if (courseLevel != null) {
            filename += courseLevel + "-";
        }

        if (beginQ != null && endQ != null) {
            filename += beginQ + "-" + endQ + ".csv";
        } else {
            filename += quarter + ".csv";
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        if ((instructor != null || course != null) && beginQ != null && endQ != null) { // multiquarter Search

            List<Course> courses = new ArrayList<Course>();
            for (Quarter qtr = new Quarter(beginQ); qtr.getValue() <= endQ; qtr.increment()) {

                if (instructor != null) { // multiquarter instructor search
                    json = curriculumService.getJSON(instructor, qtr.getYYYYQ());
                } else if (course != null) { // multiquarter course search
                    json = curriculumService.getCourse(course, qtr.getValue());
                }

                CoursePage cp2 = CoursePage.fromJSON(json);
                courses.addAll(cp2.classes);
            }

            cp = CoursePage.fromJSON(json);
            cp.setClasses(courses);

        } else {

            json = curriculumService.getCSV(subjectArea, quarter, courseLevel, dept, instructor, course, college, area);
            cp = CoursePage.fromJSON(json);
        }

        CoursePageToCSV.writeSections(response.getWriter(), cp);
    }

    @GetMapping("/CSVDownload/csDept")
    public void downloadCSV(@RequestParam(name = "quarter", required = true) String quarter,
            HttpServletResponse response) throws IOException {

        String filename = "CMPSC-A-" + quarter + ".csv";

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        String json = curriculumService.getJSON("CMPSC", quarter, "A");
        CoursePage cp = CoursePage.fromJSON(json);
        CoursePageToCSV.writeSections(response.getWriter(), cp);

    }

    @GetMapping("/personalSchedule")
    public void downloadCSV(OAuth2AuthenticationToken token, HttpServletResponse response) throws IOException {

        if (token != null) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid=" + uid);
            logger.info("courseRepository=" + scheduleItemRepository);
            Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(uid);
            // need full path, because there are two "Cource", java cannot import both
            String filename = "MySchedule.csv";
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            PersonalScheduleToCSV.writeSections(response.getWriter(), myclasses);
        }

    }
}