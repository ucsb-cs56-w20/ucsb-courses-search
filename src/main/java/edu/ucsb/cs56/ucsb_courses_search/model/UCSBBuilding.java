package edu.ucsb.cs56.ucsb_courses_search.model;

public class UCSBBuilding{



    private String buildingCode;
    private String name;
    private double longitude;
    private double latitude;

    public UCSBBuilding(String buildingCode, String name, double longitude, double latitude) {
        this.buildingCode = buildingCode;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getBuildingCode(){
        return this.buildingCode;
    }

    public String getName(){
        return this.name;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }
    
	public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}