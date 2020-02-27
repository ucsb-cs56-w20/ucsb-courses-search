package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class QuarterListService {

    private List<Quarter> quarters;

    /**
     *
     * @param startQuarter String in format (QYY) in application.properties, first quarter to show
     * @param endQuarter String in format (QYY) in application.properties, last quarter to show
     */
    public QuarterListService(@Value("${app.start_quarter}") String startQuarter, @Value("${app.end_quarter}") String endQuarter) {
        quarters = Quarter.quarterList(startQuarter, endQuarter);
    }

    public List<Quarter> getQuarters(){
        return quarters;
    }
}
