package edu.ucsb.cs56.ucsb_courses_search.service;

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
        String params = String.format(
                "?quarter=%s&subjectCode=%s&objLevelCode=%s&pageNumber=%d&pageSize=%d&includeClassSections=%s", quarter,
                subjectArea, courseLevel, 1, 100, "true");
        String url = uri + params;

        if (courseLevel.equals("A")) {
            params = String.format(
                    "?quarter=%s&subjectCode=%s&pageNumber=%d&pageSize=%d&includeClassSections=%s",
                    quarter, subjectArea, 1, 100, "true");
            url = uri + params;
        }

        logger.info("url=" + url);

        String retVal = "";
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

    public String getJSON(String instructor, String quarter) {
        logger.info("api: " + apiKey);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ucsb-api-version", "1.0");
        headers.set("ucsb-api-key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String uri = "https://api.ucsb.edu/academics/curriculums/v1/classes/search";
        String params = String.format("?quarter=%s&instructor=%s&pageNumber=%d&pageSize=%d&includeClassSections=%s",
                quarter, instructor, 1, 100, "true");
        String url = uri + params;
        logger.info("url=" + url);

        String retVal = "";
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



    /**
     * Gets the json response for a query for a given course in a given quarter.
     * 
     * @param course  name of course, e.g. "CMPSC 130A"
     * @param quarter quarter as an integer, e.g. 20201 for W20
     * @return json results from API
     */

    public String getCourse(String course, int quarter) {
        logger.info("api: " + apiKey);
        logger.info("getCourse: course: " + course + " quarter: " + quarter);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ucsb-api-version", "1.0");
        headers.set("ucsb-api-key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String uri = "https://api.ucsb.edu/academics/curriculums/v1/classes/search";
        String params = String.format("?quarter=%d&courseId=%s&pageNumber=%d&pageSize=%d&includeClassSections=%s",
                quarter, course, 1, 100, "true");

        String url = uri + params;
        logger.info("url=" + url);

        String retVal = "";
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


    public String getGE(String college, String areas, String quarter) {
        logger.info("api: " + apiKey);
        logger.info("getGE: college: " + college + " areas: " + areas +" quarter: " + quarter);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ucsb-api-version", "1.0");
        headers.set("ucsb-api-key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String uri = "https://api.ucsb.edu/academics/curriculums/v1/classes/search";
        String params = String.format(
                "?college=%s&areas=%s&quarter=%s&pageNumber=%d&pageSize=%d&includeClassSections=%s",
                college, areas, quarter, 1, 100, "true");
        String url = uri + params;
        logger.info("url=" + url);

        String retVal = "";
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
