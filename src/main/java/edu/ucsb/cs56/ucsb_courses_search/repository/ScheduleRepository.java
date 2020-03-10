package edu.ucsb.cs56.ucsb_courses_search.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs56.ucsb_courses_search.entity.Schedule;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, String> {
   List<Schedule> findByUid(String uid);
}