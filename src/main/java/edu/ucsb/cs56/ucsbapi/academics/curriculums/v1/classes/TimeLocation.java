package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import edu.ucsb.cs56.ucsb_courses_search.service.UCSBBuildingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimeLocation {

    private Logger logger = LoggerFactory.getLogger(TimeLocation.class);

    private String room;
    private String building;
    private String roomCapacity;
    private String days; 
    private String beginTime; 
    private String endTime;

    public String displayBuildingAndRoom() {
        String displayBuilding = 
            (building == null) ? "" : building;
        String displayRoom = 
            (room == null) ? "" : room;
        return displayBuilding + " " + displayRoom;    
    }

    public TimeLocation () {
    }

    public String getLocationURL(){
        logger.info("building="+building);
        if (building==null)
          return "";
        String result = UCSBBuildingService.getLink(building);
        logger.info("result="+result);
        return result;
    }


    public Logger getLogger() {
        return this.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getRoom() {
        return this.room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBuilding() {
        return this.building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoomCapacity() {
        return this.roomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
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

}