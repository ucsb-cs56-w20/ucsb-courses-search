package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsb_courses_search.model.UCSBBuilding;


public class UCSBBuildingService{

    public static UCSBBuilding getBuilding(String buildingCode){
         if(buildingCode.equals("PHELP")){
             return new UCSBBuilding("PHELP", "Phelps Hall", 34.16078600000006, -119.8446671701341);
         }
         else return null;
    }
}