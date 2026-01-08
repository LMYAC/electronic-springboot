package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.mapper.InspectionTaskMapper;
import com.qf.electronic.pojo.InspectionTask;
import com.qf.electronic.service.InspectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class InspectionTaskServiceImpl implements InspectionTaskService {

    @Autowired
    private InspectionTaskMapper inspectionTaskMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addInspectionTask(Map<String, Object> params) {
        return inspectionTaskMapper.addInspectionTask(params);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<InspectionTask> searchInspectionTask(InspectionTaskCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<InspectionTask> tasks = inspectionTaskMapper.searchInspectionTask(condition);
        return new PageInfo<>(tasks);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateInspectionTask(Map<String, Object> params) {
        return inspectionTaskMapper.updateInspectionTask(params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteInspectionTask(Long id) {
        return inspectionTaskMapper.deleteInspectionTask(id);
    }
}
