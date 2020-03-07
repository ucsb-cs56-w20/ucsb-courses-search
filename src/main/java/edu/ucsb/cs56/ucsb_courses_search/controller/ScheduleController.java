package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import edu.ucsb.cs56.ucsb_courses_search.entity.Schedule;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ScheduleController {

    private Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MembershipService membershipService;

    @Autowired
    public ScheduleController(ScheduleItemRepository scheduleItemRepository, MembershipService membershipService, ScheduleRepository scheduleRepository) {
        this.scheduleItemRepository = scheduleItemRepository;
	    this.membershipService = membershipService;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/schedule")
    public String index(Model model, OAuth2AuthenticationToken token) throws AccessForbiddenException {
        logger.info("Inside /schedule controller method ScheduleController#index");
        logger.info("model=" + model + " token=" + token);

        if (token!=null && this.membershipService.isMember(token)) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("scheduleItemRepository="+scheduleItemRepository);
            List<Schedule> myschedules = scheduleRepository.findByUid(uid);
            List<ScheduleItem> myclasses;
            if(myschedules.size() > 0){
                Schedule lastSchedule = myschedules.get(myschedules.size()-1);
                myclasses = scheduleItemRepository.findByScheduleid(lastSchedule.getScheduleid());
            }
            else{
                myclasses = new ArrayList<ScheduleItem>();
            }
            model.addAttribute("myclasses", myclasses);
            model.addAttribute("myschedules", myschedules);
        } else {
	        throw new AccessForbiddenException();
        }
        return "schedule/index";
    }
    @PostMapping("/schedule/add/{scheduleid}")
    public String add( @PathVariable("scheduleid") Long scheduleid,
                        ScheduleItem scheduleItem, 
                        @RequestParam String lecture_classname,
                        @RequestParam String lecture_enrollCode,
                        @RequestParam String lecture_professor,
                        @RequestParam String lecture_meettime,
                        @RequestParam String lecture_meetday,
                        @RequestParam String lecture_location,
                        @RequestParam String lecture_quarter) {
        logger.info("ScheduleItem = " + scheduleItem); 
        scheduleItem.setScheduleid(scheduleid);                  
        scheduleItemRepository.save(scheduleItem);

        ScheduleItem primary = new ScheduleItem();
        primary.setClassname(lecture_classname);
        primary.setEnrollCode(lecture_enrollCode);
        primary.setProfessor(lecture_professor);
        primary.setMeetday(lecture_meetday);
        primary.setMeettime(lecture_meettime);
        primary.setLocation(lecture_location);
        primary.setQuarter(lecture_quarter);
        primary.setScheduleid(scheduleid);
        logger.info("primary = " + primary); 
        scheduleItemRepository.save(primary);

        return "redirect:/schedule";
    }

}
