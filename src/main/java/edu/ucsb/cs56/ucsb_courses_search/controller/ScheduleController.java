package edu.ucsb.cs56.ucsb_courses_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import edu.ucsb.cs56.ucsb_courses_search.entity.Schedule;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleRepository;
import edu.ucsb.cs56.ucsb_courses_search.formbeans.ScheduleSearch;

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
    public String index(Model model, OAuth2AuthenticationToken token, ScheduleSearch scheduleSearch) throws AccessForbiddenException {
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
            //model.addAttribute("quarters", quarterListService.getQuarters());
        } else {
	        throw new AccessForbiddenException();
        }
        model.addAttribute("scheduleSearch", scheduleSearch);
        return "schedule/index";
    }
    @PostMapping("/schedule/add")
    public String add(  ScheduleItem scheduleItem, 
                        OAuth2AuthenticationToken token,
                        @RequestParam String lecture_classname,
                        @RequestParam String lecture_enrollCode,
                        @RequestParam String lecture_professor,
                        @RequestParam String lecture_meettime,
                        @RequestParam String lecture_meetday,
                        @RequestParam String lecture_location,
                        @RequestParam String lecture_quarter) {
        logger.info("ScheduleItem = " + scheduleItem); 
        
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        List<Schedule> myschedules = scheduleRepository.findByUid(uid);
        long scheduleid = myschedules.size()-1;
        for (int i = 0; i < myschedules.size(); i++) {
            if (myschedules.get(i).getIsActive() == true) {
                scheduleid = myschedules.get(i).getScheduleid();
            }
        }

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
        // scheduleItemRepository.save(primary);

        return "redirect:/schedule";
    }

    @PostMapping("/schedule/create")
    public String add_schedule(OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        Schedule newschedule = new Schedule();
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        newschedule.setUid(uid);
        newschedule.setSchedulename("New Schedule");
        scheduleRepository.save(newschedule);
        redirAttrs.addFlashAttribute("alertSuccess", "Schedule Created!");
        return "redirect:/schedule/";
    }

    @PostMapping("/schedule/isactive/{scheduleid}")
    public String setisactive_schedule( @PathVariable("scheduleid") Long scheduleid, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        List<Schedule> myschedules = scheduleRepository.findByUid(uid);
        Schedule schedule = new Schedule();
        for (int i = 0; i < myschedules.size(); i++) {
            if (myschedules.get(i).getScheduleid() == scheduleid) {
                schedule = (myschedules.get(i));
            }
            else{
                myschedules.get(i).setIsActive(false);
            }
        }
        schedule.setIsActive(true);
        logger.info("SCHEDULE IS ACTIVE: " + schedule.getIsActive());
        scheduleRepository.save(schedule);
        redirAttrs.addFlashAttribute("alertSuccess", "Schedule Set to Active");
        return "redirect:/schedule/{scheduleid}";
    }

    @PostMapping("/schedule/rename/{scheduleid}")
    public String rename_schedule( @PathVariable("scheduleid") Long scheduleid, String newName, OAuth2AuthenticationToken token, RedirectAttributes redirAttrs) {
        String uid = token.getPrincipal().getAttributes().get("sub").toString();
        List<Schedule> myschedules = scheduleRepository.findByUid(uid);
        Schedule schedule = new Schedule();
        for (int i = 0; i < myschedules.size(); i++) {
            if (myschedules.get(i).getScheduleid() == scheduleid) {
                schedule = (myschedules.get(i));
            }
        }
        schedule.setSchedulename(newName);
        scheduleRepository.save(schedule);
        redirAttrs.addFlashAttribute("alertSuccess", "Schedule Renamed");
        return "redirect:/schedule/{scheduleid}";
    }

    @GetMapping("/schedule/{scheduleid}")
    public String viewSchedule(Model model, OAuth2AuthenticationToken token, @PathVariable("scheduleid") Long scheduleid) {
        
        logger.info("Inside /schedule controller method ScheduleController#viewschedule");
        logger.info("model=" + model + " token=" + token);

        if (token!=null) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            logger.info("scheduleItemRepository="+scheduleItemRepository);
            List<Schedule> myschedules = scheduleRepository.findByUid(uid);// get all schedule ids by uid
            // get courses by each scheduleid to a list
            Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByScheduleid(scheduleid);
            Schedule schedule = new Schedule();
            for (int i = 0; i < myschedules.size(); i++) {
                if (myschedules.get(i).getScheduleid() == scheduleid) {
                    schedule = (myschedules.get(i));
                }
            }
            String schedulename = schedule.getSchedulename();
            // logger.info("there are " + myclasses.size() + " courses that match uid: " + uid);
            model.addAttribute("schedulename", schedulename);
            model.addAttribute("myclasses", myclasses);
            model.addAttribute("myschedules", myschedules);
        } else {
            ArrayList<ScheduleItem> emptyList = new ArrayList<ScheduleItem>();
            model.addAttribute("myclasses", emptyList);
        }
        return "schedule/viewschedule";
    }

}
