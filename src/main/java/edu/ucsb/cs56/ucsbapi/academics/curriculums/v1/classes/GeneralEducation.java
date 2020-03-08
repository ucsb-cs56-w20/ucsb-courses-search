package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class GeneralEducation {

    private String geCode;
    private String geCollege;

    public GeneralEducation() {}

    public String getGeCode() {
        return this.geCode;
    }

    public void setGeCode(String geCode) {
        this.geCode = geCode;
    }

    public String getGeCollege() {
        return this.geCollege;
    }

    public void setGeCollege(String geCollege) {
        this.geCollege = geCollege;
    }

    @Override
    public String toString() {
        return "{" + 
            " geCode='" + getGeCode() + "'" +
            ", geCollege='" + getGeCollege() + "'" +
            "}";
    }

}