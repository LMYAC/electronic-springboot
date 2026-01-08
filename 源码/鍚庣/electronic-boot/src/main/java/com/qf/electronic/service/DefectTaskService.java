package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.pojo.DefectTask;

import java.util.Map;

public interface DefectTaskService {

    int addDefectTask(Map<String,Object> params);

    PageInfo<DefectTask> searchDefectTask(DefectTaskCondition condition);

    Integer updateDefectTask(Map<String, Object> params);
}
