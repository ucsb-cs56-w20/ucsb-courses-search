package edu.ucsb.cs56.ucsb_courses_search.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Course {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;
    private String classname;
    private String professor;
    private String meettime;
    private String meetday;
    private String location;
    private String quarter;

    public String getUid() {
        return uid;
    }

 
 
    public String getClassname() {
        return classname;
    }

    public String getProfessor() {
        return professor;
    }

    public String getMeettime() {
        return meettime;
    }

    public String getMeetday() {
        return meetday;
    }

    public String getLocation() {
        return location;
    }

   public String getQuarter(){
        return quarter;
    }

    public void setUid(String uid) { 
        this.uid = uid; 
    }

    public void setClassname(String classname) { 
        this.classname = classname; 
    }

    public void setProfessor(String professor) { 
        this.professor = professor; 
    }

    public void setMeettime(String meettime) { 
        this.meettime =  meettime; 
    }

    public void setMeetday(String meetday) { 
        this.meetday = meetday; 
    }

    public void setLocation(String location) { 
        this.location = location; 
    }

   public void setQuarter(String quarter){
        this.quarter = quarter;
    }

    
}