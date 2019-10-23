package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CoursePage object, represents a page of returned {@link Course} objects from
 * the <a href="https://developer.ucsb.edu/content/academic-curriculums">UCSB
 * Academic Curriculums API</a>
 * 
 */

@Data
public class CoursePage {

    private static Logger logger = LoggerFactory.getLogger(CoursePage.class);

    private int pageNumber;
    private int pageSize;
    private int total;
    private List<Course> classes;

    /**
     * Create a CoursePage object from json representation
     * 
     * @param json String of json returned by API endpoint {@code /classes/search}
     * @return a new CoursePage object
     * @see <a href=
     *      "https://developer.ucsb.edu/content/academic-curriculums">https://developer.ucsb.edu/content/academic-curriculums</a>
     */
    public static CoursePage fromJSON(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
            CoursePage coursePage = objectMapper.readValue(json, CoursePage.class);
            return coursePage;
        } catch (JsonProcessingException jpe) {
            logger.error("JsonProcessingException:" + jpe);
            return null;
        }
        
    }
}
