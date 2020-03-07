package edu.ucsb.cs56.ucsb_courses_search.model;
import java.util.ArrayList;
import java.util.List;


import edu.ucsb.cs56.ucsb_courses_search.entity.Course;


public class WeeklyView {
    //
    public WeeklyView(){

    }

    //function to return Day in number form , Monday=1 , takes in Course object
    public int returnColLecture(Course course){
      //If MW return 1, if TR return 2, if MWF return 3
      if( course.getAssociatedLectureDay()=="M W" ){
          return 13;
      }
      else if (course.getAssociatedLectureDay()=="T R"){
        return 24;
      }
      else if (course.getAssociatedLectureDay()=="M W F"){
        return 135;
      }
      else if (course.getAssociatedLectureDay()=="M"){
        return 1;
      }
      else if (course.getAssociatedLectureDay()=="T"){
        return 2;
      }
      else if (course.getAssociatedLectureDay()=="W"){
        return 3;
      }
      else if (course.getAssociatedLectureDay()=="R"){
        return 4;
      }
      else if (course.getAssociatedLectureDay()=="F"){
        return 5;
      }
      else if (course.getAssociatedLectureDay()=="W F"){
        return 35;
      }

      return 0;
    }
    public int returnColSection(Course course){
      //Monday corresponds to Column 1 etc
      if(course.getMeetday() =="M"){
        return 1;
      }
      else if(course.getMeetday()=="T"){
        return 2;
      }
      else if (course.getMeetday()=="W"){
        return 3;
      }
      else if (course.getMeetday()=="R"){
        return 4;
      }
      else if(course.getMeetday()=="F"){
        return 5;
      }
      else if(course.getMeetday()=="T R"){
        return 24;
      }
      else if(course.getMeetday()=="W F"){
        return 35;
      }
      else if(course.getMeetday()=="M W"){
        return 13;
      }
      return 0;
    }

    public String returnLectureStartTime(Course course){
      String time = course.getAssociatedLectureTime();
      String startHour = time.substring(0,5); //String will return 9:00 for 9:00 AM but 13:00 for 1 PM
      return startHour;
    }

    public String returnSectionStartTime(Course course){
      String time = course.getMeettime();
      String startHour = time.substring(0,5); //String will return 9:00 for 9:00 AM but 13:00 for 1 PM
      return startHour;
    }

    public int returnClasslength(Course course, String LectureOrSection){
      //for rowspan nte that meetime gives the section time not lecture time in form 09:00-09:50
      // for a 75 minute class, usually 12:30-1:45 or 2-3:14, the hour is difference of 1
      //for a 50 min class, the hour is the same etc; 7-7:50
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      String times = "";
      if(LectureOrSection=="lecture"){
          times = course.getAssociatedLectureTime();
      }
      else{
          times = course.getMeettime();
      }

      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(8,10));
      int hourDifference = hour2 - hour1;
      int minutes1 = Integer.parseInt(times.substring(3,5));
      int minutes2 = Integer.parseInt(times.substring(11,13));
      int minuteDifference = minutes2 - minutes1;
      if(hourDifference == 0){
        return 2;
      }
      else if (hourDifference == 1 || hourDifference == -11){//12:30-1:45
        return 3;
      }
      else if (hourDifference == 2){ //11:00-13:30 or 16-18:50 = 3 hours
        if(minuteDifference == 50){
          return 6;
        }
        else if(minuteDifference == 30){
          return 5;
        }
      }
      else if (hourDifference == 3) {//could be 17:30-20:00 for 2.5 hours which is rowspan 5{
        if (minuteDifference == -30){
          return 5;
        }
        else if (minuteDifference == 50 ){//8:00-11:50 -4 hours which is rowspan { or 13:00-16:50
          return 8;
        }


      }
      else if (hourDifference ==4){//17:30-21:20 for chem1bl which is rowpan 8 for 4 hours
        if(minuteDifference == -10){
          return 8;
        }

      }
      return 0;
    }

    public String returnSpanLecture(Course course){
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

      return course.getClassname() +"<br />" + course.getAssociatedLectureTime() + "<br />" + course.getAssociatedLectureTime();
      //return "stub";
    }
    public String returnSpanSection(Course course){
      //returns course in span format
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

      return course.getClassname() + "<br />" + course.getLocation() + "<br />" + course.getMeettime();

      //return "stub";
    }

    public String returnStub(){
      return "stub";
    }
}
