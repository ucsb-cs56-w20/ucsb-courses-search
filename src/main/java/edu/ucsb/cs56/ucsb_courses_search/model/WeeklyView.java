package edu.ucsb.cs56.ucsb_courses_search.model;
import java.util.ArrayList;
import java.util.List;


import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;


public class WeeklyView {
    //
    public WeeklyView(){

    }

    //function to return Day in number form , Monday=1 , takes in Course object
    public int returnColLecture(ScheduleItem course){
      String time = course.getAssociatedLectureDay();
      String lec = "";
      for(int i = 0; i < time.length(); i++)
      {
        if(time.charAt(i) != ' ')
        {
          lec += time.charAt(i);
        }
      }
      //If MW return 1, if TR return 2, if MWF return 3
      if( lec.equals("MW") ){
          return 13;
      }
      else if (lec.equals("TR")){
        return 24;
      }
      else if (lec.equals("MWF")){
        return 135;
      }
      else if (lec.equals("M")){
        return 1;
      }
      else if (lec.equals("T")){
        return 2;
      }
      else if (lec.equals("W")){
        return 3;
      }
      else if (lec.equals("R")){
        return 4;
      }
      else if (lec.equals("F")){
        return 5;
      }
      else if (lec.equals("WF")){
        return 35;
      }

      return 0;
    }
    public int returnColSection(ScheduleItem course){
      String time = course.getMeetday();
      String sec = "";
      for(int i = 0; i < time.length(); i++)
      {
        if(time.charAt(i) != ' ')
        {
          sec += time.charAt(i);
        }
      }

      //Monday corresponds to Column 1 etc
      if(sec.equals("M")){
         return 1;
       }
       else if(sec.equals("T")){
         return 2;
       }
       else if (sec.equals("W")){
         return 3;
       }
       else if (sec.equals("R")){
         return 4;
       }
       else if(sec.equals("F")){
         return 5;
       }
       else if(sec.equals("TR")){
         return 24;
       }
       else if(sec.equals("WF")){
         return 35;
      }
      else if(sec.equals("MW")){
         return 13;
       }

      return 0;
    }

<<<<<<< HEAD
    public int returnLectureStartTime(ScheduleItem course){
=======
    public int returnLectureStartTime(Course course){
>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 1, 8:30AM = 2

      String times = course.getAssociatedLectureTime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));
<<<<<<< HEAD

      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      if(time ==480){
        return 2;
      }
      int row = (time-480)/30+2;
      return row;
    }

    public int returnSectionStartTime(ScheduleItem course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 2, 8:30AM = 3
=======
      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      int row = (time-480)/30+1;
      return row;
    }

    public int returnSectionStartTime(Course course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 1, 8:30AM = 2
>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both

      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));
<<<<<<< HEAD

      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      if(time == 480){
        return 2;
      }
      int row = (time-480)/30+2;
      return row;
    }

<<<<<<< HEAD
    public int returnClasslength(ScheduleItem course){

=======
      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      int row = (time-480)/30+1;
      return row;
    }

    public int returnClasslength(Course course, String LectureOrSection){
      //TESTED
>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both
=======
    public int returnClasslength(Course course){
      
>>>>>>> lh- fixed the rowspan function to accurately display rowspan
      //Course course, String LectureOrSection
      //for rowspan nte that meetime gives the section time not lecture time in form 09:00-09:50
      // for a 75 minute class, usually 12:30-1:45 or 2-3:14, the hour is difference of 1
      //for a 50 min class, the hour is the same etc; 7-7:50
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
<<<<<<< HEAD
<<<<<<< HEAD
      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
<<<<<<< HEAD
=======
      String times = "12:30 - 13:45";
>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both
=======
>>>>>>> lh- fixed the rowspan function to accurately display rowspan
      // if(LectureOrSection=="lecture"){
      //     times = course.getAssociatedLectureTime();
      // }
      // else{
      //     times = course.getMeettime();
      // }

<<<<<<< HEAD
      //Put both times in minutes

      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(6,8));

      int minutes1 = Integer.parseInt(times.substring(3,5));
      int minutes2 = Integer.parseInt(times.substring(9,11));

=======
      String times = "";
      if(LectureOrSection=="lecture"){
          times = course.getAssociatedLectureTime();
      }
      else{
          times = course.getMeettime();
      }
=======
>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both
      //Put both times in minutes

      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(6,8));

      int minutes1 = Integer.parseInt(times.substring(3,5));
<<<<<<< HEAD
      int minutes2 = Integer.parseInt(times.substring(11,13));
<<<<<<< HEAD
      //int minuteDifference = minutes2 - minutes1;
>>>>>>> lh-changed the rowspan function to make an easier implementation
=======
=======
      int minutes2 = Integer.parseInt(times.substring(9,11));
>>>>>>> lh- fixed the rowspan function to accurately display rowspan

>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both
      int time1 = hour1*60 + minutes1;
      int time2 = hour2*60 + minutes2;
      int differenceInMinutes = time2 - time1; //note that 11:00-11:50= 50 amd 17:30-21:20 = 3 hr 50 min = 230 min
      differenceInMinutes+=15; //everytime ends either 15,50 or 20 so add extra
<<<<<<< HEAD
<<<<<<< HEAD
      return differenceInMinutes/30;//rowspanr
      //return course.getMeettime();

=======
      return differenceInMinutes/30;//rowspan
=======
      return differenceInMinutes/30;//rowspanr
      //return course.getMeettime();
>>>>>>> lh- fixed the rowspan function to accurately display rowspan

<<<<<<< HEAD
      // if(hourDifference == 0){
      //   return 2;
      // }
      // else if (hourDifference == 1 || hourDifference == -11){//12:30-1:45
      //   return 3;
      // }
      // else if (hourDifference == 2){ //11:00-13:30 or 16-18:50 = 3 hours
      //   if(minuteDifference == 50){
      //     return 6;
      //   }
      //   else if(minuteDifference == 30){
      //     return 5;
      //   }
      // }
      // else if (hourDifference == 3) {//could be 17:30-20:00 for 2.5 hours which is rowspan 5{
      //   if (minuteDifference == -30){
      //     return 5;
      //   }
      //   else if (minuteDifference == 50 ){//8:00-11:50 -4 hours which is rowspan { or 13:00-16:50
      //     return 8;
      //   }
      //
      //
      // }
      // else if (hourDifference ==4){//17:30-21:20 for chem1bl which is rowpan 8 for 4 hours
      //   if(minuteDifference == -10){
      //     return 8;
      //   }
      //
      // }
      //return 0;
>>>>>>> lh-changed the rowspan function to make an easier implementation
=======
>>>>>>> lh-fixed both lecture start time and section start time to return the correct row number instead of a string and tested both
    }

    public String returnSpanLecture(ScheduleItem course){
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

<<<<<<< HEAD
      return course.getClassname() +"<br />" + course.getAssociatedLectureDay() + "<br />" + course.getAssociatedLectureTime();
=======
      return course.getClassname() +"<br />" + course.getAssociatedLectureTime() + "<br />" + course.getAssociatedLectureTime();
>>>>>>> lh- changed the return span functions as well added weeklyview to model and checked with stub function
      //return "stub";
    }
    public String returnSpanSection(ScheduleItem course){
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
