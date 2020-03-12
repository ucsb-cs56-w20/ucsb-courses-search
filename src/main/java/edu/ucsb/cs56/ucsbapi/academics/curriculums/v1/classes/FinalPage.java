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


public class FinalPage{

    private static Logger logger = LoggerFactory.getLogger(CoursePage.class);

    private boolean hasFinals;
    private String comments = "none";
    private String examDay;
    private String examDate;
    private String beginTime;
    private String endTime;
    private String courseName;
   
    /**
     * Create a FInalPage object from json representation
     * 
     * @param json String of json returned by API endpoint {@code /classes/search}
     * @return a new CoursePage object
     * @see <a href=
     *      "https://developer.ucsb.edu/content/academic-curriculums">https://developer.ucsb.edu/content/academic-curriculums</a>
     */
    public static FinalPage fromJSON(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
            FinalPage finalPage = objectMapper.readValue(json, FinalPage.class);
            return finalPage;
        } catch (JsonProcessingException jpe) {
            logger.error("JsonProcessingException:" + jpe);
            return null;
        }
    }

    public String getCourseName(){
        return courseName;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public boolean getHasFinals(){
        return hasFinals;
    }

    public void setHasFinals(boolean hasFinals){
        this.hasFinals = hasFinals;
    }

    public String getComments(){
        return comments;
    }

    public void setComments(String comment){
        this.comments = comments;
    }

    public String getExamDay(){
        return examDay;
    }

    public void setExamDay(String examDay){
        this.examDay = examDay;
    }

    public String getExamDate(){
        return examDate;
    }

    public void setExamDate(String examDate){
        this.examDate = examDate.substring(4,6) + "/" + examDate.substring(6,8) + "/" + examDate.substring(0,4); //formats date nicely
    }

    public String getBeginTime(){
        return beginTime;
    }
    
    public void setBeginTime(String beginTime){
        this.beginTime = beginTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FinalPage)) {
            return false;
        }
        FinalPage finalPage = (FinalPage) o;
        return hasFinals == finalPage.hasFinals && comments.equals(finalPage.comments) && examDay.equals(finalPage.examDay)
         && examDate.equals(finalPage.examDate) && beginTime.equals(finalPage.beginTime) && endTime.equals(finalPage.endTime) && courseName.equals(finalPage.courseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasFinals, comments, examDay, examDate, beginTime, endTime, courseName);
    }

}
