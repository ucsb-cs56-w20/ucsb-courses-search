package edu.ucsb.cs56.ucsb_courses_search.controller;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.FinalPage;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseStatus;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.model.WeeklyView;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;
import edu.ucsb.cs56.ucsb_courses_search.service.CalendarService;
import edu.ucsb.cs56.ucsb_courses_search.service.FinalService;
import edu.ucsb.cs56.ucsb_courses_search.service.MembershipService;
import edu.ucsb.cs56.ucsb_courses_search.service.UCSBBuildingService;

import org.springframework.beans.factory.annotation.Qualifier;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucsb.cs56.ucsb_courses_search.service.ScheduleItemService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.FinalPage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.QuarterDeadlines;


@ResponseStatus(HttpStatus.FORBIDDEN)
class AccessForbiddenException extends RuntimeException {
}

@Controller
public class CourseController {

    private Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired(required = false)
    private FinalService finalService;

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    @Autowired 
    private CalendarService calendarservice;

    @Autowired
    private MembershipService membershipService;
	
    @Autowired
    private ScheduleItemService scheduleItemService;

    @Autowired
    public CourseController(ScheduleItemRepository scheduleItemRepository, MembershipService membershipService,
			   ScheduleItemService scheduleItemService) {
        this.scheduleItemRepository = scheduleItemRepository;
	      this.membershipService = membershipService;
	      this.scheduleItemService = scheduleItemService;
    }


    @GetMapping("/courseschedule")
    public String index(Model model, OAuth2AuthenticationToken token) throws AccessForbiddenException {

        logger.info("Inside /courseschedule controller method CourseController#index");
        logger.info("model=" + model + " token=" + token);

        String json_ = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json_);
        quarterdeadline.format();


        if (token!=null && this.membershipService.isMember(token)) {
            String uid = token.getPrincipal().getAttributes().get("sub").toString();
            logger.info("uid="+uid);
            Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(uid);
            Set<FinalPage> myfinals = new LinkedHashSet<FinalPage>();
            for(ScheduleItem scheduleItem : myclasses){
                String json = finalService.getJSON(scheduleItem.getEnrollCode(), scheduleItem.getQuarter());
                logger.info(json);
                FinalPage fp = FinalPage.fromJSON(json);
                fp.setCourseName(scheduleItem.getClassname());
                myfinals.add(fp);
            }
            model.addAttribute("myfinals", myfinals);
            logger.info("scheduleItemRepository="+scheduleItemRepository);
            String[] days = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday"};
            
            WeeklyView week = new WeeklyView(myclasses);

            model.addAttribute("myclasses", myclasses);
            model.addAttribute("days", days);
            model.addAttribute("timerange", week.getTimeRange());
            model.addAttribute("week",week);
            model.addAttribute ("calendar", quarterdeadline);
        } else {
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
      
        //here we find and delete the courses if they already exist.
        List<ScheduleItem> myClasses = scheduleItemRepository.findByUid(scheduleItem.getUid());
        scheduleItemService.deleteByClassname(myClasses, scheduleItem.getClassname());

        scheduleItemRepository.save(primary);
        myClasses.add(primary);
        scheduleItemRepository.save(scheduleItem);
        myClasses.add(scheduleItem);

        model.addAttribute("myclasses", myClasses);
        return "redirect:/courseschedule";
    }

    @PostMapping("/courseschedule/delete")
    public String delete(ScheduleItem scheduleItem, Model model){
        logger.info("ScheduleItem to delete: " + scheduleItem.getUid());
        List<ScheduleItem> myClasses = scheduleItemRepository.findByUid(scheduleItem.getUid());
	
        scheduleItemService.deleteByClassname(myClasses, scheduleItem.getClassname());

        Set<FinalPage> myfinals = new LinkedHashSet<FinalPage>();
        for(ScheduleItem si : myClasses){
            String json = finalService.getJSON(si.getEnrollCode(), si.getQuarter());
            logger.info(json);
            FinalPage fp = FinalPage.fromJSON(json);
            fp.setCourseName(si.getClassname());
            myfinals.add(fp);
        }
        model.addAttribute("myfinals", myfinals);
        model.addAttribute("myclasses", scheduleItemRepository.findByUid(scheduleItem.getUid()));

        String json_ = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json_);
        quarterdeadline.format();

        model.addAttribute ("calendar", quarterdeadline);

        return "redirect:/courseschedule";
    }

    @PostMapping("/courseschedule/addLecture")
    public String addLecture(ScheduleItem scheduleItem, Model model) {
        logger.info("Hello!\n");
        logger.info("ScheduleItem's uid: " + scheduleItem.getUid());
        logger.info("ScheduleItem = " + scheduleItem);
        scheduleItemRepository.save(scheduleItem);

        Iterable<ScheduleItem> myclasses = scheduleItemRepository.findByUid(scheduleItem.getUid());
        Set<FinalPage> myfinals = new LinkedHashSet<FinalPage>();
        for(ScheduleItem si : myclasses){
            String json = finalService.getJSON(si.getEnrollCode(), si.getQuarter());
            logger.info(json);
            FinalPage fp = FinalPage.fromJSON(json);
            fp.setCourseName(si.getClassname());
            myfinals.add(fp);
        }
        model.addAttribute("myfinals", myfinals);

        model.addAttribute("myclasses", scheduleItemRepository.findByUid(scheduleItem.getUid()));

        String json_ = calendarservice.getJSON();
        QuarterDeadlines quarterdeadline = QuarterDeadlines.fromJSON(json_);
        quarterdeadline.format();

        model.addAttribute ("calendar", quarterdeadline);
        
        return "redirect:/courseschedule";
    }

}
