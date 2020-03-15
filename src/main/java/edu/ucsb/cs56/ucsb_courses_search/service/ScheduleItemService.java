package edu.ucsb.cs56.ucsb_courses_search.service;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import java.util.List;

public interface ScheduleItemService {
    public List<ScheduleItem> deleteByClassname(List<ScheduleItem> courseList, String name);
}

