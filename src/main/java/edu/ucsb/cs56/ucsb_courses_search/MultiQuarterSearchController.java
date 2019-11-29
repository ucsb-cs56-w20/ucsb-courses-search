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
	model.addAttribute("multiSearchObject", new MyMultiSearchResult());
        return "multi";
    }

    
    @GetMapping("/multiResults")
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
	String json = curriculumService.getJSON(instructor,quarter);

        CoursePage cp = CoursePage.fromJSON(json);

        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
	*/

        return "multiResults"; // corresponds to src/main/resources/templates/searchResults.html
    }

 

   

}
