package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuarterDeadlines{
    
    private static Logger logger = LoggerFactory.getLogger(Course.class);

    private String quarter;
    private String firstDayOfClasses;
    private String lastDayOfClasses;
    private String lastDayOfFinals;
    private String firstDayOfQuarter;
    private String pass1Begin;
    private String pass2Begin;
    private String pass3Begin;
    private String feeDeadline;
    private String lastDayToAddUnderGrad;
    private String lastDayToAddGrad;
    private String lastDayThirdWeek;

    public static QuarterDeadlines fromJSON(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
            QuarterDeadlines quarterdeadlines = objectMapper.readValue(json, QuarterDeadlines.class);
            return quarterdeadlines;
        } catch (JsonProcessingException jpe) {
            logger.error("JsonProcessingException:" + jpe);
            return null;
        }
    }
        


    public QuarterDeadlines() {
    }

    public String getQuarter() {
        return this.quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getFirstDayOfClasses() {
        return this.firstDayOfClasses;
    }

    public void setFirstDayOfClasses(String firstDayOfClasses) {
        this.firstDayOfClasses = firstDayOfClasses;
    }

    public String getLastDayOfClasses() {
        return this.lastDayOfClasses;
    }

    public void setLastDayOfClasses(String lastDayOfClasses) {
        this.lastDayOfClasses = lastDayOfClasses;
    }

    public String getLastDayOfFinals() {
        return this.lastDayOfFinals;
    }

    public void setLastDayOfFinals(String lastDayOfFinals) {
        this.lastDayOfFinals = lastDayOfFinals;
    }

    public String getFirstDayOfQuarter() {
        return this.firstDayOfQuarter;
    }

    public void setFirstDayOfQuarter(String firstDayOfQuarter) {
        this.firstDayOfQuarter = firstDayOfQuarter;
    }

    public String getPass1Begin() {
        return this.pass1Begin;
    }

    public void setPass1Begin(String pass1Begin) {
        this.pass1Begin = pass1Begin;
    }

    public String getPass2Begin() {
        return this.pass2Begin;
    }

    public void setPass2Begin(String pass2Begin) {
        this.pass2Begin = pass2Begin;
    }

    public String getPass3Begin() {
        return this.pass3Begin;
    }

    public void setPass3Begin(String pass3Begin) {
        this.pass3Begin = pass3Begin;
    }

    public String getFeeDeadline() {
        return this.feeDeadline;
    }

    public void setFeeDeadline(String feeDeadline) {
        this.feeDeadline = feeDeadline;
    }

    public String getLastDayToAddUnderGrad() {
        return this.lastDayToAddUnderGrad;
    }

    public void setLastDayToAddUnderGrad(String lastDayToAddUnderGrad) {
        this.lastDayToAddUnderGrad = lastDayToAddUnderGrad;
    }

    public String getLastDayToAddGrad() {
        return this.lastDayToAddGrad;
    }

    public void setLastDayToAddGrad(String lastDayToAddGrad) {
        this.lastDayToAddGrad = lastDayToAddGrad;
    }

    public String getLastDayThirdWeek() {
        return this.lastDayThirdWeek;
    }

    public void setLastDayThirdWeek(String lastDayThirdWeek) {
        this.lastDayThirdWeek = lastDayThirdWeek;
    }

}