package edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.ArrayList;

public class TestQuarter {

    @Test
    public void test_constructor_20194() {
        Quarter q = new Quarter(20194);
        assertEquals(20194, q.getValue());
        assertEquals("F19", q.toString());
    }

    @Test
    public void test_constructor_F19() {
        Quarter q = new Quarter("F19");
        assertEquals(20194, q.getValue());
        assertEquals("F19", q.toString());
    }

    @Test
    public void test_constructor_20201() {
        Quarter q = new Quarter(20201);
        assertEquals(20201, q.getValue());
        assertEquals("W20", q.toString());
    }

    @Test
    public void test_constructor_W20() {
        Quarter q = new Quarter("W20");
        assertEquals(20201, q.getValue());
        assertEquals("W20", q.toString());
    }


    @Test
    public void test_constructor_S20() {
        Quarter q = new Quarter("S20");
        assertEquals(20202, q.getValue());
        assertEquals("S20", q.toString());
    }

    
    @Test
    public void test_constructor_M20() {
        Quarter q = new Quarter("M20");
        assertEquals(20203, q.getValue());
        assertEquals("M20", q.toString());
    }


    @Test
    public void test_constructor_F01() {
        Quarter q = new Quarter("F01");
        assertEquals(20014, q.getValue());
        assertEquals("F01", q.toString());
    }

    @Test
    public void test_constructor_S99() {
        Quarter q = new Quarter("S99");
        assertEquals(19992, q.getValue());
        assertEquals("S99", q.toString());
    }

    @Test
    public void test_increment_F19() {
        Quarter q = new Quarter("F19");
        assertEquals(20201, q.increment());
        assertEquals("W20", q.toString());
    }

    @Test
    public void test_increment_W20() {
        Quarter q = new Quarter("W20");
        assertEquals(20202, q.increment());
        assertEquals("S20", q.toString());
    }

    @Test
    public void test_increment_S20() {
        Quarter q = new Quarter("S20");
        assertEquals(20203, q.increment());
        assertEquals("M20", q.toString());
    }

    @Test
    public void test_increment_M20() {
        Quarter q = new Quarter("M20");
        assertEquals(20204, q.increment());
        assertEquals("F20", q.toString());
    }

}