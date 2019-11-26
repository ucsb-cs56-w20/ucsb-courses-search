package edu.ucsb.cs56.ucsb_courses_search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

@Controller
public class MultiQuarterSearchController {

    @Autowired   
    private CurriculumService curriculumService;

    @GetMapping("/multi")
    public String multi(Model model) {
        return "multi";
    }

    

   

}
