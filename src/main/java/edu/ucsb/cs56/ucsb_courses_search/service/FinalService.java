package edu.ucsb.cs56.ucsb_courses_search.service;

public interface FinalService {
    public String getJSON(String enrollCode, String quarter);
    public String getFinal(int enrollCode, int quarter);
}