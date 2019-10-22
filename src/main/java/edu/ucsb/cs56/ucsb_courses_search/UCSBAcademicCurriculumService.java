package edu.ucsb.cs56.ucsb_courses_search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Service object that wraps the UCSB Academic Curriculum API
 */
@Service
public class UCSBAcademicCurriculumService implements CurriculumService {

    private Logger logger = LoggerFactory.getLogger(UCSBAcademicCurriculumService.class);
    private String apiKey;

    public UCSBAcademicCurriculumService(@Value("${ucsb.api.consumer_key}") String apiKey) {
        this.apiKey = apiKey;
        logger.info("apiKey=" + apiKey);
    }

    public String getJSON(String subjectArea, String quarter, String courseLevel) {
        String retVal = "{\"this\": \"is a stub\"}";
        logger.info("from UCSBAcademicCurriculumService.getJSON: " + retVal);
        return retVal;
    }

}