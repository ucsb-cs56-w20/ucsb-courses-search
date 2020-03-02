package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsbapi.academics.curriculums.utilities.Quarter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuarterListService {

    private String startQuarter;
    private String endQuarter;

    public QuarterListService(@Value("${app.start_quarter}") String startQuarter,  @Value("${app.end_quarter}") String endQuarter) {
        this.startQuarter = startQuarter;
        this.endQuarter = endQuarter;
    }

    public List<Quarter> getQuarters() {
        return Quarter.quarterList(startQuarter, endQuarter);
    }
}
