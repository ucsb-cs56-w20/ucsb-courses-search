package edu.ucsb.cs56.ucsb_courses_search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import edu.ucsb.cs56.ucsb_courses_search.ScheduleAdvice;

import edu.ucsb.cs56.ucsb_courses_search.entities.Course;
import edu.ucsb.cs56.ucsb_courses_search.repositories.CourseRepository;

@Controller
public class CourseController {

    private ScheduleAdvice scheduleAdvice;

    @Autowired
    public CourseController(ScheduleAdvice scheduleAdvice) {
        this.scheduleAdvice = scheduleAdvice;   
    }
    
    @GetMapping("/courseschedule")
    public String index(Model model) {
        scheduleAdvice.getClassname("Underwater");
        CourseRepository input = scheduleAdvice.getRepo();
        Iterable<Course> myclasses = input.findAll();
        model.addAttribute("myclasses", myclasses);
        return "courseschedule/index";
    }

}