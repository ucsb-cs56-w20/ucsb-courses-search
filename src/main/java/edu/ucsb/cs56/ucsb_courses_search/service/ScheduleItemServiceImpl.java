package edu.ucsb.cs56.ucsb_courses_search.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import edu.ucsb.cs56.ucsb_courses_search.entity.ScheduleItem;
import edu.ucsb.cs56.ucsb_courses_search.repository.ScheduleItemRepository;

import java.util.List;
import java.util.ArrayList;

@Service
public class ScheduleItemServiceImpl implements ScheduleItemService {

    @Autowired
    private ScheduleItemRepository scheduleItemRepository;

    public ScheduleItemServiceImpl(){
    }
    
    public ScheduleItemRepository getScheduleItemRepository() { 
        return scheduleItemRepository; 
    }
    public void setScheduleItemRepository(ScheduleItemRepository scheduleItemRepository) {
        this.scheduleItemRepository = scheduleItemRepository;
    }

    @Override
    public List<ScheduleItem> deleteByClassname(List<ScheduleItem> courseList, String name) {
        List<ScheduleItem> courseListOld = new ArrayList<ScheduleItem>(courseList);
        for (ScheduleItem i : courseListOld){
            if (i.getClassname().equals(name)){
                scheduleItemRepository.delete(i);
                courseList.remove(i);
            }
        }
        return courseList;
    }
}

