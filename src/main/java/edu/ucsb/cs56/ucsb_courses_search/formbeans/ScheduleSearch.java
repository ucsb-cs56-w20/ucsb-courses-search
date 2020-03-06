package edu.ucsb.cs56.ucsb_courses_search.formbeans;

public class ScheduleSearch {
    private int scheduleid;
    private String schedulename;

    public ScheduleSearch() {
    }

    public int getScheduleid() {
        return this.scheduleid;
    }

    public String getSchedulename() {
        return this.schedulename;
    }

    public void setScheduleid(int scheduleid) {
        this.scheduleid = scheduleid;
    }

    public void setSchedulename(String schedulename) {
        this.schedulename = schedulename;
    }
}