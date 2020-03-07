package edu.ucsb.cs56.ucsb_courses_search.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;

@Repository
public interface ScheduleItemRepository extends CrudRepository<ScheduleItem, Long> {
   List<ScheduleItem> findByUid(String uid);
   List<ScheduleItem> findByScheduleid(long scheduleid);
}