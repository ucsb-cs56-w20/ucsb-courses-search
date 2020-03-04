package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import edu.ucsb.cs56.ucsb_courses_search.service.UCSBBuildingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimeLocation {

    private Logger logger = LoggerFactory.getLogger(TimeLocation.class);

    public String room;
    public String building;
    public String roomCapacity;
    public String days; 
    public String beginTime; 
    public String endTime;

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
}