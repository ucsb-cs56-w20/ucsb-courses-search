package classes.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import classes.entities.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, String> {
   List<Course> findByClassname(String classname);
}