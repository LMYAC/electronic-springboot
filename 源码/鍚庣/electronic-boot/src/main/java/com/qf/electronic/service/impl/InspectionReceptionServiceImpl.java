package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.mapper.InspectionReceptionMapper;
import com.qf.electronic.pojo.InspectionReception;
import com.qf.electronic.service.DefectTaskService;
import com.qf.electronic.service.InspectionReceptionService;
import com.qf.electronic.service.InspectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InspectionReceptionServiceImpl implements InspectionReceptionService {

    @Autowired
    private InspectionReceptionMapper inspectionReceptionMapper;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private DefectTaskService defectTaskService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addInspectionReception(Map<String, Object> params) {
        return inspectionReceptionMapper.addInspectionReception(params);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<InspectionReception> searchInspectionReception(InspectionTaskCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<InspectionReception> receptions = inspectionReceptionMapper.searchInspectionReception(condition);
        return new PageInfo<>(receptions);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateInspectionReception(Map<String, Object> params) {
        int affectedRows = inspectionReceptionMapper.updateInspectionReception(params);
        Integer state = (Integer) params.get("state");
        if(affectedRows == 1 && (state != null && state == 2)){//回执已经审核通过，需要将巡检任务状态翻转为已完成
            int id = (int) params.get("id");
            long inspectionTaskId = inspectionReceptionMapper.getInspectionTaskId(id);
            Map<String,Object> taskMap = new HashMap<>();
            taskMap.put("id", inspectionTaskId);
            taskMap.put("state", 3);
            inspectionTaskService.updateInspectionTask(taskMap);
            //这里还需要添加消缺任务
            defectTaskService.addDefectTask(params);
        }
        return affectedRows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteInspectionReception(Long id) {
        return inspectionReceptionMapper.deleteInspectionReception(id);
    }
}
