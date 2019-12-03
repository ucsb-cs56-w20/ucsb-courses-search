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


    @GetMapping("/instructor/search")
    public String instructor(Model model) {
    	model.addAttribute("searchObject", new MySearchResult());
        return "instructor/search"; // corresponds to src/main/resources/templates/instructor/search.html
    }

	@GetMapping("/instructorResults")
    public String singleQtrSearch(
        @RequestParam(name = "instructor", required = true) 
        String instructor,
        @RequestParam(name = "quarter", required = true) 
        String quarter, 
        Model model
        ) {
        model.addAttribute("instructor", instructor);
        model.addAttribute("quarter", quarter);
        
        // calls curriculumService method to get JSON from UCSB API
        String json = curriculumService.getJSON(instructor, quarter);
        
        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage.fromJSON(json);
        
        // adds the json and CoursePage object as attributes so they can be accessed in the html, e.g. ${json} or ${cp.classes}
        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        
        return "instructorResults"; // corresponds to src/main/resources/templates/instructorResults.html
    }

    @GetMapping("/instructor/multi")
    public String multi(Model model) {
    	model.addAttribute("searchObject", new MySearchResult());
        return "instructor/multi"; // corresponds to src/main/resources/templates/instructor/multi.html
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
        
        // calls curriculumService method to get JSON from UCSB API
        // Note: the same curriculum service as the single quarter search above is used, so we will need to implement a function that takes in multiple quarters
	    String json = curriculumService.getJSON(instructor, quarter);
        
        // maps json to a CoursePage object so values can be easily accessed
        CoursePage cp = CoursePage.fromJSON(json);
        
        // adds the json and CoursePage object as attributes so they can be accessed in the html, e.g. ${json} or ${cp.classes}
        model.addAttribute("json",json);
        model.addAttribute("cp",cp);
        

        return "multiResults"; // corresponds to src/main/resources/templates/multiResults.html
    }

   

}
