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
    public boolean returnColSection(Course course){
      String time = course.getMeetday();

      return time.equals("M ");

      //Monday corresponds to Column 1 etc
      // if((course.getMeetday()).equals("M ")){
      //   return 1;
      // }
      // else if(course.getMeetday()=="T "){
      //   return 2;
      // }
      // else if (course.getMeetday()=="W "){
      //   return 3;
      // }
      // else if (course.getMeetday()=="R "){
      //   return 4;
      // }
      // else if(course.getMeetday()=="F "){
      //   return 5;
      // }
      // else if(course.getMeetday()=="T R "){
      //   return 24;
      // }
      // else if(course.getMeetday()=="W F "){
      //   return 35;
      // }
      // else if(course.getMeetday()=="M W "){
      //   return 13;
      // }
      // else{
      //   return 2;
      // }




    }

    public int returnLectureStartTime(Course course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 1, 8:30AM = 2

      String times = course.getAssociatedLectureTime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));
      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      int row = (time-480)/30+1;
      return row;
    }

    public int returnSectionStartTime(Course course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 2, 8:30AM = 3

      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));
      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      int row = (time-480)/30+2;
      return row;
    }

    public int returnClasslength(Course course){
      //TESTED
      //Course course, String LectureOrSection
      //for rowspan nte that meetime gives the section time not lecture time in form 09:00-09:50
      // for a 75 minute class, usually 12:30-1:45 or 2-3:14, the hour is difference of 1
      //for a 50 min class, the hour is the same etc; 7-7:50
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      String times = course.getMeettime();
      // if(LectureOrSection=="lecture"){
      //     times = course.getAssociatedLectureTime();
      // }
      // else{
      //     times = course.getMeettime();
      // }

      //Put both times in minutes

      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(8,10));

      int minutes1 = Integer.parseInt(times.substring(3,5));
      int minutes2 = Integer.parseInt(times.substring(11,13));

      int time1 = hour1*60 + minutes1;
      int time2 = hour2*60 + minutes2;
      int differenceInMinutes = time2 - time1; //note that 11:00-11:50= 50 amd 17:30-21:20 = 3 hr 50 min = 230 min
      differenceInMinutes+=15; //everytime ends either 15,50 or 20 so add extra
      return differenceInMinutes/30;//rowspan

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
