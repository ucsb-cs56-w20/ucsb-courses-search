package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import org.springframework.beans.factory.annotation.Qualifier;
import edu.ucsb.cs56.ucsb_courses_search.entity.Schedule;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleRepository;

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
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private MembershipService membershipService;


    @Autowired
    private ScheduleRepository scheduleRepository;

    public CourseController(ScheduleItemRepository scheduleItemRepository, MembershipService membershipService, ScheduleRepository scheduleRepository) {
        this.scheduleItemRepository = scheduleItemRepository;
        this.membershipService = membershipService;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) throws AccessForbiddenException {
        
        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        if (token!=null && this.membershipService.isMember(token)) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("scheduleItemRepository="+scheduleItemRepository);
            Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(uid);
            Iterable<Schedule> myschedules = scheduleRepository.findByUid(uid);
            // get all schedule ids by uid
            // get courses by each scheduleid to a list
            // stores in a list of schedules
            // Iterable<Course> myclasses = courseRepository.findByScheduleid(scheduleid);
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);
            model.addAttribute("myschedules", myschedules);
        } else {
            //ArrayList<Course> emptyList = new ArrayList<Course>();
            //model.addAttribute("myclasses", emptyList);
	    //org.springframework.security.access.AccessDeniedException("403 returned");
	    throw new AccessForbiddenException();
        }
        return "courseschedule/index";
    }

    @PostMapping("/courseschedule/add")
    public String add(ScheduleItem scheduleItem, 
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
        logger.info("ScheduleItem's uid: " + scheduleItem.getUid());
        logger.info("ScheduleItem = " + scheduleItem);                   
        scheduleItemRepository.save(scheduleItem);

        ScheduleItem primary = new ScheduleItem();
        primary.setClassname(lecture_classname);
        primary.setEnrollCode(lecture_enrollCode);
        primary.setUid(lecture_uid);
        primary.setProfessor(lecture_professor);
        primary.setMeetday(lecture_meetday);
        primary.setMeettime(lecture_meettime);
        primary.setLocation(lecture_location);
        primary.setQuarter(lecture_quarter);
        logger.info("primary = " + primary); 
        scheduleItemRepository.save(primary);

        model.addAttribute("myclasses", scheduleItemRepository.findByUid(scheduleItem.getUid()));
        return "courseschedule/index";
    }
    @PostMapping("/courseschedule/add/{scheduleid}")
    public String add(
        @PathVariable("scheduleid") long scheduleid, 
        ScheduleItem scheduleItem, Model model
        ) {
        scheduleItem.setScheduleid(scheduleid);

        scheduleItemRepository.save(scheduleItem);
        // model.addAttribute("myclasses", courseRepository.findByScheduleid(scheduleid));
        return "redirect:/courseschedule";
    }

}
