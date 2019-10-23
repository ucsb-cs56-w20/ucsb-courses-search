package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import lombok.Data;

@Data
public class TimeLocation {
    private String enrollCode;
    private int enrolledTotal;
    private int maxEnroll;

    private String room;
    private String building;
    private String roomCapacity;
    private String days; 
    private String beginTime; 
    private String endTime; 

    public TimeLocation () {}
}