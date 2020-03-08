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
 * Service object that wraps the UCSB Academic Curriculum Finals API
 */
@Service
public class UCSBFinalService implements FinalService {

    private Logger logger = LoggerFactory.getLogger(UCSBFinalService.class);

    private String apiKey;

    public UCSBFinalService(@Value("${ucsb.api.consumer_key}") String apiKey) {
        this.apiKey = apiKey;
        logger.info("apiKey=" + apiKey);
    }

    public String setQuarter(String quarter){
        String result = "";
        if(quarter.charAt(0) == 'F'){
            result = "20" + quarter.substring(1,3) + "3";
        }
        else if(quarter.charAt(0) == 'W'){
            result = "20" + quarter.substring(1,3) + "1";
        }
        else if(quarter.charAt(0) == 'S'){
            result = "20" + quarter.substring(1,3) + "2";
        }
        return result;
    }

    public String getJSON(String enrollCode, String quarter) {
        
        quarter = setQuarter(quarter); //formats quarter correctly

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ucsb-api-version", "1.0");
        headers.set("ucsb-api-key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String uri = "https://api.ucsb.edu/academics/curriculums/v1/finals";
        String params = String.format("?quarter=%s&enrollCode=%s", quarter, enrollCode);
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
        logger.info("from UCSBFinalService.getJSON: " + retVal);
        return retVal;
    }

    /**
     * Gets the json response for a query for a given course's final in a given quarter.
     * 
     * @param enrollCode enrollCode of a course, e.g. 07393
     * @param quarter quarter as an integer, e.g. 20201 for W20
     * @return json results from API
     */

    public String getFinal(int enrollCode, int quarter) {
        logger.info("api: " + apiKey);
        logger.info("getCourse: course: " + enrollCode + " quarter: " + quarter);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ucsb-api-version", "1.0");
        headers.set("ucsb-api-key", this.apiKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String uri = "https://api.ucsb.edu/academics/curriculums/v1/finals";
        String params = String.format("?quarter=%d&enrollCode=%d", quarter, enrollCode);

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
        logger.info("from UCSBFinalService.getJSON: " + retVal);
        return retVal;
    }
}