package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
    public String add(Course course, 
                        Model model,
                        @RequestParam String lecture_classname,
                        @RequestParam String lecture_enrollCode,
                        @RequestParam String lecture_uid,
                        @RequestParam String lecture_professor,
                        @RequestParam String lecture_meettime,
                        @RequestParam String lecture_meetday,
                        @RequestParam String lecture_location,
                        @RequestParam String lecture_quarter) {
        logger.info("Hello!\n");
        logger.info("course's uid: " + course.getUid());
        logger.info("course = " + course);                   
        courseRepository.save(course);

        Course primary = new Course();
        primary.setClassname(lecture_classname);
        primary.setEnrollCode(lecture_enrollCode);
        primary.setUid(lecture_uid);
        primary.setProfessor(lecture_professor);
        primary.setMeetday(lecture_meetday);
        primary.setMeettime(lecture_meettime);
        primary.setLocation(lecture_location);
        primary.setQuarter(lecture_quarter);
        logger.info("primary = " + primary); 
        courseRepository.save(primary);

        model.addAttribute("myclasses", courseRepository.findByUid(course.getUid()));
        return "courseschedule/index";
    }

}
