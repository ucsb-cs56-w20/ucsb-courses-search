package edu.ucsb.cs56.ucsb_courses_search.controller;

import java.util.*; 
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import edu.ucsb.cs56.ucsb_courses_search.repository.ArchivedCourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.QuarterListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.service.FinalExamService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;

import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseOffering;
import edu.ucsb.cs56.ucsb_courses_search.model.search.SearchByInstructorMultiQuarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchByInstructorController {

    private Logger logger = LoggerFactory
            .getLogger(SearchByInstructorController.class);

    @Autowired
    private ArchivedCourseRepository archivedCourseRepository;
    
    @Autowired
    private CurriculumService curriculumService;

    @Autowired
    private FinalExamService finalExamService;

    @Autowired
    private QuarterListService quarterListService;

    @GetMapping("/search/byinstructor/multiquarter") // search/instructor/multiquarter
    public String multi(Model model, SearchByInstructorMultiQuarter searchObject) {
        model.addAttribute("searchObject", new SearchByInstructorMultiQuarter());
        model.addAttribute("quarters", quarterListService.getQuarters());
        List<String> instructorNames = new ArrayList<>();
        instructorNames = archivedCourseRepository.listInstructorNamesByQuarterInterval(Integer.toString(Quarter.qyyToQyyyy("F18")),Integer.toString(Quarter.qyyToQyyyy("S19")));
        List<String> instructorNewNames = new ArrayList<>();
        for(int i = 0; i<instructorNames.size(); i++)
        {
            String[] instructor = instructorNames.get(i).split(",");
            for(int j = 0; j<instructor.length; j++)
            {
                instructorNewNames.add(instructor[j]);
            }
        }
        instructorNewNames.removeAll(Arrays.asList("", null));
        Set<String> instructorSet = new HashSet<>(instructorNewNames);
        List<String> instructorList = new ArrayList<>(instructorSet);

        model.addAttribute("instructorList", instructorList);
        //model.addAttribute("instructorList", archivedCourseRepository.listInstructorNamesByQuarterInterval(Integer.toString(Quarter.qyyToQyyyy(quarterListService.getStartQuarter())),
        //                Integer.toString(Quarter.qyyToQyyyy(quarterListService.getEndQuarter()))));
        return "search/byinstructor/multiquarter/search";
    }

    @GetMapping("/search/byinstructor/multiquarter/results")
    public String search(
        @RequestParam(name = "instructor", required = true) 
        String instructor,
        @RequestParam(name = "beginQ", required = true) 
        int beginQ,
        @RequestParam(name = "endQ", required = true) 
        int endQ,
        Model model,
        SearchByInstructorMultiQuarter searchObject,
        RedirectAttributes redirAttrs) {

	
	if (instructor  == ""){
            redirAttrs.addFlashAttribute("alertDanger", "You cannot leave instructor blank");
            return "redirect:.";
	}
        if(endQ<beginQ){
             redirAttrs.addFlashAttribute("alertDanger", "End quarter must be later than begin quarter");
             return "redirect:.";
        }

	    logger.info("GET request for /search/byinstructor/results");
            logger.info("beginQ=" + beginQ + " endQ=" + endQ);

	    List<Course> courses = archivedCourseRepository.findByQuarterIntervalAndInstructor(beginQ+"", endQ+"", instructor);
	    
            model.addAttribute("courses", courses);

            List<CourseOffering> courseOfferings = CourseOffering.fromCourses(courses);
            List<CourseListingRow> rows = CourseListingRow.fromCourseOfferings(courseOfferings);

            rows = finalExamService.assignFinalExams(rows);
            
            model.addAttribute("rows", rows);
            model.addAttribute("searchObject", searchObject );

            model.addAttribute("quarters", quarterListService.getQuarters());

            return "search/byinstructor/multiquarter/results";
    }

}
