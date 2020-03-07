package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Controller
public class GoogleCalendarController {

    private Logger logger = LoggerFactory.getLogger(GoogleCalendarController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public GoogleCalendarController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    private Iterable<Course> getClassesFromRepo(OAuth2AuthenticationToken token){
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        logger.info("uid="+uid);
        logger.info("courseRepository="+courseRepository);
        Iterable<Course> myclasses = courseRepository.findByUid(uid);
        return myclasses;
    }

    @GetMapping("/GoogleCalendar")
    //<button type="submit" th:formaction="@{/searchResults}" class="btn btn-primary" id="js-course-search-submit">Find Courses</button> use this format to create button in templates/courseschedule/index.html
    //function goes here
    public String exportToGoogleCalendar(Model model, OAuth2AuthenticationToken token) {
        logger.info("Inside /GoogleCalendar controller method GoogleCalendarController#exportToGoogleCalendar");

        GoogleCalendarService gService = new GoogleCalendarService();

        if (token!=null) {
            Iterable<Course> myclasses = getClassesFromRepo(token);
            try{
                gService.createGoogleCalendar(myclasses);
            } catch (IOException e) {
                System.out.println("IOException caught");
            } catch (GeneralSecurityException e) {
                System.out.println("GeneralSecurityException caught");
            }
        }
        //if not logged in, handle emptyList case from CourseController
        return "courseschedule/index";
    }

}