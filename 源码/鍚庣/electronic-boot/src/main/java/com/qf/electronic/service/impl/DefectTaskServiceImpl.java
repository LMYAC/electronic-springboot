package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.mapper.DefectTaskMapper;
import com.qf.electronic.pojo.DefectTask;
import com.qf.electronic.service.DefectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class DefectTaskServiceImpl implements DefectTaskService {

    @Autowired
    private DefectTaskMapper defectTaskMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addDefectTask(Map<String, Object> params) {
        return defectTaskMapper.addDefectTask(params);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<DefectTask> searchDefectTask(DefectTaskCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<DefectTask> tasks = defectTaskMapper.searchDefectTask(condition);
        return new PageInfo<>(tasks);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateDefectTask(Map<String, Object> params) {
        return defectTaskMapper.updateDefectTask(params);
    }
}
