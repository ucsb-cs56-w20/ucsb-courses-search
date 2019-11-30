package edu.ucsb.cs56.ucsb_courses_search.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs56.ucsb_courses_search.entities.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, String> {
   List<Course> findByClassname(String classname);
}