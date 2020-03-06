package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.ucsb.cs56.ucsb_courses_search.entity.Course;
import edu.ucsb.cs56.ucsb_courses_search.repository.CourseRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResponseStatus(HttpStatus.FORBIDDEN)
class AccessForbiddenException extends RuntimeException {
}

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MembershipService membershipService;


    @Autowired
    public CourseController(CourseRepository courseRepository, MembershipService membershipService) {
        this.courseRepository = courseRepository;
	this.membershipService = membershipService;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) throws AccessForbiddenException {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        if (token!=null && this.membershipService.isMember(token)) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("courseRepository="+courseRepository);
            Iterable<Course> myclasses = courseRepository.findByUid(uid);
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);
            model.addAttribute("myclasses", myclasses);
        } else {
            //ArrayList<Course> emptyList = new ArrayList<Course>();
            //model.addAttribute("myclasses", emptyList);
	    //org.springframework.security.access.AccessDeniedException("403 returned");
	    throw new AccessForbiddenException();
        }
        return "courseschedule/index";
    }

    /**
     * Deletes all courses with the same name as "name" from "courseList" and CourseRepository.
     * Returns a list without any courses of name "name".
    */
    private List<Course> deleteByClassname(List<Course> courseList, String name){
        List<Course> left = new ArrayList<Course>(courseList);
        for (Course i : courseList){
            if (i.getClassname().equals(name)){
                courseRepository.delete(i);
                left.remove(i);
            }
        }
        return left;
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

        //here we find and delete the courses if they already exist.
        List<Course> myClasses = courseRepository.findByUid(course.getUid());
        myClasses = deleteByClassname(myClasses, course.getClassname());

        courseRepository.save(primary);
        myClasses.add(primary);
        courseRepository.save(course);
        myClasses.add(course);

        model.addAttribute("myclasses", myClasses);
        return "courseschedule/index";
    }

    @PostMapping("/courseschedule/delete")
    public String delete(Course course, Model model){
        logger.info("course to delete: " + course.getUid());
        List<Course> myClasses = courseRepository.findByUid(course.getUid());
        List<Course> remainingClasses = deleteByClassname(myClasses, course.getClassname());

        model.addAttribute("myclasses", remainingClasses);
        return "courseschedule/index";
    }

}
