package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import edu.ucsb.cs56.ucsb_courses_search.service.UCSBBuildingService;
import lombok.Data;

@Data
public class TimeLocation {
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
        return UCSBBuildingService.getLink(building);
    }
}