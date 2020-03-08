package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.GoogleCalendarService;
import edu.ucsb.cs56.ucsb_courses_search.service.GoogleCalendarCallbackService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);
    @Autowired
    private GoogleCalendarService googleCalendarService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private GoogleCalendarCallbackService googleCalendarCallbackService;

    @Autowired
    public CourseController(CourseRepository courseRepository, GoogleCalendarService googleCalendarService, GoogleCalendarCallbackService googleCalendarCallbackService) {
        this.courseRepository = courseRepository;
        this.googleCalendarService = googleCalendarService;
        this.googleCalendarCallbackService = googleCalendarCallbackService;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        if (token!=null) {
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

    //@GetMapping("/GoogleCalendar")
    /*public String exportToGoogleCalendar(Model model, OAuth2AuthenticationToken token) {
        
        logger.info("Inside /GoogleCalendar controller method CourseController#exportToGoogleCalendar");
        logger.info("model=" + model + " token=" + token);


        if (token!=null) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("courseRepository="+courseRepository);
            Iterable<Course> myclasses = courseRepository.findByUid(uid);
            googleCalendarService.setClasses(myclasses);
            try{
                //googleCalendarService.createGoogleCalendar(token);
                logger.info("Google Calendar created");
            } catch (IOException e) {
                logger.error("IOException caught");
            } catch (GeneralSecurityException e) {
                logger.error("GeneralSecurityException caught");
            }
        }
        
        model.addAttribute("message", "Export completed!");

        return "courseschedule/index";
    }*/
}
