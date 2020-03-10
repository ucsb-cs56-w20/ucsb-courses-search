package edu.ucsb.cs56.ucsb_courses_search.service;

import javax.annotation.PostConstruct;
import javax.persistence.SecondaryTable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class provides a container for values defined
 * in application.properties that are used as feature toggles.
 * For example, app.feature.multiple_schedules.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FeatureToggleService {

    private Logger logger = LoggerFactory.getLogger(FeatureToggleService.class);

    @Value("${app.feature.multiple_schedules:false}")
    private Boolean multipleSchedules;

    /**
     * This method exists only as a place to put logging statements
     * for the value of feature toggles set via Value annotations.
     * 
     * @See:  <a href="https://stackoverflow.com/a/46355745">This Stack Overflow Answer</a>
     */
    @PostConstruct
    public void logFeatureToggles() {
        logger.info("multipleSchedules="+multipleSchedules);
    }

    public boolean getMultipleSchedules() {
        return multipleSchedules != null && multipleSchedules;
    }
}
