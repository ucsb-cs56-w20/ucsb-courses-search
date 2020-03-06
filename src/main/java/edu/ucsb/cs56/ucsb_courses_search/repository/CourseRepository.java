package edu.ucsb.cs56.ucsb_courses_search.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs56.ucsb_courses_search.entity.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
   List<Course> findByUid(String uid);
}