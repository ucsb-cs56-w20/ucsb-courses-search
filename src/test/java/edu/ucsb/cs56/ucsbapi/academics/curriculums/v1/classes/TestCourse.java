package edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.ArrayList;

public class TestCourse {

    @Test
    public void test_mainInstructorList__null_section_returns_empty_string() {
        Course c = new Course();
        assertEquals(null, c.getClassSections());

        assertEquals("", c.mainInstructorList());
    }

    @Test
    public void test_mainInstructorList__empty_section_returns_empty_string() {
        Course c = new Course();
        c.setClassSections(new ArrayList<Section>());
        assertEquals(0, c.getClassSections().size());
    
        assertEquals("", c.mainInstructorList());
    }

    @Test
    public void test_mainInstructorList__first_section_null_instructor_list_returns_empty_string() {
        Section s = new Section();
        Course c = new Course();
        c.setClassSections(new ArrayList<Section>());
        c.getClassSections().add(s);
        assertEquals(null, s.instructors);
            
        assertEquals("", c.mainInstructorList());
    }

    @Test
    public void test_mainInstructorList__first_section_empty_instructor_list_returns_empty_string() {
        Section s = new Section();
        s.instructors = new ArrayList<Instructor>();
        Course c = new Course();
        c.setClassSections(new ArrayList<Section>());
        c.getClassSections().add(s);
            
        assertEquals("", c.mainInstructorList());            
    }

    @Test
    public void test_mainInstructorList__first_section_single_instructor() {
        Instructor i = new Instructor();
        i.instructor = "GAUCHO C";
        Section s = new Section();
        s.instructors = new ArrayList<Instructor>();
        s.instructors.add(i);
        Course c = new Course();
        c.setClassSections(new ArrayList<Section>());
        c.getClassSections().add(s);
            
        assertEquals("GAUCHO C", c.mainInstructorList());            
    }

    @Test
    public void test_mainInstructorList__first_section_multiple_instructors() {
        Instructor i1 = new Instructor();
        Instructor i2 = new Instructor();
        Instructor i3 = new Instructor();

        i1.instructor = "GAUCHO A";
        i2.instructor = "OLE B";
        i3.instructor = "SURFER C";

        Section s = new Section();
        s.instructors = new ArrayList<Instructor>();
        s.instructors.add(i1);
        s.instructors.add(i2);
        s.instructors.add(i3);

        Course c = new Course();
        c.setClassSections(new ArrayList<Section>());
        c.getClassSections().add(s);
            
        assertEquals("GAUCHO A, OLE B, SURFER C", c.mainInstructorList());            
    }
}