package edu.ucsb.cs56.ucsb_courses_search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
// import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;

import edu.ucsb.cs56.ucsb_courses_search.entities.Course;
import edu.ucsb.cs56.ucsb_courses_search.repositories.CourseRepository;

@ControllerAdvice
public class ScheduleAdvice {

    @Autowired   
    private CourseRepository courseRepository;

    public ScheduleAdvice(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;   
    }

    public CourseRepository getRepo() {
        return courseRepository;
    }


    public String getClassname(String classname){
        if (classname == null) return "";

        String fake = classname;
        List<Course> myclasses = courseRepository.findByClassname(fake);

        if (myclasses.size()==0) {
            Course u = new Course();
            u.setClassname(fake);
            u.setProfessor("Dr Justin Tjoa");
            u.setMeettime("9:00 AM");
            u.setMeetday("Tuesdays and Thursdays");
            u.setLocation("Cal Poly Slo");
            courseRepository.save(u);
        }

        return fake;
    }

}