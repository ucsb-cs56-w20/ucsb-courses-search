package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CoursePage object, represents a page of returned {@link Course} objects from
 * the <a href="https://developer.ucsb.edu/content/academic-curriculums">UCSB
 * Academic Curriculums API</a>
 * 
 */


public class CoursePage {

    private static Logger logger = LoggerFactory.getLogger(CoursePage.class);

    public int pageNumber;
    public int pageSize;
    public int total;
    public List<Course> classes;
   
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

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Course> getClasses() {
        return this.classes;
    }

    public void setClasses(List<Course> classes) {
        this.classes = classes;
    }


    @Override
    public String toString() {
        return "{" +
            " pageNumber='" + getPageNumber() + "'" +
            ", pageSize='" + getPageSize() + "'" +
            ", total='" + getTotal() + "'" +
            ", classes='" + getClasses() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CoursePage)) {
            return false;
        }
        CoursePage coursePage = (CoursePage) o;
        return pageNumber == coursePage.pageNumber && pageSize == coursePage.pageSize && total == coursePage.total && Objects.equals(classes, coursePage.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageNumber, pageSize, total, classes);
    }


}
