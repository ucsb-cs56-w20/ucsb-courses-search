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

            File file = ResourceUtils.getFile("classpath:ucsb_buildings.json");
            InputStream is = new FileInputStream(file);

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
}