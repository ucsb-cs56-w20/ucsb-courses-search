package edu.ucsb.cs56.ucsb_courses_search.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Schedule {
 
    @Id
    private String scheduleid;
    private String schedulename;
    private String quarter;
    private String uid;
    

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) { 
        this.uid = uid; 
    }

    public String getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(String scheduleid) { 
        this.scheduleid = scheduleid; 
    }

    public String getSchedulename() {
        return schedulename;
    }

    public void setSchedulename(String schedulename) { 
        this.schedulename = schedulename; 
    }

    public String getQuarter(){
        return quarter;
    }

    public void setQuarter(String quarter){
        this.quarter = quarter;
    }

    
}