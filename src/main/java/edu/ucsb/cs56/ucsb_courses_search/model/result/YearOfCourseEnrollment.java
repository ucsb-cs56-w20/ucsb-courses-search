package edu.ucsb.cs56.ucsb_courses_search.model.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

/**
 * The year field (e.g. 2019) represents the year of the fall quarter of an
 * academic year
 *
 * The courses array represents fall, winter, spring, and summer for that
 * academic year.
 *
 *
 * For example, if the year field is 2019, F19 (20194) would be in courses[0]
 *
 * W20 (20201) would be in courses[1]
 *
 * S20 (20202) would be in courses[2]
 *
 * M20 (20203) would be in courses[3]
 */

public class YearOfCourseEnrollment
{


  private String[] courses = new String[4];
  private String year;

  public YearOfCourseEnrollment(String year)
  {
    courses[0] = "-";
    courses[1] = "-";
    courses[2] = "-";
    courses[3] = "-";
    this.year = year;
  }

  public YearOfCourseEnrollment(String year, String fall, String winter, String spring, String summer)
  {
    courses[0] = fall;
    courses[1] = winter;
    courses[2] = spring;
    courses[3] = summer;
    this.year = year;
  }

  /** get the enrollment number of a quarter as a string.
  * This will return TBD, - , or a number depending if the quarter is in the future or if there's valid numbers
  @param quarter 0 for fall, 1 for winter, 2 for spring, 3 for summer
  */
  public String getEnrollmentNumbers(int quarter)
  {
    return courses[quarter];
  }

  public String getYear()
  {
    return this.year;
  }

  public void setQuarter(int quarter, String value)
  {
    courses[quarter % 4] = value;
  }

  @Override
  public String toString()
  {
    return year + " | " + courses[0] + " | " + courses[1] + " | " + courses[2] + " | " + courses[3];
  }


}
