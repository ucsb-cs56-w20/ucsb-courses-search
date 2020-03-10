package edu.ucsb.cs56.ucsb_courses_search.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import edu.ucsb.cs56.ucsb_courses_search.service.CurriculumService;
import edu.ucsb.cs56.ucsb_courses_search.model.result.CourseListingRow;
import edu.ucsb.cs56.ucsbapi.academics.curriculums.v1.classes.FinalExam;

@Service
public class FinalExamService {

    @Autowired
    private CurriculumService curriculumService;

    public FinalExamService() {}

    public List<CourseListingRow> assignFinalExams(List<CourseListingRow> rows) {
        for (CourseListingRow r : rows){
            if (r.getFirstRow()) {
                String finalJson = curriculumService.getFinalExam(r.getCourse().getQuarter(), r.getSection().getEnrollCode());
                FinalExam fe = FinalExam.fromJSON(finalJson);
                r.getCourse().setFinalExam(fe);
            }
        }
        return rows;
    }
}
