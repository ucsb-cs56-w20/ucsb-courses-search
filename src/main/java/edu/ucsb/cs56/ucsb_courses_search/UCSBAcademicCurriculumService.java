package edu.ucsb.cs56.ucsb_courses_search;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ucsb-api-version", "1.0");
        headers.set("ucsb-api-key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String uri = "https://api.ucsb.edu/academics/curriculums/v1/classes/search";
        String params = String.format("?quarter=%s&subjectCode=%s&objLevelCode=%s&pageNumber=%d&pageSize=%d&includeClassSections=%s",
            quarter, subjectArea, courseLevel, 1, 10, "true");
        String url = uri + params;
        logger.info("url=" + url);

        String retVal="";
        try {   
            ResponseEntity<String> re = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
             MediaType contentType = re.getHeaders().getContentType();
            HttpStatus statusCode = re.getStatusCode();
            retVal = re.getBody();
        } catch (HttpClientErrorException e) {
            retVal = "{\"error\": \"401: Unauthorized\"}";
        }
        logger.info("from UCSBAcademicCurriculumService.getJSON: " + retVal);
        return retVal;
    }

}