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

    private CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;   
    }
    
    @GetMapping("/courseschedule")
    public String index(Model model) {
        courseRepository.findByClassname("Under");
        Iterable<Course> myclasses = courseRepository.findAll();
        model.addAttribute("myclasses", myclasses);
        return "courseschedule/index";
    }

}