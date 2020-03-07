package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.UCSBQuarterCalendarService;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.QuarterDeadlines;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired 
        private CourseRepository courseRepository; 

    @Autowired 
        private UCSBQuarterCalendarService calendarservice;
    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        String json = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json);

        if (token!=null) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("courseRepository="+courseRepository);
            Iterable<Course> myclasses = courseRepository.findByUid(uid);

            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);

            //how is this getting the info to index.html
            model.addAttribute("myclasses", myclasses);
            model.addAttribute ("calendar", quarterdeadline);
        } else {
            ArrayList<Course> emptyList = new ArrayList<Course>();
            model.addAttribute("myclasses", emptyList);
        }
        return "courseschedule/index";
    }
    @PostMapping("/courseschedule/add")
    public String add(Course course, Model model) {
        logger.info("Hello!\n");
        logger.info("course's uid: " + course.getUid());

        courseRepository.save(course);
        model.addAttribute("myclasses", courseRepository.findByUid(course.getUid()));
        return "courseschedule/index";
    }

}
