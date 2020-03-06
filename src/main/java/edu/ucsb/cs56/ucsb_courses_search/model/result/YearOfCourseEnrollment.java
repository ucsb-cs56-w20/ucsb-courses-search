package edu.ucsb.cs56.ucsb_courses_search.model.result;

public class YearOfCourseEnrollment
{

  private String[] courses = new String[4];
  private String year;

  public YearOfCourseEnrollment(String year, String fall, String winter, String spring, String summer)
  {
    courses[0] = fall;
    courses[1] = winter;
    courses[2] = spring;
    courses[3] = summer;
    this.year = year;
  }

  //get the enrollment number of a quarter as a string.
  //This will return TBD, - , or a number depending if the quarter is in the future or if there's valid numbers
  public String getEnrollmentNumbers(int quarter)
  {
    return courses[quarter];
  }

  public String getYear()
  {
    return this.year;
  }

  @Override
  public String toString()
  {
    return year + " | " + courses[0] + " | " + courses[1] + " | " + courses[2] + " | " + courses[3];
  }
}
