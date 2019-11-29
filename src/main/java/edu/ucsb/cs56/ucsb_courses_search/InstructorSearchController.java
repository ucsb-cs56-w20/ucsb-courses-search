package edu.ucsb.cs56.ucsb_courses_search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

@Controller
public class InstructorSearchController {

    @Autowired   
    private CurriculumService curriculumService;

    @GetMapping("/instructor")
    public String instructor(Model model) {
    	model.addAttribute("instructorSearchObject", new MyInstructorSearchResult());
        return "instructor";
    }

	@GetMapping("/instructorResults")
    public String search(
        @RequestParam(name = "instructor", required = true) 
        String instructor,
        @RequestParam(name = "quarter", required = true) 
        String quarter, 
        Model model
        ) {
        model.addAttribute("instructor", instructor);
        model.addAttribute("quarter", quarter);
        /*
        String json = curriculumService.getJSON(subjectArea,quarter,courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);

        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        */
        return "InstructorResults";
    }



    

   

}