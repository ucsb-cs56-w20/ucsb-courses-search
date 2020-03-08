package edu.ucsb.cs56.ucsb_courses_search.controller.advice;

import edu.ucsb.cs56.ucsb_courses_search.service.FeatureToggleService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class FeatureToggleControllerAdvice {

    @Autowired
    FeatureToggleService fts;

    @ModelAttribute("feature_multipleSchedules")
    public Boolean getMultipleSchedules(){
        return fts.getMultipleSchedules();
    }

}
