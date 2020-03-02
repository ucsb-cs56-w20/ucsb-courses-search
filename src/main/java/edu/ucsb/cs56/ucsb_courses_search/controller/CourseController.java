package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MembershipService membershipService;


    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        if (token!=null && membershipService.isMember(token)) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("courseRepository="+courseRepository);
            Iterable<Course> myclasses = courseRepository.findByUid(uid);
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);
            model.addAttribute("myclasses", myclasses);
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
