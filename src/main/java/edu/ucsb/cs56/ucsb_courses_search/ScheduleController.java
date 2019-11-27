package edu.ucsb.cs56.ucsb_courses_search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScheduleController {

    @GetMapping("/courseschedule")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
	model.addAttribute("name", name);
	return "courseschedule/index"; // corresponds to src/main/resources/templates/courseschedule/index.html
    }

    
}
