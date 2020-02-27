package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class QuarterListService {
    @Value("${app.start_quarter}")
    private String startQuarter;

    @Value("${app.end_quarter}")
    private String endQuarter;

    public QuarterListService() {

    }

    public List<Quarter> getQuarters() {
        return Quarter.quarterList(startQuarter, endQuarter);
    }
}
