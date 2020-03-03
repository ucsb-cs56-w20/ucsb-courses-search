package edu.ucsb.cs56.ucsb_courses_search.entity;

import javax.persistence.*;

@Entity
public class Schedule {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scheduleid;
    private String schedulename;
    private String quarter;
    private String uid;
    

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) { 
        this.uid = uid; 
    }

    public long getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(long scheduleid) { 
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