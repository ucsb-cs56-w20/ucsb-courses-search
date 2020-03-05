package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.entity.Schedule;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository, ScheduleRepository scheduleRepository) {
        this.courseRepository = courseRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        if (token!=null) {
            String uid = token.getPrincipal().getAttributes().get("id").toString();
            logger.info("uid="+uid);
            logger.info("courseRepository="+courseRepository);
            List<Schedule> myschedules = scheduleRepository.findByUid(uid);// get all schedule ids by uid
            // get courses by each scheduleid to a list
            Schedule lastSchedule = myschedules.get(myschedules.size() -1);
            Iterable<Course> myclasses = courseRepository.findByScheduleid(lastSchedule.getScheduleid());
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);
            model.addAttribute("myclasses", myclasses);
            model.addAttribute("myschedules", myschedules);
        } else {
            ArrayList<Course> emptyList = new ArrayList<Course>();
            model.addAttribute("myclasses", emptyList);
        }
        return "courseschedule/index";
    }
    @PostMapping("/courseschedule/add/{scheduleid}")
    public String add(
        @PathVariable("scheduleid") long scheduleid, 
        Course course, Model model
        ) {
        course.setScheduleid(scheduleid);
        courseRepository.save(course);
        return "redirect:/courseschedule";
    }

}
