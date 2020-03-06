package edu.ucsb.cs56.ucsb_courses_search.downloaders;

import java.io.PrintWriter;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Course;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Section;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.Instructor;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.TimeLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class PersonalScheduleToCSV {

  private static final Logger logger = LoggerFactory.getLogger(personalScheduleToCSV.class);

  public static void writeSections(PrintWriter writer, Iterable<Course> myclasses) {

    String[] CSV_HEADER = { "Quarter", "Course Name", "EnrollCode", "UID", "Professor", "Meeting Times", "Meeting Days", "Location" };
    
    try (CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
        CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);) {
      csvWriter.writeNext(CSV_HEADER);
      for (Course c : myclasses) {
        String lectureInstructor = instructorsToString(c.getClassSections().get(0).instructors);
        for (Section s : c.getClassSections()) {
          for (TimeLocation t : s.timeLocations) {
            String[] data = { c.getQuarter(), c.getTitle(), "enroll Code", "UID", lectureInstructor, t.beginTime, t.days, "Location"};
            csvWriter.writeNext(data);

          } // t
        } // s
      } // c

      logger.info("CSV generated successfully");
    } catch (Exception e) {
      logger.error("CSV generation error", e);
    } // try
  }
}