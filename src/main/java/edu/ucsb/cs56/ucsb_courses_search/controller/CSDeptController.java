package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByDept;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByClassroom;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CSDeptController {

    private Logger logger = LoggerFactory.getLogger(CSDeptController.class);

    private HashSet<String> classrooms = new HashSet<String>(Arrays.asList("PHELP 3525", "PHELP 3526", "PHELP 2510", "HFH 1132", "HFH 1152"));

    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private QuarterListService quarterListService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public CSDeptController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    @GetMapping("/csdept/search/classroom")
    public String instructor(Model model, SearchByDept searchByClassroom) {
        SearchByClassroom sbc = new SearchByClassroom();
        model.addAttribute("quarters", quarterListService.getQuarters());
        model.addAttribute("searchByClassroom", sbc);
        // model.addAttribute("building", building);
        
        return "csdept/classroom/search";
    }

    @GetMapping("/csdept/search/classroom/results")
    public String search(@RequestParam(name = "quarter", required = true) String quarter, @RequestParam(name = "classroom", required = true) String classroom, @RequestParam(name = "building", required = true) String building,
            Model model,
            SearchByDept searchByDept) {
	logger.info("QUARTERRRRR=" + quarter);
	logger.info("CLASSROOMMMMM=" + classroom);
	logger.info("BUILDINGGGGG=" + building);
        model.addAttribute("quarter", quarter);
        model.addAttribute("classroom", classroom);
        model.addAttribute("building", building);
        // String json = curriculumService.getJSON("CMPSC", quarter, "A");
        // CoursePage cp = CoursePage.fromJSON(json);

        // List<CourseOffering> courseOfferings = CourseOffering.fromCoursePage(cp);

        List<Course> myclasses = courseRepository.findByLocation(building + "" + classroom);
	logger.info("MYCLASSES=" + myclasses);
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);
        model.addAttribute("myclasses", myclasses);

        // List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);
        // List<CourseListingRow> filteredRows = filter(rows);

        // Comparator<CourseListingRow> byBuildingRoom = (r1, r2) -> {
        //     return r1.getBuildingRoom().compareTo(r2.getBuildingRoom());
        // };

        // Comparator<CourseListingRow> byDays = (r1, r2) -> {
        //     // This comparator depends on the format of days that comes from the Courses
        //     // API, where days only occur in their place in the string "MTWRFSU".
        //     // This comparator must be reversed to get chronological order.
        //     return r1.getDays().compareTo(r2.getDays());
        // };

        // Comparator<CourseListingRow> byBeginTime = (r1, r2) -> {
        //     return r1.getBeginTime().compareTo(r2.getBeginTime());
        // };

        // Collections.sort(filteredRows, byBuildingRoom.thenComparing(byDays.reversed()).thenComparing(byBeginTime));

        // model.addAttribute("cp", cp);
        // model.addAttribute("rows", filteredRows);
        // model.addAttribute("quarters", quarterListService.getQuarters());

        return "csdept/classroom/results";
    }

    // public List<CourseListingRow> filter(List<CourseListingRow> rows) {
    //     List<CourseListingRow> result = new ArrayList<CourseListingRow>();

    //     for(CourseListingRow row : rows) {
    //         if(classrooms.contains(row.getBuildingRoom())) {
    //             result.add(row);
    //         }            
    //     }

    //     return result;
    // }


}
