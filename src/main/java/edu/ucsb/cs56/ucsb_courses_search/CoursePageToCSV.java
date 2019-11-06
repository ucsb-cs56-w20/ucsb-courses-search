package edu.ucsb.cs56.ucsb_courses_search;

import java.io.PrintWriter;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.CoursePage;

public class CoursePageToCSV {
    public static void writeSections(PrintWriter writer, CoursePage cp) {
        writer.println("This is a test.");
        writer.println("If this were a real CSV, it would contain data from the cp object");
    }
}