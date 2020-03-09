package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.Objects;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FinalExam object, represents a page of returned {@link Course} objects from
 * the <a href="https://developer.ucsb.edu/content/academic-curriculums">UCSB
 * Academic Curriculums API</a>
 * 
 */


public class FinalExam {

    private static Logger logger = LoggerFactory.getLogger(FinalExam.class);

    private boolean hasFinals;
    private String comments;
    private String examDay;
    private String examDate;
    private String beginTime;
    private String endTime;
   
    /**
     * Create a FinalExam object from json representation
     * 
     * @param json String of json returned by API endpoint {@code /classes/search}
     * @return a new CoursePage object
     * @see <a href=
     *      "https://developer.ucsb.edu/content/academic-curriculums">https://developer.ucsb.edu/content/academic-curriculums</a>
     */
    public static FinalExam fromJSON(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
            FinalExam FinalExam = objectMapper.readValue(json, FinalExam.class);
            return FinalExam;
        } catch (JsonProcessingException jpe) {
            logger.error("JsonProcessingException:" + jpe);
            return null;
        }
        
    }

    public boolean isHasFinals() {
        return this.hasFinals;
    }

    public void setHasFinals(boolean hasFinals) {
        this.hasFinals = hasFinals;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getExamDay() {
        return this.examDay;
    }

    public void setExamDay(String examDay) {
        this.examDay = examDay;
    }

    public String getExamDate() {
        return this.examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "{" +
            " hasFinals='" + isHasFinals() + "'" +
            ", comments='" + getComments() + "'" +
            ", examDay='" + getExamDay() + "'" +
            ", examDate='" + getExamDate() + "'" +
            ", beginTime='" + getBeginTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
    
    public String formatFinal(){
        String formatDay = LocalDate.parse(this.examDate, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("EEEE, MMMM d, y"));
        String formatStartTime = LocalTime.parse(this.beginTime, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("h:mm a"));
        String formatEndTime = LocalTime.parse(this.endTime, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("h:mm a"));
        return formatDay + " " + formatStartTime + " - " + formatEndTime;
    }
}
