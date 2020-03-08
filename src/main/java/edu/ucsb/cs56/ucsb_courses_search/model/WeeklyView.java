package edu.ucsb.cs56.ucsb_courses_search.model;
import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;



public class WeeklyView {
    private int counter;
    private int rowcounter;
    private int rowspan;

    public WeeklyView(){
      this.counter=0;
      this.rowcounter=2;
      this.rowspan=1;
    }
    public String[] getTimeRange(){
      String[] timerange = new String[30];
      int index = 0;
      for(int h = 8; h <= 22; h++){
        for(int m = 0; m <= 1; m++){
          String time = "";
          if(h > 12){
            time += (h - 12);
          } else{
            time += h;
          }
          time += ":";
          if(m == 0){
            time += "00";
          } else{
            time += "30";
          }
          if(h >= 12){
            time += " PM";
          } else{
            time += " AM";
          }
          timerange[index] = time;
          index++;
        }
      }
      return timerange;
    }

    public boolean returnCol(ScheduleItem course, int count){
      String time = course.getMeetday();
      if(count == 1)
        return time.contains("M");
      if(count == 2)
        return time.contains("T");
      if(count == 3)
        return time.contains("W");
      if(count == 4)
        return time.contains("R");
      if(count == 5)
        return time.contains("F");
      return false;

    }


    public int returnStartTime(ScheduleItem course){
      //TESTED
      //RETURNS ThE ROW, where 8:00AM = 2, 8:30AM = 3

      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
      int hour= Integer.parseInt(times.substring(0,2));

      int minutes = Integer.parseInt(times.substring(3,5));
      int time = hour*60 + minutes;//time in minutes
      if(time == 480){
        return 2;
      }
      int row = (time-480)/30+2;
      return row;
    }

    public int returnClasslength(ScheduleItem course){

      //Course course, String LectureOrSection
      //for rowspan nte that meetime gives the section time not lecture time in form 09:00-09:50
      // for a 75 minute class, usually 12:30-1:45 or 2-3:14, the hour is difference of 1
      //for a 50 min class, the hour is the same etc; 7-7:50
      //for a long labs, there will be first a 2 hour difference such as 14-16:30 or 14-16:50, then also check the minutes for an extra
      String times = course.getMeettime();
      //String times = "11:00 - 11:50";
      // if(LectureOrSection=="lecture"){
      //     times = course.getAssociatedLectureTime();
      // }
      // else{
      //     times = course.getMeettime();
      // }

      //Put both times in minutes

      int hour1 = Integer.parseInt(times.substring(0,2));
      int hour2 = Integer.parseInt(times.substring(6,8));

      int minutes1 = Integer.parseInt(times.substring(3,5));
      int minutes2 = Integer.parseInt(times.substring(9,11));

      int time1 = hour1*60 + minutes1;
      int time2 = hour2*60 + minutes2;
      int differenceInMinutes = time2 - time1; //note that 11:00-11:50= 50 amd 17:30-21:20 = 3 hr 50 min = 230 min
      differenceInMinutes+=15; //everytime ends either 15,50 or 20 so add extra
      return differenceInMinutes/30;//rowspanr
      //return course.getMeettime();

    }

    public String returnDescription(ScheduleItem course){
      //returns course in span format
      //rowspan
      //     'HELLO <br/> \
      //                 HWLLO <br/> \
      //                 MW';

      return course.getClassname() + "\n" + course.getLocation() + "\n" + course.getMeettime();

      //return "stub";
    }
    public String iterateOverArray(Iterable<ScheduleItem> myclasses){
      //ITERATE OVER
      int count = this.iterateCounter();//MONDAY = 1
      //loop through myclasses and look for a colsection = to 1
      for(ScheduleItem s:myclasses){
        this.iterateRowSpan(this.returnClasslength(s));
        if(this.returnCol(s, count)){
          if(this.returnStartTime(s)== this.rowcounter){
            //this.iterateRowSpan(this.returnClasslength(s));
            return this.returnDescription(s)+this.rowspan;
            //return "STUB"+count+" "+this.rowcounter;
          }

        }

      }
      this.iterateRowSpan(1);
      return "count:"+count+"rowcounter:"+this.rowcounter +"rowspan:"+this.rowspan;

      //return "NO item in myclasses";

    }
    public int iterateRowSpan(int newspan){
      this.rowspan = newspan;
      return this.rowspan;

    }
    public int returnRowspan(){
      return this.rowspan;
    }
    public int iterateCounter(){
       if(this.counter==5){
        //reset the iterateCounter
        this.counter =0;
        this.rowcounter+=1;
       }
      this.counter+=1;
      return this.counter;
    }
    public String returnStub(){
      return "stub";
    }
}
