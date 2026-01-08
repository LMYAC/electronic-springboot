package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.pojo.InspectionTask;

import java.util.Map;

public interface InspectionTaskService {

    Integer addInspectionTask(Map<String,Object> params);

    PageInfo<InspectionTask> searchInspectionTask(InspectionTaskCondition condition);

    Integer updateInspectionTask(Map<String, Object> params);

    Integer deleteInspectionTask(Long id);
}
