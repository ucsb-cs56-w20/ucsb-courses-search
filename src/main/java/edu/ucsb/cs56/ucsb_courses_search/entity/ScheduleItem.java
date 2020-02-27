package edu.ucsb.cs56.ucsb_courses_search.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ScheduleItem {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String classname;
    private String professor;
    private String meettime;
    private String meetday;
    private String location;
    private String quarter;
    private String uid;
    private String scheduleid; // id for specific schedule
    private String associatedLectureDay;
    private String associatedLectureTime;
    private String associatedLectureLocation;
    private String enrollCode;
    private String notes;

    /**
     * Sets the "notes" field, which stores extra information to be displayed about the course.
     * The original intended purpose is to allow development to be simplified by assuming the common
     * case that there is only one timeLocation associated with each section, and with each primary,
     * in order to simplify the application structure, but to allow notes to be added in the unusual cases
     * where there is more than one.  That way the application structure is simplified, but the user
     * is alerted that there may be an additional time and location that is not being taken into account.
     */
    
    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


	public String getEnrollCode() {
		return this.enrollCode;
	}

	public void setEnrollCode(String enrollCode) {
		this.enrollCode = enrollCode;
	}

	public String getAssociatedLectureDay() {
		return this.associatedLectureDay;
	}

	public void setAssociatedLectureDay(String associatedLectureDay) {
		this.associatedLectureDay = associatedLectureDay;
	}

	public String getAssociatedLectureTime() {
		return this.associatedLectureTime;
	}

	public void setAssociatedLectureTime(String associatedLectureTime) {
		this.associatedLectureTime = associatedLectureTime;
	}

	public String getAssociatedLectureLocation() {
		return this.associatedLectureLocation;
	}

	public void setAssociatedLectureLocation(String associatedLectureLocation) {
		this.associatedLectureLocation = associatedLectureLocation;
	}

    public String getUid() {
        return uid;
    }

 
 
    public String getClassname() {
        return classname;
    }

    public String getProfessor() {
        return professor;
    }

    public String getMeettime() {
        return meettime;
    }

    public String getMeetday() {
        return meetday;
    }

    public String getLocation() {
        return location;
    }

   public String getQuarter(){
        return quarter;
    }

    public void setUid(String uid) { 
        this.uid = uid; 
    }

    public void setClassname(String classname) { 
        this.classname = classname; 
    }

    public void setProfessor(String professor) { 
        this.professor = professor; 
    }

    public void setMeettime(String meettime) { 
        this.meettime =  meettime; 
    }

    public void setMeetday(String meetday) { 
        this.meetday = meetday; 
    }

    public void setLocation(String location) { 
        this.location = location; 
    }

   public void setQuarter(String quarter){
        this.quarter = quarter;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", classname='" + classname + "'" +
            ", professor='" + professor + "'" +
            ", meettime='" + meettime + "'" +
            ", meetday='" + meetday + "'" +
            ", location='" + location + "'" +
            ", quarter='" + quarter + "'" +
            ", uid='" + uid + "'" +
            ", associatedLectureDay='" + associatedLectureDay + "'" +
            ", associatedLectureTime='" + associatedLectureTime + "'" +
            ", associatedLectureLocation='" + associatedLectureLocation + "'" +
            ", enrollCode='" + enrollCode + "'" +
            ", notes='" + notes + "'" +
            "}";
    }
    
}