package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsb_courses_search.model.UCSBBuilding;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.ResourceUtils;

public class UCSBBuildingService {

    private static UCSBBuildingService instance = null;

    private HashMap<String, UCSBBuilding> buildingMap;

    private UCSBBuildingService() {

        this.buildingMap = new HashMap<String,UCSBBuilding>();

        try {
            ObjectMapper mapper = new ObjectMapper();

            ClassLoader cl = this.getClass().getClassLoader();
            InputStream is = cl.getResourceAsStream("ucsb_buildings.json");

            UCSBBuilding[] buildings = mapper.readValue(is, UCSBBuilding[].class);
            for (int i = 0; i < buildings.length; i++) {
                this.buildingMap.put(buildings[i].getCode(), buildings[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static UCSBBuildingService getInstance() {
        if (instance == null) {
            instance = new UCSBBuildingService();
        }
        return instance;
    }

    public static UCSBBuilding getBuilding(String buildingCode) {
        UCSBBuildingService service = UCSBBuildingService.getInstance();

        return service.buildingMap.get(buildingCode);
    }

    public static String getLink(String buildingCode){
        UCSBBuildingService service = UCSBBuildingService.getInstance();
        UCSBBuilding building = service.buildingMap.get(buildingCode);
        if (building==null) {
            return "";
        }
        String lat = Double.toString(building.getLatitude());
        String lon = Double.toString(building.getLongitude());
        String result = "https://maps.openrouteservice.org/directions?n1=34.412592&n2=-119.845887&n3=16&a="
        + lat + "," + lon + ",null,null&b=0&c=0&k1=en-US&k2=km";
        return result;
    }

    public static String getWalkingLink(String code1, String code2){
        UCSBBuildingService service = UCSBBuildingService.getInstance();
        UCSBBuilding b1 = service.buildingMap.get(code1);
        UCSBBuilding b2 = service.buildingMap.get(code2);

        if (b1 == null || b2 == null) {
            return "";
        }

        String lat1 = Double.toString(b1.getLatitude());
        String lon1 = Double.toString(b1.getLongitude());
        String lat2 = Double.toString(b2.getLatitude());
        String lon2 = Double.toString(b2.getLongitude());

        String result = "https://maps.openrouteservice.org/directions?n1=34.412592&n2=-119.845887&n3=16&a="
        + lat1 + "," + lon1 + "," + lat2 + "," + lon2 + "&b=2&c=0&k1=en-US&k2=km";

        return result;
    }

    public static String getBikingLink(String code1, String code2){
        UCSBBuildingService service = UCSBBuildingService.getInstance();
        UCSBBuilding b1 = service.buildingMap.get(code1);
        UCSBBuilding b2 = service.buildingMap.get(code2);

        if (b1 == null || b2 == null) {
            return "";
        }

        String lat1 = Double.toString(b1.getLatitude());
        String lon1 = Double.toString(b1.getLongitude());
        String lat2 = Double.toString(b2.getLatitude());
        String lon2 = Double.toString(b2.getLongitude());

        String result = "https://maps.openrouteservice.org/directions?n1=34.412592&n2=-119.845887&n3=16&a="
        + lat1 + "," + lon1 + "," + lat2 + "," + lon2 + "&b=1&c=0&k1=en-US&k2=km";

        return result;
    }
}