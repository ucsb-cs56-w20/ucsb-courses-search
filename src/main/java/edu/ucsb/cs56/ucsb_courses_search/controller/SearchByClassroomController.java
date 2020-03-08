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
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByClassroom;
import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByClassroomController {

    private Logger logger = LoggerFactory.getLogger(SearchByClassroomController.class);

    @Autowired
    private QuarterListService quarterListService;

    @Autowired
    private ArchivedCourseRepository archivedCourseRepository;

    @GetMapping("/search/byclassroom")
    public String search(Model model,  SearchByClassroom searchByClassroom) {
        logger.info("Entering search controller");
        model.addAttribute("searchByClassroom", new SearchByClassroom());
        model.addAttribute("quarters", quarterListService.getQuarters());
        return "/classroom/search";
    }

    @GetMapping("/search/byclassroom/results")
    public String search(@RequestParam(name = "building", required = true) String building,
             @RequestParam(name = "quarter", required = true) String quarter,
             @RequestParam(name = "classroom", required = true) String classroom,
             Model model,
             SearchByClassroom searchByClassroom) {
        // logger.info("Entering results controller method");

        
        model.addAttribute("building", building);
        model.addAttribute("classroom", classroom);
        model.addAttribute("quarter", quarter);
        model.addAttribute("quarters", quarterListService.getQuarters());

        List<Course> courses = archivedCourseRepository.findByQuarterAndRoom(quarter, building, classroom);

        List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);
        List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

        model.addAttribute("rows", rows);

        for (int i = 0; i < rows.size(); i++){
            logger.info("HELLLLP " + rows.get(i).getSection().toString());
        }

        model.addAttribute("rows", rows);

        // Iterator<CourseListingRow> itr = rows.iterator();
        // while (itr.hasNext()){
        //     if (r.getPrimary() == null){
        //         itr.remove();
        //     }
        //     // logger.info("LINAAAAAA" + r.toString());
        // }



        return "classroom/results";
    }
}
