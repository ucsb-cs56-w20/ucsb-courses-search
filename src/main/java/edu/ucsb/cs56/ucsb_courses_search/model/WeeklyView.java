package edu.ucsb.cs56.ucsb_courses_search.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;

public class WeeklyView {
  private Logger logger = LoggerFactory.getLogger(WeeklyView.class);

  private class Cell {
    public int col;
    public int row;
    public Cell(int col, int row) {
      this.col = col;
      this.row = row;
    }
    public boolean equals(Cell other){
      return (col == other.col) && (row == other.row);
    }
  }

  private int col;
  private int row;
  private int rowspan;
  private Iterable<ScheduleItem> myclasses;
  private ArrayList<Cell> ignoredCells;

  public WeeklyView(Iterable<ScheduleItem> myclasses) {
    this.col = 0;
    this.row = 2;
    this.rowspan = 1;
    this.myclasses = myclasses;
    ignoredCells = new ArrayList<Cell>();
  }

  public String[] getTimeRange() {
    String[] timerange = new String[30];
    int index = 0;
    for (int h = 8; h <= 22; h++) {
      for (int m = 0; m <= 1; m++) {
        String time = "";
        if (h > 12) {
          if(h - 12 < 10)
            time += "0";
          time += (h - 12);
        } else {
          if(h < 10)
            time += "0";
          time += h;
        }
        time += ":";
        if (m == 0) {
          time += "00";
        } else {
          time += "30";
        }
        if (h >= 12) {
          time += " PM";
        } else {
          time += " AM";
        }
        timerange[index] = time;
        index++;
      }
    }
    return timerange;
  }

  public boolean isColEqualsMeetday(ScheduleItem course, int column) {
    String time = course.getMeetday();
    if (column == 0)
      return time.contains("M");
    if (column == 1)
      return time.contains("T");
    if (column == 2)
      return time.contains("W");
    if (column == 3)
      return time.contains("R");
    if (column == 4)
      return time.contains("F");
    return false;
  }

  public int getCourseStartRow(ScheduleItem course) {
    // TESTED
    // RETURNS ThE ROW, where 8:00AM = 2, 8:30AM = 3

    String times = course.getMeettime();
    // String times = "11:00 - 11:50";
    int hour = Integer.parseInt(times.substring(0, 2));

    int minutes = Integer.parseInt(times.substring(3, 5));
    int time = hour * 60 + minutes;// time in minutes
    if (time == 480) {
      return 2;
    }
    int row = (time - 480) / 30 + 2;
    return row;
  }

  public int getCourseRowSpan(ScheduleItem course) {
    // Course course, String LectureOrSection
    // for rowspan nte that meetime gives the section time not lecture time in form
    // 09:00-09:50
    // for a 75 minute class, usually 12:30-1:45 or 2-3:14, the hour is difference
    // of 1
    // for a 50 min class, the hour is the same etc; 7-7:50
    // for a long labs, there will be first a 2 hour difference such as 14-16:30 or
    // 14-16:50, then also check the minutes for an extra
    String times = course.getMeettime();
    // String times = "11:00 - 11:50";
    // if(LectureOrSection=="lecture"){
    // times = course.getAssociatedLectureTime();
    // }
    // else{
    // times = course.getMeettime();
    // }

    // Put both times in minutes

    int hour1 = Integer.parseInt(times.substring(0, 2));
    int hour2 = Integer.parseInt(times.substring(6, 8));

    int minutes1 = Integer.parseInt(times.substring(3, 5));
    int minutes2 = Integer.parseInt(times.substring(9, 11));

    int time1 = hour1 * 60 + minutes1;
    int time2 = hour2 * 60 + minutes2;
    int differenceInMinutes = time2 - time1; // note that 11:00-11:50= 50 amd 17:30-21:20 = 3 hr 50 min = 230 min
    differenceInMinutes += 15; // everytime ends either 15,50 or 20 so add extra
    return differenceInMinutes / 30;// rowspanr
    // return course.getMeettime();
  }

  public String getCourseDescription(ScheduleItem course) {
    // returns course in span format
    // rowspan
    // 'HELLO <br/> \
    // HWLLO <br/> \
    // MW';

    return course.getClassname() + "\n" + course.getLocation() + "\n" + course.getMeettime();

    // return "stub";
  }

  public String insertItem(Iterable<ScheduleItem> myclasses) {
    int column = getCol(); // MONDAY = 0
    this.rowspan = 1;
    String courseDesc = "";
    // loop through myclasses and look for a colsection = to 1
    for (ScheduleItem s : myclasses) {
      if (isColEqualsMeetday(s, column)) {
        if (getCourseStartRow(s) == this.row) {
          this.rowspan = getCourseRowSpan(s);
          for (int i = this.rowspan - 1; i >= 1; i--) {
            ignoredCells.add(new Cell(column, this.row + i));
          }
          courseDesc = getCourseDescription(s);
        }
      }
    }
    incrementCol();
    return courseDesc;

    // return "NO item in myclasses";

  }

  public boolean notIgnoredCell() {
    for(Cell c: ignoredCells){
      if(c.col == getCol()){
        if(c.row == this.row){
          incrementCol();
          return false;
        }
      }
    }
    return true;
  }

  public void setRowSpan(int newspan) {
    this.rowspan = newspan;
  }

  public int getRowSpan() {
    for (ScheduleItem s : myclasses) {
      if (isColEqualsMeetday(s, getCol())) {
        if (getCourseStartRow(s) == this.row) {
          return getCourseRowSpan(s);
        }
      }
    }
    return 1;
  }

  public int getCol() {
    return this.col % 5;
  }

  public void incrementCol() {
    this.col++;
    if (this.col % 5 == 0)
      this.row++;
  }

  public String returnStub() {
    return "stub";
  }
}
