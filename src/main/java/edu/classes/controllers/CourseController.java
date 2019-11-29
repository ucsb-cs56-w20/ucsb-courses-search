package classes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import classes.entities.Course;
import classes.repositories.CourseRepository;

@Controller
public class CourseController {

    private CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;   
    }
    
    @GetMapping("/courseschedule")
    public String index(Model model) {
        Iterable<Course> myclasses = courseRepository.findAll();
        model.addAttribute("myclasses", myclasses);
        return "courseschedule/index";
    }

}