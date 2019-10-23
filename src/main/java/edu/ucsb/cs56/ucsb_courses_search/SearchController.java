package edu.ucsb.cs56.ucsb_courses_search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

@Controller
public class SearchController {

    @Autowired   
    private CurriculumService curriculumService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("searchObject", new MySearchResult());
        return "index";
    }

    @GetMapping("/searchResults")
    public String search(
        @RequestParam(name = "subjectArea", required = true) 
        String subjectArea,
        @RequestParam(name = "quarter", required = true) 
        String quarter, 
        @RequestParam(name = "courseLevel", required = true) 
        String courseLevel,  
        Model model
        ) {
        model.addAttribute("subjectArea", subjectArea);
        model.addAttribute("quarter", quarter);
        model.addAttribute("courseLevel", courseLevel);
        
        String json = curriculumService.getJSON(subjectArea,quarter,courseLevel);

        CoursePage cp = CoursePage.fromJSON(json);

        model.addAttribute("json",json);
        model.addAttribute("cp",cp);

        return "searchResults"; // corresponds to src/main/resources/templates/searchResults.html
    }

}